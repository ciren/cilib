/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.tuning.problem;

import fj.data.List;
import static fj.data.Stream.*;
import net.sourceforge.cilib.problem.Problem;

public class StandardProblemListProvider extends ProblemListProvider {

    private List<Problem> problems;
    private int count;

    public StandardProblemListProvider() {
        this.problems = List.<Problem>nil();
        this.count = 100;
    }

    @Override
    public List<Problem> _1() {
        return cycle(iterableStream(problems)).take(count).toList();
    }

    public void addProblem(Problem problem) {
        problems = problems.snoc(problem);
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

}
