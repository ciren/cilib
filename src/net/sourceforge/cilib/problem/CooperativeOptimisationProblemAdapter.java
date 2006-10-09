package net.sourceforge.cilib.problem;

import net.sourceforge.cilib.algorithm.InitialisationException;
import net.sourceforge.cilib.cooperative.CooperativeEntity;
import net.sourceforge.cilib.type.DomainParser;
import net.sourceforge.cilib.type.DomainRegistry;
import net.sourceforge.cilib.type.types.MixedVector;

/**
 * TODO test this class
 * @author Theuns Cloete
 */
public class CooperativeOptimisationProblemAdapter extends OptimisationProblemAdapter {
	private OptimisationProblem problem = null;
	private CooperativeEntity context = null;
	private DomainRegistry domainRegistry = null;
	private int dimension = 0;
	private int offset = 0;

	/**
	 * Creates an OptimisationProblemAdapter that has the specified dimension starting at the given offset (index) position of the full given problem.
	 * @param problem the full problem that is being split up
	 * @param dimension the dimension that that this sub-problem should be
	 * @param offset the offset (index) position in the full-problem where this sub-problem should start
	 */
	public CooperativeOptimisationProblemAdapter(OptimisationProblem p, CooperativeEntity c, int d, int o) {
		problem = p;
		context = c.clone();
		dimension = d;
		offset = o;
		domainRegistry = new DomainRegistry();
		String expandedDomain = "";
		for(int i = offset; i < offset + dimension; i++) {
			expandedDomain += ((MixedVector)context.get()).get(i).getRepresentation();
			if(i < offset + dimension - 1)
				expandedDomain += ",";
		}
		DomainParser dp = DomainParser.getInstance();
		if(dp.parse(expandedDomain)) {
			domainRegistry.setDomainString(expandedDomain);
			domainRegistry.setExpandedRepresentation(expandedDomain);
			domainRegistry.setBuiltRepresenation(dp.getBuiltRepresentation());
		}
		else throw new InitialisationException("The expanded domain string \"" + expandedDomain + "\" could not be parsed.");
	}

	public int getDimension() {
		return dimension;
	}

	public int getOffset() {
		return offset;
	}

	public void updateContext(CooperativeEntity c) {
		context = c.clone();
	}

	@Override
	protected Fitness calculateFitness(Object solution) {
		MixedVector participant = (MixedVector) solution;
		for(int i = 0; i < dimension; ++i) {
			((MixedVector)context.get()).setReal(offset + i, participant.getReal(i));
		}
		return problem.getFitness(context.get(), true);
	}

	public DomainRegistry getDomain() {
		return domainRegistry;
	}

	public DomainRegistry getBehaviouralDomain() {
		//QUESTION What exactly does the problem.getBehaviouralDomain() method return and what is really needed?
		return domainRegistry;
	}
}
