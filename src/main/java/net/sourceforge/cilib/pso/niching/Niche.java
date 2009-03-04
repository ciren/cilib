/**
 * Copyright (C) 2003 - 2009
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package net.sourceforge.cilib.pso.niching;

import java.util.ArrayList;
import java.util.List;
import net.sourceforge.cilib.algorithm.initialisation.ClonedPopulationInitialisationStrategy;
import net.sourceforge.cilib.algorithm.initialisation.PopulationInitialisationStrategy;
import net.sourceforge.cilib.algorithm.population.MultiPopulationBasedAlgorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.initialization.ConstantInitializationStrategy;
import net.sourceforge.cilib.problem.InferiorFitness;
import net.sourceforge.cilib.problem.OptimisationSolution;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.particle.StandardParticle;
import net.sourceforge.cilib.pso.velocityupdatestrategies.StandardVelocityUpdate;
import net.sourceforge.cilib.stoppingcondition.StoppingCondition;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 *
 * @author gpampara
 */
public class Niche extends MultiPopulationBasedAlgorithm {
    private static final long serialVersionUID = 3575627467034673738L;

    private PopulationBasedAlgorithm mainSwarm;

    private NicheIdentificationStrategy nicheIdentificationStrategy;
    private NicheCreationStrategy swarmCreationStrategy;
    private AbsorptionStrategy absorptionStrategy;
    private MergeStrategy mergeStrategy;

    public Niche() {
        this.mainSwarm = new PSO();

        Particle mainSwarmParticle = new StandardParticle();
        mainSwarmParticle.setVelocityInitializationStrategy(new ConstantInitializationStrategy(0.0));
        StandardVelocityUpdate velocityUpdateStrategy = new StandardVelocityUpdate();
        velocityUpdateStrategy.setSocialAcceleration(new ConstantControlParameter(0.0));

        mainSwarmParticle.setVelocityUpdateStrategy(velocityUpdateStrategy);
        PopulationInitialisationStrategy mainSwarmInitialisationStrategy = new ClonedPopulationInitialisationStrategy();
        mainSwarmInitialisationStrategy.setEntityType(mainSwarmParticle);
        mainSwarmInitialisationStrategy.setEntityNumber(40);

        this.mainSwarm.setInitialisationStrategy(mainSwarmInitialisationStrategy);

        this.nicheIdentificationStrategy = new StandardNicheIdentificationStrategy();
        this.swarmCreationStrategy = new StandardSwarmCreationStrategy();
        this.absorptionStrategy = new StandardAbsorptionStrategy();
        this.mergeStrategy = new StandardMergeStrategy();
    }

    @Override
    public void performInitialisation() {
        for (StoppingCondition stoppingCondition : getStoppingConditions())
            this.mainSwarm.addStoppingCondition(stoppingCondition);

        this.mainSwarm.setOptimisationProblem(getOptimisationProblem());

        this.mainSwarm.initialise();
    }

    @Override
    protected void algorithmIteration() {
        mainSwarm.performIteration();

        for (PopulationBasedAlgorithm subSwarm : this) {
            subSwarm.performIteration(); // TODO: There may be an issue with this and the number of iterations
        }

        this.mergeStrategy.merge(this);
        this.absorptionStrategy.absorb(this);

        List<Entity> niches = this.nicheIdentificationStrategy.identify(this.getTopology());
        this.swarmCreationStrategy.create(this, niches);
    }

    @Override
    public PopulationBasedAlgorithm getClone() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public OptimisationSolution getBestSolution() {
//        throw new UnsupportedOperationException("Niching does not provide a single solution.");
        return new OptimisationSolution(new Vector(), InferiorFitness.instance());
    }

    @Override
    public List<OptimisationSolution> getSolutions() {
        List<OptimisationSolution> list = new ArrayList<OptimisationSolution>();

        for (PopulationBasedAlgorithm subSwarm : this)
            list.add(subSwarm.getBestSolution());

        return list;
    }

    public PopulationBasedAlgorithm getMainSwarm() {
        return this.mainSwarm;
    }

}
