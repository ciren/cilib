package net.sourceforge.cilib.pso.dynamic;

import net.sourceforge.cilib.problem.InferiorFitness;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.pso.PSO;

/**
 * Charged Particle used by charged PSO (ChargedVelocityUpdate). The only difference
 * from DynamicParticle is that a charged particle stores the charge magnitude and
 * the inialisation strategy for charge.
 * 
 * @author Anna Rakitianskaia
 *
 */
public class ChargedParticle extends DynamicParticle/*StandardParticle implements ReevaluatingParticle*/{
	private static final long serialVersionUID = 7872499872488908368L;
	private double charge;
	private ChargedParticleInitialisationStrategy chargedParticleInitialisationStrategy;
	
	public ChargedParticle() {
		super();
		velocityUpdateStrategy = new ChargedVelocityUpdateStrategy();
		chargedParticleInitialisationStrategy = new StandardChargedParticleInitialisationStrategy();
	}
	
	public ChargedParticle(ChargedParticle copy) {
		super(copy);
		
		this.charge = copy.charge;
		this.chargedParticleInitialisationStrategy = copy.chargedParticleInitialisationStrategy.clone();
	}
	
	public ChargedParticle clone() {
		return new ChargedParticle(this);
	}
	
	/**
	 * @return the charge
	 */
	public double getCharge() {
		return charge;
	}
	/**
	 * @param charge the charge to set
	 */
	public void setCharge(double charge) {
		this.charge = charge;
	}
	/**
	 * @return the chargedParticleInitialisationStrategy
	 */
	public ChargedParticleInitialisationStrategy getChargedParticleInitialisationStrategy() {
		return chargedParticleInitialisationStrategy;
	}
	/**
	 * @param chargedParticleInitialisationStrategy the chargedParticleInitialisationStrategy to set
	 */
	public void setChargedParticleInitialisationStrategy(
			ChargedParticleInitialisationStrategy chargedParticleInitialisationStrategy) {
		this.chargedParticleInitialisationStrategy = chargedParticleInitialisationStrategy;
	}
	
	@Override
	public void initialise(OptimisationProblem problem) {
        setId(PSO.getNextParticleId());
        
        getPositionInitialisationStrategy().initialise(this, problem);
        
        // Create the velocity vector by cloning the position and setting all the values
        // within the velocity to 0
        this.properties.put("velocity", getPosition().clone());
        
        velocityInitialisationStrategy.initialise(this);
        
        // Initialise particle charge
        chargedParticleInitialisationStrategy.initialise(this);
        
        this.properties.put("fitness", InferiorFitness.instance());
        this.properties.put("bestFitness", InferiorFitness.instance());
        neighbourhoodBest = this;
    }
}
