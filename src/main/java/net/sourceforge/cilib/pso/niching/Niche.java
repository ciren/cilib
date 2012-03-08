/**
 * Computational Intelligence Library (CIlib)
 * Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.cilib.pso.niching;

import com.google.common.collect.Lists;
import fj.F;
import fj.P;
import fj.P2;
import fj.data.List;
import java.util.ArrayList;
import net.sourceforge.cilib.algorithm.initialisation.ClonedPopulationInitialisationStrategy;
import net.sourceforge.cilib.algorithm.initialisation.PopulationInitialisationStrategy;
import net.sourceforge.cilib.algorithm.population.MultiPopulationBasedAlgorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.initialization.RandomInitializationStrategy;
import net.sourceforge.cilib.problem.OptimisationSolution;
import net.sourceforge.cilib.problem.boundaryconstraint.ReinitialisationBoundary;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.iterationstrategies.SynchronousIterationStrategy;
import net.sourceforge.cilib.pso.niching.creation.NicheCreationStrategy;
import net.sourceforge.cilib.pso.niching.creation.NicheDetection;
import net.sourceforge.cilib.pso.niching.creation.StandardNicheIdentificationStrategy;
import net.sourceforge.cilib.pso.niching.creation.StandardSwarmCreationStrategy;
import net.sourceforge.cilib.pso.niching.merging.*;
import net.sourceforge.cilib.pso.particle.StandardParticle;
import net.sourceforge.cilib.pso.velocityprovider.StandardVelocityProvider;
import net.sourceforge.cilib.stoppingcondition.StoppingCondition;

/**
 * <p>
 * Generalized Niche algorithm.
 * </p>
 * <p>
 * This class is intended to be the base class (or even the only class) for all
 * algorithms implementing a form of niching.
 * </p>
 * <p>
 * Currently the main implementation is the NichePSO, however, any implementation
 * can be achieved by correctly applying the different setter methods with the appropriate
 * instances.
 * </p>
 * <pre>
 * {@literal @}inproceedings{}
 * </pre>
 */
public class Niche extends MultiPopulationBasedAlgorithm {
    private static final long serialVersionUID = 3575627467034673738L;

    protected PopulationBasedAlgorithm mainSwarm;

    protected NicheDetection nicheIdentificationStrategy;
    protected NicheCreationStrategy swarmCreationStrategy;
    
    protected MergeStrategy subSwarmsMergeStrategy;
    protected MergeStrategy mainSwarmMergeStrategy;
    protected MergeDetection mergeDetection;
    
    protected MergeStrategy mainSwarmAbsorptionStrategy;
    protected MergeStrategy subSwarmsAbsorptionStrategy;
    protected MergeDetection absorptionDetection;
    
    protected Particle mainSwarmParticle;

    /**
     * Create a new instance of Niche.
     */
    public Niche() {
        this.mainSwarm = new PSO();
        PSO pso = (PSO) this.mainSwarm;
        ((SynchronousIterationStrategy)pso.getIterationStrategy()).setBoundaryConstraint(new ReinitialisationBoundary());

        StandardVelocityProvider velocityUpdateStrategy = new StandardVelocityProvider();
        velocityUpdateStrategy.setSocialAcceleration(ConstantControlParameter.of(0.0));
        
        this.mainSwarmParticle = new StandardParticle();
        this.mainSwarmParticle.setVelocityInitializationStrategy(new RandomInitializationStrategy());
        this.mainSwarmParticle.setVelocityProvider(velocityUpdateStrategy);
        
        PopulationInitialisationStrategy mainSwarmInitialisationStrategy = new ClonedPopulationInitialisationStrategy();
        mainSwarmInitialisationStrategy.setEntityType(this.mainSwarmParticle);
        mainSwarmInitialisationStrategy.setEntityNumber(20);

        this.mainSwarm.setInitialisationStrategy(mainSwarmInitialisationStrategy);

        this.nicheIdentificationStrategy = new StandardNicheIdentificationStrategy();
        this.swarmCreationStrategy = new StandardSwarmCreationStrategy();
        
        this.mainSwarmAbsorptionStrategy = new SingleSwarmMergeStrategy();
        this.subSwarmsAbsorptionStrategy = new StandardMergeStrategy();
        this.absorptionDetection = new RadiusOverlapMergeDetection();
        
        this.subSwarmsMergeStrategy = new StandardMergeStrategy();
        this.mainSwarmMergeStrategy = new SingleSwarmMergeStrategy();
        this.mergeDetection = new RadiusOverlapMergeDetection();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PopulationBasedAlgorithm getClone() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Initialise the main population based algorithm, provided such a notion exists.
     * @see MultiPopulationBasedAlgorithm#performInitialisation()
     */
    @Override
    public void performInitialisation() {
        for (StoppingCondition stoppingCondition : getStoppingConditions())
            this.mainSwarm.addStoppingCondition(stoppingCondition);

        this.mainSwarm.setOptimisationProblem(getOptimisationProblem());

        this.mainSwarm.performInitialisation();
    }
    
    public static class Swarms extends P2<PopulationBasedAlgorithm, List<PopulationBasedAlgorithm>> {
        
        private final PopulationBasedAlgorithm mainSwarm;
        private final List<PopulationBasedAlgorithm> subSwarms;
        
        public Swarms(PopulationBasedAlgorithm mainSwarm, List<PopulationBasedAlgorithm> subSwarms) {
            this.mainSwarm = mainSwarm;
            this.subSwarms = subSwarms;
        }
        
        public static Swarms of(PopulationBasedAlgorithm mainSwarm, List<PopulationBasedAlgorithm> subSwarms) {
            return new Swarms(mainSwarm, subSwarms);
        }

        @Override
        public PopulationBasedAlgorithm _1() {
            return mainSwarm;
        }

        @Override
        public List<PopulationBasedAlgorithm> _2() {
            return subSwarms;
        }
        
    }
    
    /**
     * Merges sub-swarms that satisfy given conditions. Also handles merging with
     * the main swarm if applicable.
     * 
     * @param mergeDetection
     * @param mainSwarmMergeStrategy
     * @param subSwarmsMergeStrategy
     * 
     * @return A tuple containing the main swarm and the sub-swarms.
     */
    public static F<Swarms, Swarms> merge(final MergeDetection mergeDetection, final MergeStrategy mainSwarmMergeStrategy, final MergeStrategy subSwarmsMergeStrategy) {
        return new F<Swarms, Swarms>() {
            @Override
            public Swarms f(Swarms swarms) {
                if (swarms._2().isEmpty()) {
                    return Swarms.of(swarms._1(), List.<PopulationBasedAlgorithm>nil());
                }

                if (swarms._2().length() == 1) {
                    return Swarms.of(swarms._1(), List.single(swarms._2().head()));
                }

                PopulationBasedAlgorithm newMainSwarm = swarms._2().tail()
                        .filter(mergeDetection.f(swarms._2().head()))
                        .foldLeft(mainSwarmMergeStrategy, swarms._1());

                PopulationBasedAlgorithm mergedSwarms = swarms._2().tail()
                        .filter(mergeDetection.f(swarms._2().head()))
                        .foldLeft(subSwarmsMergeStrategy.flip(), swarms._2().head());

                Swarms newSwarms = 
                        this.f(Swarms.of(newMainSwarm, swarms._2().tail().removeAll(mergeDetection.f(swarms._2().head()))));

                return Swarms.of(newSwarms._1(), List.cons(mergedSwarms, newSwarms._2()));
            }        
        };
    }
    
    /**
     * Performs absorption between one sub-swarm and the main swarm. Each entity 
     * in the main swarm is in a swarm of its own.
     * 
     * @param absorptionDetection
     * @param mainSwarmAbsorptionStrategy
     * @param subSwarmsAbsorptionStrategy
     * 
     * @return A tuple containing the main swarm and the sub-swarm.
     */
    public static F<Swarms, P2<PopulationBasedAlgorithm, PopulationBasedAlgorithm>> absorbSingleSwarm(final MergeDetection absorptionDetection,
                                                                                                      final MergeStrategy mainSwarmAbsorptionStrategy,
                                                                                                      final MergeStrategy subSwarmsAbsorptionStrategy) {
        return new F<Swarms, P2<PopulationBasedAlgorithm, PopulationBasedAlgorithm>>() {
            @Override
            public P2<PopulationBasedAlgorithm, PopulationBasedAlgorithm> f(Swarms swarms) {
                PopulationBasedAlgorithm newSubSwarm = swarms._2()
                        .filter(absorptionDetection.f(swarms._1()))
                        .foldLeft(subSwarmsAbsorptionStrategy, swarms._1());

                PopulationBasedAlgorithm unmergedSwarms = swarms._2()
                        .removeAll(absorptionDetection.f(swarms._1()))
                        .foldLeft1(new StandardMergeStrategy());
                
                PopulationBasedAlgorithm mergedSwarms = swarms._2()
                        .filter(absorptionDetection.f(swarms._1()))
                        .foldLeft1(new StandardMergeStrategy());
                
                PopulationBasedAlgorithm newMainSwarm = mainSwarmAbsorptionStrategy.f(unmergedSwarms, mergedSwarms);

                return P.p(newMainSwarm, newSubSwarm);
            }        
        };
    }
    
    /**
     * Performs absorption between the main swarm and each sub-swarm. Each entity 
     * in the main swarm is split up into its own swarm.
     * 
     * @param absorptionDetection
     * @param mainSwarmAbsorptionStrategy
     * @param subSwarmsAbsorptionStrategy
     * @return A tuple containing the mainSwarm and a list of sub-swarms.
     */
    public static F<Swarms, Swarms> absorb(final MergeDetection absorptionDetection,
                                           final MergeStrategy mainSwarmAbsorptionStrategy,
                                           final MergeStrategy subSwarmsAbsorptionStrategy) {
        return new F<Swarms, Swarms>() {
            @Override
            public Swarms f(Swarms swarms) {
                if (swarms._2().isEmpty()) {
                    return Swarms.of(swarms._1(), List.<PopulationBasedAlgorithm>nil());
                }
                
                P2<PopulationBasedAlgorithm, PopulationBasedAlgorithm> newPopulations = 
                        absorbSingleSwarm(absorptionDetection, mainSwarmAbsorptionStrategy, subSwarmsAbsorptionStrategy)
                        .f(Swarms.of(swarms._2().head(), swarmToAlgorithms.f(swarms._1())));
                
                Swarms joinedPopulations = 
                        this.f(Swarms.of(newPopulations._1(), swarms._2().tail()));
                
                return Swarms.of(joinedPopulations._1(), List.cons(newPopulations._2(), joinedPopulations._2()));
            }
        };
    }
    
    public static F<Swarms, Swarms> createNiches(final NicheDetection nicheDetection,
                                                 final NicheCreationStrategy creationStrategy,
                                                 final MergeStrategy mainSwarmCreationMergingStrategy) {
        return new F<Swarms, Swarms>() {
            @Override
            public Swarms f(Swarms swarms) {
                List<Entity> entities = List.<Entity>iterableList((Topology<Entity>) swarms._1().getTopology());
                List<PopulationBasedAlgorithm> extraSubswarms = List.<PopulationBasedAlgorithm>nil();
                
                for (Entity e : entities.filter(nicheDetection)) {
                    extraSubswarms.cons(creationStrategy.f(swarms._1(), e));
                    //this is wrong: swarms._1() must change
                    //swarms = ???
                }
                
                PopulationBasedAlgorithm unmergedSwarms = entities
                        .removeAll(nicheDetection).map(entityToAlgorithm)
                        .foldLeft1(new StandardMergeStrategy());
                
                PopulationBasedAlgorithm mergedSwarms = entities
                        .filter(nicheDetection).map(entityToAlgorithm)
                        .foldLeft1(new StandardMergeStrategy());
                
                PopulationBasedAlgorithm newMainSwarm = mainSwarmCreationMergingStrategy.f(unmergedSwarms, mergedSwarms);
                
                return Swarms.of(newMainSwarm, swarms._2().append(extraSubswarms));
            }
        };
    }
    
    /**
     * A function that puts each entity of a swarm into its own swarm.
     */
    public static F<PopulationBasedAlgorithm, List<PopulationBasedAlgorithm>> swarmToAlgorithms = new F<PopulationBasedAlgorithm, List<PopulationBasedAlgorithm>>() {
        @Override
        public List<PopulationBasedAlgorithm> f(final PopulationBasedAlgorithm a) {
            return entitiesToAlgorithms.f((Topology<Entity>) a.getTopology());
        }  
    };
    
    public static F<Entity, PopulationBasedAlgorithm> entityToAlgorithm = new F<Entity, PopulationBasedAlgorithm>() {
        @Override
        public PopulationBasedAlgorithm f(Entity e) {
            PSO tmp = new PSO();
            tmp.getTopology().add((Particle) e);

            return (PopulationBasedAlgorithm) tmp;
        }
    };
    
    public static F<Iterable<Entity>, List<PopulationBasedAlgorithm>> entitiesToAlgorithms = new F<Iterable<Entity>, List<PopulationBasedAlgorithm>>() {

        @Override
        public List<PopulationBasedAlgorithm> f(Iterable<Entity> a) {
            return List.<Entity>iterableList(a).map(entityToAlgorithm);
        }
    };

    /**
     * <p>
     * Perform the iteration of the algorithm.
     * </p>
     * <p>
     * The general format of this method would be the following steps:
     * <ol>
     *   <li>Perform an iteration of the main swarm.</li>
     *   <li>Perform an iteration for each of the contained sub-swarms.</li>
     *   <li>Merge any sub-swarms as defined my the associated {@link MergeStrategy}.</li>
     *   <li>Perform an absorption step defined by a {@link AbsorptionStrategy}.</li>
     *   <li>Identify any new potential niches using a {@link NicheDetection}.</li>
     *   <li>Create new sub-swarms via a {@link NicheCreationStrategy} for the identified niches.</li>
     * </ol>
     * </p>
     */
    @Override
    protected void algorithmIteration() {
        mainSwarm.performIteration();

        for (PopulationBasedAlgorithm subSwarm : subPopulationsAlgorithms) {
            subSwarm.performIteration(); // TODO: There may be an issue with this and the number of iterations
        }

        List<PopulationBasedAlgorithm> subSwarms = List.<PopulationBasedAlgorithm>iterableList(subPopulationsAlgorithms);
        Swarms newPops = Swarms.of(mainSwarm, subSwarms);
        
        newPops = merge(mergeDetection, mainSwarmMergeStrategy, subSwarmsMergeStrategy).andThen(
                  absorb(absorptionDetection, mainSwarmAbsorptionStrategy, subSwarmsAbsorptionStrategy)).f(newPops);
        
        subPopulationsAlgorithms = Lists.newArrayList(newPops._2().toCollection());
        mainSwarm = newPops._1();
        
        for(Entity e : mainSwarm.getTopology()) {
            if (nicheIdentificationStrategy.f(e)) {
                //swarm creation
            }
        }

        //List<Entity> niches = this.nicheIdentificationStrategy.identify(mainSwarm.getTopology());
        //this.swarmCreationStrategy.create(this, niches);
    }

    /**
     * There is no best solution associated with a top level Niche algorithm.
     * @see #getSolutions()
     */
    @Override
    public OptimisationSolution getBestSolution() {
        throw new UnsupportedOperationException("Niching algorithms do not have a single solution.");
    }

    /**
     * Get the solutions of the the optimisation. The solutions are the best
     * entities within each identified niche.
     * @return The list of best solutions for each niche.
     */
    @Override
    public java.util.List<OptimisationSolution> getSolutions() {
        java.util.List<OptimisationSolution> list = new ArrayList<OptimisationSolution>();

        for (PopulationBasedAlgorithm subSwarm : this)
            list.add(subSwarm.getBestSolution());

        return list;
    }

    /**
     * Get the main swarm.
     * @return The main swarm.
     */
    public PopulationBasedAlgorithm getMainSwarm() {
        return this.mainSwarm;
    }

    /**
     * Set the main swarm of the Niche.
     * @param mainSwarm The swarm to set.
     */
    public void setMainSwarm(PopulationBasedAlgorithm mainSwarm) {
        this.mainSwarm = mainSwarm;
    }

    public MergeDetection getMergeDetection() {
        return mergeDetection;
    }

    public void setMergeDetection(MergeDetection mergeDetection) {
        this.mergeDetection = mergeDetection;
    }

    public MergeStrategy getMainSwarmMergeStrategy() {
        return mainSwarmMergeStrategy;
    }

    public void setMainSwarmMergeStrategy(MergeStrategy mainSwarmMergeStrategy) {
        this.mainSwarmMergeStrategy = mainSwarmMergeStrategy;
    }

    public MergeStrategy getSubSwarmsMergeStrategy() {
        return subSwarmsMergeStrategy;
    }

    public void setSubSwarmsMergeStrategy(MergeStrategy subSwarmsMergeStrategy) {
        this.subSwarmsMergeStrategy = subSwarmsMergeStrategy;
    }

    public MergeDetection getAbsorptionDetection() {
        return absorptionDetection;
    }

    public void setAbsorptionDetection(MergeDetection absorptionDetection) {
        this.absorptionDetection = absorptionDetection;
    }

    public MergeStrategy getMainSwarmAbsorptionStrategy() {
        return mainSwarmAbsorptionStrategy;
    }

    public void setMainSwarmAbsorptionStrategy(MergeStrategy mainSwarmAbsorptionStrategy) {
        this.mainSwarmAbsorptionStrategy = mainSwarmAbsorptionStrategy;
    }

    public MergeStrategy getSubSwarmsAbsorptionStrategy() {
        return subSwarmsAbsorptionStrategy;
    }

    public void setSubSwarmsAbsorptionStrategy(MergeStrategy subSwarmsAbsorptionStrategy) {
        this.subSwarmsAbsorptionStrategy = subSwarmsAbsorptionStrategy;
    }
}
