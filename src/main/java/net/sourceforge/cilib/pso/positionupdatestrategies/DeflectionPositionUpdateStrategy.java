package net.sourceforge.cilib.pso.positionupdatestrategies;

import net.sourceforge.cilib.controlparameterupdatestrategies.ConstantUpdateStrategy;
import net.sourceforge.cilib.controlparameterupdatestrategies.ControlParameterUpdateStrategy;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * @author Wiehann Matthysen
 *
 * Standard Position Update Strategy with boundary constraint handling. Particles that overstep
 * the boundary gets re-initialised to simulate a bouncing effect. This is also achieved by
 * flipping the velocity (multiplying it with -1.0).
 */
public class DeflectionPositionUpdateStrategy implements PositionUpdateStrategy {
	private static final long serialVersionUID = -6403371840318791814L;
	
	private ControlParameterUpdateStrategy velocityDampingFactor;

	public DeflectionPositionUpdateStrategy() {
		velocityDampingFactor = new ConstantUpdateStrategy();
		velocityDampingFactor.setParameter(-1.0);
	}
	
	public DeflectionPositionUpdateStrategy(DeflectionPositionUpdateStrategy copy) {
		velocityDampingFactor = copy.velocityDampingFactor.clone();
	}
	
	public DeflectionPositionUpdateStrategy clone() {
		return new DeflectionPositionUpdateStrategy(this);
	}
	
	public void setVelocityDampingFactor(ControlParameterUpdateStrategy velocityDampingFactor) {
		this.velocityDampingFactor = velocityDampingFactor;
	}
	
	public ControlParameterUpdateStrategy getVelocityDampingFactor() {
		return velocityDampingFactor;
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.pso.positionupdatestrategies.PositionUpdateStrategy#updatePosition(net.sourceforge.cilib.entity.Particle)
	 */
	public void updatePosition(Particle particle) {
		Vector position = (Vector) particle.getPosition();
		Vector velocity = (Vector) particle.getVelocity();
		
		for (int i = 0; i < position.getDimension(); ++i) {
			Numeric position_i = position.getNumeric(i);
			Numeric velocity_i = velocity.getNumeric(i);
			double upper = position_i.getUpperBound();
			double lower = position_i.getLowerBound();
			double range = Math.abs(upper - lower);
			double value = position_i.getReal() + velocity_i.getReal();
			
			if (value < lower) {
				position_i.setReal(lower + (lower - position_i.getReal()) % range);
				velocity_i.setReal(velocity.getReal(i) * velocityDampingFactor.getParameter());
			}
			else if (value >= upper) {
				position_i.setReal(upper - (position_i.getReal() - upper) % range);
				velocity_i.setReal(velocity.getReal(i) * velocityDampingFactor.getParameter());
			}
			else {
				position_i.setReal(value);
			}
		}
	}
}
