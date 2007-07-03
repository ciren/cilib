package net.sourceforge.cilib.type.types;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sourceforge.cilib.container.visitor.Visitor;

public class StandardGraph<E extends Comparable<E>> implements Graph<E> {
	
	private static final long serialVersionUID = -5517089079342858152L;
	private Map<E, List<E>> adjacencyMap;
	
	public StandardGraph() {
		adjacencyMap = new LinkedHashMap<E, List<E>>();
	}
	
	public StandardGraph clone() {
		return null;
	}

	public int edges() {
		int count = 0;
		
		Collection<List<E>> edgeLists = this.adjacencyMap.values();
		for (List<E> list : edgeLists) {
			count += list.size();
		}
		
		return count;
	}

	public int verticies() {
		return this.adjacencyMap.size();
	}
	
	public boolean addEdge(E a, E b) {
		if (!contains(a)) return false;
		if (!contains(b)) return false;
		
		List<E> connectedVerticies = this.adjacencyMap.get(a);
		connectedVerticies.add(b);
		
		return true;
	}

	public boolean isConnected(E a, E b) {
		if (!contains(a)) return false;
		if (!contains(b)) return false;
		
		List<E> connectedVerticies = this.adjacencyMap.get(a);
		return connectedVerticies.contains(b);
	}

	public void accept(Visitor<E> visitor) {
		throw new UnsupportedOperationException("accept() for the class " + getClass().getName() + " still needs an implementation");		
	}

	public boolean add(E element) {
		if (this.adjacencyMap.containsKey(element))
			return false;
		
		this.adjacencyMap.put(element, new LinkedList<E>());
		return true;
	}

	public void clear() {
		this.adjacencyMap.clear();
	}

	public boolean contains(E element) {
		return this.adjacencyMap.containsKey(element);
	}

	public boolean isEmpty() {
		return this.adjacencyMap.isEmpty();
	}

	public Iterator<E> iterator() {
		return this.adjacencyMap.keySet().iterator();
	}

	public boolean remove(E element) {
		if (!this.adjacencyMap.containsKey(element))
			return false;
		
		this.adjacencyMap.remove(element);
		
		Collection<List<E>> lists = this.adjacencyMap.values();
		
		for (List<E> list : lists) {
			if (list.contains(element))
				list.remove(element);
		}
		
		return true;
	}
	
	public E remove(int index) {
		int count = 0;
		for (Map.Entry<E, List<E>> e : this.adjacencyMap.entrySet()) {
			if (count == index) {
				this.adjacencyMap.remove(e.getKey());
				return e.getKey();
			}
			
			count++;				
		}
		
		return null;
	}

	public int size() {
		return this.adjacencyMap.size();
	}

	public int getDimension() {
		return this.verticies();
	}

	public String getRepresentation() {
		throw new UnsupportedOperationException("getRepresentation() is not supportd");
	}

	public void randomise() {
		throw new UnsupportedOperationException("randomise() is not supportd");		
	}

	public void reset() {
		throw new UnsupportedOperationException("reset() is not supportd");	
	}

	public boolean addAll(Structure<E> structure) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean removeAll(Structure<E> structure) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isInsideBounds() {
		// TODO Auto-generated method stub
		return false;
	}

}
