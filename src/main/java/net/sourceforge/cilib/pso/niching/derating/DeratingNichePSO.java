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

package net.sourceforge.cilib.pso.niching.derating;

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
import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.functions.continuous.DeratingFunction;
import net.sourceforge.cilib.problem.FunctionMaximisationProblem;
import net.sourceforge.cilib.problem.OptimisationSolution;
import net.sourceforge.cilib.problem.boundaryconstraint.ReinitialisationBoundary;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.iterationstrategies.SynchronousIterationStrategy;
import net.sourceforge.cilib.pso.niching.AbsorptionStrategy;
import net.sourceforge.cilib.pso.niching.MergeStrategy;
import net.sourceforge.cilib.pso.niching.Niche;
import net.sourceforge.cilib.pso.niching.NicheCreationStrategy;
import net.sourceforge.cilib.pso.niching.NicheIdentificationStrategy;
import net.sourceforge.cilib.pso.niching.StandardAbsorptionStrategy;
import net.sourceforge.cilib.pso.niching.StandardMergeStrategy;
import net.sourceforge.cilib.pso.niching.StandardNicheIdentificationStrategy;
import net.sourceforge.cilib.pso.niching.StandardSwarmCreationStrategy;
import net.sourceforge.cilib.pso.particle.StandardParticle;
import net.sourceforge.cilib.pso.velocityprovider.GCVelocityProvider;
import net.sourceforge.cilib.pso.velocityprovider.StandardVelocityProvider;
import net.sourceforge.cilib.stoppingcondition.StoppingCondition;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 *
 * @author wayne
 */
public class DeratingNichePSO extends Niche {

    private PopulationBasedAlgorithm mainSwarm;
    private NicheIdentificationStrategy nicheIdentificationStrategy;
    private StandardSwarmCreationStrategy swarmCreationStrategy;
    private AbsorptionStrategy absorptionStrategy;
    private MergeStrategy mergeStrategy;
    private DeratingMergeStrategy dmergeStrategy;
    private int stopPhase1;
    private int stopPhase2;
    private PopulationInitialisationStrategy subSwarmInitialisationStrategy;
    private FunctionMaximisationProblem modifiedSpace;
    private ArrayList<Particle> x;
    private int uniqueSolutions;
    private double alpha;
    private int dim;
    private double radius;
    private boolean started;
    private boolean firstRun;
    private int initialSize = 20;
    private Particle mainSwarmParticle;
    private PSO initialSwarm;


    public DeratingNichePSO(){
        this.mainSwarm = new PSO();
        PSO pso = (PSO) this.mainSwarm;
        ((SynchronousIterationStrategy)pso.getIterationStrategy()).setBoundaryConstraint(new ReinitialisationBoundary());

        mainSwarmParticle = new StandardParticle();
        mainSwarmParticle.setVelocityInitializationStrategy(new RandomInitializationStrategy());
        StandardVelocityProvider velocityUpdateStrategy = new StandardVelocityProvider();
        velocityUpdateStrategy.setSocialAcceleration(ConstantControlParameter.of(0.0));
        velocityUpdateStrategy.setCognitiveAcceleration(ConstantControlParameter.of(1.2));

        mainSwarmParticle.setVelocityProvider(velocityUpdateStrategy);
        PopulationInitialisationStrategy mainSwarmInitialisationStrategy = new ClonedPopulationInitialisationStrategy();
        mainSwarmInitialisationStrategy.setEntityType(mainSwarmParticle);
        mainSwarmInitialisationStrategy.setEntityNumber(initialSize);

        this.mainSwarm.setInitialisationStrategy(mainSwarmInitialisationStrategy);

        this.nicheIdentificationStrategy = new StandardNicheIdentificationStrategy();
        this.swarmCreationStrategy = new StandardSwarmCreationStrategy();
        this.absorptionStrategy = new StandardAbsorptionStrategy();
        this.mergeStrategy = new StandardMergeStrategy();
        this.dmergeStrategy = new DeratingMergeStrategy();

        this.stopPhase1 = 500;
        this.stopPhase2 = 500;
        
        this.x = new ArrayList<Particle>();

        this.uniqueSolutions = 9;
        this.alpha = 0.8;

        started = false;
        firstRun = true;
        radius = 0.1;
    }

    /**
     * Initialise the main population based algorithm, provided such a notion exists.
     * @see MultiPopulationBasedAlgorithm#performInitialisation()
     */
    @Override
    public void performInitialisation() {
        for (StoppingCondition stoppingCondition : getStoppingConditions())
            this.getMainSwarm().addStoppingCondition(stoppingCondition);

        if(!started){
            this.modifiedSpace = (FunctionMaximisationProblem) getOptimisationProblem();
            started = true;
        }
        this.getMainSwarm().setOptimisationProblem(modifiedSpace);
        this.getMainSwarm().performInitialisation();

        this.dim = this.getMainSwarm().getTopology().get(0).getDimension();
        initialSwarm = (PSO) this.mainSwarm;

    }

    @Override
    protected void algorithmIteration() {
        reinitialiseNiche();
        
        while (stopPhase1 > 0 && this.getMainSwarm().getTopology().size() > 0){
            runPhase1();
            stopPhase1--;
        }
        Topology<StandardParticle> top;
        GCVelocityProvider velUpdate = new GCVelocityProvider();
        for (PopulationBasedAlgorithm subSwarm : this) {
            top = (Topology<StandardParticle>) subSwarm.getTopology();
            for(StandardParticle p: top){
                p.setVelocityProvider(velUpdate);
            }
            subSwarm.setOptimisationProblem(getOptimisationProblem());
        }
        while (stopPhase2 > 0){
            runPhase2();
            stopPhase2--;
        }
        for (PopulationBasedAlgorithm subSwarm : this) {
            x.add((Particle) subSwarm.getTopology().getBestEntity());
        }
        this.dmergeStrategy.merge(x);
        calculateModifiedFunction();
        firstRun = false;
    }

    protected void reinitialiseNiche(){

        if(!firstRun){
            mainSwarm = initialSwarm;
        }        

        this.stopPhase1 = 500;
        this.stopPhase2 = 500;
    }

    protected void runPhase1() {
        getMainSwarm().performIteration();

        this.mergeStrategy.merge(this);

        List<Entity> niches = this.nicheIdentificationStrategy.identify(getMainSwarm().getTopology());
        
        this.swarmCreationStrategy.create(this, niches);
    }

    protected void runPhase2(){
        for (PopulationBasedAlgorithm subSwarm : this) {
            subSwarm.performIteration();
        }

        this.absorptionStrategy.absorb(this);
        List<Entity> niches = this.nicheIdentificationStrategy.identify(getMainSwarm().getTopology());
        this.swarmCreationStrategy.create(this, niches);
    }

    protected void calculateModifiedFunction(){
        ContinuousFunction f = (ContinuousFunction) modifiedSpace.getFunction();
        FunctionMaximisationProblem temp = new FunctionMaximisationProblem();
        DeratingFunction der = new DeratingFunction();
        der.setPreviousFunction(f);
        der.setAlpha(alpha);
        der.setRadius(radius);
        ArrayList<Vector> yheads = new ArrayList<Vector>();
        for(Particle subSwarm: getX()){
            yheads.add((Vector) subSwarm.getBestPosition());
        }
        der.setYbests(yheads);

        temp.setFunction(der);
        modifiedSpace = temp;
    }

    protected double calculateNicheRadius(){
        double sqrt = Math.sqrt(dim*1.0);
        double sqrt2 = Math.pow(dim, 1/uniqueSolutions);

        return (sqrt/ 2.0*sqrt2);
    }

    @Override
    public OptimisationSolution getBestSolution() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Get the solutions of the the optimisation. The solutions are the best
     * entities within each identified niche.
     * @return The list of best solutions for each niche.
     */
    @Override
    public List<OptimisationSolution> getSolutions() {
        List<OptimisationSolution> list = new ArrayList<OptimisationSolution>();
        OptimisationSolution temp = null;

        for (Particle bestParticle : x){
            temp = new OptimisationSolution(bestParticle.getBestPosition(), bestParticle.getBestFitness());
            list.add(temp);
        }

        return list;
    }

    @Override
    public PopulationBasedAlgorithm getClone() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * @return the nicheIdentificationStrategy
     */
    public NicheIdentificationStrategy getNicheIdentificationStrategy() {
        return nicheIdentificationStrategy;
    }

    /**
     * @param nicheIdentificationStrategy the nicheIdentificationStrategy to set
     */
    public void setNicheIdentificationStrategy(NicheIdentificationStrategy nicheIdentificationStrategy) {
        this.nicheIdentificationStrategy = nicheIdentificationStrategy;
    }

    /**
     * @return the swarmCreationStrategy
     */
    public NicheCreationStrategy getSwarmCreationStrategy() {
        return swarmCreationStrategy;
    }

    /**
     * @return the absorptionStrategy
     */
    public AbsorptionStrategy getAbsorptionStrategy() {
        return absorptionStrategy;
    }

    /**
     * @return the mergeStrategy
     */
    public MergeStrategy getMergeStrategy() {
        return mergeStrategy;
    }

    /**
     * @return the stopPhase1
     */
    public int getStopPhase1() {
        return stopPhase1;
    }

    /**
     * @param stopPhase1 the stopPhase1 to set
     */
    public void setStopPhase1(int stopPhase1) {
        this.stopPhase1 = stopPhase1;
    }

    /**
     * @return the stopPhase2
     */
    public int getStopPhase2() {
        return stopPhase2;
    }

    /**
     * @param stopPhase2 the stopPhase2 to set
     */
    public void setStopPhase2(int stopPhase2) {
        this.stopPhase2 = stopPhase2;
    }

    /**
     * @return the subSwarmInitialisationStrategy
     */
    public PopulationInitialisationStrategy getSubSwarmInitialisationStrategy() {
        return subSwarmInitialisationStrategy;
    }

    /**
     * @param subSwarmInitialisationStrategy the subSwarmInitialisationStrategy to set
     */
    public void setSubSwarmInitialisationStrategy(PopulationInitialisationStrategy subSwarmInitialisationStrategy) {
        this.subSwarmInitialisationStrategy = subSwarmInitialisationStrategy;
    }

    /**
     * @param uniqueSolutions the uniqueSolutions to set
     */
    public void setUniqueSolutions(int uniqueSolutions) {
        this.uniqueSolutions = uniqueSolutions;
    }

    /**
     * @param mainSwarm the mainSwarm to set
     */
    @Override
    public void setMainSwarm(PopulationBasedAlgorithm mainSwarm) {
        this.mainSwarm = mainSwarm;
    }

    /**
     * @return the mainSwarm
     */
    @Override
    public PopulationBasedAlgorithm getMainSwarm() {
        return this.mainSwarm;
    }

    /**
     * @return the x
     */
    public ArrayList<Particle> getX() {
        return x;
    }

    /**
     * @param x the x to set
     */
    public void setX(ArrayList<Particle> x) {
        this.x = x;
    }

}
