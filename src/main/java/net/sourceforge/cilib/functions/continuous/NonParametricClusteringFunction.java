package net.sourceforge.cilib.functions.continuous;

import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This class makes use of the helper/member functions defined and implemented in
 * {@link net.sourceforge.cilib.functions.continuous.ClusteringFitnessFunction) to calculate a non-parameterised fitness of a
 * particular clustering in the evaluate method.<br/>
 * References:<br/>
 * <p>
 * Mahamed G.H. Omran, "Particle Swarm Optimization Methods for Pattern Recognition and Image Processing",
 * University Of Pretoria, Faculty of Engineering, Built Environment and Information Technology, 251. Section 4.2.7; Pages 128
 * & 129. November 2004
 * </p>
 * @author Theuns Cloete
 */
public class NonParametricClusteringFunction extends ClusteringFitnessFunction {
	private static final long serialVersionUID = 5712216719378084294L;

	/**
	 * The constructor, nothing much is done here, except that it calls the base class' constructor.
	 * {@link net.sourceforge.cilib.functions.continuous.ClusteringFitnessFunction}
	 */
	public NonParametricClusteringFunction() {
		super();
	}

	@Override
	/**
	 * This method is responsible for two things:
	 * <ol>
	 *     <li>Assign each pattern in the dataset to its closest centroid. We don't care how this is done, since it is handled
	 *     by the {@link net.sourceforge.cilib.problem.dataset.ClusterableDataSet} abstraction. This has to be done before the
	 *     fitness is evaluated for the given centroids vector.</li>
	 *     <li>Calculate the parameterised fitness with the help of the following values:
	 *         <ul>
	 *             <li>maximum average distance between patterns and their respective centroids;</li>
	 *             <li>quantisation error; and</li>
	 *             <li>minimum distance between centroid pairs</li>
	 *         </ul>
	 *     {@link net.sourceforge.cilib.functions.continuous.ClusteringFitnessFunction.calculateQuantisationError}
	 *     </li>
	 * </ol>
	 * @param centroids The vector representing the centroid vectors
	 * @return the parameterised fitness that has been calculated
	 */
	public double evaluate(Vector centroids) {
		if(dataset == null)
			resetDataSet();
		//assign each pattern in the dataset to its closest centroid
		dataset.assign(centroids);
		calculateQuantisationErrorAndMaximumAverageDistanceBetweenPatternsAndCentroids(centroids);
		calculateInterClusterDistance(centroids);

		//the maximum average distance and minimum centroid distance should never drop below 0.0, but just in case something goes wrong, we want to know about it
		if(maximumAverageDistance < 0.0 || interClusterDistance <= 0.0) {
			System.out.println("maximumAverageDistance = " + maximumAverageDistance);
			System.out.println("minimumCentroidDistance = " + interClusterDistance);
			System.exit(0);
		}
		return (maximumAverageDistance + quantisationError) / interClusterDistance;
	}
}
