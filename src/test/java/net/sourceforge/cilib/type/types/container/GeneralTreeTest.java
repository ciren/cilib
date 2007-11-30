package net.sourceforge.cilib.type.types.container;

import static org.junit.Assert.assertEquals;
import net.sourceforge.cilib.container.visitor.PrePostVisitor;
import net.sourceforge.cilib.type.types.Real;

import org.junit.Test;

public class GeneralTreeTest {
	
	@Test
	public void creation() {
		Tree<Real> tree = new GeneralTree<Real>(new Real(3.0));
		
		assertEquals(0, tree.edges());
		
		tree.add(new Real(1.0));
		tree.add(new Real(2.0));
		
		assertEquals(2, tree.edges());
		
		Tree<Real> child = tree.getSubTree(new Real(2.0));
		assertEquals(0, child.edges());
	}
	
	@Test
	public void preOrderVisitorTraversal() {
		Tree<Real> tree = new GeneralTree<Real>(new Real(0.0));
		tree.add(new Real(1.0));
		tree.add(new Real(2.0));
		tree.add(new Real(3.0));
		
		assertEquals(3, tree.size());
		
		Tree<Real> child1 = tree.getSubTree(new Real(1.0));
		child1.add(new Real(4.0));
		
		assertEquals(1, child1.size());
		
		StringBuffer buffer = new StringBuffer();
		PrintingVisitor<Real> visitor = new PrintingVisitor<Real>(buffer);
		
		tree.accept(visitor);
		
		assertEquals("0.0,1.0,4.0,2.0,3.0", buffer.toString());
	}
	
	private class PrintingVisitor<E> extends PrePostVisitor<E> {
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
