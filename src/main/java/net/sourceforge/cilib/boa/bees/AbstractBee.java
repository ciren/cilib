/**
 * 
 */
package net.sourceforge.cilib.boa.bees;

import net.sourceforge.cilib.boa.positionupdatestrategies.BeePositionUpdateStrategy;
import net.sourceforge.cilib.boa.positionupdatestrategies.VisualPositionUpdateStategy;
import net.sourceforge.cilib.entity.AbstractEntity;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.operators.selection.RandomSelectionStrategy;
import net.sourceforge.cilib.entity.operators.selection.SelectionStrategy;
import net.sourceforge.cilib.problem.InferiorFitness;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * The entity class for the ABC algortihm that represents the bees
 * @author Andrich
 *
 */
public abstract class AbstractBee extends AbstractEntity implements HoneyBee {
	private static final long serialVersionUID = 7005546673802814268L;

	protected BeePositionUpdateStrategy positionUpdateStrategy;
	protected SelectionStrategy targetSelectionStrategy;
	protected int dimension;

	/**
	 * Default contstructor
	 */
	public AbstractBee() {
		this.positionUpdateStrategy = new VisualPositionUpdateStategy();
		this.targetSelectionStrategy = new RandomSelectionStrategy();
	}

	/**
	 * Copy constructor
	 * @param copy the reference of the bee that is deep copied
	 */
	public AbstractBee(AbstractBee copy) {
		super(copy);
		this.positionUpdateStrategy = copy.getPositionUpdateStrategy().getClone();
		this.targetSelectionStrategy = copy.targetSelectionStrategy;
		this.dimension = copy.dimension;
	}

	@Override
	public abstract AbstractBee getClone();

	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.abc.bees.HoneyBee#getPositionUpdateStrategy()
	 */
	@Override
	public BeePositionUpdateStrategy getPositionUpdateStrategy() {
		return this.positionUpdateStrategy;
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.abc.bees.HoneyBee#updatePosition()
	 */
	@Override
	public abstract void updatePosition();

	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.entity.Entity#calculateFitness()
	 */
	@Override
	public void calculateFitness() {
		this.calculateFitness(true);
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.entity.Entity#calculateFitness(boolean)
	 */
	@Override
	public void calculateFitness(boolean count) {
		this.getProperties().put(EntityType.FITNESS, getFitnessCalculator().getFitness(this, count));
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.entity.Entity#compareTo(net.sourceforge.cilib.entity.Entity)
	 */
	@Override
	public int compareTo(Entity o) {
		return getFitness().compareTo(o.getFitness());
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.entity.Entity#getDimension()
	 */
	@Override
	public int getDimension() {
		return this.dimension;
	}
	
	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.abc.bees.HoneyBee#getPosition()
	 */
	public Vector getPosition() {
		return (Vector)this.getCandidateSolution();
	}
	
	public void setPosition(Vector position) {
		this.setCandidateSolution(position);
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.entity.Entity#initialise(net.sourceforge.cilib.problem.OptimisationProblem)
	 */
	@Override
	public void initialise(OptimisationProblem problem) {
		this.setCandidateSolution((Type) problem.getDomain().getBuiltRepresenation().getClone());
		this.getCandidateSolution().randomise();

		this.dimension = this.getCandidateSolution().getDimension();
		this.getProperties().put(EntityType.FITNESS, InferiorFitness.instance());
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.entity.Entity#reinitialise()
	 */
	@Override
	public void reinitialise() {
		throw new UnsupportedOperationException("Reinitialise not implemented for AbstractBee");
	}

}
