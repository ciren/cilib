/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.cilib.pso.crossover.operations;

import net.cilib.controlparameter.ControlParameter;
import net.cilib.entity.operators.crossover.CrossoverStrategy;
import net.cilib.pso.PSO;
import net.cilib.pso.crossover.CrossoverReplaceFunction;
import net.cilib.pso.crossover.parentupdate.ParentReplacementStrategy;
import net.cilib.pso.guideprovider.GuideProvider;
import net.cilib.pso.particle.Particle;
import fj.F;

public class DiscreteCrossoverOperation extends PSOCrossoverOperation {

    private CrossoverReplaceFunction function;

    public DiscreteCrossoverOperation() {
        this.function = new CrossoverReplaceFunction();
    }

    @Override
    public fj.data.List<Particle> f(PSO pso) {
        return pso.getTopology().map(new F<Particle, Particle>() {
        	public Particle f(Particle p) {
        		return function.f(p);
        	}
        });
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
        this.function.setParentReplacementStrategy(parentReplacementStrategy);
    }

    public ParentReplacementStrategy getParentReplacementStrategy() {
        return function.getParentReplacementStrategy();
    }

    public void setCrossoverProbability(ControlParameter crossoverProbability) {
        this.function.setCrossoverProbability(crossoverProbability);
    }

    public ControlParameter getCrossoverProbability() {
        return function.getCrossoverProbability();
    }

}
