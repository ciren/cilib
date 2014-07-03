/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.velocityprovider;

import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.type.types.container.Vector;

public class GBestMovingVelocityProvider implements VelocityProvider {
    
    private VelocityProvider delegateGbest;
    private VelocityProvider delegateOther;

    public GBestMovingVelocityProvider() {
        delegateGbest = new StandardVelocityProvider(ConstantControlParameter.of(1.0), 
                ConstantControlParameter.of(1.496),
                ConstantControlParameter.of(1.496));
        delegateOther = new StandardVelocityProvider();
    }

    @Override
    public VelocityProvider getClone() {
        return this;
    }

    @Override
    public Vector get(Particle particle) {
        if (particle.equals(particle.getNeighbourhoodBest())) {
            return (Vector) delegateGbest.get(particle);
        }
        
        return (Vector) delegateOther.get(particle);
    }

    public void setDelegateOther(VelocityProvider delegateOther) {
        this.delegateOther = delegateOther;
    }

    public void setDelegateGbest(VelocityProvider delegateGbest) {
        this.delegateGbest = delegateGbest;
    }

}
