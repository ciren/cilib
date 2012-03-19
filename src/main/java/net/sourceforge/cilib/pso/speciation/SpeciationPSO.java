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

package net.sourceforge.cilib.pso.speciation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.sourceforge.cilib.algorithm.initialisation.ClonedPopulationInitialisationStrategy;
import net.sourceforge.cilib.algorithm.initialisation.PopulationInitialisationStrategy;
import net.sourceforge.cilib.algorithm.population.MultiPopulationBasedAlgorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.initialization.RandomInitializationStrategy;
import net.sourceforge.cilib.problem.OptimisationSolution;
import net.sourceforge.cilib.problem.boundaryconstraint.ReinitialisationBoundary;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.iterationstrategies.SynchronousIterationStrategy;
import net.sourceforge.cilib.pso.particle.StandardParticle;
import net.sourceforge.cilib.pso.velocityprovider.StandardVelocityProvider;
import net.sourceforge.cilib.stoppingcondition.StoppingCondition;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 *
 */
public class SpeciationPSO extends MultiPopulationBasedAlgorithm {
    private PSO mainSwarm;
    private ArrayList<Particle> sortedList;
    private ArrayList<Particle> seedsList;
    private ControlParameter radius;


    public SpeciationPSO(){
        this.mainSwarm = new PSO();
        PSO pso = (PSO) this.mainSwarm;
        ((SynchronousIterationStrategy)pso.getIterationStrategy()).setBoundaryConstraint(new ReinitialisationBoundary());

        Particle mainSwarmParticle = new StandardParticle();
        mainSwarmParticle.setVelocityInitializationStrategy(new RandomInitializationStrategy());
        StandardVelocityProvider velocityUpdateStrategy = new StandardVelocityProvider();

        mainSwarmParticle.setVelocityProvider(velocityUpdateStrategy);
        PopulationInitialisationStrategy mainSwarmInitialisationStrategy = new ClonedPopulationInitialisationStrategy();
        mainSwarmInitialisationStrategy.setEntityType(mainSwarmParticle);
        mainSwarmInitialisationStrategy.setEntityNumber(20);

        this.mainSwarm.setInitialisationStrategy(mainSwarmInitialisationStrategy);
        this.sortedList = new ArrayList<Particle>();
        this.seedsList = new ArrayList<Particle>();
        this.radius = ConstantControlParameter.of(3.0);
    }

    /**
     * Initialise the main population based algorithm.
     * @see MultiPopulationBasedAlgorithm#performInitialisation()
     */
    @Override
    public void performInitialisation() {
        for (StoppingCondition stoppingCondition : getStoppingConditions())
            this.mainSwarm.addStoppingCondition(stoppingCondition);

        this.mainSwarm.setOptimisationProblem(getOptimisationProblem());
        this.mainSwarm.performInitialisation();
    }

    @Override
    protected void algorithmIteration() {
        sortParticles();
        getSeeds();
        replaceRedundantSpecies();
        mainSwarm.performIteration();
    }

    @Override
    public OptimisationSolution getBestSolution() {
        throw new UnsupportedOperationException("Niching algorithms do not have a single solution.");
    }

    @Override
    public Iterable<OptimisationSolution> getSolutions() {
        List<OptimisationSolution> list = new ArrayList<OptimisationSolution>();

        Particle p;
        for(int i=0; i<seedsList.size(); i++){
            p = seedsList.get(i);
            list.add(new OptimisationSolution(p.getBestPosition(), p.getBestFitness()));
        }

        return list;
    }

    @Override
    public PopulationBasedAlgorithm getClone() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * The algorithm takes a sorted list containing all particles sorted in
     * decreasing order of fitness. The species seed set is initially empty.
     * All particles are checked in turn (from best to least-fit) against
     * the species seeds found so far. If a particle does not fall within the
     * radius of all the seeds of seeds list, then this particle will become
     * a new seed and be added to the seeds list
     */
    private void getSeeds() {
        boolean FOUND;
        double euDistPresent = 0.0;
        int seedListSize;
        double sumPresent;

        seedsList.clear();

        int dimension = sortedList.get(0).getDimension();

        Iterator sortedIter = sortedList.iterator();
        StandardParticle p, seed;
        Vector p2, seed2;

        while (sortedIter.hasNext())
        {
          p = (StandardParticle) sortedIter.next();
          p2 = p.getBestPosition();

          FOUND = false;

          seedListSize = seedsList.size();

          for (int i=0; i < seedListSize; i++)
          {
             sumPresent = 0;
             seed = (StandardParticle) seedsList.get(i);
             seed2 = seed.getBestPosition();

             for (int j=0; j < dimension; j++){
                 sumPresent += Math.pow(p2.getReal(j) - seed2.getReal(j), 2);
             }
             euDistPresent = Math.sqrt(sumPresent);

             // if Euclidean distance is less than the radius
             if (euDistPresent < radius.getParameter())
             {
                // assign each particle its leader
                p.setNeighbourhoodBest(seed);

                FOUND = true;
                break;   // break the for loop
             }
          }

          if (FOUND != true)
              seedsList.add(p);
        }//while loop
    }

    /**
     * Sorts the particles in a decreasing order.
     */
    private void sortParticles(){
        sortedList = (ArrayList<Particle>) this.mainSwarm.getTopology().asList();
        Particle p1,p2,temp;
        int end = sortedList.size();
        for(int i=0; i<end-1;i++){
            p1 = sortedList.get(i);
            for(int j=i+1; j<end; j++){
                p2 = sortedList.get(i);
                p1.calculateFitness();
                p2.calculateFitness();
                if(p1.getFitness().compareTo(p2.getFitness()) < 0){
                    temp = p1;
                    p1 = p2;
                    p2 = temp;
                    sortedList.set(i, p1);
                    sortedList.set(j, p2);
                }
            }
        }
    }

    /**
     * Replace the particles that converge to the same optima with randomly
     * generated particles
     */
    private void replaceRedundantSpecies(){
        int initialSize = sortedList.size();
        int initial = initialSize;
        Particle p, seed;
        for(int i=0; i<initial; i++){
            p = sortedList.get(i);
            seed = (StandardParticle) p.getNeighbourhoodBest();
            if(p.getFitness().compareTo(seed.getFitness()) == 0){
                sortedList.remove(i);
                initial--;
                i--;
            }
        }
        sortedList.addAll(seedsList);
        while(sortedList.size() < initialSize){
            p = sortedList.get(0);
            p.reinitialise();
            p.setNeighbourhoodBest(p);
            sortedList.add(p);
        }

        Particle temp;
        Topology<Particle> sendList = (Topology<Particle>) mainSwarm.getTopology();
        int end = sendList.size();

        for(int i=0; i<end; i++){
            temp = (Particle) sortedList.get(i);
            sendList.set(i, temp);
        }

        mainSwarm.setTopology(sendList);
        
    }

    /**
     * @param radius the radius to set
     */
    public void setRadius(ControlParameter radius) {
        this.radius = radius;
    }
}
