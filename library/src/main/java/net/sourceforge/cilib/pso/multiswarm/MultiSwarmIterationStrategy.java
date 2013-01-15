/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.multiswarm;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.algorithm.population.AbstractIterationStrategy;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.visitor.DiameterVisitor;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.distancemeasure.DistanceMeasure;
import net.sourceforge.cilib.util.distancemeasure.EuclideanDistanceMeasure;

/**
 * Implementation of the multi-swarm algorithm as described in:
 * <pre>
 * {@literal @}article{blackwell51pso,
 *  title={{Particle swarm optimization in dynamic environments}},
 *  author={Blackwell, T.},
 *  journal={Evolutionary Computatation in Dynamic and Uncertain Environments},
 *  volume={51},
 *  pages={29--49}
 * }
 * </pre>
 *
 * Example of XML specification:
 * <pre>
 * {@literal
 *     <algorithm id="multiswarms_5" class="pso.multiswarms.MultiSwarm">
 *        <addStoppingCondition class="stoppingcondition.MaximumIterations" maximumIterations="1000"/>
 *        <multiSwarmsIterationStrategy class="pso.multiswarms.MultiSwarmIterationStrategy" exclusionRadius="5.0">
 *        </multiSwarmsIterationStrategy>
 *        <algorithm idref="multi_quantum_20"/>
 *        <algorithm idref="multi_quantum_20"/>
 *        <algorithm idref="multi_quantum_20"/>
 *        <algorithm idref="multi_quantum_20"/>
 *        <algorithm idref="multi_quantum_20"/>
 *    </algorithm>}
 * </pre>
 *
 *
 */
public class MultiSwarmIterationStrategy extends AbstractIterationStrategy<MultiSwarm> {

    private static final long serialVersionUID = 1416926223484924869L;
    private double exclusionRadius = 2.0;

    public MultiSwarmIterationStrategy() {
        super();
    }

    public MultiSwarmIterationStrategy(MultiSwarmIterationStrategy copy) {
        super();
        this.exclusionRadius = copy.exclusionRadius;
    }

    @Override
    public MultiSwarmIterationStrategy getClone() {
        return new MultiSwarmIterationStrategy(this);
    }

    public double getExclusionRadius() {
        return exclusionRadius;
    }

    public void setExclusionRadius(double exlusionRadius) {
        this.exclusionRadius = exlusionRadius;
    }

    double calculateRadius() {
        double d = AbstractAlgorithm.get().getOptimisationProblem().getDomain().getDimension();
        //    double X = ((Vector) Algorithm.get().getOptimisationProblem().getDomain().getBuiltRepresentation()).getNumeric(0).getBounds().getUpperBound()
        //            - ((Vector) Algorithm.get().getOptimisationProblem().getDomain().getBuiltRepresentation()).getNumeric(0).getBounds().getLowerBound();
        double X = ((Vector) AbstractAlgorithm.get().getOptimisationProblem().getDomain().getBuiltRepresentation()).get(0).getBounds().getUpperBound()
                - ((Vector) AbstractAlgorithm.get().getOptimisationProblem().getDomain().getBuiltRepresentation()).get(0).getBounds().getLowerBound();
        double M = ((MultiSwarm) (AbstractAlgorithm.get())).getPopulations().size();
        return X / (2 * Math.pow(M, 1 / d));
    }

    boolean isConverged(PopulationBasedAlgorithm algorithm) {
        double r = calculateRadius();

        DiameterVisitor visitor = new DiameterVisitor();
        double radius = (Double) ((PSO) algorithm).accept(visitor);
        return radius <= r;
    }

    @Override
    public void performIteration(MultiSwarm ca) {
        int converged = 0;
        for (PopulationBasedAlgorithm current : ca.getPopulations()) {
            if (isConverged(current)) {
                converged++;
            }
        }

        //all swarms have converged-> must re-initialise worst swarm
        if (converged == ca.getPopulations().size()) {
            PopulationBasedAlgorithm weakest = null;
            for (PopulationBasedAlgorithm current : ca.getPopulations()) {
                if (weakest == null || weakest.getBestSolution().compareTo(current.getBestSolution()) > 0) {
                    weakest = current;
                }
            }
            reInitialise((PSO) weakest);
        }

        for (PopulationBasedAlgorithm current : ca.getPopulations()) {
            current.performIteration();
        }

        for (PopulationBasedAlgorithm current : ca.getPopulations()) {
            for (PopulationBasedAlgorithm other : ca.getPopulations()) {
                Vector currentPosition, otherPosition;
                if (!current.equals(other)) {
                    currentPosition = (Vector) ((PSO) current).getBestSolution().getPosition(); //getBestParticle().getPosition();
                    otherPosition = (Vector) ((PSO) other).getBestSolution().getPosition();
                    DistanceMeasure dm = new EuclideanDistanceMeasure();
                    double distance = dm.distance(currentPosition, otherPosition);
                    if (distance < exclusionRadius) {
                        if (((PSO) current).getBestSolution().getFitness().compareTo(((PSO) other).getBestSolution().getFitness()) > 0) {
                            reInitialise((PSO) current);
                        } else {
                            reInitialise((PSO) other);
                        }
                    }
                }
            }
        }
    }

    public void reInitialise(PSO algorithm) {
        algorithm.getTopology().clear();
        algorithm.algorithmInitialisation();
    }
}
