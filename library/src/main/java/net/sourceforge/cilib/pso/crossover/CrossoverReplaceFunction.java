/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.crossover;

import fj.F;
import java.util.Arrays;
import java.util.List;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.operators.crossover.CrossoverStrategy;
import net.sourceforge.cilib.math.random.UniformDistribution;
import net.sourceforge.cilib.pso.crossover.parentupdate.AlwaysReplaceParentReplacementStrategy;
import net.sourceforge.cilib.pso.crossover.parentupdate.ParentReplacementStrategy;
import net.sourceforge.cilib.pso.guideprovider.GuideProvider;
import net.sourceforge.cilib.pso.guideprovider.NBestGuideProvider;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.selection.recipes.ElitistSelector;

public class CrossoverReplaceFunction extends F<Particle, Particle> {

    private GuideProvider parentProvider;
    private CrossoverStrategy crossoverStrategy;
    private ParentReplacementStrategy parentReplacementStrategy;
    private ControlParameter crossoverProbability;
    private UniformDistribution uniform;

    public CrossoverReplaceFunction() {
        this.parentProvider = new NBestGuideProvider();
        this.crossoverStrategy = new DiscreteVelocityCrossoverStrategy();
        this.parentReplacementStrategy = new AlwaysReplaceParentReplacementStrategy();
        this.crossoverProbability = ConstantControlParameter.of(0.5);
        this.uniform = new UniformDistribution();
    }

    public void setParentProvider(GuideProvider parentProvider) {
        this.parentProvider = parentProvider;
    }

    public GuideProvider getParentProvider() {
        return parentProvider;
    }

    public void setCrossoverStrategy(CrossoverStrategy crossoverStrategy) {
        this.crossoverStrategy = crossoverStrategy;
    }

    public CrossoverStrategy getCrossoverStrategy() {
        return crossoverStrategy;
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

    @Override
    public Particle f(Particle p) {
        if (uniform.getRandomNumber() < crossoverProbability.getParameter()) {
            Particle parent = p.getClone();
            Vector parentSolution = (Vector) parentProvider.get(p);
            parent.setCandidateSolution(parentSolution);

            List<Particle> offspring = crossoverStrategy.crossover(Arrays.asList(p, parent));

            Particle bestOffspring = new ElitistSelector<Particle>().on(offspring).select();
            Particle newParticle = parentReplacementStrategy.f(Arrays.asList(p), Arrays.asList(bestOffspring)).get(0);

            return newParticle;
        } else {
            return p;
        }
    }
}
