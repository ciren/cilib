package net.sourceforge.cilib.problem.dataset;

import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Vector;
import net.sourceforge.cilib.util.DistanceMeasure;

/**
 * All datasets that will be clustered have to implement this interface.
 * @author Theuns Cloete
 */
public interface ClusterableDataSet {

	public void assign(Vector vector);
	public Vector patternsInCluster(Numeric key);

	public void setDistanceMeasure(DistanceMeasure distanceMeasure);
	public DistanceMeasure getDistanceMeasure();
	
	public void setNumberOfClusters(int clusters);
	public int getNumberOfClusters();
	
	public void setNumberOfClasses(int classes);
	public int getNumberOfClasses();
	
	public Vector getSubCentroid(Vector centroids, int cluster);
}
