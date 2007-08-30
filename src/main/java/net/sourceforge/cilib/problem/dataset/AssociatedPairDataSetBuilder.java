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
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.DistanceMeasure;
import net.sourceforge.cilib.util.EuclideanDistanceMeasure;

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
 */
public class AssociatedPairDataSetBuilder extends DataSetBuilder implements ClusterableDataSet {
	private static final long serialVersionUID = -7035524554252462144L;

	// The data structure used is an ArrayList that holds Pattern entries
	protected ArrayList<Pattern> patterns = null;
	protected ArrayList<ArrayList<Pattern>> arrangedClusters = null;
	protected ArrayList<Vector> arrangedCentroids = null;
	// A DistanceMeasure will be used to calculate the distance between a pattern and a centroid
	protected DistanceMeasure distanceMeasure = null;
	// the expert can specify how many clusters there should be
	protected int numberOfClusters = 0;

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
	 * Construct the data structure that will contain all the patterns of all the datasets that exist
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
	 * Parse the given line using the given dataset's patternExpression and add it to the list of patterns
	 * @param line a String representing one line of the DataSet
	 * @param dataset the DataSet in which the given line resides
	 */
	private void addToDataSet(int index, String line, DataSet dataset, Vector builtRepresentation) {
		// split the received line using the regular expression given in the XML file (or 'patternExpression' in the DataSet class)
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
		patterns.add(new Pattern(index, 0, pattern));
	}

	/**
	 * The three methods called in this method must be called in that specific order, i.e.
	 * <ol>
	 * <li>Arrange the centroids (split them up to be manageable)</li>
	 * <li>Arrange the clusters (assign patterns to their closest centroids) (depends on Step 1)</li>
	 * <li>Remove the empty clusters and their associated centroids from the <i>arranged lists</i>,
	 * thereby finalizing the arranging of clusters (depends on both Steps 1 & 2)</li>
	 * </ol>
	 * @param centroids the @{@linkplain Vector} that represents the centroids
	 */
	public synchronized void arrangeClustersAndCentroids(Vector centroids) {
		arrangeCentroids(centroids);
		arrangeClusters();
		removeEmptyClustersAndCentroids();
		System.gc();
	}

	/**
	 * Take the given centroids {@linkplain Vector}, split it up and return an {@linkplain ArrayList}
	 * of {@linkplain Vector}s so that each element in this list consist of a single centroid.
	 * @param centroids the centriods {@linkplain Vector} that should be arranged
	 */
	private void arrangeCentroids(Vector centroids) {
		arrangedCentroids = new ArrayList<Vector>(numberOfClusters);
		int dimension = centroids.size() / numberOfClusters;

		for (int i = 0; i < numberOfClusters; i++) {
			arrangedCentroids.add(centroids.subVector(i * dimension, (i * dimension) + dimension - 1));
		}
	}

	/**
	 * Assign all patterns to their closest centroid, building up an {@linkplain ArrayList}
	 * that contains {@linkplain ArrayList}s of {@linkplain Pattern}s. The specific {@linkplain Pattern}'s
	 * <tt>clas</tt> member variable is updated each time a closer centroid is found. When
	 * this method is done, all patterns will <i>belong</i> to it's closest centroid.
	 */
	private void arrangeClusters() {
		arrangedClusters = new ArrayList<ArrayList<Pattern>>(numberOfClusters);

		for (int i = 0; i < numberOfClusters; i++) {
			arrangedClusters.add(new ArrayList<Pattern>(patterns.size() / numberOfClusters));
		}

		for (Pattern pattern : patterns) {
			double minimum = Double.MAX_VALUE;
			for (int i = 0; i < numberOfClusters; i++) {
				double distance = calculateDistance(pattern.data, arrangedCentroids.get(i));

				if (distance < minimum) {
					minimum = distance;
					pattern.clas = i;
				}
			}
			arrangedClusters.get(pattern.clas).add(pattern);
		}
	}

	/**
	 * Empty clusters are caused due to centroids that are not associated with any of the patterns in
	 * the dataset. Empty clusters should not be included in the calculation of fitness functions or
	 * validity indices. This method removes the empty clusters from the <tt>arrangedClusters</tt>
	 * list and also the corresponding centroid from the <tt>arrangedCentroids</tt> list.
	 */
	private void removeEmptyClustersAndCentroids() {
		// traverse list of clusters in reverse due to the way in which the remove(i) method works
		// i.e to prevent skipping elements, because all elements after i are shift left
		for (int i = arrangedClusters.size() - 1; i >= 0; i--) {
			if (arrangedClusters.get(i).isEmpty()) {
				arrangedClusters.remove(i);
				arrangedCentroids.remove(i);
			}
			else {
				arrangedClusters.get(i).trimToSize();
			}
		}
		arrangedClusters.trimToSize();
		arrangedCentroids.trimToSize();
	}

	/**
	 * Returns the centroids as they have been arranged by the {@linkplain arrangeCentroids} method.
	 * NOTE: It is guaranteed that this list will not contain <i>unassociated</i> centroids ONLY
	 * when {@linkplain arrangeClustersAndCentroids} has been called before this method.
	 * @return an easy manageable list containing the different centroids
	 */
	public ArrayList<Vector> getArrangedCentroids() {
		return arrangedCentroids;
	}

	/**
	 * Returns the clusters as they have been arranged by the {@linkplain arrangeClusters} method.
	 * NOTE: It is guaranteed that this list will not contain <i>empty</i> clusters ONLY when
	 * {@linkplain arrangeClustersAndCentroids} has been called before this method.
	 * @return an easy manageable list containing the different clusters
	 */
	public ArrayList<ArrayList<Pattern>> getArrangedClusters() {
		return arrangedClusters;
	}

	/**
	 * Get the pattern that is represented by the given index
	 * @param index the index representing a pattern in the <tt>patterns</tt> ArrayList
	 * @return the pattern represented by index as a {@linkplain Vector}
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

	/**
	 * Calculates the mean {@linkplain Vector} of the given set / cluster as shown in Equation 4.b of:<br/>
	 * @InProceedings{ 657864, author = "Maria Halkidi and Michalis Vazirgiannis", title =
	 *                 "Clustering Validity Assessment: Finding the Optimal Partitioning of a Data
	 *                 Set", booktitle = "Proceedings of the IEEE International Conference on Data
	 *                 Mining", year = "2001", isbn = "0-7695-1119-8", pages = "187--194", publisher =
	 *                 "IEEE Computer Society", address = "Washington, DC, USA" }
	 */
	public Vector getSetMean(ArrayList<Pattern> set) {
		if (set.isEmpty())
			throw new IllegalArgumentException("Cannot calculate the mean for an empty set");

		Vector mean = null;

		for (Pattern pattern : set) {
			if (mean == null) {		// initialise the mean to be all zeroes
				mean = pattern.data.clone();
				mean.reset();
			}
			mean = mean.plus(pattern.data);
		}
		return mean.divide(set.size());
	}

	/**
	 * Calculates the variance {@linkplain Vector} of the given set / cluster as shown in Equation
	 * 4.c of:<br/>
	 * @InProceedings{ 657864, author = "Maria Halkidi and Michalis Vazirgiannis", title =
	 *                 "Clustering Validity Assessment: Finding the Optimal Partitioning of a Data
	 *                 Set", booktitle = "Proceedings of the IEEE International Conference on Data
	 *                 Mining", year = "2001", isbn = "0-7695-1119-8", pages = "187--194", publisher =
	 *                 "IEEE Computer Society", address = "Washington, DC, USA" }
	 */
	public Vector getSetVariance(ArrayList<Pattern> set) {
		if (set.isEmpty())
			throw new IllegalArgumentException("Cannot calculate the variance for an empty set");

		Vector mean = getSetMean(set);
		Vector variance = mean.clone();

		variance.reset();		// initialize the variance to be all zeroes
		for (Pattern pattern : set) {
			Vector diffSquare = pattern.data.subtract(mean);
			diffSquare = diffSquare.multiply(diffSquare);
			variance = variance.plus(diffSquare);
		}
		return variance.divide(set.size());
	}

	/**
	 * Calculates the mean {@linkplain Vector} of the entire dataset as shown in Equation 4.b of:<br/>
	 * @InProceedings{ 657864, author = "Maria Halkidi and Michalis Vazirgiannis", title =
	 *                 "Clustering Validity Assessment: Finding the Optimal Partitioning of a Data
	 *                 Set", booktitle = "Proceedings of the IEEE International Conference on Data
	 *                 Mining", year = "2001", isbn = "0-7695-1119-8", pages = "187--194", publisher =
	 *                 "IEEE Computer Society", address = "Washington, DC, USA" }
	 */
	public Vector getMean() {
		return getSetMean(patterns);
	}

	/**
	 * Calculates the variance {@linkplain Vector} of the entire dataset as shown in Equation 4.a of:<br/>
	 * @InProceedings{ 657864, author = "Maria Halkidi and Michalis Vazirgiannis", title =
	 *                 "Clustering Validity Assessment: Finding the Optimal Partitioning of a Data
	 *                 Set", booktitle = "Proceedings of the IEEE International Conference on Data
	 *                 Mining", year = "2001", isbn = "0-7695-1119-8", pages = "187--194", publisher =
	 *                 "IEEE Computer Society", address = "Washington, DC, USA" }
	 */
	public Vector getVariance() {
		return getSetVariance(patterns);
	}
}
