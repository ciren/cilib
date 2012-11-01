/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.hpso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sourceforge.cilib.algorithm.population.IterationStrategy;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.Topologies;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.math.random.ProbabilityDistributionFunction;
import net.sourceforge.cilib.math.random.UniformDistribution;
import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.problem.boundaryconstraint.BoundaryConstraint;
import net.sourceforge.cilib.problem.boundaryconstraint.UnconstrainedBoundary;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.particle.ParticleBehavior;
import net.sourceforge.cilib.pso.particle.StandardParticle;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.selection.recipes.RouletteWheelSelector;
import net.sourceforge.cilib.util.selection.recipes.Selector;
import net.sourceforge.cilib.util.selection.weighting.ParticleBehaviorWeighting;
import net.sourceforge.cilib.util.selection.weighting.SpecializedRatio;

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
public class AdaptiveLearningIterationStrategy implements IterationStrategy<PSO>, HeterogeneousIterationStrategy {
    private BoundaryConstraint boundaryConstraint;
    private Selector<ParticleBehavior> behaviorSelectionRecipe;
    private List<ParticleBehavior> behaviorPool;

    private Map<Particle, List<Double>> selectionRatio;
    private Map<Particle, List<Double>> selectionRatioPrime;
    private Map<Particle, List<Double>> progress;
    private Map<Particle, List<Double>> progressPrime;
    private Map<Particle, List<Double>> reward;
    private Map<Particle, List<Double>> rewardPrime;

    private Map<Particle, Double> learningProbability;
    private Map<Particle, Double> improvRatio;
    private Map<Particle, Integer> stagnation;

    private Map<Particle, List<Double>> success;
    private Map<Particle, List<Double>> successPrime;
    private Map<Particle, List<Double>> selected;
    private Map<Particle, List<Double>> selectedPrime;

    private SpecializedRatio weighting;
    private ControlParameter minRatio;
    private ProbabilityDistributionFunction random;

    private Particle aBest;
    private boolean initialized;

    public AdaptiveLearningIterationStrategy() {
        this.progress = new HashMap<Particle, List<Double>>();
        this.reward = new HashMap<Particle, List<Double>>();
        this.selectionRatio = new HashMap<Particle, List<Double>>();
        this.progressPrime = new HashMap<Particle, List<Double>>();
        this.rewardPrime = new HashMap<Particle, List<Double>>();
        this.selectionRatioPrime = new HashMap<Particle, List<Double>>();

        this.success = new HashMap<Particle, List<Double>>();
        this.successPrime = new HashMap<Particle, List<Double>>();
        this.selected = new HashMap<Particle, List<Double>>();
        this.selectedPrime = new HashMap<Particle, List<Double>>();

        this.stagnation = new HashMap<Particle, Integer>();
        this.learningProbability = new HashMap<Particle, Double>();
        this.improvRatio = new HashMap<Particle, Double>();

        this.minRatio = ConstantControlParameter.of(0.01);
        this.random = new UniformDistribution();

        this.behaviorPool = new ArrayList<ParticleBehavior>();
        this.weighting = new SpecializedRatio();
        this.weighting.setBehaviors(behaviorPool);

        this.behaviorSelectionRecipe = new RouletteWheelSelector<ParticleBehavior>(new ParticleBehaviorWeighting(weighting));

        this.boundaryConstraint = new UnconstrainedBoundary();
        this.aBest = new StandardParticle();

        this.initialized = false;
    }

    public AdaptiveLearningIterationStrategy(AdaptiveLearningIterationStrategy copy) {
        this.progress = new HashMap<Particle, List<Double>>(copy.progress);
        this.reward = new HashMap<Particle, List<Double>>(copy.reward);
        this.selectionRatio = new HashMap<Particle, List<Double>>(copy.selectionRatio);
        this.progressPrime = new HashMap<Particle, List<Double>>(copy.progressPrime);
        this.rewardPrime = new HashMap<Particle, List<Double>>(copy.rewardPrime);
        this.selectionRatioPrime = new HashMap<Particle, List<Double>>(copy.selectionRatioPrime);

        this.stagnation = new HashMap<Particle, Integer>(copy.stagnation);
        this.learningProbability = new HashMap<Particle, Double>(copy.learningProbability);
        this.improvRatio = new HashMap<Particle, Double>(copy.improvRatio);

        this.minRatio = copy.minRatio.getClone();
        this.random = copy.random;

        this.behaviorPool = new ArrayList<ParticleBehavior>(copy.behaviorPool);
        this.weighting = copy.weighting;

        this.behaviorSelectionRecipe = copy.behaviorSelectionRecipe;

        this.boundaryConstraint = copy.boundaryConstraint.getClone();
        this.aBest = copy.aBest.getClone();
    }

    @Override
    public AdaptiveLearningIterationStrategy getClone() {
        return new AdaptiveLearningIterationStrategy(this);
    }

    @Override
    public void performIteration(PSO algorithm) {
        initialize(algorithm);

        Topology<Particle> topology = algorithm.getTopology();

        for(int k = 0; k < topology.size(); k++) {
            Particle particle = topology.get(k);

            //get behavior
            weighting.setWeights(selectionRatio.get(particle));

            ParticleBehavior behavior = behaviorSelectionRecipe.on(behaviorPool).select();
            particle.setParticleBehavior(behavior);

            int i = behaviorPool.indexOf(behavior);
            selected.get(particle).set(i, selected.get(particle).get(i) + 1);
            selectedPrime.get(particle).set(i, selectedPrime.get(particle).get(i) + 1);

            Fitness prevFitness = particle.getFitness();
            Fitness prevPBest = (Fitness) particle.getProperties().get(EntityType.Particle.BEST_FITNESS);

            //update particle
            particle.updateVelocity();
            particle.updatePosition();

            boundaryConstraint.enforce(particle);
            particle.calculateFitness();

            //if particle improved
            if (particle.getFitness().compareTo(prevFitness) > 0) {
                success.get(particle).set(i, success.get(particle).get(i) + 1);
                stagnation.put(particle, 0);
                progress.get(particle).set(i, progress.get(particle).get(i)
                        + Math.abs((prevFitness.getValue().isNaN() ? 0 : prevFitness.getValue())
                        - particle.getFitness().getValue()));

                //do this?
                double progressSum = sumList(progress.get(particle));
                double alpha = random.getRandomNumber();
                double penalty = success.get(particle).get(i) == 0
                        && selectionRatio.get(particle).get(i) == Collections.max(selectionRatio.get(particle))
                        ? 0.9 : 1.0;

                if(progressSum != 0 && selected.get(particle).get(i) != 0) {
                    reward.get(particle).set(i, reward.get(particle).get(i)
                            + progress.get(particle).get(i) * alpha / progressSum
                            + (1 - alpha) * success.get(particle).get(i) / selected.get(particle).get(i)
                            + penalty * selectionRatio.get(particle).get(i));
                } else {
                    reward.get(particle).set(i, reward.get(particle).get(i)
                            + penalty * selectionRatio.get(particle).get(i));
                }

                //gbestupdate with abest
                for(int j = 0; j < particle.getPosition().size(); j++) {
                    if(random.getRandomNumber() < learningProbability.get(particle)) {
                        Particle aBestClone = aBest.getClone();
                        Vector aBestVector = (Vector) aBestClone.getBestPosition();

                        aBestVector.setReal(j, ((Vector)particle.getPosition()).doubleValueOf(j));
                        aBestClone.setCandidateSolution(aBestVector);
                        Fitness fitness = particle.getFitnessCalculator().getFitness(aBestClone);

                        if(fitness.compareTo(aBest.getBestFitness()) > 0) {
                            aBest.getProperties().put(EntityType.Particle.BEST_POSITION, aBestVector);
                            aBest.getProperties().put(EntityType.Particle.BEST_FITNESS, fitness);
                        }
                    }
                }

                //calculate improvement ratio
                improvRatio.put(particle, (prevFitness.getValue() - particle.getFitness().getValue()) / prevFitness.getValue());
            } else {
                stagnation.put(particle, stagnation.get(particle) + 1);
                improvRatio.put(particle, 0.0);
            }

            //if pbest improved
            if(particle.getFitness().compareTo(particle.getBestFitness()) == 0) {
                successPrime.get(particle).set(i, successPrime.get(particle).get(i) + 1);
                progressPrime.get(particle).set(i, progressPrime.get(particle).get(i)
                        + Math.abs((prevPBest.getValue().isNaN() ? 0 : prevPBest.getValue())
                        - particle.getFitness().getValue()));

                //do this?
                double progressSum = sumList(progressPrime.get(particle));
                double alpha = random.getRandomNumber();
                double penalty = successPrime.get(particle).get(i) == 0
                        && selectionRatioPrime.get(particle).get(i) == Collections.max(selectionRatioPrime.get(particle))
                        ? 0.9 : 1.0;

                if(progressSum != 0 && selectedPrime.get(particle).get(i) != 0) {
                    rewardPrime.get(particle).set(i, rewardPrime.get(particle).get(i)
                            + progressPrime.get(particle).get(i) * alpha / progressSum
                            + (1 - alpha) * successPrime.get(particle).get(i) / selectedPrime.get(particle).get(i)
                            + penalty * selectionRatioPrime.get(particle).get(i));
                } else {
                    rewardPrime.get(particle).set(i, rewardPrime.get(particle).get(i)
                            + penalty * selectionRatioPrime.get(particle).get(i));
                }

                //set abest
                if(aBest.getBestFitness().compareTo(particle.getFitness()) < 0) {
                    aBest.getProperties().put(EntityType.Particle.BEST_POSITION, particle.getPosition().getClone());
                    aBest.getProperties().put(EntityType.Particle.BEST_FITNESS, particle.getFitness().getClone());
                }
            }

            //if m_{k} >= U_{f}^{k}
            if (stagnation.get(particle) >= Math.max(10*Math.exp(-Math.pow(1.6*k/topology.size(),4)), 1)) {
                double rewardSum = sumList(reward.get(particle));
                double rewardPrimeSum = sumList(rewardPrime.get(particle));

                //update selection ratios
                for(int j = 0; j < behaviorPool.size(); j++) {
                    selectionRatio.get(particle).set(j,
                            (reward.get(particle).get(j) / rewardSum)
                            * (1 - selectionRatio.get(particle).size() * minRatio.getParameter())
                            + minRatio.getParameter());

                    //update selection ratio prime
                    selectionRatioPrime.get(particle).set(j,
                            (rewardPrime.get(particle).get(j) / rewardPrimeSum)
                            * (1 - selectionRatioPrime.get(particle).size() * minRatio.getParameter())
                            + minRatio.getParameter());
                }

                //reset
                for(ParticleBehavior pb : behaviorPool) {
                    resetList(progress.get(particle), 0);
                    resetList(selected.get(particle), 0);
                    resetList(success.get(particle), 0);
                    resetList(reward.get(particle), 0);
                    resetList(progressPrime.get(particle), 0);
                    resetList(selectedPrime.get(particle), 0);
                    resetList(successPrime.get(particle), 0);
                    resetList(rewardPrime.get(particle), 0);
                }
            }

            //check for convergence
            double var = variance(selectionRatioPrime.get(particle));
            if(var <= 0.05 && var > 0.0) {
                particle.reinitialise();
                resetList(selectionRatio.get(particle), 1.0 / behaviorPool.size());
                resetList(selectionRatioPrime.get(particle), 1.0 / behaviorPool.size());
            }

            //set one particle's best to abest so it can be measured and the rests neighbourhood best to abest
            if(k == 0) {
                particle.getProperties().put(EntityType.Particle.BEST_FITNESS, aBest.getBestFitness());
                particle.getProperties().put(EntityType.Particle.BEST_POSITION, aBest.getBestPosition());
            }

            particle.setNeighbourhoodBest(aBest);
        }

        //update learningProbability
        int bestImprov = 0;
        for(int k = 0; k < topology.size(); k++) {
            Particle p = topology.get(k);
            if(improvRatio.get(p) > improvRatio.get(topology.get(bestImprov))) {
                bestImprov = k;
            }
        }

        for(Particle p : topology) {
            if(random.getRandomNumber() <= improvRatio.get(topology.get(bestImprov))/(improvRatio.get(topology.get(bestImprov))+ improvRatio.get(p))) {
                learningProbability.put(p, learningProbability.get(topology.get(bestImprov)));
            } else {
                learningProbability.put(p, Math.max(1-Math.exp(-Math.pow(1.6*topology.indexOf(p)/topology.size(), 4)), 0.05));
            }
        }
    }

    private void initialize(PSO algorithm) {
        if(!initialized) {
            Topology<Particle> topology = algorithm.getTopology();

            aBest = Topologies.getBestEntity(topology).getClone();

            ArrayList<Double> list = new ArrayList<Double>();
            ArrayList<Double> srList = new ArrayList<Double>();
            for(ParticleBehavior pb : behaviorPool) {
                list.add(0.0);
                srList.add(1.0 / behaviorPool.size());
            }

            for(Particle p : topology) {
                learningProbability.put(p, Math.max(1-Math.exp(-Math.pow(1.6*topology.indexOf(p)/topology.size(), 4)), 0.05));
                stagnation.put(p, 0);
                improvRatio.put(p, 0.0);

                for(ParticleBehavior pb : behaviorPool) {
                    selectionRatio.put(p, new ArrayList<Double>(srList));
                    selectionRatioPrime.put(p, new ArrayList<Double>(srList));
                    progress.put(p, new ArrayList<Double>(list));
                    progressPrime.put(p, new ArrayList<Double>(list));
                    reward.put(p, new ArrayList<Double>(list));
                    rewardPrime.put(p, new ArrayList<Double>(list));

                    selected.put(p, new ArrayList<Double>(list));
                    selectedPrime.put(p, new ArrayList<Double>(list));
                    success.put(p, new ArrayList<Double>(list));
                    successPrime.put(p, new ArrayList<Double>(list));
                }
            }

            initialized = true;
        }
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

    private double sumList(List<Double> list) {
        double sum = 0.0;

        for (Double d : list) {
            sum += d;
        }

        return sum;
    }

    private void resetList(List<Double> list, double n) {
        for (int i = 0; i < list.size(); i++) {
            list.set(i, n);
        }
    }

    private double variance(List<Double> list) {
        double mean = 0;
        double s = 0.0;

        for (double x : list) {
            s += x;
        }

        mean = s / list.size();
        s = 0.0;

        for (double x : list) {
            s += (x-mean)*(x-mean);
        }

        return (s / (list.size() - 1));
    }

    public void setMinRatio(ControlParameter minRatio) {
        this.minRatio = minRatio;
    }

    public void setRandom(ProbabilityDistributionFunction random) {
        this.random = random;
    }

    /**
     * Get the currently associated {@linkplain BoundaryConstraint}.
     * @return The current {@linkplain BoundaryConstraint}.
     */
    @Override
    public BoundaryConstraint getBoundaryConstraint() {
        return boundaryConstraint;
    }

    /**
     * Set the {@linkplain BoundaryConstraint} to maintain within this {@linkplain IterationStrategy}.
     * @param boundaryConstraint The {@linkplain BoundaryConstraint} to set.
     */
    @Override
    public void setBoundaryConstraint(BoundaryConstraint boundaryConstraint) {
        this.boundaryConstraint = boundaryConstraint;
    }
}
