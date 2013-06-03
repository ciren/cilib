/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.measurement.single;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import fj.F;
import fj.data.Java;
import java.util.ArrayList;
import java.util.List;
import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Property;
import net.sourceforge.cilib.entity.Topologies;
import net.sourceforge.cilib.entity.topologies.SpeciationNeighbourhood;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.niching.NichingAlgorithm;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.type.types.container.TypeList;

public class Niches implements Measurement<TypeList> {

    private List<Particle> niches;
    private SpeciationNeighbourhood neighbourhood;
    private Double peakHeight;
    private Double error;
    private Boolean useMemoryInformation;

    public Niches() {
        this.neighbourhood = new SpeciationNeighbourhood();
        this.peakHeight = null;
        this.error = 1e-4;
        this.useMemoryInformation = true;
    }

    private Niches(Niches copy) {
        this.niches = copy.niches;
        this.neighbourhood = copy.neighbourhood;
        this.peakHeight = copy.peakHeight;
        this.error = copy.error;
        this.useMemoryInformation = copy.useMemoryInformation;
    }

    public Niches getClone() {
        return new Niches(this);
    }

    public TypeList getValue(Algorithm algorithm) {
        Preconditions.checkNotNull(peakHeight, "GlobalOptimaFitness must be set in GlobalOptimaCount measurement.");
        niches = Lists.newArrayList();

        if (algorithm instanceof NichingAlgorithm) {
            NichingAlgorithm pba = (NichingAlgorithm) algorithm;
            Iterables.addAll(niches, pba.getTopology());

            for (SinglePopulationBasedAlgorithm p : pba.getPopulations()) {
                Iterables.addAll(niches, p.getTopology());
            }
        } else {
            SinglePopulationBasedAlgorithm pba = (SinglePopulationBasedAlgorithm) algorithm;
            Iterables.addAll(niches, pba.getTopology());
        }

        if (useMemoryInformation) {
            for (int i = 0; i < niches.size(); i++) {
                if (niches.get(i) instanceof Particle) {
                    Particle p = niches.get(i);
                    Particle clone = p.getClone();
                    clone.setPosition(clone.getBestPosition());
                    clone.put(Property.BEST_FITNESS, clone.getBestFitness());
                    niches.set(i, clone);
                }
            }
        }

        neighbourhood.setNeighbourhoodSize(ConstantControlParameter.of(niches.size()));
        ArrayList<Particle> es = Java.<Particle>List_ArrayList().f(fj.data.List.iterableList(Topologies.getNeighbourhoodBestEntities(fj.data.List.iterableList(niches), neighbourhood))
                .filter(new F<Particle, Boolean>() {
                    @Override
                    public Boolean f(Particle a) {
                        return Math.abs(a.getFitness().getValue() - peakHeight) < error;
                    }
                }));
        TypeList t = new TypeList();
        for (Entity e : es) {
            t.add(e.getPosition());
        }
        return t;
    }

    public void setPeakHeight(double ph) {
        this.peakHeight = ph;
    }

    public void setRadius(double r) {
        neighbourhood.setRadius(ConstantControlParameter.of(r));
    }

    public void setError(Double error) {
        this.error = error;
    }
}
