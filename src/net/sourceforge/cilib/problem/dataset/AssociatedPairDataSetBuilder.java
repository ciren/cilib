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
 *
 */
package net.sourceforge.cilib.problem.dataset;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import net.sourceforge.cilib.container.Pair;
import net.sourceforge.cilib.type.types.Int;
import net.sourceforge.cilib.type.types.MixedVector;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Vector;
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
 * TODO: Make the key of a pair also able to be a StringType
 * TODO: specify format of file generically
 */
public class AssociatedPairDataSetBuilder extends DataSetBuilder implements ClusterableDataSet {
	//The datastructure used is an ArrayList that holds Pair entries
	protected ArrayList<Pair<Numeric, Vector>> keyPatternPair = null;
	//A DistanceMeasure will be used to calculate the distance between a pattern and a centroid 
	protected DistanceMeasure distanceMeasure = null;
	//the expert should specify how many clusters there should be
	protected int numberOfClusters = 0;
	//the expert can also specify how many classes there are (although this is not really neccessary)
	protected int numberOfClasses = 0;

	/**
	 * Initialise the keyPatternPair data structure using the {@link net.sourceforge.cilib.util.EuclideanDistanceMeasure} as the default distance
	 * measure
	 */
	public AssociatedPairDataSetBuilder() {
		super();
		keyPatternPair = new ArrayList<Pair<Numeric, Vector>>();
		distanceMeasure = new EuclideanDistanceMeasure();
	}

	/**
	 * Construct the data structure (keyPatternPair) that will contain all the patterns of all the datasets that exist
	 */
	public void initialise() {
		//run through all the datasets specified in the XML file
		for(DataSet dataset : this) {
			//every dataset is represented by a file on disk
			BufferedReader br = new BufferedReader(new InputStreamReader(dataset.getInputStream()));
			try {
				//every line in a dataset represents a pattern
				String line = br.readLine();
				while (line != null) {
					addToDataSet(line, dataset);
					line = br.readLine();
				}
			}
			catch (IOException io) {
				throw new RuntimeException(io);
			}
		}
	}
	
	/**
	 * Parse the given line using the given dataset's patternExpression and add it to keyPatternPair
	 * @param line a String representing one line of the DataSet
	 * @param dataset the DataSet in which the given line resides
	 */
	private void addToDataSet(String line, DataSet dataset) {
		//split the received line using the regular expression given in the XML file (or 'patternExpression' in {@link net.sourceforge.cilib.problem.dataset.DataSet})
		String [] elements = line.split(dataset.getPatternExpression());
		Vector vector = new MixedVector();
		//the elements of the split are stored inside a vector that will form the pattern 
		for(String element : elements) {
			vector.add(new Real(Double.parseDouble(element)));
		}
		//the pattern is added to the "dataset"
		keyPatternPair.add(new Pair<Numeric, Vector>(new Real(0.0), vector));
	}

	/**
	 * Update pattern assignations, i.e. determine in which cluster a pattern belongs based on its closest centroid by running through the entire
	 * centroids vector
	 * @param centroids The vector representing the centroid vectors
	 */
	public void assign(Vector centroids) {
		Vector pattern = null;
		Vector centroid = null;
		//run through all the patterns in the dataset
		for(int i = 0; i < getNumberOfPatterns(); i++) {
			//the i'th pattern will be compared
			pattern = getPattern(i);
			//reset the minimum distance between pattern and the other centroids to be the largets double possible
			double minimum = Double.MAX_VALUE;
			//run through all the different clusters
			for(int j = 0; j < numberOfClusters; j++) {
				//extract the i'th centroid from the given centroids Vector that contains all the centroids
				centroid = getSubCentroid(centroids, j);
				//calculate the distances between the i'th pattern and the j'th centroid
				double distance = distanceMeasure.distance(centroid, pattern);
				//remember what the minimum distance is so far
				if(distance < minimum) {
					minimum = distance;
					//assign pattern i to cluster j
					setKey(i, new Int(j));
				}
			}
		}
	}

	/**
	 * Build up a list (Vector) of all the patterns that belong to cluster 'key'
	 * @param key the {@link net.sourceforge.cilib.type.types.Numeric} type that represents the cluster (number)
	 * @return a vector containing all the patterns that belong to cluster 'key'
	 */
	public Vector patternsInCluster(Numeric key) {
		Vector patterns = new MixedVector();
		//run through all the patterns in the dataset
		for(int j = 0; j < getNumberOfPatterns(); j++) {
			//check whether the patterns 'key' is the same as the cluster number (cluster's 'key')
			if(getKey(j).getInt() == key.getInt()) {
				//add the pattern to the list of patterns
				patterns.add(getPattern(j));
			}
		}
		return patterns;
	}
	
	/**
	 * Get the pattern that is represented by the given index
	 * @param index the index representing a pattern in the keyPatternPair ArrayList
	 * @return the pattern represented by index as a Vector
	 */
	public Vector getPattern(int index) {
		return this.keyPatternPair.get(index).getValue();
	}
	
	/**
	 * Get the key that represents a cluster for the given index that represents a pattern
	 * @param index the index representing a pattern in the keyPatternPair ArrayList
	 * @return the Numeric that represents the cluster to which the requested pattern has been assigned
	 */
	public Numeric getKey(int index) {
		return this.keyPatternPair.get(index).getKey();
	}
	
	/**
	 * Assign the pattern represented by index to the given key that represent a cluster
	 * @param index the index representing a pattern in the keyPatternPair ArrayList
	 * @param key the Numeric to which the key for the pattern represented by index should be set
	 */
	public void setKey(int index, Numeric key) {
		this.keyPatternPair.get(index).setKey(key);
	}

	/**
	 * Determine how many patterns are in the dataset(s)
	 * @return the size of the keyPatternPair ArrayList
	 */
	public int getNumberOfPatterns() {
		return keyPatternPair.size();
	}

	public ArrayList<Pair<Numeric, Vector>> getKeyPatternPair() {
		return keyPatternPair;
	}
	
	/**
	 * Set the type of DistanceMeasure that should be used for determining the distance between two Vectors.
	 * @param distanceMeasure any class that inherits from DistanceMeasure can be used
	 */
	public void setDistanceMeasure(DistanceMeasure distanceMeasure) {
		this.distanceMeasure = distanceMeasure;
	}
	
	/**
	 * Get the type of DistanceMeasure that should be used for determining the distance between two Vectors.
	 * @return a DistanceMeasure object
	 */
	public DistanceMeasure getDistanceMeasure() {
		return this.distanceMeasure;
	}
	
	/**
	 * Set the number of clusters that exist in the dataset that will be optimised.
	 * @param clusters the value that numberOfClusters will be set to
	 */
	public void setNumberOfClusters(int clusters) {
		if(clusters < 1)
			throw new IllegalArgumentException("The number of clusters cannot be less than 1");
		numberOfClusters = clusters;
	}
	
	public int getNumberOfClusters() {
		return numberOfClusters;
	}
	
	public void setNumberOfClasses(int classes) {
		if(classes < 1)
			throw new IllegalArgumentException("The number of classes cannot be less than 1");
		numberOfClasses = classes;
	}
	
	public int getNumberOfClasses() {
		return numberOfClasses;
	}

	/**
	 * Determine which part of the centroids vector (i.e. which centroid) the patterns should be compared to
	 * @param centroids the current Vector that contains all the cluster centroids
	 * @param cluster the cluster number that should be returned
	 * @return a vector representing the specific centroid in the given centroids Vector
	 */
	public Vector getSubCentroid(Vector centroids, int cluster) {
		int dimension = centroids.size() / numberOfClusters;
		return (Vector)centroids.subVector(cluster * dimension, ((cluster * dimension) + dimension) - 1);
	}
}
