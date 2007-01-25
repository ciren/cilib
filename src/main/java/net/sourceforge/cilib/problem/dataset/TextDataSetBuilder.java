package net.sourceforge.cilib.problem.dataset;

public abstract class TextDataSetBuilder extends DataSetBuilder {

	public abstract void initialise();
	
	/**
	 * Get the length of the shortest string contained in the processed
	 * <tt>DataSet</tt> objects. 
	 * @return The length of the shortest string within this <tt>TextDataSetBuilder</tt>.
	 */
	public abstract String getShortestString();
	
	/**
	 * Get the length of the longest string contained in the processed
	 * <tt>DataSet</tt> objects.
	 * @return The length of the longest string within this <tt>TextDataSetBuilder</tt>.
	 */
	public abstract String getLongestString();
	
	
	public abstract int size();
	
	public abstract String get(int index);

}
