/**
 * 
 */
package net.sourceforge.cilib.boa;

import java.util.Arrays;
import java.util.List;

import net.sourceforge.cilib.algorithm.InitialisationException;
import net.sourceforge.cilib.algorithm.initialisation.ClonedPopulationInitialisationStrategy;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.boa.bees.ExplorerBee;
import net.sourceforge.cilib.boa.bees.HoneyBee;
import net.sourceforge.cilib.boa.bees.OnlookerBee;
import net.sourceforge.cilib.boa.bees.WorkerBee;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.operators.selection.RouletteWheelSelectionStrategy;
import net.sourceforge.cilib.entity.operators.selection.SelectionStrategy;
import net.sourceforge.cilib.entity.topologies.GBestTopology;
import net.sourceforge.cilib.entity.visitor.TopologyVisitor;
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
public class ABC extends PopulationBasedAlgorithm {
	
	private Topology<HoneyBee> workerBees;				//keeps references to the worker bees
	private Topology<HoneyBee> onlookerBees;			//keeps references to the onlooker bees
	private Topology<HoneyBee> hive;					//keeps references to all the bees (workers and onlookers)
	
	private ExplorerBee explorerBee;					//explorer bee
	private SelectionStrategy dancingSelectionStrategy; //bee dancing selection strategy
	
	private ControlParameter workerBeePercentage;		//control parameter for number of worker bees
	private ControlParameter forageLimit;				//control parameter for the forage limit
	private ControlParameter explorerBeeUpdateLimit;	//control parameter to limit the explorer bee position updates per iteration
	
	private HoneyBee bestBee;							//reference to best solution found so far
	
	/**
	 * Default contstructor
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
	 * Copy constructor
	 * @param copy ABC reference of which a deep copy is made
	 */
	public ABC(ABC copy) {
		super(copy);
		workerBees = copy.getWorkerTopology().getClone();
		onlookerBees = copy.getOnlookerTopology().getClone();
		hive.clear();
		hive.addAll(workerBees);
		hive.addAll(onlookerBees);
		
		explorerBee = new ExplorerBee(copy.explorerBee);
		dancingSelectionStrategy = new RouletteWheelSelectionStrategy();
		
		forageLimit = copy.forageLimit.getClone();
		workerBeePercentage = copy.workerBeePercentage.getClone();
		explorerBeeUpdateLimit = new ConstantControlParameter((ConstantControlParameter)copy.explorerBeeUpdateLimit);
	}
	
	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.algorithm.Algorithm#performInitialisation()
	 */
	public void performInitialisation() {
		this.initialisationStrategy.initialise(hive, this.optimisationProblem);
		
		int i;
		int numWorkerBees = (int)(workerBeePercentage.getParameter()*hive.size());
		for (i = 0; i < numWorkerBees; i++) {
			WorkerBee bee = (WorkerBee)hive.get(i);
			bee.setForageLimit(this.forageLimit.getClone());
			this.workerBees.add(hive.get(i));
		}
		
		for (int j = 0; j < initialisationStrategy.getEntityNumber() - numWorkerBees; j++) {
			WorkerBee worker = (WorkerBee)hive.get(i);
			OnlookerBee onlooker = new OnlookerBee(worker);
			hive.remove(i);
			hive.add(onlooker);
			onlookerBees.add(onlooker);
		}
		explorerBee.setExplorerBeeUpdateLimit(this.explorerBeeUpdateLimit);
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.algorithm.Algorithm#accept(net.sourceforge.cilib.entity.visitor.TopologyVisitor)
	 */
	@Override
	public double accept(TopologyVisitor visitor) {
		visitor.setCurrentAlgorithm(this);
		this.getTopology().accept(visitor);
		return visitor.getResult();
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.algorithm.Algorithm#algorithmIteration()
	 */
	@Override
	protected void algorithmIteration() {
		for (HoneyBee bee : workerBees) {
			bee.updatePosition();
			bee.calculateFitness();
			if (bestBee == null)
			{
				bestBee = bee.getClone();
			}
			else if (bee.getBestFitness().compareTo(bestBee.getBestFitness()) > 0)
			{
				bestBee = bee.getClone();
			}
		}
		
		for (HoneyBee bee : onlookerBees) {
			HoneyBee selectedBee = (HoneyBee)dancingSelectionStrategy.select(workerBees);
			bee.setPosition(selectedBee.getPosition().getClone());
			bee.updatePosition();
			bee.calculateFitness();
			if (bestBee == null)
			{
				bestBee = bee;
			}
			else if (bee.getBestFitness().compareTo(bestBee.getBestFitness()) > 0)
			{
				bestBee = bee;
			}
		}
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.algorithm.Algorithm#getBestSolution()
	 */
	@Override
	public OptimisationSolution getBestSolution() {
		if (this.bestBee == null)
			throw new InitialisationException("Best solution cannot be determined before algorithm is run");
		return new OptimisationSolution(this.getOptimisationProblem(), bestBee.getPosition());
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.algorithm.Algorithm#getClone()
	 */
	@Override
	public ABC getClone() {
		return new ABC(this);
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.algorithm.Algorithm#getSolutions()
	 */
	@Override
	public List<OptimisationSolution> getSolutions() {
		return Arrays.asList(getBestSolution());
	}
	
	public Topology<HoneyBee> getTopology() {
		return this.hive;
	}
	
	public Topology<HoneyBee> getWorkerTopology() {
		return this.workerBees;
	}
	
	public Topology<HoneyBee> getOnlookerTopology() {
		return this.onlookerBees;
	}
	
	@Override
	public void setTopology(Topology<? extends Entity> topology) {
		throw new UnsupportedOperationException("Method not implemented");
	}
	
	public SelectionStrategy getDancingSelectionStrategy() {
		return dancingSelectionStrategy;
	}

	public void setDancingSelectionStrategy(
			SelectionStrategy dancingSelectionStrategy) {
		this.dancingSelectionStrategy = dancingSelectionStrategy;
	}
	
	public ExplorerBee getExplorerBee() {
		return this.explorerBee;
	}

	public ControlParameter getWorkerBeeNumber() {
		return workerBeePercentage;
	}

	public void setWorkerBeePercentage(ControlParameter workerBeeNumber) {
		this.workerBeePercentage = workerBeeNumber;
	}

	public ControlParameter getForageLimit() {
		return forageLimit;
	}

	public void setForageLimit(ControlParameter forageThreshold) {
		this.forageLimit = forageThreshold;
	}
	
	public ControlParameter getExplorerBeeUpdateLimit() {
		return explorerBeeUpdateLimit;
	}

	public void setExplorerBeeUpdateLimit(ControlParameter explorerBeeUpdateLimit) {
		this.explorerBeeUpdateLimit = explorerBeeUpdateLimit;
	}
	
	private static final long serialVersionUID = 7918711449442012960L;
}
