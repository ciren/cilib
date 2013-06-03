/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.boa;

import java.util.Arrays;
import java.util.List;

import net.sourceforge.cilib.algorithm.initialisation.ClonedPopulationInitialisationStrategy;
import net.sourceforge.cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import net.sourceforge.cilib.boa.bee.ExplorerBee;
import net.sourceforge.cilib.boa.bee.HoneyBee;
import net.sourceforge.cilib.boa.bee.OnlookerBee;
import net.sourceforge.cilib.boa.bee.WorkerBee;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.problem.solution.OptimisationSolution;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.selection.recipes.RouletteWheelSelector;
import net.sourceforge.cilib.util.selection.recipes.Selector;

import com.google.common.base.Preconditions;

import fj.F;
import fj.P2;

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
 *
 */
public class ABC extends SinglePopulationBasedAlgorithm<HoneyBee> {

    private static final long serialVersionUID = 7918711449442012960L;
    private fj.data.List<HoneyBee> workerBees;              //keeps references to the worker bees
    private fj.data.List<HoneyBee> onlookerBees;            //keeps references to the onlooker bees
    private ExplorerBee explorerBee;                    //explorer bee
    private Selector<HoneyBee> dancingSelectionStrategy;//bee dancing selection strategy
    private ControlParameter workerBeePercentage;       //control parameter for number of worker bees
    private ControlParameter forageLimit;               //control parameter for the forage limit
    private ControlParameter explorerBeeUpdateLimit;    //control parameter to limit the explorer bee position updates per iteration
    private HoneyBee bestBee;                            //reference to best solution found so far

    /**
     * Default constructor. Creates a new instance of {@code ABC}.
     */
    public ABC() {
        this.initialisationStrategy = new ClonedPopulationInitialisationStrategy();
        initialisationStrategy.setEntityNumber(100);
        initialisationStrategy.setEntityType(new WorkerBee());

        workerBees = fj.data.List.nil();
        onlookerBees = fj.data.List.nil();

        explorerBee = new ExplorerBee();
        dancingSelectionStrategy = new RouletteWheelSelector();

        forageLimit = ConstantControlParameter.of(500);
        workerBeePercentage = ConstantControlParameter.of(0.5);
        explorerBeeUpdateLimit = ConstantControlParameter.of(1.0);
    }

    /**
     * Copy constructor. Creates a copy of the provided instance.
     * @param copy ABC reference of which a deep copy is made.
     */
    public ABC(ABC copy) {
        super(copy);

        explorerBee = copy.explorerBee.getClone();
        dancingSelectionStrategy = new RouletteWheelSelector();

        forageLimit = copy.forageLimit.getClone();
        workerBeePercentage = copy.workerBeePercentage.getClone();
        explorerBeeUpdateLimit = copy.explorerBeeUpdateLimit.getClone();

        final int workerBeeCount = Double.valueOf(workerBeePercentage.getParameter() * topology.length()).intValue();
        workerBees = topology.take(workerBeeCount);
        onlookerBees = topology.drop(workerBeeCount);
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
    public void algorithmInitialisation() {
        topology = fj.data.List.iterableList(initialisationStrategy.<HoneyBee>initialise(optimisationProblem));

        int numWorkerBees = (int) (workerBeePercentage.getParameter() * topology.length());
        P2<fj.data.List<HoneyBee>, fj.data.List<HoneyBee>> split = topology.splitAt(numWorkerBees);
        this.workerBees = split._1();
        this.onlookerBees = split._2().map(new F<HoneyBee, HoneyBee>() {
            public HoneyBee f(HoneyBee b) {
                return new OnlookerBee((WorkerBee) b);
            }
	});

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
            HoneyBee selectedBee = dancingSelectionStrategy.on(workerBees).select();
            bee.setPosition(Vector.copyOf((Vector) selectedBee.getPosition()));
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
        Preconditions.checkNotNull(bestBee, "Best solution cannot be determined before algorithm is run");
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
     * Gets the bee dancing selection strategy.
     * @return the bee dancing selection strategy.
     */
    public Selector getDancingSelectionStrategy() {
        return dancingSelectionStrategy;
    }

    /**
     * Sets the bee dancing selection strategy.
     * @param dancingSelectionStrategy the new bee dancing selection strategy.
     */
    public void setDancingSelectionStrategy(Selector dancingSelectionStrategy) {
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
     * Get the {@code Topology}  containing the onlooker bees.
     * @return the {@code Topology} containing the onlooker bees.
     */
    public fj.data.List<HoneyBee> getOnlookerBees() {
        return onlookerBees;
    }

    /**
     * Set the {@code Topology}  containing the onlooker bees.
     * @param onlookerBees the new {@code Topology} containing the onlooker bees.
     */
    public void setOnlookerBees(fj.data.List<HoneyBee> onlookerBees) {
        this.onlookerBees = onlookerBees;
    }

    /**
     * Get the {@code Topology}  containing the worker bees.
     * @return the {@code Topology} containing the worker bees.
     */
    public fj.data.List<HoneyBee> getWorkerBees() {
        return workerBees;
    }

    /**
     * Set the {@code Topology}  containing the worker bees.
     * @param workerBees the new {@code Topology} containing the worker bees.
     */
    public void setWorkerBees(fj.data.List<HoneyBee> workerBees) {
        this.workerBees = workerBees;
    }
}
