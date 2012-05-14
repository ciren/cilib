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
package net.sourceforge.cilib.clustering.entity;

import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.initialization.CentroidInitializationStrategy;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.problem.InferiorFitness;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.pso.particle.AbstractParticle;
import net.sourceforge.cilib.pso.particle.StandardParticle;
import net.sourceforge.cilib.pso.pbestupdate.StandardPersonalBestUpdateStrategy;
import net.sourceforge.cilib.type.types.Int;
import net.sourceforge.cilib.type.types.container.CentroidHolder;
import net.sourceforge.cilib.type.types.container.ClusterCentroid;
import net.sourceforge.cilib.util.calculator.EntityBasedFitnessCalculator;

/**
 *
 * @author Kristina
 */
public class ClusterParticle extends AbstractParticle {
    
    //private CentroidHolder candidateSolution;
    private ClusterParticle neighbourhoodBest;
    private int numberOfClusters;
    private CentroidInitializationStrategy centroidInitialisationStrategyCandidate;
    private CentroidInitializationStrategy centroidInitialisationStrategyBest;
    private CentroidInitializationStrategy centroidInitialisationStrategyVelocity;
    

    public ClusterParticle() {
        super();
        personalBestUpdateStrategy = new StandardPersonalBestUpdateStrategy();
        centroidInitialisationStrategyCandidate = new CentroidInitializationStrategy();
        centroidInitialisationStrategyBest = new CentroidInitializationStrategy();
        centroidInitialisationStrategyVelocity = new CentroidInitializationStrategy();
        numberOfClusters = 1;
    }
    
    public ClusterParticle(ClusterParticle copy) {
        super(copy);
        personalBestUpdateStrategy = copy.personalBestUpdateStrategy.getClone();
        centroidInitialisationStrategyCandidate = copy.centroidInitialisationStrategyCandidate;
        centroidInitialisationStrategyBest = copy.centroidInitialisationStrategyBest;
        centroidInitialisationStrategyVelocity = copy.centroidInitialisationStrategyVelocity;
        numberOfClusters = copy.numberOfClusters;
    }
    
    /*@Override
    public CentroidHolder getCandidateSolution() {
        return candidateSolution;
    }
    
    public void setCandidateSolution(CentroidHolder solution) {
        candidateSolution = solution;
    }*/
    
    @Override
    public ClusterParticle getClone() {
        return new ClusterParticle(this);
    }

   
    @Override
    public void calculateFitness() {
        /*QuantizationErrorBasedFitnessCalculation fitnessCalculator = new QuantizationErrorBasedFitnessCalculation();
        Fitness newFitness = fitnessCalculator.getFitness((Entity) this);
        this.getProperties().put(EntityType.FITNESS, newFitness);
        
        this.personalBestUpdateStrategy.updatePersonalBest(this);*/
        
        EntityBasedFitnessCalculator f = new EntityBasedFitnessCalculator();
        Fitness fitness = f.getFitness(this);
        this.getProperties().put(EntityType.FITNESS, fitness);

        this.personalBestUpdateStrategy.updatePersonalBest(this);
    }

    @Override
    public Fitness getBestFitness() {
        return (Fitness) this.getProperties().get(EntityType.Particle.BEST_FITNESS);
    }

    @Override
    public int getDimension() {
        return getCandidateSolution().size();
    }

    @Override
    public CentroidHolder getPosition() {
        return (CentroidHolder) getCandidateSolution();
    }

    @Override
    public CentroidHolder getBestPosition() {
        return (CentroidHolder) this.getProperties().get(EntityType.Particle.BEST_POSITION);
    }

    @Override
    public CentroidHolder getVelocity() {
        return (CentroidHolder) this.getProperties().get(EntityType.Particle.VELOCITY);
    }

    @Override
    public ClusterParticle getNeighbourhoodBest() {
        return neighbourhoodBest;
    }

    @Override
    public void updatePosition() {
        CentroidHolder newCandidateSolution = new CentroidHolder();
        ClusterCentroid newCentroid;
        Particle particle;
        Particle neighbourhoodBestParticle;
        int index = 0;
        for(ClusterCentroid centroid : (CentroidHolder) getCandidateSolution()) {
            particle = new StandardParticle();
            neighbourhoodBestParticle = new StandardParticle();
            particle.setCandidateSolution(centroid.toVector());
            particle.getProperties().put(EntityType.Particle.VELOCITY, this.getVelocity().get(index).toVector());
            particle.getProperties().put(EntityType.Particle.BEST_POSITION, this.getBestPosition().get(index).toVector());
            particle.getProperties().put(EntityType.Particle.BEST_FITNESS, this.getBestFitness());
            particle.getProperties().put(EntityType.FITNESS, this.getFitness());
            
            neighbourhoodBestParticle.setCandidateSolution(((CentroidHolder) getNeighbourhoodBest().getCandidateSolution()).get(index).toVector());
            neighbourhoodBestParticle.getProperties().put(EntityType.Particle.VELOCITY, getNeighbourhoodBest().getVelocity().get(index).toVector());
            neighbourhoodBestParticle.getProperties().put(EntityType.Particle.BEST_POSITION, getNeighbourhoodBest().getBestPosition().get(index).toVector());
            neighbourhoodBestParticle.getProperties().put(EntityType.Particle.BEST_FITNESS, getNeighbourhoodBest().getBestFitness());
            neighbourhoodBestParticle.getProperties().put(EntityType.FITNESS, getNeighbourhoodBest().getFitness());
            
            particle.setNeighbourhoodBest(neighbourhoodBestParticle);
            newCentroid = new ClusterCentroid();
            newCentroid.copy(this.behavior.getPositionProvider().get(particle));
            newCandidateSolution.add(newCentroid);
            index++;
        }
        
        this.setCandidateSolution(newCandidateSolution);
        
    }

    @Override
    public void updateVelocity() {
        CentroidHolder newVelocity = new CentroidHolder();
        ClusterCentroid newCentroid;
        Particle particle;
        int index = 0;
        Particle neighbourhoodBestParticle;
        for(ClusterCentroid centroid : (CentroidHolder) getCandidateSolution()) {
            particle = new StandardParticle();
            neighbourhoodBestParticle = new StandardParticle();
            particle.setCandidateSolution(centroid.toVector());
            particle.getProperties().put(EntityType.Particle.VELOCITY, this.getVelocity().get(index).toVector());
            particle.getProperties().put(EntityType.Particle.BEST_POSITION, this.getBestPosition().get(index).toVector());
            particle.getProperties().put(EntityType.Particle.BEST_FITNESS, this.getBestFitness());
            particle.getProperties().put(EntityType.FITNESS, this.getFitness());
            
            neighbourhoodBestParticle.setCandidateSolution(((CentroidHolder) getNeighbourhoodBest().getCandidateSolution()).get(index).toVector());
            neighbourhoodBestParticle.getProperties().put(EntityType.Particle.VELOCITY, getNeighbourhoodBest().getVelocity().get(index).toVector());
            neighbourhoodBestParticle.getProperties().put(EntityType.Particle.BEST_POSITION, getNeighbourhoodBest().getBestPosition().get(index).toVector());
            neighbourhoodBestParticle.getProperties().put(EntityType.Particle.BEST_FITNESS, getNeighbourhoodBest().getBestFitness());
            neighbourhoodBestParticle.getProperties().put(EntityType.FITNESS, getNeighbourhoodBest().getFitness());
            
            particle.setNeighbourhoodBest(neighbourhoodBestParticle);
            newCentroid = new ClusterCentroid();
            newCentroid.copy(this.behavior.getVelocityProvider().get(particle));
            newVelocity.add(newCentroid);
            index++;
        }
        
        getProperties().put(EntityType.Particle.VELOCITY, newVelocity);
    }

    @Override
    public void initialise(OptimisationProblem problem) {
        this.getProperties().put(EntityType.CANDIDATE_SOLUTION, new CentroidHolder(numberOfClusters, problem.getDomain().getDimension()));
        this.getProperties().put(EntityType.Particle.BEST_POSITION,  new CentroidHolder(numberOfClusters, problem.getDomain().getDimension()));
        this.getProperties().put(EntityType.Particle.VELOCITY,  new CentroidHolder(numberOfClusters, problem.getDomain().getDimension()));
        
        centroidInitialisationStrategyCandidate.setInitialisationStrategy(positionInitialisationStrategy);
        centroidInitialisationStrategyCandidate.initialize(EntityType.CANDIDATE_SOLUTION, this);
        
        centroidInitialisationStrategyBest.setInitialisationStrategy(personalBestInitialisationStrategy);
        centroidInitialisationStrategyBest.initialize(EntityType.Particle.BEST_POSITION, this);
        
        centroidInitialisationStrategyVelocity.setInitialisationStrategy(velocityInitializationStrategy);
        centroidInitialisationStrategyVelocity.initialize(EntityType.Particle.VELOCITY, this);

        this.getProperties().put(EntityType.FITNESS, InferiorFitness.instance());
        this.getProperties().put(EntityType.Particle.BEST_FITNESS, InferiorFitness.instance());
        this.neighbourhoodBest = this;

        this.getProperties().put(EntityType.Particle.Count.PBEST_STAGNATION_COUNTER, Int.valueOf(0));
        this.getProperties().put(EntityType.PREVIOUS_SOLUTION, getCandidateSolution());
        
        
    }

    @Override
    public void reinitialise() {
        centroidInitialisationStrategyCandidate.setInitialisationStrategy(positionInitialisationStrategy);
        this.centroidInitialisationStrategyCandidate.initialize(EntityType.CANDIDATE_SOLUTION, this);
        centroidInitialisationStrategyBest.setInitialisationStrategy(personalBestInitialisationStrategy);
        this.centroidInitialisationStrategyBest.initialize(EntityType.Particle.BEST_POSITION, this);
        centroidInitialisationStrategyVelocity.setInitialisationStrategy(velocityInitializationStrategy);
        this.centroidInitialisationStrategyVelocity.initialize(EntityType.Particle.VELOCITY, this);
    }

    @Override
    public void setNeighbourhoodBest(Particle particle) {
        neighbourhoodBest = (ClusterParticle) particle;
    }

    public void setNumberOfCusters(int total) {
        numberOfClusters = total;
    }
    
    public int getNumberOfClusters() {
        return numberOfClusters;
    }
    
    public void setVelocity(CentroidHolder holder) {
        this.getProperties().put(EntityType.Particle.VELOCITY, holder);
    }
    
}
