package net.cilib

import scala.tools.nsc._
import interpreter._
import scala.reflect._
import java.io._

object Main {

  def main(args: Array[String]): Unit = {
    val interpreter = new InterpreterWrapper() {
      def prompt = "cilib> "
      def welcomeMsg = """Welcome to Awesomeness!
This is my version of the Scala interpreter"""
      def helpMsg = """This is printed *before* the help for eveyr command!"""
//       bind("josh", new MyClass("josh"))
      autoImport("net.sourceforge.cilib._")
    }

    if (args.isEmpty) interpreter.startInterpreting else ScriptEngine.compile(args(0))
  }
}

object ScriptEngine {
  def compile(file: String) = {
    val settings = new Settings(s => {
        sys.error("errors report: " + s)
      })
//    settings.sourcepath.tryToSet(h.sourceDir.getAbsolutePath :: Nil)
//    val cp = done.map(_.targetDir) ++ classPaths
//    settings.classpath.tryToSet(List(cp.map(_.getAbsolutePath).mkString(File.pathSeparator)))
//    settings.outdir.tryToSet(h.targetDir.getAbsolutePath :: Nil)

    val g = new Global(settings, new CompilationReporter)
    val run = new g.Run

    run.compile(List(file))
  }

  import scala.tools.nsc.reporters.Reporter
  import scala.tools.nsc.ast._
  import reflect.internal.util.Position

  private class CompilationReporter extends Reporter// with Logging
  {
    protected def info0(pos: Position, msg: String, severity: Severity, force: Boolean): Unit = {
      val m = "At line " + pos.safeLine + ": " + msg
      sys.error(m)
      if (severity == ERROR)
        throw new Error("error during compilation : %s".format(m))
    }

    override def hasErrors: Boolean = false
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