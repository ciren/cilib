/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.hpso;

import fj.Ord;
import static fj.data.List.iterableList;
import static fj.function.Doubles.sum;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.sourceforge.cilib.algorithm.population.AbstractIterationStrategy;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import static net.sourceforge.cilib.entity.EntityType.Particle.*;
import net.sourceforge.cilib.entity.Topologies;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.math.Stats;
import net.sourceforge.cilib.math.random.ProbabilityDistributionFunction;
import net.sourceforge.cilib.math.random.UniformDistribution;
import static net.sourceforge.cilib.niching.VectorBasedFunctions.*;
import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.pso.particle.ParticleBehavior;
import net.sourceforge.cilib.pso.particle.StandardParticle;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.distancemeasure.EuclideanDistanceMeasure;
import net.sourceforge.cilib.util.selection.recipes.RouletteWheelSelector;
import net.sourceforge.cilib.util.selection.recipes.Selector;
import net.sourceforge.cilib.util.selection.weighting.ParticleBehaviorWeighting;
import net.sourceforge.cilib.util.selection.weighting.SpecialisedRatio;

/**
 * Li and Yang's Adaptive Learning PSO-II (ALPSO-II)
 * <p>
 * References:
 * </p>
 * <ul><li>
 * Changhe Li; Shengxiang Yang; , "Adaptive learning particle swarm optimizer-II for global optimization,"
 * Evolutionary Computation (CEC), 2010 IEEE Congress on , pp.1-8, 2010.
 * </li></ul>
 */
public class AdaptiveLearningIterationStrategy extends AbstractIterationStrategy<PSO> implements HeterogeneousIterationStrategy {
    private Selector<ParticleBehavior> behaviorSelectionRecipe;
    private List<ParticleBehavior> behaviorPool;
    private SpecialisedRatio weighting;
    private ControlParameter minRatio;
    private ControlParameter q;
    private ProbabilityDistributionFunction random;
    private Particle aBest;

    private enum Props {
        PROPS;
    }

    private class ParticleProperties implements Type {

        public class AdaptiveProperties {
            public List<Double> selectionRatio;
            public List<Double> progress;
            public List<Double> reward;
            public List<Double> success;
            public List<Double> selected;

            public void incrementSuccess(int i) {
                success.set(i, success.get(i) + 1);
            }

            public void incrementSelected(int i) {
                selected.set(i, selected.get(i) + 1);
            }
        }

        public AdaptiveProperties common = new AdaptiveProperties();
        public AdaptiveProperties prime = new AdaptiveProperties();

        public double updateFrequency;
        public double learningProbability;
        public double improvRatio;
        public double stagnation;

        public ParticleProperties getClone() {
            return new ParticleProperties();
        }
    }

    public AdaptiveLearningIterationStrategy() {
        this.minRatio = ConstantControlParameter.of(0.01);
        this.random = new UniformDistribution();
        this.behaviorPool = new ArrayList<ParticleBehavior>();
        this.weighting = new SpecialisedRatio();
        this.weighting.setBehaviors(behaviorPool);
        this.behaviorSelectionRecipe = new RouletteWheelSelector<ParticleBehavior>(new ParticleBehaviorWeighting(weighting));
        this.aBest = new StandardParticle();
        this.q = ConstantControlParameter.of(10);
    }

    public AdaptiveLearningIterationStrategy(AdaptiveLearningIterationStrategy copy) {
        super(copy);
        this.minRatio = copy.minRatio.getClone();
        this.random = copy.random;
        this.behaviorPool = new ArrayList<ParticleBehavior>(copy.behaviorPool);
        this.weighting = copy.weighting;
        this.behaviorSelectionRecipe = copy.behaviorSelectionRecipe;
        this.aBest = copy.aBest.getClone();
        this.q = copy.q.getClone();
    }

    @Override
    public AdaptiveLearningIterationStrategy getClone() {
        return new AdaptiveLearningIterationStrategy(this);
    }

    private ParticleProperties get(Particle p) {
        return (ParticleProperties)p.getProperties().get(Props.PROPS);
    }

    @Override
    public void performIteration(PSO algorithm) {
        Topology<Particle> topology = algorithm.getTopology();

        if (algorithm.getIterations() == 0) {
            initialise(topology, behaviorPool.size());
        }

        Ord<Particle> ord = Ord.ord(sortByDistance(aBest, new EuclideanDistanceMeasure()));
        fj.data.List<Particle> canLearnFromAbest = iterableList(topology)
                .sort(ord).take((int) q.getParameter());

        for(int k = 0; k < topology.size(); k++) {
            Particle particle = topology.get(k);
            ParticleProperties props = get(particle);
            boolean canLearn = canLearnFromAbest.exists(equalParticle.f(particle));
            double oldWeight = props.common.selectionRatio.get(0);

            if (!canLearn) {
                props.common.selectionRatio.set(0, 0.0);
            }

            //get behavior
            weighting.setWeights(props.common.selectionRatio);

            ParticleBehavior behavior = behaviorSelectionRecipe.on(behaviorPool).select();
            particle.setParticleBehavior(behavior);

            int i = behaviorPool.indexOf(behavior);
            props.common.incrementSelected(i);
            props.prime.incrementSelected(i);

            if (!canLearn) {
                props.common.selectionRatio.set(0, oldWeight);
            }

            Fitness prevFitness = particle.getFitness();
            Fitness prevPBest = (Fitness) particle.getProperties().get(BEST_FITNESS);

            //update particle
            particle.updateVelocity();
            particle.updatePosition();

            boundaryConstraint.enforce(particle);
            particle.calculateFitness();

            //if particle improved
            if (particle.getFitness().compareTo(prevFitness) > 0) {
                props.stagnation = 0;
                updateProgressAndReward(props.common, i, particle, prevFitness);

                //gbestupdate with abest
                for(int j = 0; j < particle.getPosition().size(); j++) {
                    if(random.getRandomNumber() < props.learningProbability) {
                        Particle aBestClone = aBest.getClone();
                        Vector aBestVector = (Vector) aBestClone.getBestPosition();

                        aBestVector.setReal(j, ((Vector)particle.getPosition()).doubleValueOf(j));
                        aBestClone.setCandidateSolution(aBestVector);
                        Fitness fitness = particle.getFitnessCalculator().getFitness(aBestClone);

                        if(fitness.compareTo(aBest.getBestFitness()) > 0) {
                            aBest.getProperties().put(BEST_POSITION, aBestVector);
                            aBest.getProperties().put(BEST_FITNESS, fitness);
                        }
                    }
                }
            } else {
                props.stagnation += 1.0;
            }

            //if pbest improved
            if(particle.getFitness().compareTo(particle.getBestFitness()) == 0) {
                updateProgressAndReward(props.prime, i, particle, prevPBest);

                //set abest
                if(aBest.getBestFitness().compareTo(particle.getFitness()) < 0) {
                    aBest.getProperties().put(BEST_POSITION, particle.getPosition().getClone());
                    aBest.getProperties().put(BEST_FITNESS, particle.getFitness().getClone());
                }
            }

            //if m_{k} >= U_{f}^{k}
            if (props.stagnation >= props.updateFrequency) {
                updateSelectionRatio(props.common);
                updateSelectionRatio(props.prime);

                //reset
                initAdaptiveProperties(props.common);
                initAdaptiveProperties(props.prime);
            }

            //check for convergence
            double var = Stats.variance(props.prime.selectionRatio);
            if(var <= 0.05 && var > 0.0) {
                props.common.selectionRatio = resetList(1.0 / behaviorPool.size());
                props.prime.selectionRatio = resetList(1.0 / behaviorPool.size());
            }

            //calculate improvement ratio
            props.improvRatio = Math.max(fitnessDifference(particle.getFitness(), prevFitness) / prevFitness.getValue(), 0);
        }

        //update learningProbability
        Particle bestImprov = topology.get(0);
        for(Particle p : topology) {
            if(get(p).improvRatio > get(bestImprov).improvRatio) {
                bestImprov = p;
            }
        }

        for(int k = 0; k < topology.size(); k++) {
            Particle p = topology.get(k);
            if(random.getRandomNumber() <= get(bestImprov).improvRatio/(get(bestImprov).improvRatio + get(p).improvRatio)) {
                get(p).learningProbability = get(bestImprov).learningProbability;
            } else {
                get(p).learningProbability = Math.max(1-Math.exp(-Math.pow(1.6*k/topology.size(), 4)), 0.05);
            }
        }

        //set one particle's best to abest so it can be measured and the rests neighbourhood best to abest
        topology.get(0).getProperties().put(BEST_FITNESS, aBest.getBestFitness());
        topology.get(0).getProperties().put(BEST_POSITION, aBest.getBestPosition());
    }

    private void updateProgressAndReward(ParticleProperties.AdaptiveProperties props, int i, Particle p, Fitness f) {
        props.incrementSuccess(i);
        props.progress.set(i, props.progress.get(i)
                + Math.max(fitnessDifference(p.getFitness(), f), 0));

        double progressSum = sum(iterableList(props.progress));
        double alpha = random.getRandomNumber();
        double penalty = props.success.get(i) == 0
                && props.selectionRatio.get(i) == Collections.max(props.selectionRatio)
                ? 0.9 : 1.0;

        props.reward.set(i, props.reward.get(i) +
            progressSum != 0 && props.selected.get(i) != 0
            ? props.progress.get(i) * alpha / progressSum
                + (1 - alpha) * props.success.get(i) / props.selected.get(i)
                + penalty * props.selectionRatio.get(i)
            : penalty * props.selectionRatio.get(i)
        );
    }

    private void updateSelectionRatio(ParticleProperties.AdaptiveProperties props) {
        double rewardSum = sum(iterableList(props.reward));

        //update selection ratios
        for(int j = 0; j < behaviorPool.size(); j++) {
            props.selectionRatio.set(j,
                    (rewardSum == 0 ? 0 : props.reward.get(j) / rewardSum)
                    * (1 - behaviorPool.size() * minRatio.getParameter())
                    + minRatio.getParameter());
        }
    }

    private double fitnessDifference(Fitness newF, Fitness oldF) {
        return newF.compareTo(oldF) *
                Math.abs((newF.getValue().isNaN() ? 0 : newF.getValue()) - (oldF.getValue().isNaN() ? 0 : oldF.getValue()));
    }

    private List<Double> resetList(double n) {
        List<Double> l = Collections.nCopies(behaviorPool.size(), n);
        return new ArrayList<Double>(l);
    }

    private void initialise(Topology<Particle> topology, int poolSize) {
        aBest = Topologies.getBestEntity(topology).getClone();

        for(int k = 0; k < topology.size(); k++) {
            Particle p = topology.get(k);
            ParticleProperties props = new ParticleProperties();

            props.updateFrequency = Math.max(10*Math.exp(-Math.pow(1.6*k/topology.size(),4)), 1);
            props.learningProbability = Math.max(1-Math.exp(-Math.pow(1.6*k/topology.size(), 4)), 0.05);
            props.stagnation = 0;
            props.improvRatio = 0.0;

            initAdaptiveProperties(props.common);
            initAdaptiveProperties(props.prime);

            props.common.selectionRatio = resetList(1.0 / poolSize);
            props.prime.selectionRatio = resetList(1.0 / poolSize);

            p.getProperties().put(Props.PROPS, props);
            p.setNeighbourhoodBest(aBest);
        }
    }

    private void initAdaptiveProperties(ParticleProperties.AdaptiveProperties props) {
        props.progress = resetList(0);
        props.reward = resetList(0);
        props.selected = resetList(0);
        props.success = resetList(0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addBehavior(ParticleBehavior behavior) {
        behaviorPool.add(behavior);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setBehaviorPool(List<ParticleBehavior> pool) {
        behaviorPool = pool;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ParticleBehavior> getBehaviorPool() {
        return behaviorPool;
    }

    public void setMinRatio(ControlParameter minRatio) {
        this.minRatio = minRatio;
    }

    public ControlParameter getMinRatio() {
        return minRatio;
    }

    public void setQ(ControlParameter q) {
        this.q = q;
    }

    public ControlParameter getQ() {
        return q;
    }
}
