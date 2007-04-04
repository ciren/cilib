package net.sourceforge.cilib.problem.dataset;

import java.util.ArrayList;

import net.sourceforge.cilib.type.types.Vector;
import net.sourceforge.cilib.util.DistanceMeasure;

/**
 * All datasets that will be clustered have to implement this interface.
 * @author Theuns Cloete
 */
public interface ClusterableDataSet {

	public void assign(Vector vector);
	public ArrayList<ArrayList<Pattern>> arrangedClusters();

	public int getNumberOfPatterns();
	public Pattern getPattern(int index);
	public ArrayList<Pattern> getPatterns();

	public void setDistanceMeasure(DistanceMeasure distanceMeasure);
	public DistanceMeasure getDistanceMeasure();
	public double calculateDistance(Vector lhs, Vector rhs);
	public double calculateDistance(int lhs, int rhs);
	
	public void setNumberOfClusters(int clusters);
	public int getNumberOfClusters();
	
	public void setNumberOfClasses(int classes);
	public int getNumberOfClasses();
	
	public Vector getSubCentroid(Vector centroids, int cluster);
	public void setSubCentroid(Vector centroids, Vector centroid, int cluster);
	
	public Vector getSetMean(ArrayList<Pattern> set);
	public Vector getSetVariance(ArrayList<Pattern> set);
	public Vector getMean();
	public Vector getVariance();

	public class Pattern {
		public int index = 0;
		public int clas = 0; 
		public Vector data = null;

		protected Pattern(int i, int c, Vector d) {
			index = i;
			clas = c;
			data = d;
		}
	}
}
