package net.sourceforge.cilib.cooperative;

import net.sourceforge.cilib.entity.AbstractEntity;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.problem.InferiorFitness;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.calculator.FitnessCalculator;
import net.sourceforge.cilib.util.calculator.VectorBasedFitnessCalculator;

/**
 * 
 * @author Theuns Cloete
 */
public class CooperativeEntity extends AbstractEntity {
	private static final long serialVersionUID = -8298684370426283216L;
	
	protected Vector context = null;
	protected Fitness fitness = null;
	protected FitnessCalculator fitnessCalculator;

	public CooperativeEntity() {
		context = new Vector();
		fitness = InferiorFitness.instance();
		fitnessCalculator = new VectorBasedFitnessCalculator();
	}
	
	public CooperativeEntity(CooperativeEntity rhs) {
		context = rhs.context.clone();
		fitness = rhs.fitness;
		fitnessCalculator = rhs.fitnessCalculator;//.clone();
	}

	public CooperativeEntity clone() {
		return new CooperativeEntity(this);
	}
	
	public void reset() {
		context.clear();
	}

	public int compareTo(Entity o) {
		return getFitness().compareTo(o.getFitness());
	}

	public void append(Type value) {
		if(value instanceof Vector)
			context.append((Vector)value);
		else
			context.append(value);
	}
	
	public void append(Entity entity) {
		append(entity.getContents());
	}
	
	public void update(Entity src, int srcPos, int dstPos, int length) {
		for(int i = dstPos; i < dstPos + length; ++i) {
			context.setReal(i, ((Vector)src.getContents()).getReal(srcPos + i - dstPos));
		}
	}
	
	public Type getContents() {
		return context;
	}

	public void setContents(Type type) {
		context.clear();
		append(type);
	}

	public int getDimension() {
		return context.getDimension();
	}

	public void setDimension(int dim) {
		throw new UnsupportedOperationException("The dimension of a CooperativeEntity is determined by its context");
	}

	public Fitness getFitness() {
		return fitness;
	}

	public void setFitness(Fitness f) {
		fitness = f;
	}

	public String getId() {
		throw new UnsupportedOperationException("If you want to use this, implement it");
	}
	
	public void setId(String i) {
		throw new UnsupportedOperationException("If you want to use this, implement it");
	}

	public void initialise(OptimisationProblem problem) {
		context = (Vector) problem.getDomain().getBuiltRepresenation().clone();
	}

	public void reinitialise() {
		// TODO Auto-generated method stub
		
	}
	
	public void calculateFitness() {
		calculateFitness(true);
	}

	public void calculateFitness(boolean count) {
		fitness = fitnessCalculator.getFitness(context, count);
	}
}
