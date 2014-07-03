/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity.initialisation;

import fj.P1;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Property;
import net.sourceforge.cilib.math.random.ProbabilityDistributionFunction;
import net.sourceforge.cilib.math.random.UniformDistribution;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.StructuredType;
import net.sourceforge.cilib.type.types.container.Vector;

public class MutatedPBestInitialisationStrategy implements InitialisationStrategy<Particle> {
    
    private ProbabilityDistributionFunction distribution;

    public MutatedPBestInitialisationStrategy() {
        this.distribution = new UniformDistribution(ConstantControlParameter.of(0.0), ConstantControlParameter.of(0.1));
    }

    private MutatedPBestInitialisationStrategy(MutatedPBestInitialisationStrategy copy) {
        this.distribution = copy.distribution;
    }

    @Override
    public MutatedPBestInitialisationStrategy getClone() {
        return new MutatedPBestInitialisationStrategy(this);
    }

    @Override
    public void initialise(Property key, Particle entity) {
        Vector pbest = (Vector) entity.get(Property.CANDIDATE_SOLUTION);
        pbest = pbest.plus(Vector.newBuilder().repeat(pbest.size(), Real.valueOf(1.0)).build().multiply(new P1<Number>() {
            @Override
            public Number _1() {
                return distribution.getRandomNumber();
            }
        }));

        entity.updateFitness(entity.getBehaviour().getFitnessCalculator().getFitness(entity));
        //entity.calculateFitness();
        
        Entity a = entity.getClone();
        a.put(Property.BEST_POSITION, entity.getPosition().getClone());
        a.put(Property.BEST_FITNESS, entity.getFitness().getClone());
        a.setPosition(pbest);
        a.updateFitness(a.getBehaviour().getFitnessCalculator().getFitness(a));
        
        if (a.compareTo(entity) > 0) {
            entity.put(Property.BEST_POSITION, pbest);
            entity.put(Property.BEST_FITNESS, a.getFitness());
        } else {
            entity.put(Property.BEST_POSITION, a.getPosition());
            entity.put(Property.BEST_FITNESS, a.getFitness());
            entity.put(Property.CANDIDATE_SOLUTION, a.get(Property.BEST_POSITION));
            entity.put(Property.FITNESS, a.getBestFitness());
        }        
        
    }
}
