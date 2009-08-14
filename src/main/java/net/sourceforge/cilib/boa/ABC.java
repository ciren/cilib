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
package net.sourceforge.cilib.boa;

import com.google.common.collect.Iterables;
import java.util.Arrays;
import java.util.List;

import net.sourceforge.cilib.algorithm.InitialisationException;
import net.sourceforge.cilib.algorithm.initialisation.ClonedPopulationInitialisationStrategy;
import net.sourceforge.cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import net.sourceforge.cilib.boa.bee.ExplorerBee;
import net.sourceforge.cilib.boa.bee.HoneyBee;
import net.sourceforge.cilib.boa.bee.OnlookerBee;
import net.sourceforge.cilib.boa.bee.WorkerBee;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.operators.selection.RouletteWheelSelectionStrategy;
import net.sourceforge.cilib.entity.operators.selection.SelectionStrategy;
import net.sourceforge.cilib.entity.topologies.GBestTopology;
import net.sourceforge.cilib.problem.OptimisationSolution;

/**
 * <p>
 * An implementation of the Artificial Bee Colony Algorithm.
 * </p>
 * <p>
 * Reference:
 * </p>
 * <p>
 * Dervis Karaboga and Bahriye Basturk, "A powerful and efficient algorithm for numerical function optimization: artificial bee colony (ABC) algorithm"
 * journal = "Journal of Global Optimization"
 * pages = "459--471"
 * volume = "39"
 * number = "3"
 * month = nov
 * year = "2007"
 * publisher = "Springer"
 * address = "Netherlands"
 * </p>
 *
 * @author Andrich
 *
 */
public class ABC extends SinglePopulationBasedAlgorithm {

    private static final long serialVersionUID = 7918711449442012960L;
    private Topology<HoneyBee> workerBees;                //keeps references to the worker bees
    private Topology<HoneyBee> onlookerBees;            //keeps references to the onlooker bees
    private Topology<HoneyBee> hive;                    //keeps references to all the bees (workers and onlookers)
    private ExplorerBee explorerBee;                    //explorer bee
    private SelectionStrategy dancingSelectionStrategy; //bee dancing selection strategy
    private ControlParameter workerBeePercentage;        //control parameter for number of worker bees
    private ControlParameter forageLimit;                //control parameter for the forage limit
    private ControlParameter explorerBeeUpdateLimit;    //control parameter to limit the explorer bee position updates per iteration
    private HoneyBee bestBee;                            //reference to best solution found so far

    /**
     * Default constructor. Creates a new instance of {@code ABC}.
     */
    public ABC() {
        this.initialisationStrategy = new ClonedPopulationInitialisationStrategy();
        initialisationStrategy.setEntityNumber(100);
        initialisationStrategy.setEntityType(new WorkerBee());

        workerBees = new GBestTopology<HoneyBee>();
        onlookerBees = new GBestTopology<HoneyBee>();
        hive = new GBestTopology<HoneyBee>();

        explorerBee = new ExplorerBee();
        dancingSelectionStrategy = new RouletteWheelSelectionStrategy();

        forageLimit = new ConstantControlParameter(500);
        workerBeePercentage = new ConstantControlParameter(0.5);
        explorerBeeUpdateLimit = new ConstantControlParameter(1.0);
    }

    /**
     * Copy constructor. Creates a copy of the provided instance.
     * @param copy ABC reference of which a deep copy is made.
     */
    public ABC(ABC copy) {
        super(copy);
        workerBees = copy.workerBees.getClone();
        onlookerBees = copy.onlookerBees.getClone();
        hive.clear();
        hive.addAll(workerBees);
        hive.addAll(onlookerBees);

        explorerBee = copy.explorerBee.getClone();
        dancingSelectionStrategy = new RouletteWheelSelectionStrategy();

        forageLimit = copy.forageLimit.getClone();
        workerBeePercentage = copy.workerBeePercentage.getClone();
        explorerBeeUpdateLimit = copy.explorerBeeUpdateLimit.getClone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ABC getClone() {
        return new ABC(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void performInitialisation() {
        Iterable<? extends Entity> bees = this.initialisationStrategy.initialise(this.optimisationProblem);
        //Iterables.addAll(getTopology(), particles); // Use this instead?
        for (Entity bee : bees)
            hive.add((HoneyBee) bee);

        int i;
        int numWorkerBees = (int) (workerBeePercentage.getParameter() * hive.size());
        for (i = 0; i < numWorkerBees; i++) {
            WorkerBee bee = (WorkerBee) hive.get(i);
            bee.setForageLimit(this.forageLimit.getClone());
            this.workerBees.add(hive.get(i));
        }

        for (int j = 0; j < initialisationStrategy.getEntityNumber() - numWorkerBees; j++) {
            WorkerBee worker = (WorkerBee) hive.get(i);
            OnlookerBee onlooker = new OnlookerBee(worker);
            hive.remove(i);
            hive.add(onlooker);
            onlookerBees.add(onlooker);
        }
        explorerBee.setExplorerBeeUpdateLimit(this.explorerBeeUpdateLimit);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void algorithmIteration() {
        for (HoneyBee bee : workerBees) {
            bee.updatePosition();
            if (bestBee == null) {
                bestBee = bee.getClone();
            } else if (bee.getBestFitness().compareTo(bestBee.getBestFitness()) > 0) {
                bestBee = bee.getClone();
            }
        }

        for (HoneyBee bee : onlookerBees) {
            HoneyBee selectedBee = dancingSelectionStrategy.select(workerBees);
            bee.setPosition(selectedBee.getPosition().getClone());
            bee.updatePosition();
            if (bestBee == null) {
                bestBee = bee;
            } else if (bee.getBestFitness().compareTo(bestBee.getBestFitness()) > 0) {
                bestBee = bee;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OptimisationSolution getBestSolution() {
        if (this.bestBee == null) {
            throw new InitialisationException("Best solution cannot be determined before algorithm is run");
        }

        return new OptimisationSolution(bestBee.getPosition(), bestBee.getFitness());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<OptimisationSolution> getSolutions() {
        return Arrays.asList(getBestSolution());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Topology<HoneyBee> getTopology() {
        return this.hive;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setTopology(Topology<? extends Entity> topology) {
        throw new UnsupportedOperationException("Method not implemented");
    }

    /**
     * Gets the bee dancing selection strategy.
     * @return the bee dancing selection strategy.
     */
    public SelectionStrategy getDancingSelectionStrategy() {
        return dancingSelectionStrategy;
    }

    /**
     * Sets the bee dancinc selection strategy.
     * @param dancingSelectionStrategy the new bee dancing selection strategy.
     */
    public void setDancingSelectionStrategy(
            SelectionStrategy dancingSelectionStrategy) {
        this.dancingSelectionStrategy = dancingSelectionStrategy;
    }

    /**
     * Gets the explorer bee.
     * @return the explorer bee.
     */
    public ExplorerBee getExplorerBee() {
        return this.explorerBee;
    }

    /**
     * Sets the explorer bee.
     * @param explorerBee the new explorer bee.
     */
    public void setExplorerBee(ExplorerBee explorerBee) {
        this.explorerBee = explorerBee;
    }

    /**
     * Gets the {@code ControlParameter} specifying the number of worker bees.
     * @return the {@code ControlParameter} specifying the number of worker bees.
     */
    public ControlParameter getWorkerBeeNumber() {
        return workerBeePercentage;
    }

    /**
     * Gets the {@code ControlParameter} specifying  the percentage of worker bees.
     * @return the  {@code ControlParameter} specifying the percentage of worker bees.
     */
    public ControlParameter getWorkerBeePercentage() {
        return workerBeePercentage;
    }

    /**
     * Sets the {@code ControlParameter} specifying  the percentage of worker bees..
     * @param workerBeeNumber  the new  {@code ControlParameter} specifying the percentage of worker bees.
     */
    public void setWorkerBeePercentage(ControlParameter workerBeeNumber) {
        this.workerBeePercentage = workerBeeNumber;
    }

    /**
     * Gets the {@code ControlParameter} specifying the foraging limit.
     * @return the  {@code ControlParameter} specifying the foraging limit.
     */
    public ControlParameter getForageLimit() {
        return forageLimit;
    }

    /**
     * Sets the {@code ControlParameter} specifying the foraging limit.
     * @param forageThreshold  the new  {@code ControlParameter} specifying the foraging limit.
     */
    public void setForageLimit(ControlParameter forageThreshold) {
        this.forageLimit = forageThreshold;
    }

     /**
     * Gets the {@code ControlParameter} specifying the limit to how many times the explorer bee can update positions.
     * @return the  {@code ControlParameter} specifying the limit to how many times the explorer bee can update positions.
     */
    public ControlParameter getExplorerBeeUpdateLimit() {
        return explorerBeeUpdateLimit;
    }

    /**
     * Sets the {@code ControlParameter} specifying the limit to how many times the explorer bee can update positions.
     * @param explorerBeeUpdateLimit  the  {@code ControlParameter} specifying the limit to how many times the explorer bee can update positions.
     */
    public void setExplorerBeeUpdateLimit(ControlParameter explorerBeeUpdateLimit) {
        this.explorerBeeUpdateLimit = explorerBeeUpdateLimit;
    }

    /**
     * Gets the bee with the highest fitness during the algorithm execution.
     * @return the bee with the highest fitness.
     */
    public HoneyBee getBestBee() {
        return bestBee;
    }

    /**
     * Sets the bee with the highest fitness during the algorithm execution.
     * @param bestBee the new bee with the highest fitness.
     */
    public void setBestBee(HoneyBee bestBee) {
        this.bestBee = bestBee;
    }

    /**
     * Get the {@code Topology}  containing all the bees in the hive.
     * @return the {@code Topology}  containing all bees in the hive.
     */
    public Topology<HoneyBee> getHive() {
        return hive;
    }

    /**
     * Set the {@code Topology}  containing all the bees in the hive.
     * @param hive the new {@code Topology}  containing all bees in the hive.
     */
    public void setHive(Topology<HoneyBee> hive) {
        this.hive = hive;
    }

    /**
     * Get the {@code Topology}  containing the onlooker bees.
     * @return the {@code Topology} containing the onlooker bees.
     */
    public Topology<HoneyBee> getOnlookerBees() {
        return onlookerBees;
    }

    /**
     * Set the {@code Topology}  containing the onlooker bees.
     * @param onlookerBees the new {@code Topology} containing the onlooker bees.
     */
    public void setOnlookerBees(Topology<HoneyBee> onlookerBees) {
        this.onlookerBees = onlookerBees;
    }

    /**
     * Get the {@code Topology}  containing the worker bees.
     * @return the {@code Topology} containing the worker bees.
     */
    public Topology<HoneyBee> getWorkerBees() {
        return workerBees;
    }

    /**
     * Set the {@code Topology}  containing the worker bees.
     * @param workerBees the new {@code Topology} containing the worker bees.
     */
    public void setWorkerBees(Topology<HoneyBee> workerBees) {
        this.workerBees = workerBees;
    }
}
