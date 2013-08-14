/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.problem;

import net.sourceforge.cilib.io.DataTable;
import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.CentroidHolder;
import net.sourceforge.cilib.type.types.container.ClusterCentroid;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.distancemeasure.DistanceMeasure;
import net.sourceforge.cilib.util.distancemeasure.EuclideanDistanceMeasure;

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
    private DistanceMeasure distanceMeasure;

    /*
     * Default constructor of the QuantisationErrorMinimisationProblem
     */
    public QuantisationErrorMinimisationProblem() {
        super();
        distanceMeasure = new EuclideanDistanceMeasure();
    }

    /*
     * Copy constructor of the QuantisationErrorMinimisationProblem
     * @param copy the QuantisationErrorMinimisationProblem to be copied
     */
    public QuantisationErrorMinimisationProblem(QuantisationErrorMinimisationProblem copy) {
        super(copy);
        distanceMeasure = copy.distanceMeasure;
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
        CentroidHolder candidateSolution = (CentroidHolder) solution.getClone();

        //find shortest distances for data partterns
        candidateSolution.clearAllCentroidDataItems();
        assignDataPatternsToParticle(candidateSolution);

        //Calculate quantisation error
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

    /*
     * Adds the data patterns closest to a centroid to its data pattern list
     * @param candidateSolution The solution holding all the centroids
     * @param dataset The dataset holding all the data patterns
     */
    public void assignDataPatternsToParticle(CentroidHolder candidateSolution) {
        DataTable dataSet = window.slideWindow();
        double euclideanDistance;
        Vector addedPattern;
        Vector pattern;

        for(int i = 0; i < dataSet.size(); i++) {
            euclideanDistance = Double.POSITIVE_INFINITY;
            addedPattern = Vector.of();
            pattern = ((StandardPattern) dataSet.getRow(i)).getVector();
            int centroidIndex = 0;
            int patternIndex = 0;
            for(ClusterCentroid centroid : candidateSolution) {
                if(distanceMeasure.distance(centroid.toVector(), pattern) < euclideanDistance) {
                    euclideanDistance = distanceMeasure.distance(centroid.toVector(), pattern);
                    addedPattern = Vector.copyOf(pattern);
                    patternIndex = centroidIndex;
                }
                centroidIndex++;
            }

            candidateSolution.get(patternIndex).addDataItem(euclideanDistance, addedPattern);
        }
    }
}
