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
public class ParallelVBPSO extends MultiPopulationBasedAlgorithm {
    protected VBPSO mainSwarm;
    protected RandomProvider randomProvider;
    protected ContinuousFunction function;
    protected boolean isMax;
    protected VBParticle yhead;
    protected DistanceMeasure distanceMeaser;
    protected double nicheRadius;
    protected int currentNiche;
    protected double granularity;
    protected int innerIterations = 10;
    protected int m;

    public ParallelVBPSO(){
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
        PopulationInitialisationStrategy mainSwarmInitialisationStrategy = new ClonedPopulationInitialisationStrategy();
        mainSwarmInitialisationStrategy.setEntityType(mainSwarmParticle);
        mainSwarmInitialisationStrategy.setEntityNumber(20);

        this.mainSwarm.setInitialisationStrategy(mainSwarmInitialisationStrategy);
        this.randomProvider = new MersenneTwister();
        distanceMeaser = new EuclideanDistanceMeasure();
        currentNiche = 0;
        granularity = 0.05;
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
        m = 500/this.innerIterations;
    }

    @Override
    protected void algorithmIteration() {
        if(this.getIterations() == 0){
            updateVPs();
            while(this.mainSwarm.getTopology().size() > 0){
                createNiches();
            }
        }
        iterateSubSwarms();
        double iter = this.getIterations();
        if(((iter%m == 0) && (iter != 0))){
            merge();
        }
    }

    @Override
    public OptimisationSolution getBestSolution() {
        throw new UnsupportedOperationException("Niching algorithms do not have a single solution.");
    }

    @Override
    public Iterable<OptimisationSolution> getSolutions() {
        List<OptimisationSolution> list = new ArrayList<OptimisationSolution>();

        for (PopulationBasedAlgorithm subSwarm : this.subPopulationsAlgorithms){
            list.add(subSwarm.getBestSolution());
        }

        return list;
    }

    @Override
    public PopulationBasedAlgorithm getClone() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private void updateVPs(){
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
            }
    }

    private void createNiches(){
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
        setCurrentNiche(getCurrentNiche() + 1);
        VBPSO sub = mainSwarm.getClone();
        sub.getTopology().clear();
        yhead.setNicheID(getCurrentNiche());
        yhead.setNeighbourhoodBest(yhead.getClone());
        sub.getTopology().add(yhead);

        for(VBParticle p: mainSwarm.getTopology()){
            if((p.getDotProduct() > 0 && p.getRadius() < nicheRadius)||(p.getBestPosition().equals(yhead.getBestPosition()))){
                p.setNicheID(getCurrentNiche());
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

        subPopulationsAlgorithms.add(sub);
    }

    public void iterateSubSwarms(){
        for(int i=0; i<subPopulationsAlgorithms.size(); i++){
            VBPSO sub = (VBPSO) subPopulationsAlgorithms.get(i);
            sub.performIteration();
        }
    }

    private void merge(){
        if(subPopulationsAlgorithms.size() < 2)
            return;
        VBPSO subi, subj;
        double d1, d2;
        VBParticle yi,yj, swap;
        for(int i=0; i<subPopulationsAlgorithms.size() - 1; i++){
            subi = (VBPSO) subPopulationsAlgorithms.get(i);
            yi = (VBParticle) subi.getTopology().get(0).getNeighbourhoodBest();
            for(int j=i+1; j<subPopulationsAlgorithms.size(); j++){
                subj = (VBPSO) subPopulationsAlgorithms.get(j);
                yj = (VBParticle) subj.getTopology().get(0).getNeighbourhoodBest();
                d1 = distanceMeaser.distance(yi.getBestPosition(), yj.getBestPosition());
                if(d1<granularity){
                    if(yi.getBestFitness().compareTo(yj.getBestFitness()) >= 0.0 ){
                        int n = subj.getTopology().size();
                        VBParticle p;
                        if(n == 1){
                            swap = subj.getTopology().get(0).getClone();
                            swap.setNeighbourhoodBest(yi);
                            swap.setNicheID(i);
                            swap.calculateVP();
                            swap.calculateVG();
                            swap.calculateDotProduct();
                            subi.getTopology().add(swap);
                            subj.getTopology().remove(0);
                        }else{
                            for(int k=0; k<n; k++){
                                p = subj.getTopology().get(k);
                                d2 = distanceMeaser.distance(p.getBestPosition(), yi.getBestPosition());
                                if(d2 < granularity){
                                    swap = p.getClone();
                                    swap.setNeighbourhoodBest(yi);
                                    swap.setNicheID(i);
                                    swap.calculateVP();
                                    swap.calculateVG();
                                    swap.calculateDotProduct();
                                    subi.getTopology().add(swap);
                                    subj.getTopology().remove(k);
                                    k--;
                                    n--;
                                }else if(n == 1){
                                    swap = subj.getTopology().get(0).getClone();
                                    swap.setNeighbourhoodBest(yi);
                                    swap.setNicheID(i);
                                    swap.calculateVP();
                                    swap.calculateVG();
                                    swap.calculateDotProduct();
                                    subi.getTopology().add(swap);
                                    subj.getTopology().remove(0);
                                    break;
                                }
                            }
                        }
                    }else{
                        int n = subi.getTopology().size();
                        VBParticle p;
                        if(n == 1){
                            swap = subi.getTopology().get(0);
                            swap.setNeighbourhoodBest(yj);
                            swap.setNicheID(j);
                            swap.calculateVP();
                            swap.calculateVG();
                            swap.calculateDotProduct();
                            subj.getTopology().add(swap);
                            subi.getTopology().remove(0);
                        }else{
                            for(int k=0; k<n; k++){
                                p = subi.getTopology().get(k);
                                d2 = distanceMeaser.distance(p.getPosition(), yj.getBestPosition());
                                if(d2 < granularity){
                                    swap = p.getClone();
                                    swap.setNeighbourhoodBest(yj);
                                    swap.setNicheID(j);
                                    swap.calculateVP();
                                    swap.calculateVG();
                                    swap.calculateDotProduct();
                                    subj.getTopology().add(swap);
                                    subi.getTopology().remove(k);
                                    k--;
                                    n--;
                                }else if(n == 1){
                                    swap = subi.getTopology().get(0);
                                    swap.setNeighbourhoodBest(yj);
                                    swap.setNicheID(j);
                                    swap.calculateVP();
                                    swap.calculateVG();
                                    swap.calculateDotProduct();
                                    subj.getTopology().add(swap);
                                    subi.getTopology().remove(0);
                                }
                            }
                        }
                    }
                }
                subPopulationsAlgorithms.set(j, subj);
                if(subj.getTopology().size() == 0){
                    subPopulationsAlgorithms.remove(j);
                    j--;
                }
            }
            subPopulationsAlgorithms.set(i, subi);
            if(subi.getTopology().size() == 0){
                subPopulationsAlgorithms.remove(i);
                i--;
            }
        }
    }

    /**
     * @return the granularity
     */
    public double getGranularity() {
        return granularity;
    }

    /**
     * @param granularity the granularity to set
     */
    public void setGranularity(double granularity) {
        this.granularity = granularity;
    }

    /**
     * @return the innerIterations
     */
    public int getInnerIterations() {
        return innerIterations;
    }

    /**
     * @param innerIterations the innerIterations to set
     */
    public void setInnerIterations(int innerIterations) {
        this.innerIterations = innerIterations;
    }

    /**
     * @return the currentNiche
     */
    public int getCurrentNiche() {
        return currentNiche;
    }

    /**
     * @param currentNiche the currentNiche to set
     */
    public void setCurrentNiche(int currentNiche) {
        this.currentNiche = currentNiche;
    }
}