/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.niching.creation;

import fj.data.List;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.math.Stats;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.TypeList;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.functions.Numerics;

/**
 * <p>
 * Identify if a niche has been located.
 * </p>
 * <p>
 * Niches are defined to be found if the fitness of an entity has a standard deviation
 * of less than a threshold value for a predefined number of iterations.
 * </p>
 * <p>
 * If the fitness of the entity is indeed less than the predefined threshold, it is
 * determined that a niche has been located and the entity is returned as a
 * result for a new niching location.
 * </p>
 */
public class MaintainedFitnessNicheDetection extends NicheDetection {
    
    public enum NicheEnum{
        NICHE_DETECTION_FITNESSES
    }

    private ControlParameter threshold;
    private ControlParameter stationaryCounter;

    public MaintainedFitnessNicheDetection() {
        this.threshold = ConstantControlParameter.of(1.0E-6);
        this.stationaryCounter = ConstantControlParameter.of(3.0);
    }

    /**
     * Get the defined threshold value.
     * @return The threshold value.
     */
    public ControlParameter getThreshold() {
        return threshold;
    }

    /**
     * Set the threshold value
     * @param threshold The value to set.
     */
    public void setThreshold(ControlParameter threshold) {
        this.threshold = threshold;
    }

    /**
     * Obtain the stationary counter for the identification process.
     * @return The value of the stationary counter.
     */
    public ControlParameter getStationaryCounter() {
        return stationaryCounter;
    }

    /**
     * Set the stationary counter for the identification process.
     * @param stationaryCounter The counter value to set.
     */
    public void setStationaryCounter(ControlParameter stationaryCounter) {
        this.stationaryCounter = stationaryCounter;
    }

    @Override
    public Boolean f(PopulationBasedAlgorithm swarm, Entity entity) {
        TypeList fitnesses = (TypeList) entity.getProperties().get(NicheEnum.NICHE_DETECTION_FITNESSES);
        
        if (fitnesses == null) {
            TypeList fitness = new TypeList();
            
            fitness.add(Real.valueOf(entity.getFitness().getValue()));
            entity.getProperties().put(NicheEnum.NICHE_DETECTION_FITNESSES, fitness);
            
            return false;
        }
        
        if (fitnesses.size() >= stationaryCounter.getParameter()) {
            fitnesses.remove(fitnesses.get(0));
        }
        
        fitnesses.add(Real.valueOf(entity.getFitness().getValue()));
        
        Vector.Builder builder = Vector.newBuilder();
        for (Type t : fitnesses) {
            builder.add((Real) t);
        }
                
        return fitnesses.size() == (int) this.stationaryCounter.getParameter()
                && Stats.stdDev(List.iterableList(builder.build()).map(Numerics.doubleValue())) < threshold.getParameter();
    }
}
