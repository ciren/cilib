package net.sourceforge.cilib.type.types.container;

import static org.junit.Assert.assertEquals;

import net.sourceforge.cilib.container.visitor.Visitor;
import net.sourceforge.cilib.type.types.container.GeneralTree;
import net.sourceforge.cilib.type.types.container.Tree;

import org.junit.Test;

public class GeneralTreeTest {
	
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
	
	@Test
	public void preOrderVisitorTraversal() {
		Tree<Double> tree = new GeneralTree<Double>(0.0);
		tree.add(1.0);
		tree.add(2.0);
		tree.add(3.0);
		
		assertEquals(3, tree.size());
		
		Tree<Double> child1 = tree.getSubtree(1.0);
		child1.add(4.0);
		
		assertEquals(1, child1.size());
		
		StringBuffer buffer = new StringBuffer();
		PrintingVisitor<Double> visitor = new PrintingVisitor<Double>(buffer);
		
		tree.accept(visitor);
		
		assertEquals("0.0,1.0,4.0,2.0,3.0", buffer.toString());
	}
	
	private class PrintingVisitor<E> extends Visitor<E> {
		private StringBuffer buffer;
		
		public PrintingVisitor(StringBuffer buffer) {
			this.buffer = buffer;
		}

		@Override
		public void visit(E o) {
			if (buffer.length() != 0)
				buffer.append(",");
			
			buffer.append(o.toString());			
		}
	}

}
