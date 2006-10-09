package net.sourceforge.cilib.functions.continuous;

import net.sourceforge.cilib.type.types.Vector;

/**
 * This class makes use of the helper/member functions defined and implemented in
 * {@link net.sourceforge.cilib.functions.continuous.ClusteringFitnessFunction} to calculate the Quantisation Error of a
 * particular clustering in the evaluate method.<br/>
 * References:<br/>
 * <p>
 * Mahamed G.H. Omran, "Particle Swarm Optimization Methods for Pattern Recognition and Image Processing",
 * University Of Pretoria, Faculty of Engineering, Built Environment and Information Technology, 251. Section 4.1.1; Pages 104
 * & 105. November 2004
 * </p>
 * @author Theuns Cloete
 */
public class QuantisationErrorFunction extends ClusteringFitnessFunction {
	
	/**
	 * The constructor, nothing much is done here, except that it calls the base class' constructor.
	 * {@link net.sourceforge.cilib.functions.continuous.ClusteringFitnessFunction}
	 */
	public QuantisationErrorFunction() {
		super();
	}

	/**
	 * This method is responsible for two things:
	 * <ol>
	 *     <li>Assign each pattern in the dataset to its closest centroid. We don't care how this is done, since it is handled
	 *     by the {@link net.sourceforge.cilib.problem.dataset.ClusterableDataSet} abstraction. This has to be done before the
	 *     fitness is evaluated for the given centroids vector.</li>
	 *     <li>Calculate the Quantisation Error by making use of
	 *     {@link net.sourceforge.cilib.functions.continuous.ClusteringFitnessFunction.calculateQuantisationError}</li>
	 * </ol>
	 * @param centroids The vector representing the centroid vectors
	 * @return the quantisation error that has been calculated
	 */
	@Override
	public double evaluate(Vector centroids) {
		if(dataset == null)
			setDataSet(null);
		//assign each pattern in the dataset to its closest centroid
		dataset.assign(centroids);
		calculateQuantisationError(centroids);
		return quantisationError;
	}
}
