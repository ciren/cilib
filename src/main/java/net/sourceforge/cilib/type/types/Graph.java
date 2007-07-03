package net.sourceforge.cilib.type.types;

public interface Graph<E> extends Structure<E>, Type {
	
	public int verticies();
	
	public int edges();
	
	public boolean addEdge(E a, E b);
	
	public boolean isConnected(E a, E b);

}
