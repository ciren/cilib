/*
 * Copyright (C) 2003 - 2008
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package net.sourceforge.cilib.problem;

import net.sourceforge.cilib.algorithm.InitialisationException;
import net.sourceforge.cilib.cooperative.CooperativeEntity;
import net.sourceforge.cilib.type.DomainParser;
import net.sourceforge.cilib.type.DomainRegistry;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * TODO test this class.
 * @author Theuns Cloete
 */
public class CooperativeOptimisationProblemAdapter extends OptimisationProblemAdapter {
	private static final long serialVersionUID = 6643809900184758346L;

	private OptimisationProblem problem = null;
	private CooperativeEntity context = null;
	private DomainRegistry domainRegistry = null;
	private int dimension = 0;
	private int offset = 0;

	/**
	 * Creates an OptimisationProblemAdapter that has the specified dimension starting at the given
	 * offset (index) position of the full given problem.
	 * @param problem the full problem that is being split up
	 * @param dimension the dimension that that this sub-problem should be
	 * @param offset the offset (index) position in the full-problem where this sub-problem should
	 *        start
	 */
	public CooperativeOptimisationProblemAdapter(OptimisationProblem p, CooperativeEntity c, int d, int o) {
		problem = p;
		context = c.getClone();
		dimension = d;
		offset = o;
		domainRegistry = new DomainRegistry();
		String expandedDomain = "";
		for (int i = offset; i < offset + dimension; i++) {
			expandedDomain += ((Vector) context.getContents()).get(i).getRepresentation();
			if (i < offset + dimension - 1)
				expandedDomain += ",";
		}
		DomainParser dp = DomainParser.getInstance();
		if (dp.parse(expandedDomain)) {
			domainRegistry.setDomainString(expandedDomain);
			domainRegistry.setExpandedRepresentation(expandedDomain);
			domainRegistry.setBuiltRepresenation(dp.getBuiltRepresentation());
		}
		else
			throw new InitialisationException("The expanded domain string \"" + expandedDomain + "\" could not be parsed.");
	}

	public CooperativeOptimisationProblemAdapter(CooperativeOptimisationProblemAdapter copy) {
		super(copy);
		problem = copy.problem.getClone();
		context = copy.context.getClone();
		domainRegistry = copy.domainRegistry.getClone();
		dimension = copy.dimension;
		offset = copy.offset;
	}

	public CooperativeOptimisationProblemAdapter getClone() {
		return new CooperativeOptimisationProblemAdapter(this);
	}

	public int getDimension() {
		return dimension;
	}

	public int getOffset() {
		return offset;
	}

	public void updateContext(CooperativeEntity c) {
		context = c.getClone();
	}

	@Override
	protected Fitness calculateFitness(Object solution) {
		Vector participant = (Vector) solution;
		for (int i = 0; i < dimension; ++i) {
			((Vector) context.getContents()).setReal(offset + i, participant.getReal(i));
		}
		return problem.getFitness(context.getContents(), true);
	}

	public DomainRegistry getDomain() {
		return domainRegistry;
	}

	public DomainRegistry getBehaviouralDomain() {
		// QUESTION What exactly does the problem.getBehaviouralDomain() method return and what is
		// really needed?
		return domainRegistry;
	}
}
