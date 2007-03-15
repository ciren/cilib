package net.sourceforge.cilib.entity.operators.selection;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;

/**
 * This class implements the Composite design pattern to correctly
 * apply a group of selection operators to perform a sepecific selection.
 * For example, a selection my require an Elitism selection of 10% of the
 * available entities, followed by a greedy fitness selection operator.
 * 
 * @author gpampara
 *
 */
public class CompoundSelection extends SelectionStrategy {
	
	private List<SelectionStrategy> selectors;

	public CompoundSelection() {
		selectors = new ArrayList<SelectionStrategy>();		
	}
	
	public CompoundSelection(CompoundSelection copy) {
		
	}
	
	@Override
	public SelectionStrategy clone() {
		return new CompoundSelection(this);
	}

	@Override
	public Entity select(Topology<Entity> population) {
		for (SelectionStrategy selection : selectors) {
			selection.select(population);
		}
		
		return null;
	}

	/**
	 * @TODO: Correct this method
	 */
	@SuppressWarnings("unchecked")
	public void performOperation(Topology<? extends Entity> topology, Topology<Entity> offspring) {
		this.select((Topology<Entity>) topology); // This method needs to be corrected
	}
	
	public void addSelection(SelectionStrategy selectionOperator) {
		this.selectors.add(selectionOperator);
	}

}
