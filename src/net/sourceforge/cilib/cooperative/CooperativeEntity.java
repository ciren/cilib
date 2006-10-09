package net.sourceforge.cilib.cooperative;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.problem.InferiorFitness;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.type.types.MixedVector;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.Vector;

/**
 * 
 * @author Theuns Cloete
 */
public class CooperativeEntity implements Entity {
	protected MixedVector context = null;
	protected Fitness fitness = null;

	public CooperativeEntity() {
		context = new MixedVector();
		fitness = InferiorFitness.instance();
	}
	
	public CooperativeEntity(CooperativeEntity rhs) {
		context = rhs.context.clone();
		fitness = rhs.fitness;
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
		if(value instanceof Vector || value instanceof MixedVector)
			context.append((MixedVector)value);
		else
			context.append(value);
	}
	
	public void append(Entity entity) {
		append(entity.get());
	}
	
	public void update(Entity src, int srcPos, int dstPos, int length) {
		for(int i = dstPos; i < dstPos + length; ++i) {
			context.setReal(i, ((MixedVector)src.get()).getReal(srcPos + i - dstPos));
		}
	}
	
	public Type get() {
		return context;
	}

	public void set(Type type) {
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
		context = (MixedVector)problem.getDomain().getBuiltRepresenation().clone();
	}

	public Type getBehaviouralParameters() {
		// TODO I don't think we need this
		throw new UnsupportedOperationException("What exactly are Behvioural Parameters?");
	}

	public void setBehaviouralParameters(Type behaviouralParameters) {
		//TODO I don't think we need this
		throw new UnsupportedOperationException("What exactly are Behvioural Parameters?");
	}

	public void reinitialise() {
		// TODO Auto-generated method stub
		
	}
}
