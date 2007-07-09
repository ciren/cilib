package net.sourceforge.cilib.type.types.container;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.cilib.container.visitor.PreOrderVisitorDecorator;
import net.sourceforge.cilib.container.visitor.PrePostVisitor;
import net.sourceforge.cilib.container.visitor.Visitor;

public class GeneralTree<E extends Comparable<E>> implements Tree<E> {
	private static final long serialVersionUID = 3453326928796685749L;
	
	private E key;
	private List<Tree<E>> nodes;
	
	public GeneralTree() {
		key = null;
		nodes = new ArrayList<Tree<E>>();
	}
	
	public GeneralTree(E element) {
		this();
		this.key = element;
	}
	
	public GeneralTree<E> clone() {
		return null;
	}

	public boolean addSubtree(Tree<E> subtree) {
		if (subtree == null)
			throw new IllegalArgumentException("Cannot add a null object as a child of a tree");
	
		if (getKey() == null)
			throw new IllegalStateException("Cannot add a subtree to a tree with a null for the key value");
		
		this.nodes.add(subtree);
		return true;
	}

	/**
	 * The number of edges eminating from this tree node. Also known as the
	 * degree of the tree.
	 */
	public int edges() {
		return this.nodes.size();
	}

	public int verticies() {
		return 0;
	}

	/**
	 * Create an empty tree object and add it to the current tree node.
	 */
	public boolean add(E element) {
		Tree<E> subTree = new GeneralTree<E>(element);
		return addSubtree(subTree);
	}

	public void clear() {
		this.nodes.clear();
	}

	public boolean contains(E element) {
		for (Tree<E> e : this.nodes) {
			if (e.getKey().equals(element))
				return true;
		}

		return false;
	}

	public boolean isEmpty() {
		return this.key == null;
	}

	public boolean remove(E element) {
		for (Tree<E> tree : this.nodes) {
			if (tree.getKey().equals(this.key)) {
				this.nodes.remove(tree);
				return true;
			}
		}
		
		return false;
	}
	
	// TODO: Implement this method 
	public E remove(int index) {
		if (index >= this.nodes.size())
			throw new IndexOutOfBoundsException("");
		
		return this.nodes.remove(index).getKey();
	}

	public boolean remove(Tree<E> subTree) {
		return this.remove(subTree.getKey());
	}

	public int size() {
		return this.nodes.size();
	}

	public int getDimension() {
		return size();
	}

	public String getRepresentation() {
		// TODO Auto-generated method stub
		return null;
	}

	public void randomise() {
		// TODO Auto-generated method stub
		
	}

	public void reset() {
		// TODO Auto-generated method stub
		
	}

	public boolean addEdge(E a, E b) {
		return false;
	}

	public boolean isConnected(E a, E b) {
		return false;
	}

	public void accept(Visitor<E> visitor) {
		PreOrderVisitorDecorator<E> preOrder = new PreOrderVisitorDecorator<E>(visitor);
		depthFirstTraversal(preOrder);
	}
	
	private void depthFirstTraversal(PrePostVisitor<E> visitor) {
		if (visitor.isDone()) {
			return;
		}
		
		if (!isEmpty()) {
			visitor.preVisit(getKey());
			for (int i = 0; i < nodes.size(); i++) {
				GeneralTree<E> t = (GeneralTree<E>) this.nodes.get(i);
				System.out.println("traversting" + t.getKey());
				t.depthFirstTraversal(visitor);
			}
			
			visitor.postVisit(getKey());
		}	
	}

	public boolean addAll(Structure<E> structure) {
		for (E e : structure)
			add(e);

		return true;
	}

	public Iterator<E> iterator() {
		return null;
	}

	public boolean removeAll(Structure<E> structure) {
		for (E e : structure) {
			this.remove(e);
		}
		return true;
	}

	public boolean isInsideBounds() {
		// TODO Auto-generated method stub
		return false;
	}

	public E getKey() {
		return this.key;
	}

	public Tree<E> getSubtree(E element) {
		for (Tree<E> tree : this.nodes) {
			if (tree.getKey().equals(element))
				return tree;
		}

		return null;
	}

}