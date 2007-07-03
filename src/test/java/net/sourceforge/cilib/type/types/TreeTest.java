package net.sourceforge.cilib.type.types;

import static org.junit.Assert.assertEquals;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.cilib.container.visitor.Visitor;

import org.junit.Test;

public class TreeTest {
	
	@Test
	public void creation() {
		Tree<Double> tree = new GeneralTree<Double>(3.0);
		
		assertEquals(0, tree.edges());
		
		tree.add(1.0);
		tree.add(2.0);
		
		assertEquals(2, tree.edges());
		
		Tree<Double> child = tree.getSubtree(2.0);
		assertEquals(0, child.edges());
	}
	
	public class GeneralTreeMock<E extends Comparable<E>> implements Tree<E> {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public GeneralTreeMock<E> clone() {
			return null;
		}

		public boolean add(E parent, E element) {
			// TODO Auto-generated method stub
			return false;
		}

		public int edges() {
			// TODO Auto-generated method stub
			return 0;
		}

		public int verticies() {
			// TODO Auto-generated method stub
			return 0;
		}

		public boolean add(E element) {
			// TODO Auto-generated method stub
			return false;
		}

		public void clear() {
			// TODO Auto-generated method stub
			
		}

		public boolean contains(E element) {
			// TODO Auto-generated method stub
			return false;
		}

		public boolean isEmpty() {
			// TODO Auto-generated method stub
			return false;
		}


		public boolean remove(E element) {
			// TODO Auto-generated method stub
			return false;
		}
		
		public E remove(int index) {
			return null;
		}

		public int size() {
			// TODO Auto-generated method stub
			return 0;
		}

		public int getDimension() {
			// TODO Auto-generated method stub
			return 0;
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

		public List<E> getChildren(E key) {
			// TODO Auto-generated method stub
			return null;
		}

		public boolean addEdge(E a, E b) {
			// TODO Auto-generated method stub
			return false;
		}

		public boolean isConnected(E a, E b) {
			// TODO Auto-generated method stub
			return false;
		}

		public int arity() {
			// TODO Auto-generated method stub
			return 0;
		}

		public boolean addSubtree(Tree<E> subTree) {
			// TODO Auto-generated method stub
			return false;
		}

		public void addSubtree(E subTree) {
			// TODO Auto-generated method stub
			
		}

		public boolean addEdge(Tree<E> a, Tree<E> b) {
			// TODO Auto-generated method stub
			return false;
		}

		public boolean isConnected(Tree<E> a, Tree<E> b) {
			// TODO Auto-generated method stub
			return false;
		}

		public void accept(Visitor<E> visitor) {
			// TODO Auto-generated method stub
			
		}

		public boolean add(Tree<E> element) {
			// TODO Auto-generated method stub
			return false;
		}

		public boolean addAll(Structure<E> structure) {
			// TODO Auto-generated method stub
			return false;
		}

		public boolean contains(Tree<E> element) {
			// TODO Auto-generated method stub
			return false;
		}

		public Iterator<E> iterator() {
			// TODO Auto-generated method stub
			return null;
		}

		public Tree<E> remove(Tree<E> element) {
			// TODO Auto-generated method stub
			return null;
		}

		public boolean removeAll(Structure<E> structure) {
			// TODO Auto-generated method stub
			return false;
		}

		public boolean isInsideBounds() {
			// TODO Auto-generated method stub
			return false;
		}

		public E getKey() {
			// TODO Auto-generated method stub
			return null;
		}

		public Tree<E> getSubtree(E element) {
			// TODO Auto-generated method stub
			return null;
		}

			
	}

}
