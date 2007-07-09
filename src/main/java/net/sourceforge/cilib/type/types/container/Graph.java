package net.sourceforge.cilib.type.types.container;

import net.sourceforge.cilib.type.types.Type;

public interface Graph<E> extends Structure<E>, Type {
	
	public int verticies();
	
	public int edges();
	
	public boolean addEdge(E a, E b);
	
	public boolean isConnected(E a, E b);

}
