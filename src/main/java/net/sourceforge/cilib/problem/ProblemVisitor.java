package net.sourceforge.cilib.problem;

import net.sourceforge.cilib.container.visitor.Visitor;

public abstract class ProblemVisitor extends Visitor<Problem> {

	/**
	 * {@inheritDoc}
	 */
	public abstract void visit(Problem o);

}
