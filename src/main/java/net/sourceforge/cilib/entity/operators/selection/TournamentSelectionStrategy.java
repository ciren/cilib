/*
 * Copyright (C) 2003 - 2008
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
package net.sourceforge.cilib.entity.operators.selection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.controlparameter.ProportionalControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.comparator.DescendingFitnessComparator;
import net.sourceforge.cilib.math.random.RandomNumber;

/**
 * Perform a tournament selection process on the provided {@linkplain Topology}
 * with a predefined tournament size.
 * 
 * @author Gary Pampara
 */
public class TournamentSelectionStrategy extends SelectionStrategy {
	private static final long serialVersionUID = -7520711765609204590L;
	private ControlParameter tournamentProportion;
	private RandomNumber randomNumber;
	private Comparator<Entity> entityComparator;
	
	/**
	 * Create a new instance of {@linkplain TournamentSelectionStrategy}.
	 */
	public TournamentSelectionStrategy() {
		this.tournamentProportion = new ProportionalControlParameter();
		this.randomNumber = new RandomNumber();
		this.entityComparator = new DescendingFitnessComparator();
	}
	
	/**
	 * Copy constructor. Create a copy of the given instance.
	 * @param copy The instance to copy.
	 */
	public TournamentSelectionStrategy(TournamentSelectionStrategy copy) {
		this.tournamentProportion = copy.tournamentProportion.getClone();
		this.randomNumber = copy.randomNumber.getClone();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public TournamentSelectionStrategy getClone() {
		return new TournamentSelectionStrategy(this);
	}

	/**
	 * {@inheritDoc}
	 */
	public <T extends Entity> T select(Topology<T> population) {
		int tournamentSize = Double.valueOf(this.tournamentProportion.getParameter()*population.size()).intValue();
		
		List<T> tournamentEntities = new ArrayList<T>();
		
		while (tournamentEntities.size() < tournamentSize) {
			double random = randomNumber.getUniform(0, population.size());
			T tmp = population.get(Double.valueOf(random).intValue());
			
			if (!tournamentEntities.contains(tmp))
				tournamentEntities.add(tmp);
		}
		
		Collections.sort(tournamentEntities, this.entityComparator);
		
		return tournamentEntities.get(0);
	}

	/**
	 * Get the defined size of the tournament.
	 * @return The size of the tournament.
	 */
	public ControlParameter getTournamentSize() {
		return tournamentProportion;
	}

	/**
	 * Set the size of the tournament.
	 * @param tournamanetSize The size of the tournament to set.
	 */
	public void setTournamentSize(ControlParameter tournamanetSize) {
		this.tournamentProportion = tournamanetSize;
	}
	
	/**
	 * Get the {@linkplain RandomNumber} defined for this {@linkplain SelectionStrategy}. 
	 * @return The current {@linkplain RandomNumber}.
	 */
	public RandomNumber getRandomNumber() {
		return randomNumber;
	}

	/**
	 * Set the {@linkplain RandomNumber} for the {@linkplain TournamentSelectionStrategy}.
	 * @param randomNumber The {@linkplain RandomNumber} to set.
	 */
	public void setRandomNumber(RandomNumber randomNumber) {
		this.randomNumber = randomNumber;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public void performOperation(Topology<? extends Entity> topology, Topology<Entity> offspring) {
		offspring.add(this.select((Topology<Entity>) topology));
	}

	/**
	 * Get the {@linkplain Comparator} used in the comparisons of the tournament participants.
	 * @return The current {@linkplain Comparator}.
	 */
	public Comparator<Entity> getEntityComparator() {
		return entityComparator;
	}

	/**
	 * Set the {@linkplain Comparator} to be used for the tournament comparisons.
	 * @param entityComparator The {@linkplain Comparator} to set.
	 */
	public void setEntityComparator(Comparator<Entity> entityComparator) {
		this.entityComparator = entityComparator;
	}
	
}
