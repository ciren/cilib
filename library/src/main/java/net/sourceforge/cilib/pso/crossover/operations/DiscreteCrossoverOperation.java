/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.crossover.operations;

import java.util.Arrays;
import java.util.List;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.operators.crossover.CrossoverStrategy;
import net.sourceforge.cilib.math.random.UniformDistribution;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.crossover.CrossoverReplaceFunction;
import net.sourceforge.cilib.pso.crossover.parentupdate.AlwaysReplaceParentReplacementStrategy;
import net.sourceforge.cilib.pso.crossover.parentupdate.ParentReplacementStrategy;
import net.sourceforge.cilib.pso.guideprovider.GuideProvider;
import net.sourceforge.cilib.util.selection.recipes.ElitistSelector;

public class DiscreteCrossoverOperation extends PSOCrossoverOperation {

    private CrossoverReplaceFunction function;
    private ParentReplacementStrategy parentReplacementStrategy;
    private ControlParameter crossoverProbability;

    public DiscreteCrossoverOperation() {
        this.function = new CrossoverReplaceFunction();
        this.parentReplacementStrategy = new AlwaysReplaceParentReplacementStrategy();
        this.crossoverProbability = ConstantControlParameter.of(0.5);
    }
    
    @Override
    public Topology<Particle> f(PSO pso) {
        Topology<Particle> newTopology = pso.getTopology();
        UniformDistribution uniform = new UniformDistribution();

        for (Particle p : newTopology) {
            if (uniform.getRandomNumber() < crossoverProbability.getParameter()) {
                List<Particle> offspring = function.f(newTopology, p);
                Particle bestOffspring = new ElitistSelector<Particle>().on(offspring).select();
                Particle newParticle = parentReplacementStrategy.f(Arrays.asList(p), Arrays.asList(bestOffspring)).get(0);
                newTopology.set(newTopology.indexOf(p), newParticle);
            }
        }

        return newTopology;
    }

    @Override
    public PSOCrossoverOperation getClone() {
        return this;
    }

    public void setParentProvider(GuideProvider parentProvider) {
        this.function.setParentProvider(parentProvider);
    }

    public GuideProvider getParentProvider() {
        return function.getParentProvider();
    }

    public void setCrossoverStrategy(CrossoverStrategy crossoverStrategy) {
        this.function.setCrossoverStrategy(crossoverStrategy);
    }

    public CrossoverStrategy getCrossoverStrategy() {
        return function.getCrossoverStrategy();
    }

    public void setParentReplacementStrategy(ParentReplacementStrategy parentReplacementStrategy) {
        this.parentReplacementStrategy = parentReplacementStrategy;
    }

    public ParentReplacementStrategy getParentReplacementStrategy() {
        return parentReplacementStrategy;
    }

    public void setCrossoverProbability(ControlParameter crossoverProbability) {
        this.crossoverProbability = crossoverProbability;
    }

    public ControlParameter getCrossoverProbability() {
        return crossoverProbability;
    }

}
