package net.sourceforge.cilib.type.types.container;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class BinaryTreeTest {
	
	private AbstractTree<Double> doubleTree;

	@Before
	public void setupBinaryTree() {
		doubleTree = new BinaryTree<Double>(1.0);
		doubleTree.addSubTree(new BinaryTree<Double>(2.0));
		doubleTree.addSubTree(new BinaryTree<Double>(3.0));
	}
	
	@Test
	public void getSubtreeWithValidIndex() {
		assertNotNull(doubleTree.getSubTree(0));
		assertNotNull(doubleTree.getSubTree(1));
	}
	
	@Test(expected = IndexOutOfBoundsException.class)
	public void getSubtreeWithInvalidIndex() {
		doubleTree.getSubTree(-1);
		doubleTree.getSubTree(2);
	}
	
	@Test
	public void removalOfSubtreeByKey() {
		Tree<Double> removed = doubleTree.removeSubTree(2.0);
		assertNotNull(removed);
		assertEquals(2.0, removed.getKey(), 0);
		assertTrue(doubleTree.getSubTree(2.0).isEmpty());
	}
	
	@Test
	public void removeSubTreeWithElement() {
		assertTrue(doubleTree.remove(2.0));
	}
	
	@Test
	public void removeSubTreeWithIndex() {
		assertNotNull(doubleTree.removeSubTree(0));
	}

}
