/*
 * TournamentSelectionStrategy.java
 * 
 * Created on Apr 1, 2006
 *
 * Copyright (C) 2003, 2004 - CIRG@UP 
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
 *
 */
package net.sourceforge.cilib.entity.operators.selection;

import java.util.ArrayList;
import java.util.Collections;
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
	
	private ControlParameter tournamentProportion;
	private RandomNumber randomNumber;
	
	public TournamentSelectionStrategy() {
		this.tournamentProportion = new ProportionalControlParameter();
	}
	
	public TournamentSelectionStrategy(TournamentSelectionStrategy copy) {
		this.tournamentProportion = copy.tournamentProportion.getClone();
		this.randomNumber = copy.randomNumber.getClone();
	}
	
	public TournamentSelectionStrategy getClone() {
		return new TournamentSelectionStrategy(this);
	}

	public Entity select(Topology<? extends Entity> population) {
		int tournamentSize = Double.valueOf(this.tournamentProportion.getParameter()*population.size()).intValue();
		
		List<Entity> tournamentEntities = new ArrayList<Entity>();
		
		while (tournamentEntities.size() < tournamentSize) {
			double random = randomNumber.getUniform(0, population.size());
			Entity tmp = population.get(Double.valueOf(random).intValue());
			
			if (!tournamentEntities.contains(tmp)) {
				tournamentEntities.add(tmp);
			}
		}
		
		Collections.sort(tournamentEntities, new DescendingFitnessComparator());
		
		return tournamentEntities.get(0);
	}

	public ControlParameter getTournamentSize() {
		return tournamentProportion;
	}

	public void setTournamentSize(ControlParameter tournamentProportion) {
		this.tournamentProportion = tournamentProportion;
	}

	@SuppressWarnings("unchecked")
	public void performOperation(Topology<? extends Entity> topology, Topology<Entity> offspring) {
		offspring.add(this.select((Topology<Entity>) topology));
	}
	
}
