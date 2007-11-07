package net.sourceforge.cilib.pso.velocityupdatestrategies;

import java.util.Random;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.problem.InferiorFitness;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * 
 * @TODO: The Rho value should be a vector to hold the rho value for each dimension!
 */
public class NewGCVelocityUpdateStrategy extends StandardVelocityUpdate {
	private static final long serialVersionUID = 5985694749940610522L;
	private Random randomNumberGenerator;
	private double rhoLowerBound = 1.0e-323;
	private double rho;
	private int successCount;
	private int failureCount;
	private int successCountThreshold;
	private int failureCountThreshold;
	
	private Fitness oldFitness;
	private double expandCoefficient;
	private double contractCoefficient;

	public NewGCVelocityUpdateStrategy() {
		super();
		randomNumberGenerator = new MersenneTwister();
		oldFitness = InferiorFitness.instance();
		
		rho = 1.0;
		successCount = 0;
		failureCount = 0;
		successCountThreshold = 15;
		failureCountThreshold = 5;
		expandCoefficient = 2.0;
		contractCoefficient = 0.5;
		
		vMax = new ConstantControlParameter(100.0); // This needs to be set dynamically - this is not valid for all problems
	}
	
	public NewGCVelocityUpdateStrategy(NewGCVelocityUpdateStrategy copy) {
		super(copy);
		this.randomNumberGenerator = copy.randomNumberGenerator;
		this.oldFitness = copy.oldFitness.clone();
		
		this.rho = copy.rho;
		this.successCount = copy.successCount;
		this.failureCount = copy.failureCount;
		this.successCountThreshold = copy.successCountThreshold;
		this.failureCountThreshold = copy.failureCountThreshold;
		this.expandCoefficient = copy.expandCoefficient;
		this.contractCoefficient = copy.contractCoefficient;
		
		this.vMax = copy.vMax.clone();
	}

	@Override
	public NewGCVelocityUpdateStrategy clone() {
		return new NewGCVelocityUpdateStrategy(this);
	}

	public void updateVelocity(Particle particle) {
		PSO pso = (PSO) Algorithm.get();
		final Particle globalBest = pso.getBestParticle();
		
		if (particle == globalBest) {
			final Vector velocity = (Vector) particle.getVelocity();
			final Vector position = (Vector) particle.getPosition();
//			final Vector pBestPosition = (Vector) particle.getBestPosition();
			final Vector nBestPosition = (Vector) particle.getNeighbourhoodBest().getPosition();
//			final Vector gbestPosition = (Vector) globalBest.getPosition();
			
			for (int i = 0; i < velocity.getDimension(); ++i) {
				double component = -position.getReal(i) + nBestPosition.getReal(i) 
					+ this.inertiaWeight.getParameter()*velocity.getReal(i)
					+ rho*(1-2*randomNumberGenerator.nextDouble());
				
				velocity.setReal(i, component);
				clamp(velocity, i);
			}
			
			oldFitness = particle.getFitness().clone(); // Keep a copy of the old Fitness object - particle.calculateFitness() within the IterationStrategy resets the fitness value
			return;
		}

		super.updateVelocity(particle);
	}
	
	public void updateControlParameters(Particle particle) {
		// Remember NOT to reset the rho value to 1.0
		PSO pso = (PSO) Algorithm.get();
		
		if (particle == pso.getBestParticle()) {
			Fitness newFitness = pso.getOptimisationProblem().getFitness(particle.getPosition(), false);
			
			if (!newFitness.equals(oldFitness)) {
				this.failureCount = 0;
				this.successCount++;
			}
			else {
				this.successCount = 0;
				this.failureCount++;
			}
			
			updateRho((Vector) particle.getPosition());
			return;
		}
		
		failureCount = 0;
		successCount = 0;
		super.updateControlParameters(particle);
	}
	
	/**
	 * Update the <code>rho</code> value.
	 * @param position
	 */
	private void updateRho(Vector position) { // the Rho value is problem and dimension dependent
		double tmp = 0.0;
		
		Numeric component = (Numeric) position.get(0);
		double average = (component.getUpperBound() - component.getLowerBound()) / expandCoefficient;
	
		if (successCount >= successCountThreshold) tmp = expandCoefficient*rho;
		if (failureCount >= failureCountThreshold) tmp = contractCoefficient*rho;
		
		if (tmp <= rhoLowerBound) tmp = rhoLowerBound;
		if (tmp >= average) tmp = average;
		
		rho = tmp;
	}

	public double getRhoLowerBound() {
		return rhoLowerBound;
	}

	public void setRhoLowerBound(double rhoLowerBound) {
		this.rhoLowerBound = rhoLowerBound;
	}

	public double getRho() {
		return rho;
	}

	public void setRho(double rho) {
		this.rho = rho;
	}

	public int getSuccessCountThreshold() {
		return successCountThreshold;
	}

	public void setSuccessCountThreshold(int successCountThreshold) {
		this.successCountThreshold = successCountThreshold;
	}

	public int getFailureCountThreshold() {
		return failureCountThreshold;
	}

	public void setFailureCountThreshold(int failureCountThreshold) {
		this.failureCountThreshold = failureCountThreshold;
	}

	public double getExpandCoefficient() {
		return expandCoefficient;
	}

	public void setExpandCoefficient(double expandCoefficient) {
		this.expandCoefficient = expandCoefficient;
	}

	public double getContractCoefficient() {
		return contractCoefficient;
	}

	public void setContractCoefficient(double contractCoefficient) {
		this.contractCoefficient = contractCoefficient;
	}
	
}
