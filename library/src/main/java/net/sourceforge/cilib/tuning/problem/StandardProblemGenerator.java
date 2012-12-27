/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.tuning.problem;

import fj.data.Stream;
import static fj.data.Stream.cycle;
import net.sourceforge.cilib.problem.Problem;

public class StandardProblemGenerator extends ProblemGenerator {

    private Stream<Problem> problems;
    private Stream<Problem> stream;

    public StandardProblemGenerator() {
        this.problems = Stream.<Problem>nil();
        this.stream = Stream.<Problem>nil();
    }

    @Override
    public Problem _1() {
        Problem p = stream.head();
        stream = stream.tail()._1();
        return p;
    }

    public void addProblem(Problem problem) {
        problems = problems.snoc(problem);
        stream = cycle(problems);
    }

}
