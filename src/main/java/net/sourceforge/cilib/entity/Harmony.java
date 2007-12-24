package net.sourceforge.cilib.entity;

import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.problem.InferiorFitness;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.util.calculator.VectorBasedFitnessCalculator;

public class Harmony extends AbstractEntity {
	private static final long serialVersionUID = -4941414797957384798L;
	private VectorBasedFitnessCalculator calculator;
	
	public Harmony() {
		this.calculator = new VectorBasedFitnessCalculator();
	}
	
	public Harmony(Harmony copy) {
		super(copy);
		this.calculator = copy.calculator.getClone();
	}

	public void calculateFitness() {
		calculateFitness(true);
	}

	public void calculateFitness(boolean count) {
		Fitness fitness = calculator.getFitness(getContents(), count);
    	this.properties.put("fitness", fitness);
	}

	public int compareTo(Entity o) {
		return this.getFitness().compareTo(o.getFitness());
	}

	public Harmony getClone() {
		return new Harmony(this);
	}

	public int getDimension() {
		return getContents().getDimension();
	}

	public void initialise(OptimisationProblem problem) {
		Type harmony = problem.getDomain().getBuiltRepresenation().getClone();
		harmony.randomise();
		
		setContents(harmony);
		this.properties.put("fitness", InferiorFitness.instance());
	}

	public void reinitialise() {
		getContents().randomise();
	}

}
