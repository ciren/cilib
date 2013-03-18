/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.measurement.single;

import com.google.common.base.Preconditions;
import fj.F;
import fj.data.List;
import static fj.data.List.iterableList;
import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Topologies;
import net.sourceforge.cilib.entity.topologies.SpeciationTopology;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.niching.NichingAlgorithm;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.type.types.container.TypeList;

public class Niches implements Measurement<TypeList> {
    
    private SpeciationTopology niches;
    private Double peakHeight;
    private Double error;
    private Boolean useMemoryInformation;

    public Niches() {
        this.niches = new SpeciationTopology();
        this.peakHeight = null;
        this.error = 1e-4;
        this.useMemoryInformation = true;
    }

    private Niches(Niches copy) {
        this.niches = copy.niches.getClone();
        this.peakHeight = copy.peakHeight;
        this.error = copy.error;
        this.useMemoryInformation = copy.useMemoryInformation;
    }
    
    public Niches getClone() {
        return new Niches(this);
    }

    public TypeList getValue(Algorithm algorithm) {
        Preconditions.checkNotNull(peakHeight, "GlobalOptimaFitness must be set in GlobalOptimaCount measurement.");
        niches.clear();
        
        if (algorithm instanceof NichingAlgorithm) {
            NichingAlgorithm pba = (NichingAlgorithm) algorithm;
            niches.addAll(pba.getTopology());
            
            for (PopulationBasedAlgorithm p : pba.getPopulations()) {
                niches.addAll(p.getTopology());
            }
        } else {
            PopulationBasedAlgorithm pba = (PopulationBasedAlgorithm) algorithm;
            niches.addAll(pba.getTopology());
        }
        
        if (useMemoryInformation) {
            for (int i = 0; i < niches.size(); i++) {
                if (niches.get(i) instanceof Particle) {
                    Particle p = (Particle) niches.get(i);
                    Particle clone = p.getClone();
                    clone.setCandidateSolution(clone.getBestPosition());
                    clone.getProperties().put(EntityType.Particle.BEST_FITNESS, clone.getBestFitness());
                    niches.set(i, clone);
                }
            }
        }
        
        niches.setNeighbourhoodSize(ConstantControlParameter.of(niches.size()));
        List<Entity> es = iterableList(Topologies.getNeighbourhoodBestEntities(niches))
                .filter(new F<Particle, Boolean>() {
                    @Override
                    public Boolean f(Particle a) {
                        return Math.abs(a.getFitness().getValue() - peakHeight) < error;
                    }
                });
        TypeList t = new TypeList();
        for (Entity e : es) {
            t.add(e.getCandidateSolution());
        }
        return t;
    }

    public void setPeakHeight(double ph) {
        this.peakHeight = ph;
    }
    
    public void setRadius(double r) {
        niches.setRadius(ConstantControlParameter.of(r));
    }

    public void setError(Double error) {
        this.error = error;
    }
}
