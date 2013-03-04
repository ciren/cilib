/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.problem;

import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.CentroidHolder;
import net.sourceforge.cilib.type.types.container.ClusterCentroid;

/**
 *This class calculates the fitness of a solution according to the Quantisation Error formula which can be found in:
 * <pre>
 * {@literal @}article{vanDerMerwe03,
 *  title={{Data Clustering using Particle Swarm Optimization }},
 *  author={van der Merwe, D.W.; Engelhrecht, A.P.},
 *  year={2003},
 *  journal={Congress on Evolutionary Computation},
 *  volume={1},
 *  pages={215-220}
 * }
 * </pre>
 */
public class QuantisationErrorMinimisationProblem extends ClusteringProblem{

    /*
     * Default constructor of the QuantisationErrorMinimisationProblem
     */
    public QuantisationErrorMinimisationProblem() {
        super();
    }

    /*
     * Copy constructor of the QuantisationErrorMinimisationProblem
     * @param copy the QuantisationErrorMinimisationProblem to be copied
     */
    public QuantisationErrorMinimisationProblem(QuantisationErrorMinimisationProblem copy) {
        super(copy);
    }

    /*
     * The clone method of the QuantisationErrorMinimisationProblem
     * @return The new instance of the QuantisationErrorMinimisationProblem
     */
    @Override
    public AbstractProblem getClone() {
        return new QuantisationErrorMinimisationProblem(this);
    }

    /*
     * Calculates the fitness of the provided solution according to the Quantisation Error formula
     * @param solution The solution whose fitness must be calculated
     * @return fitness The resulting fitness value
     */
    @Override
    protected Fitness calculateFitness(Type solution) {
        CentroidHolder candidateSolution = (CentroidHolder) solution;
        double quantisationError = 0;
        double temp;
        for(ClusterCentroid centroid : (CentroidHolder) candidateSolution) {
            temp = 0;
            for(double distance : centroid.getDataItemDistances()) {
                temp += distance;
            }
            quantisationError += temp / ((double) centroid.getDataItemDistances().length);
        }

        quantisationError /= ((double) candidateSolution.size());

        if(Double.isNaN(quantisationError)){
            quantisationError = Double.POSITIVE_INFINITY;
        }

        return objective.evaluate(quantisationError);

    }


}
