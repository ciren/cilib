package net.cilib
package simulator

import scala.tools.nsc._
import interpreter._
import scala.reflect._
import java.io._

object Main {

  def main(args: Array[String]): Unit = {
    val interpreter = new InterpreterWrapper() {
      def prompt = "cilib> "
      def welcomeMsg = """Welcome to CIlib!
                         |Type in expressions to have them evaluated""".stripMargin
      def helpMsg = """This is printed *before* the help for eveyr command!"""
      // bind("name", instance)
      autoImport("net.cilib.simulator.Simulation._")
      autoImport("net.sourceforge.cilib._")
    }

    if (args.isEmpty) interpreter.startInterpreting else ScriptEngine.compileAndRun(args(0))
  }
}

object ScriptEngine {
  import scala.tools.nsc.reporters.ConsoleReporter

  private var reporter: ConsoleReporter = _

  def compileAndRun(file: String) = {
    val tmpDir = java.nio.file.Files.createTempDirectory("cilib-simulator").toFile()
    tmpDir.deleteOnExit

    println("Compiling provided script into: " + tmpDir.getAbsolutePath)

    val settings = new Settings(s => {
        import scala.reflect.internal.util.FakePos
        reporter.error(FakePos("scalac"), s)
      })

    reporter = new ConsoleReporter(settings)

    //settings.sourcepath.tryToSet(h.sourceDir.getAbsolutePath :: Nil)
    //val cp = done.map(_.targetDir) ++ classPaths
    settings.classpath.tryToSet(getClass.getClassLoader match {
        case cl: java.net.URLClassLoader => cl.getURLs.toList.map(_.toString)
        case _ => sys.error("Class loader is not a URLClassLoader?")
      })
    //List(cp.map(_.getAbsolutePath).mkString(File.pathSeparator)))
    settings.outdir.tryToSet(tmpDir.getAbsolutePath :: Nil)

    val g = new Global(settings, reporter)
    val run = new g.Run

    val contents = scala.io.Source.fromFile(file).getLines.toList.mkString("\n")
    val runnableScriptTmpl = s"""class Eval {
                                |  import net.cilib.simulator.Simulation._
                                |  def run: Unit = {
                                |    ${contents}
                                |  }
                                |}
                                |""".stripMargin

    val src = new FileWriter(new File(tmpDir, "Eval.scala"))
    try {
      src.write(runnableScriptTmpl)
    } finally {
      src.close
    }

    run.compile(List(new File(tmpDir, "Eval.scala").getAbsolutePath))

    if (reporter.hasErrors) {
      reporter.flush
      sys.exit
    } else {
      val loader = new java.net.URLClassLoader(Array(tmpDir.getAbsoluteFile.toURI.toURL), getClass.getClassLoader)
      val clazz = loader.loadClass("Eval")
      val instance = clazz.newInstance
      val method = clazz.getMethod("run")

      println("Compilation successful...")
      println("Executing compiled specification...")
      method.invoke(instance)
    }

    deleteContents(tmpDir.toPath)
  }

  import java.nio.file.Path

  def deleteContents(d: Path) = {
    import java.nio.file.Files
    import java.nio.file.attribute.BasicFileAttributes

    java.nio.file.Files.walkFileTree(d, new java.nio.file.SimpleFileVisitor[Path] {
        override def visitFile(file: Path, attrs: BasicFileAttributes) = {
          Files.delete(file)
          java.nio.file.FileVisitResult.CONTINUE
        }

        override def visitFileFailed(file: Path, ex: IOException) = {
          Files.delete(file)
          java.nio.file.FileVisitResult.CONTINUE
        }

        override def postVisitDirectory(dir: Path, ex: IOException) = {
          Files.delete(dir)
          java.nio.file.FileVisitResult.CONTINUE
        }
      })
  }
}

trait InterpreterWrapper {

  def helpMsg: String
  def welcomeMsg: String
  def prompt: String

  private var bindings : Map[String, (java.lang.Class[_], AnyRef)] = Map()
  private var packageImports : List[String] = List()
  private var files = List[String]()

  /**
   * Binds a given value into the interpreter when it starts with its most specific class
   */
  protected def bind[A <: AnyRef](name : String, value : A)(implicit m : Manifest[A]): Unit =
    bindings += (( name, (m.runtimeClass, value)))
  /**
   * Binds a given value itnot he interpreter with a given interface/higher-level class.
   */
  protected def bindAs[A <: AnyRef, B <: A](name : String, interface : java.lang.Class[A], value : B): Unit =
    bindings += ((name, (interface, value)))

  /**
   * adds an auto-import for the interpreter.
   */
  protected def autoImport(importString : String): Unit =
    packageImports = importString :: packageImports

  /**
   * Adds a file that will be interpreter at the start of the interpreter
   */
  protected def addScriptFile(fileName : String): Unit =
    files = fileName :: files

  /**
   * This class actually runs the interpreter loop.
   */
  class MyInterpreterLoop(out : PrintWriter) extends ILoop(None, out) {
    override val prompt = InterpreterWrapper.this.prompt

    // In Scala 2.8, InterpreterLoop.bindSettings() has been removed.
    // So, I need to override repl() and bind my settings by myself.
    override def loop() {
      if (isAsync) awaitInitialized()
      bindSettings()
      super.loop()
    }

    /** Bind the settings so that evaluated code can modify them */
    def bindSettings() {
      intp beQuietDuring {
        for( (name, (clazz, value)) <- bindings) {
          intp.bind(name, clazz.getCanonicalName, value)
        }
        for( importString <- packageImports) {
          intp.interpret("import " + importString)
        }
      }
    }

    override def helpCommand(line: String): Result = {
      if (line == "") echo(helpMsg)

      super.helpCommand(line)
    }

    override def printWelcome(): Unit = {
      out.println(welcomeMsg)
      out.flush()
    }
  }

  def startInterpreting(): Unit = {
    val out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)))
    val settings = new GenericRunnerSettings(out.println)
    files foreach settings.loadfiles.appendToValue
    settings.usejavacp.value = true
    val interpreter = new MyInterpreterLoop(out)
    interpreter process settings
    ()
  }
}