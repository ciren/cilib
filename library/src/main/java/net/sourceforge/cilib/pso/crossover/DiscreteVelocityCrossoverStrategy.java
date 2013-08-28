/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.crossover;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Property;
import net.sourceforge.cilib.entity.operators.crossover.CrossoverStrategy;
import net.sourceforge.cilib.entity.operators.crossover.DiscreteCrossoverStrategy;
import net.sourceforge.cilib.entity.operators.crossover.discrete.OnePointCrossoverStrategy;
import net.sourceforge.cilib.pso.crossover.pbestupdate.CurrentPositionOffspringPBestProvider;
import net.sourceforge.cilib.pso.crossover.pbestupdate.OffspringPBestProvider;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.util.selection.recipes.ElitistSelector;

public class DiscreteVelocityCrossoverStrategy implements CrossoverStrategy {

    private DiscreteCrossoverStrategy crossoverStrategy;
    private OffspringPBestProvider pbestProvider;

    public DiscreteVelocityCrossoverStrategy() {
        this.crossoverStrategy = new OnePointCrossoverStrategy();
        this.pbestProvider = new CurrentPositionOffspringPBestProvider();
    }

    public DiscreteVelocityCrossoverStrategy(DiscreteVelocityCrossoverStrategy copy) {
        this.crossoverStrategy = copy.crossoverStrategy.getClone();
        this.pbestProvider = copy.pbestProvider;
    }

    @Override
    public CrossoverStrategy getClone() {
        return new DiscreteVelocityCrossoverStrategy(this);
    }

    @Override
    public <E extends Entity> List<E> crossover(List<E> parentCollection) {
        List<Particle> parents = (List<Particle>) parentCollection;
        List<Particle> offspring = crossoverStrategy.crossover(parents);
        List<Particle> offspringVelocity = new ArrayList<>();
        Particle nBest = new ElitistSelector<Particle>().on(parents).select();

        for (Particle p : parents) {
            Particle v = p.getClone();
            v.setPosition(v.getVelocity());
            offspringVelocity.add(v);
        }

        offspringVelocity = crossoverStrategy.crossover(offspringVelocity, crossoverStrategy.getCrossoverPoints());

        Iterator<Particle> vIter = offspringVelocity.iterator();
        for (Particle p : offspring) {
            p.put(Property.BEST_POSITION, pbestProvider.f(parents, p));

            Particle pbCalc = p.getClone();
            pbCalc.setNeighbourhoodBest(nBest);
            pbCalc.setPosition(p.getBestPosition());
            pbCalc.updateFitness(pbCalc.getBehaviour().getFitnessCalculator().getFitness(pbCalc));

            p.put(Property.BEST_FITNESS, pbCalc.getFitness());
            p.put(Property.VELOCITY, vIter.next().getPosition());

            p.setNeighbourhoodBest(nBest);
            p.updateFitness(p.getBehaviour().getFitnessCalculator().getFitness(p));
        }

        return (List<E>) offspring;
    }

    @Override
    public int getNumberOfParents() {
        return crossoverStrategy.getNumberOfParents();
    }

    public void setCrossoverStrategy(DiscreteCrossoverStrategy crossoverStrategy) {
        this.crossoverStrategy = crossoverStrategy;
    }

    public DiscreteCrossoverStrategy getCrossoverStrategy() {
        return crossoverStrategy;
    }

    public void setPbestProvider(OffspringPBestProvider pbestProvider) {
        this.pbestProvider = pbestProvider;
    }

    public OffspringPBestProvider getPbestProvider() {
        return pbestProvider;
    }

}
