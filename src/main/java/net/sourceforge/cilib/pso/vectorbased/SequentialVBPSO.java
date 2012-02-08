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

package net.sourceforge.cilib.pso.vectorbased;

import java.util.ArrayList;
import java.util.List;
import net.sourceforge.cilib.algorithm.initialisation.ClonedPopulationInitialisationStrategy;
import net.sourceforge.cilib.algorithm.initialisation.PopulationInitialisationStrategy;
import net.sourceforge.cilib.algorithm.population.MultiPopulationBasedAlgorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.entity.initialization.RandomInitializationStrategy;
import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.math.random.generator.RandomProvider;
import net.sourceforge.cilib.problem.FunctionMaximisationProblem;
import net.sourceforge.cilib.problem.FunctionMinimisationProblem;
import net.sourceforge.cilib.problem.OptimisationSolution;
import net.sourceforge.cilib.problem.boundaryconstraint.ReinitialisationBoundary;
import net.sourceforge.cilib.pso.particle.VBParticle;
import net.sourceforge.cilib.pso.velocityprovider.StandardVelocityProvider;
import net.sourceforge.cilib.stoppingcondition.StoppingCondition;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.DistanceMeasure;
import net.sourceforge.cilib.util.EuclideanDistanceMeasure;

/**
 *
 * @author wayne
 */
public class SequentialVBPSO extends MultiPopulationBasedAlgorithm {
    private VBPSO mainSwarm;
    private RandomProvider randomProvider;
    private ContinuousFunction function;
    private boolean isMax;
    private VBParticle yhead;
    private DistanceMeasure distanceMeaser;
    private double nicheRadius;
    private int currentNiche;
    private int innerIterations = 500;
    private PopulationInitialisationStrategy mainSwarmInitialisationStrategy;

    public SequentialVBPSO(){
        this.mainSwarm = new VBPSO();
        VBPSO pso = (VBPSO) this.mainSwarm;
        ((SynchronousIterationStrategy)pso.getIterationStrategy()).setBoundaryConstraint(new ReinitialisationBoundary());

        VBParticle mainSwarmParticle = new VBParticle();
        mainSwarmParticle.setVelocityInitializationStrategy(new RandomInitializationStrategy());
        StandardVelocityProvider velocityUpdateStrategy = new StandardVelocityProvider();
        velocityUpdateStrategy.setCognitiveAcceleration(new ConstantControlParameter(1.0));
        velocityUpdateStrategy.setSocialAcceleration(new ConstantControlParameter(1.0));
        velocityUpdateStrategy.setInertiaWeight(new ConstantControlParameter(0.8));

        mainSwarmParticle.setVelocityProvider(velocityUpdateStrategy);
        mainSwarmInitialisationStrategy = new ClonedPopulationInitialisationStrategy();
        mainSwarmInitialisationStrategy.setEntityType(mainSwarmParticle);
        mainSwarmInitialisationStrategy.setEntityNumber(20);

        this.mainSwarm.setInitialisationStrategy(mainSwarmInitialisationStrategy);
        this.randomProvider = new MersenneTwister();
        distanceMeaser = new EuclideanDistanceMeasure();
        currentNiche = 0;

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
        if(mainSwarm.getTopology().size() == 0){
            //do nothing
        }else{
            if(this.getIterations() == 0){
                try{
                    FunctionMaximisationProblem prob = (FunctionMaximisationProblem) this.getOptimisationProblem();
                    function = (ContinuousFunction) prob.getFunction();
                    isMax = true;
                }catch (ClassCastException e){
                    FunctionMinimisationProblem prob = (FunctionMinimisationProblem) this.getOptimisationProblem();
                    function = (ContinuousFunction) prob.getFunction();
                    isMax = false;
                }finally{
                    initialIteration();
                }
            }
            calculateNeighbourhoodBest();

            double smallR = 1000000000000000.0;
            double radius;

            for(VBParticle p: mainSwarm.getTopology()){
                p.setNeighbourhoodBest(yhead.getClone());
                p.calculateVG();
                p.calculateDotProduct();
                radius = distanceMeaser.distance(yhead.getBestPosition(), p.getPosition());
                p.setRadius(radius);
                if(radius < smallR && p.getDotProduct() < 0){
                    smallR = radius;
                }
            }
            nicheRadius = smallR;
            calculateNewNiche();

            iterateSubswarm();
        }
    }

    private void initialIteration(){
        double s;
        for(VBParticle p: mainSwarm.getTopology()){
            p.calculateFitness();
            Vector random = (Vector) p.getPosition().getClone();
            random.randomize(randomProvider);
            s = function.apply(random);
            if(isMax){
                if(s > p.getFitness().getValue()){
                    p.setPersonalBest(random);
                }else{
                    p.setPersonalBest(p.getPosition());
                    p.setPosition(random);
                }
            }else{
                if(s < p.getFitness().getValue()){
                    p.setPersonalBest(random);
                }else{
                    p.setPersonalBest(p.getPosition());
                    p.setPosition(random);
                }
            }
            p.setNeighbourhoodBest(p.getClone());
            p.calculateVP();
        }
    }

    private void calculateNeighbourhoodBest(){
        yhead = mainSwarm.getTopology().get(0);
        double first, second;
        if(isMax){
            for(VBParticle particle: mainSwarm.getTopology()){
                first = function.apply((Vector) particle.getBestPosition());
                second = function.apply(yhead.getBestPosition());
                if(first > second){
                    yhead = particle;
                }
            }
        }else{
            for(VBParticle particle: mainSwarm.getTopology()){
                first = function.apply((Vector) particle.getBestPosition());
                second = function.apply(yhead.getBestPosition());
                if(first < second){
                    yhead = particle;
                }
            }
        }
        mainSwarm.getTopology().remove(yhead);

    }

    private void calculateNewNiche(){
        currentNiche++;
        VBPSO sub = mainSwarm.getClone();
        sub.getTopology().clear();
        yhead.setNicheID(currentNiche);
        yhead.setNeighbourhoodBest(yhead.getClone());
        sub.getTopology().add(yhead);

        for(VBParticle p: mainSwarm.getTopology()){
            if((p.getDotProduct() > 0 && p.getRadius() < nicheRadius)||(p.getBestPosition().equals(yhead.getBestPosition()))){
                p.setNicheID(currentNiche);
                p.setNeighbourhoodBest(yhead.getClone());
                sub.getTopology().add(p);
            }
        }

        for(VBParticle p: sub.getTopology()){
            mainSwarm.getTopology().remove(p);
        }
        VBParticle tmp = yhead.getClone();
        while(sub.getTopology().size() < 3){
            tmp.reinitialise();
            tmp.setNeighbourhoodBest(yhead.getClone());
            tmp.setPersonalBest(tmp.getPosition().getClone());
            sub.getTopology().add(tmp);
        }

        this.subPopulationsAlgorithms.add(sub);
    }

    private void iterateSubswarm(){
        VBPSO sub = (VBPSO) this.subPopulationsAlgorithms.get(currentNiche-1);
        for(int i=0; i<innerIterations; i++){
            sub.performIteration();
        }
    }

    @Override
    public OptimisationSolution getBestSolution() {
        throw new UnsupportedOperationException("Niching algorithms do not have a single solution.");
    }

    @Override
    public Iterable<OptimisationSolution> getSolutions() {
        List<OptimisationSolution> list = new ArrayList<OptimisationSolution>();

        for (PopulationBasedAlgorithm subSwarm : this)
            list.add(subSwarm.getBestSolution());

        return list;
    }

    @Override
    public PopulationBasedAlgorithm getClone() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * @param innerIterations the innerIterations to set
     */
    public void setInnerIterations(int innerIterations) {
        this.innerIterations = innerIterations;
    }

}