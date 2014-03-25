/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.measurement.single;

import fj.F;
import fj.function.Integers;
import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.algorithm.population.MultiPopulationBasedAlgorithm;
import net.sourceforge.cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Property;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.niching.NichingAlgorithm;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.type.types.Int;

public class ParticlePositionUpdates implements Measurement<Int> {
	private Integer updates = 0;

    @Override
    public Measurement<Int> getClone() {
        return this;
    }

    @Override
    public Int getValue(Algorithm algorithm) {
        updates = 0;
        if (algorithm instanceof SinglePopulationBasedAlgorithm) {
        	updates += posUpdateCount((SinglePopulationBasedAlgorithm<Particle>) algorithm);
            return Int.valueOf(updates);
        } else {
            MultiPopulationBasedAlgorithm psos = (MultiPopulationBasedAlgorithm) algorithm;
            for (SinglePopulationBasedAlgorithm sPop : psos.getPopulations()) {
                updates += posUpdateCount(sPop);
            }
            
            if (algorithm instanceof NichingAlgorithm) {
            	updates += posUpdateCount(((NichingAlgorithm) algorithm).getMainSwarm());
            }

            return Int.valueOf(updates);
        }
    }

    private int posUpdateCount(SinglePopulationBasedAlgorithm<Particle> algorithm) {
        return Integers.sum(algorithm.getTopology().map(new F<Particle, Integer>() {
            @Override
            public Integer f(Particle a) {
            	int b = a.get(Property.POSITION_UPDATE_COUNTER).intValue();
                return b;
            }            
        }));
    }
}
