package net.sourceforge.cilib.functions.continuous;

import net.sourceforge.cilib.problem.dataset.ClusterableDataSet;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Vector;

/**
 * This class makes use of the helper/member functions defined and implemented in
 * {@link net.sourceforge.cilib.functions.continuous.ClusteringFitnessFunction) to calculate a parameterised fitness of a
 * particular clustering in the evaluate method.<br/>
 * References:<br/>
 * <p>
 * Mahamed G.H. Omran, "Particle Swarm Optimization Methods for Pattern Recognition and Image Processing",
 * University Of Pretoria, Faculty of Engineering, Built Environment and Information Technology, 251. Section 4.1.1; Page 105.
 * November 2004
 * </p>
 * @author Theuns Cloete
 */
public class ParametricClusteringFunction extends ClusteringFitnessFunction {
	/**Specifies the weight that intra-cluster-distance will contribute to the final fitness*/
	protected double w1 = 0.0;
	/**Specifies the weight that inter-cluster-distance will contribute to the final fitness*/
	protected double w2 = 0.0;
	/**Stores the calculated zMax value*/
	protected double zMax = 0.0;
	/**A flag to know whether zMax has already been calculated*/
	protected boolean zMaxFlag = false; 

	/**
	 * The constructor calls the base class' constructor {@link net.sourceforge.cilib.functions.continuous.ClusteringFitnessFunction}
	 * and sets both the parameter values (w1 and w2) to be equal to 0.5.
	 */
	public ParametricClusteringFunction() {
		super();
		w1 = 0.5;
		w2 = 0.5;
	}

	/**
	 * This method is responsible for two things:
	 * <ol>
	 *     <li>Assign each pattern in the dataset to its closest centroid. We don't care how this is done, since it is handled
	 *     by the {@link net.sourceforge.cilib.problem.dataset.ClusterableDataSet} abstraction. This has to be done before the
	 *     fitness is evaluated for the given centroids vector.</li>
	 *     <li>Calculate the parameterised fitness with the help of the following values:
	 *         <ul>
	 *             <li>maximum average distance between patterns and their respective centroids;</li>
	 *             <li>minimum distance between centroid pairs; and</li>
	 *             <li>maximum distance possible between centroids (zMax)</li>
	 *         </ul>
	 *     {@link net.sourceforge.cilib.functions.continuous.ClusteringFitnessFunction.calculateQuantisationError}
	 *     </li>
	 * </ol>
	 * @param centroids The vector representing the centroid vectors
	 * @return the parameterised fitness that has been calculated
	 */
	@Override
	public double evaluate(Vector centroids) {
		//make sure the sum of the parameters equal 1.0
		if(w1 + w2 != 1.0)
			throw new IllegalArgumentException("The sum of w1 and w2 must equal 1.0");

		if(dataset == null)
			setDataSet(null);
		//assign each pattern in the dataset to its closest centroid
		dataset.assign(centroids);

		calculateMaximumAverageDistanceBetweenPatternsAndCentroids(centroids);
		calculateMinimumDistanceBetweenCentroidPairs(centroids);

		//zMax only needs to be calculated once, because the domain is not supposed to change during a simulation 
		if(!zMaxFlag)
			zMax = zMax(dataset, centroids);

		//the fitness should never drop below 0.0, but just in case something goes wrong, we want to know about it
		double fitness = (w1 * maximumAverageDistance) + (w2 * (zMax - minimumCentroidDistance));
		if(fitness < 0.0) {
			System.out.println("w1 = " + w1);
			System.out.println("maximumAverageDistance = " + maximumAverageDistance);
			System.out.println("zMax = " + zMax);
			System.out.println("minimumCentroidDistance = " + minimumCentroidDistance);
			System.out.println("w2 = " + w2);
			System.exit(0);
		}
		return fitness;
	}
	
	/**
	 * Set the weight that the intra-cluster-distance will contribute to the final fitness
	 * @param w the weight to which w1 will be set
	 */
	public void setW1(double w) {
		w1 = w;
	}
	
	/**
	 * Set the weight that the inter-cluster-distance will contribute to the final fitness
	 * @param w the weight to which w2 will be set
	 */
	public void setW2(double w) {
		w2 = w;
	}
	
	/**
	 * Calculate the maximum distance possible between centroids
	 * @param centroids The vector representing the centroid vectors
	 * @return the maximum distance possible between the given centroids
	 */
	protected double zMax(ClusterableDataSet dataset, Vector centroids) {
		double zMax = 0.0;
		double upper = ((Numeric)centroids.get(0)).getUpperBound();
		double lower = ((Numeric)centroids.get(0)).getLowerBound();
		int dimension = centroids.size() / dataset.getNumberOfClusters();
		zMax = Math.pow(upper - lower, 2.0) * dimension;
		//we only have to calculate zMax once
		zMaxFlag = true;
		return Math.sqrt(zMax);
	}
}
