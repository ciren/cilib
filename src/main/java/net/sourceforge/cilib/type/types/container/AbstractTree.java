package net.sourceforge.cilib.type.types.container;

import net.sourceforge.cilib.container.Queue;
import net.sourceforge.cilib.container.visitor.PreOrderVisitorDecorator;
import net.sourceforge.cilib.container.visitor.PrePostVisitor;
import net.sourceforge.cilib.container.visitor.Visitor;
import net.sourceforge.cilib.type.types.AbstractType;

public abstract class AbstractTree<E extends Comparable<E>> extends AbstractType implements Tree<E> {

	protected E key;

	@Override
	public void breadthFirstTraversal(Visitor<E> visitor) {
		Queue<Tree<E>> queue = new Queue<Tree<E>>();
		
		if (!isEmpty())
			queue.enqueue(this);
		
		while (!queue.isEmpty() && !visitor.isDone()) {
			Tree<E> head = queue.dequeue();
			visitor.visit(head.getKey());
			for (int i = 0; i < head.size(); i++) {
				Tree<E> child = head.getSubTree(i);
				if (!child.isEmpty())
					queue.enqueue(child);
			}
		}
	}

	@Override
	public void depthFirstTraversal(PrePostVisitor<E> visitor) {
		if (visitor.isDone())
			return;
		if (!isEmpty()) {
			visitor.preVisit(getKey());
			for (int i = 0; i < this.size(); i++) {
				this.getSubTree(i).depthFirstTraversal(visitor);
			}
			visitor.postVisit(getKey());
		}
	}

	@Override
	public void accept(Visitor<E> visitor) {
		depthFirstTraversal(new PreOrderVisitorDecorator<E>(visitor));		
	}

	@Override
	public E getKey() {
		if (isEmpty())
			throw new UnsupportedOperationException("Empty trees do not have valid keys");
		
		return this.key;
	}

	@Override
	public boolean isEmpty() {
		return this.key == null;
	}

	@Override
	public boolean addEdge(E a, E b) {
		throw new UnsupportedOperationException("Arbitary adding of edges is not allowed for Tree structures");
	}

	@Override
	public int vertices() {
		throw new UnsupportedOperationException("The number of vertices is not a valid operation for Tree node structures - default value is 1");
	}

}
