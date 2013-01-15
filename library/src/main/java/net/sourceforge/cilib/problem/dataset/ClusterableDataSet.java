/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.problem.dataset;

import java.util.ArrayList;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.Cloneable;

/**
 * All datasets that will be clustered have to implement this interface.
 */
public interface ClusterableDataSet {

    int getNumberOfPatterns();

    Pattern getPattern(int index);

    ArrayList<Pattern> getPatterns();
    Vector getMean();
    double getVariance();
    double getCachedDistance(int x, int y);
    void initialise();

    /**
     * TODO: Complete this javadoc.
     */
    public class Pattern implements Cloneable {
        private static final long serialVersionUID = 8831874859964777328L;
        private String clas = "<not set>";
        public Vector data = null;

        public Pattern(String c, Vector d) {
            clas = c;
            data = d;
        }

        public Pattern(Pattern rhs) {
            clas = rhs.clas;
            data = rhs.data;
        }

        public Pattern getClone() {
            return new Pattern(this);
        }

        public String toString() {
            return clas + " -> " + data;
        }
    }
}
