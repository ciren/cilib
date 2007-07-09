/*
 * AssociatedPairDataSetBuilder.java
 * 
 * Created on Feb 23, 2006
 *
 * Copyright (C) 2003 - 2006 
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science 
 * University of Pretoria
 * South Africa
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package net.sourceforge.cilib.problem.dataset;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import net.sourceforge.cilib.algorithm.InitialisationException;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.MixedVector;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.DistanceMeasure;
import net.sourceforge.cilib.util.EuclideanDistanceMeasure;

import org.apache.log4j.Logger;

/**
 * Defines a dataset where each pattern in the dataset is associated with a cluster number ('key'). This pattern -> cluster association forms a pair.
 * The dataset's indexes will therefore be associated with clusters like follows:<br/>
 * [cluster] - [pattern]<br/>
 *       [0] - [0, 1, ..., i, ..., m]<br/>
 *       [1] - [0, 1, ..., i, ..., m]<br/>
 *       ... - ...<br/>
 *       [j] - [0, 1, ..., i, ..., m]<br/>
 *       ... - ...<br/>
 *       [n] - [0, 1, ..., i, ..., m]<br/>
 * @author Gary Pampara
 * @author Theuns Cloete
 * TODO: Make the key of a pair also able to be a StringType
 * TODO: specify format of file generically
 */
public class AssociatedPairDataSetBuilder extends DataSetBuilder implements ClusterableDataSet {
	private static final long serialVersionUID = -7035524554252462144L;
	private static Logger log = Logger.getLogger(AssociatedPairDataSetBuilder.class);

	// The datastructure used is an ArrayList that holds Pair entries
	protected ArrayList<Pattern> patterns = null;
	// A DistanceMeasure will be used to calculate the distance between a pattern and a centroid
	protected DistanceMeasure distanceMeasure = null;
	// the expert should specify how many clusters there should be
	protected int numberOfClusters = 0;
	// the expert can also specify how many classes there are (although this is not really
	// neccessary)
	protected int numberOfClasses = 0;
	protected String outputFile = "";

	/**
	 * Initialise the keyPatternPair data structure using the
	 * {@link net.sourceforge.cilib.util.EuclideanDistanceMeasure} as the default distance measure
	 */
	public AssociatedPairDataSetBuilder() {
		super();
		patterns = new ArrayList<Pattern>();
		distanceMeasure = new EuclideanDistanceMeasure();
	}

	public AssociatedPairDataSetBuilder(AssociatedPairDataSetBuilder rhs) {
		super(rhs);
		patterns = new ArrayList<Pattern>();
		for (Pattern pattern : rhs.patterns) {
			patterns.add(pattern.clone());
		}
		distanceMeasure = rhs.distanceMeasure;
	}

	@Override
	public AssociatedPairDataSetBuilder clone() {
		return new AssociatedPairDataSetBuilder(this);
	}

	/**
	 * Construct the data structure (keyPatternPair) that will contain all the patterns of all the
	 * datasets that exist
	 */
	public void initialise() {
		// run through all the datasets specified in the XML file
		for (DataSet dataset : this) {
			// every dataset is represented by a file on disk
			BufferedReader br = new BufferedReader(new InputStreamReader(dataset.getInputStream()));
			try {
				Vector builtRepresentation = (Vector) problem.getDomain().getBuiltRepresenation();
				builtRepresentation = builtRepresentation.subVector(0, (builtRepresentation.size() / numberOfClusters) - 1);

				// every line in a dataset represents a pattern
				String line = br.readLine();
				int index = 0;
				while (line != null) {
					addToDataSet(index++, line, dataset, builtRepresentation);
					line = br.readLine();
				}
			}
			catch (IOException io) {
				throw new RuntimeException(io);
			}
			catch (NullPointerException npe) {
				throw new InitialisationException("Make sure that the <function> tag is before the <dataSetBuilder> tag inside a <problem> tag.");
			}
		}
	}

	/**
	 * Parse the given line using the given dataset's patternExpression and add it to keyPatternPair
	 * @param line a String representing one line of the DataSet
	 * @param dataset the DataSet in which the given line resides
	 */
	private void addToDataSet(int index, String line, DataSet dataset, Vector builtRepresentation) {
		// split the received line using the regular expression given in the XML file (or
		// 'patternExpression' in {@linkplain DataSet})
		String[] elements = line.split(dataset.getPatternExpression());
		//	the elements of the split are stored inside a vector that will form the pattern
		// we construct the pattern based on the builtRepresentation that has been created from the domain that has been set
		Vector pattern = builtRepresentation.clone();
		int j = 0;
		for (String element : elements) {
			if(!element.isEmpty()) {
				pattern.getNumeric(j++).set(element);
			}
		}
		// the pattern is added to the "dataset"
		patterns.add(new Pattern(index, 0, pattern));
	}

	/**
	 * Update pattern assignations, i.e. determine in which cluster a pattern belongs based on its
	 * closest centroid by running through the entire centroids vector
	 * @param centroids The vector representing the centroid vectors
	 */
	public void assign(Vector centroids) {
		Vector centroid = null;
		double distance = 0.0, minimum = Double.MAX_VALUE;
		for (Pattern pattern : patterns) {
			minimum = Double.MAX_VALUE;
			for (int j = 0; j < numberOfClusters; j++) {
				centroid = getSubCentroid(centroids, j);
				distance = distanceMeasure.distance(pattern.data, centroid);
				if (distance < minimum) {
					minimum = distance;
					pattern.clas = j;
				}
			}
		}
	}

	/**
	 * Build up a list of all the clusters, each element containing another list of all the patterns
	 * that belong to that cluster.
	 * @return an ArrayList containing ArrayLists containing Patterns
	 */
	public ArrayList<ArrayList<Pattern>> arrangedClusters() {
		ArrayList<ArrayList<Pattern>> clusters = new ArrayList<ArrayList<Pattern>>();
		for (int i = 0; i < numberOfClusters; i++) {
			clusters.add(new ArrayList<Pattern>());
		}
		for (Pattern pattern : patterns) {
			clusters.get(pattern.clas).add(pattern);
		}
		assert clusters.size() == numberOfClusters;
		return clusters;
	}

	/**
	 * Get the pattern that is represented by the given index
	 * @param index the index representing a pattern in the keyPatternPair ArrayList
	 * @return the pattern represented by index as a Vector
	 */
	public Pattern getPattern(int index) {
		return patterns.get(index);
	}

	/**
	 * Determine how many patterns are in the dataset(s)
	 * @return the size of the keyPatternPair ArrayList
	 */
	public int getNumberOfPatterns() {
		return patterns.size();
	}

	public ArrayList<Pattern> getPatterns() {
		return patterns;
	}

	/**
	 * Set the type of DistanceMeasure that should be used for determining the distance between two
	 * Vectors.
	 * @param distanceMeasure any class that inherits from DistanceMeasure can be used
	 */
	public void setDistanceMeasure(DistanceMeasure distanceMeasure) {
		this.distanceMeasure = distanceMeasure;
	}

	/**
	 * Get the type of DistanceMeasure that should be used for determining the distance between two
	 * Vectors.
	 * @return a DistanceMeasure object
	 */
	public DistanceMeasure getDistanceMeasure() {
		return this.distanceMeasure;
	}

	public double calculateDistance(Vector x, Vector y) {
		return distanceMeasure.distance(x, y);
	}

	public double calculateDistance(int x, int y) {
		return distanceMeasure.distance(patterns.get(x).data, patterns.get(y).data);
	}

	/**
	 * Set the number of clusters that exist in the dataset that will be optimised.
	 * @param clusters the value that numberOfClusters will be set to
	 */
	public void setNumberOfClusters(int clusters) {
		if (clusters < 1)
			throw new IllegalArgumentException("The number of clusters cannot be less than 1");
		numberOfClusters = clusters;
	}

	public int getNumberOfClusters() {
		return numberOfClusters;
	}

	public void setNumberOfClasses(int classes) {
		if (classes < 1)
			throw new IllegalArgumentException("The number of classes cannot be less than 1");
		numberOfClasses = classes;
	}

	public int getNumberOfClasses() {
		return numberOfClasses;
	}

	/**
	 * Determine which part of the centroids vector (i.e. which centroid) the patterns should be
	 * compared to
	 * @param centroids the current Vector that contains all the cluster centroids
	 * @param cluster the cluster number that should be returned
	 * @return a vector representing the specific centroid in the given centroids Vector
	 */
	public Vector getSubCentroid(Vector centroids, int cluster) {
		int dimension = centroids.size() / numberOfClusters;
		return (Vector) centroids.subVector(cluster * dimension, ((cluster * dimension) + dimension) - 1);
	}

	public void setSubCentroid(Vector centroids, Vector centroid, int cluster) {
		int dimension = centroids.size() / numberOfClusters;
		for (int i = cluster * dimension, j = 0; i < ((cluster * dimension) + dimension); i++, j++) {
			centroids.set(i, centroid.get(j));
		}
	}

	public Vector getSetMean(ArrayList<Pattern> set) {
		if (set.size() == 0)
			return null;

		Vector mean = new MixedVector();
		mean.initialise(set.get(0).data.size(), new Real(0.0));

		for (Pattern pattern : set) {
			mean = mean.plus(pattern.data);
		}
		mean = mean.divide(set.size());
		return mean;
	}

	public Vector getSetVariance(ArrayList<Pattern> set) {
		if (set.size() == 0)
			return null;

		Vector mean = getSetMean(set);
		Vector diffSquare = null;
		Vector variance = new MixedVector();
		variance.initialise(mean.size(), new Real(0.0));

		for (Pattern pattern : set) {
			diffSquare = pattern.data.subtract(mean);
			diffSquare = diffSquare.multiply(diffSquare);
			variance = variance.plus(diffSquare);
		}
		variance = variance.divide(set.size());
		return variance;
	}

	public Vector getMean() {
		return getSetMean(patterns);
	}

	public Vector getVariance() {
		return getSetVariance(patterns);
	}

	public void setOutputFile(String filename) {
		outputFile = filename;
	}
}
