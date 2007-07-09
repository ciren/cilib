package net.sourceforge.cilib.functions.continuous;

import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This class makes use of the helper/member functions defined and implemented in
 * {@link net.sourceforge.cilib.functions.continuous.ClusteringFitnessFunction) to calculate an improved parameterised fitness
 * of a particular clustering in the evaluate method.<br/>
 * References:<br/>
 * <p>
 * Mahamed G.H. Omran, "Particle Swarm Optimization Methods for Pattern Recognition and Image Processing",
 * University Of Pretoria, Faculty of Engineering, Built Environment and Information Technology, 251. Section 4.2.2; Pages 114 &
 * 115. November 2004
 * </p>
 * @author Theuns Cloete
 */
public class ParametricWithQuantisationErrorFunction extends ParametricClusteringFunction {
	private static final long serialVersionUID = -2022785065235231801L;
	
	/**Specifies the weight that the Quantisation Error will contribute to the final fitness*/
	protected double w3 = 0.0;

	/**
	 * The constructor calls the base class' constructor {@link net.sourceforge.cilib.functions.continuous.ClusteringFitnessFunction}
	 * and sets the parameter values to be:
	 * <ul>
	 *     <li>w1 = 0.3</li>
	 *     <li>w2 = 0.3</li>
	 *     <li>w3 = 0.4</li>
	 * </ul>
	 */
	public ParametricWithQuantisationErrorFunction() {
		super();
		w1 = 0.3;
		w2 = 0.3;
		w3 = 0.4;
	}

	/**
	 * This method is responsible for two things:
	 * <ol>
	 *     <li>Assign each pattern in the dataset to its closest centroid. We don't care how this is done, since it is handled
	 *     by the {@link net.sourceforge.cilib.problem.dataset.ClusterableDataSet} abstraction. This has to be done before the
	 *     fitness is evaluated for the given centroids vector.</li>
	 *     <li>Calculate the improved parameterised fitness with the help of the following values:
	 *         <ul>
	 *             <li>maximum average distance between patterns and their respective centroids;</li>
	 *             <li>minimum distance between centroid pairs;</li>
	 *             <li>maximum distance possible between centroids (zMax); and</li>
	 *             <li>the quantisation error</li>
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
		if(w1 + w2 + w3 != 1.0)
			throw new IllegalArgumentException("The sum of w1, w2 and w3 must equal 1.0");

		if(dataset == null)
			resetDataSet();
		//assign each pattern in the dataset to its closest centroid
		dataset.assign(centroids);
		
		calculateQuantisationErrorAndMaximumAverageDistanceBetweenPatternsAndCentroids(centroids);
		calculateInterClusterDistance(centroids);

		//zMax only needs to be calculated once, because the domain is not supposed to change during a simulation
		if(!zMaxFlag)
			zMax = zMax(dataset, centroids);

		//the fitness should never drop below 0.0, but just in case something goes wrong, we want to know about it
		double fitness = (w1 * maximumAverageDistance) + (w2 * (zMax - interClusterDistance)) + (w3 * quantisationError); 
		if(fitness < 0.0) {
			System.out.println("w1 = " + w1);
			System.out.println("maximumAverageDistance = " + maximumAverageDistance);
			System.out.println("zMax = " + zMax);
			System.out.println("w2 = " + w2);
			System.out.println("minimumCentroidDistance = " + interClusterDistance);
			System.out.println("w3 = " + w3);
			System.out.println("quantisationError = " + quantisationError);
			System.exit(0);
		}
		return fitness;
	}
	
	/**
	 * Set the weight that the Quantisation Error will contribute to the final fitness
	 * @param w the weight to which w3 will be set
	 */
	public void setW3(double w) {
		w3 = w;
	}
}
