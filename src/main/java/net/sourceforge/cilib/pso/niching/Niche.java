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

import net.sourceforge.cilib.pso.niching.creation.NicheCreationStrategy;
import net.sourceforge.cilib.pso.niching.creation.StandardNicheIdentificationStrategy;
import net.sourceforge.cilib.pso.niching.creation.NicheDetection;
import net.sourceforge.cilib.pso.niching.creation.StandardSwarmCreationStrategy;
import com.google.common.collect.Lists;
import fj.F;
import fj.F2;
import fj.P;
import fj.P2;
import java.util.ArrayList;
import java.util.List;
import net.sourceforge.cilib.algorithm.initialisation.ClonedPopulationInitialisationStrategy;
import net.sourceforge.cilib.algorithm.initialisation.PopulationInitialisationStrategy;
import net.sourceforge.cilib.algorithm.population.MultiPopulationBasedAlgorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.initialization.RandomInitializationStrategy;
import net.sourceforge.cilib.entity.topologies.GBestTopology;
import net.sourceforge.cilib.problem.OptimisationSolution;
import net.sourceforge.cilib.problem.boundaryconstraint.ReinitialisationBoundary;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.iterationstrategies.SynchronousIterationStrategy;
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
    public static F<P2<PopulationBasedAlgorithm, fj.data.List<PopulationBasedAlgorithm>>, P2<PopulationBasedAlgorithm, fj.data.List<PopulationBasedAlgorithm>>> merge(final MergeDetection mergeDetection, final MergeStrategy mainSwarmMergeStrategy, final MergeStrategy subSwarmsMergeStrategy) {
        return new F<P2<PopulationBasedAlgorithm, fj.data.List<PopulationBasedAlgorithm>>, P2<PopulationBasedAlgorithm, fj.data.List<PopulationBasedAlgorithm>>>() {
            @Override
            public P2<PopulationBasedAlgorithm, fj.data.List<PopulationBasedAlgorithm>> f(P2<PopulationBasedAlgorithm, fj.data.List<PopulationBasedAlgorithm>> swarms) {
                if (swarms._2().isEmpty()) {
                    return P.p(swarms._1(), fj.data.List.<PopulationBasedAlgorithm>nil());
                }

                if (swarms._2().length() == 1) {
                    return P.p(swarms._1(), fj.data.List.single(swarms._2().head()));
                }

                PopulationBasedAlgorithm newMainSwarm = swarms._2().tail()
                        .filter(mergeDetection.f(swarms._2().head()))
                        .foldLeft(mainSwarmMergeStrategy, swarms._1());

                PopulationBasedAlgorithm mergedSwarms = swarms._2().tail()
                        .filter(mergeDetection.f(swarms._2().head()))
                        .foldLeft(subSwarmsMergeStrategy.flip(), swarms._2().head());

                P2<PopulationBasedAlgorithm, fj.data.List<PopulationBasedAlgorithm>> newSwarms = 
                        this.f(P.p(newMainSwarm, swarms._2().tail().removeAll(mergeDetection.f(swarms._2().head()))));

                return P.p(newSwarms._1(), fj.data.List.cons(mergedSwarms, newSwarms._2()));
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
    public static F2<PopulationBasedAlgorithm, fj.data.List<PopulationBasedAlgorithm>, P2<PopulationBasedAlgorithm, PopulationBasedAlgorithm>> absorbSingleSwarm(final MergeDetection absorptionDetection, final MergeStrategy mainSwarmAbsorptionStrategy, final MergeStrategy subSwarmsAbsorptionStrategy) {
        return new F2<PopulationBasedAlgorithm, fj.data.List<PopulationBasedAlgorithm>, P2<PopulationBasedAlgorithm, PopulationBasedAlgorithm>>() {
            @Override
            public P2<PopulationBasedAlgorithm, PopulationBasedAlgorithm> f(PopulationBasedAlgorithm absorgingSwarm, fj.data.List<PopulationBasedAlgorithm> mainSwarm) {
                PopulationBasedAlgorithm newSubSwarm = mainSwarm
                        .filter(absorptionDetection.f(absorgingSwarm))
                        .foldLeft(subSwarmsAbsorptionStrategy, absorgingSwarm);

                PopulationBasedAlgorithm unmergedSwarms = mainSwarm
                        .removeAll(absorptionDetection.f(absorgingSwarm))
                        .foldLeft1(new StandardMergeStrategy());
                
                PopulationBasedAlgorithm mergedSwarms = mainSwarm
                        .filter(absorptionDetection.f(absorgingSwarm))
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
    public static F<P2<PopulationBasedAlgorithm, fj.data.List<PopulationBasedAlgorithm>>, P2<PopulationBasedAlgorithm, fj.data.List<PopulationBasedAlgorithm>>> absorb(final MergeDetection absorptionDetection, final MergeStrategy mainSwarmAbsorptionStrategy, final MergeStrategy subSwarmsAbsorptionStrategy) {
        return new F<P2<PopulationBasedAlgorithm, fj.data.List<PopulationBasedAlgorithm>>, P2<PopulationBasedAlgorithm, fj.data.List<PopulationBasedAlgorithm>>>() {
            @Override
            public P2<PopulationBasedAlgorithm, fj.data.List<PopulationBasedAlgorithm>> f(P2<PopulationBasedAlgorithm, fj.data.List<PopulationBasedAlgorithm>> swarms) {
                if (swarms._2().isEmpty()) {
                    return P.p(swarms._1(), fj.data.List.<PopulationBasedAlgorithm>nil());
                }
                
                P2<PopulationBasedAlgorithm, PopulationBasedAlgorithm> newPopulations = 
                        absorbSingleSwarm(absorptionDetection, mainSwarmAbsorptionStrategy, subSwarmsAbsorptionStrategy)
                        .f(swarms._2().head(), swarmToAlgorithms.f(swarms._1()));
                
                P2<PopulationBasedAlgorithm, fj.data.List<PopulationBasedAlgorithm>> joinedPopulations = 
                        this.f(P.p(newPopulations._1(), swarms._2().tail()));
                
                return P.p(joinedPopulations._1(), fj.data.List.cons(newPopulations._2(), joinedPopulations._2()));
            }
        };
    }
    
    /**
     * A function that puts each entity of a swarm into its own swarm.
     */
    public static F<PopulationBasedAlgorithm, fj.data.List<PopulationBasedAlgorithm>> swarmToAlgorithms = new F<PopulationBasedAlgorithm, fj.data.List<PopulationBasedAlgorithm>>() {
        @Override
        public fj.data.List<PopulationBasedAlgorithm> f(final PopulationBasedAlgorithm a) {
            return fj.data.List.<Entity>iterableList((Topology<Entity>) a.getTopology()).map(new F<Entity, PopulationBasedAlgorithm>() {
                @Override
                public PopulationBasedAlgorithm f(Entity e) {
                    PSO tmp = (PSO) a.getClone();
                    tmp.setTopology(new GBestTopology<Particle>());
                    tmp.getTopology().add((Particle) e);

                    return (PopulationBasedAlgorithm) tmp;
                }
            });
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

        fj.data.List<PopulationBasedAlgorithm> subSwarms = fj.data.List.<PopulationBasedAlgorithm>iterableList(subPopulationsAlgorithms);
        P2<PopulationBasedAlgorithm, fj.data.List<PopulationBasedAlgorithm>> newPops = P.p(mainSwarm, subSwarms);
        
        newPops = merge(mergeDetection, mainSwarmMergeStrategy, subSwarmsMergeStrategy)
                .andThen(absorb(absorptionDetection, mainSwarmAbsorptionStrategy, subSwarmsAbsorptionStrategy)).f(newPops);
        
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
    public List<OptimisationSolution> getSolutions() {
        List<OptimisationSolution> list = new ArrayList<OptimisationSolution>();

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
