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
package net.sourceforge.cilib.coevolution.selection;

import java.util.ArrayList;
import java.util.List;
import net.sourceforge.cilib.coevolution.competitors.CoevolutionCompetitorList;
import net.sourceforge.cilib.coevolution.competitors.Competitor;
import net.sourceforge.cilib.math.random.RandomNumber;


/**
 * Select N random opponents from the pool of competitors
 * @author Julien Duhain
 * @author leo
 */
public class SelectNOpponentSelectionStrategy extends OpponentSelectionStrategy {

	private static final long serialVersionUID = -7703414982437941424L;
	protected int numberOfOpponents;
	protected RandomNumber random;

	public SelectNOpponentSelectionStrategy(){
		numberOfOpponents = 5;
		random = new RandomNumber();
	}

	public SelectNOpponentSelectionStrategy(SelectNOpponentSelectionStrategy copy){
		this.numberOfOpponents = copy.numberOfOpponents;
		this.random = copy.random;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public OpponentSelectionStrategy getClone() {
		return new SelectNOpponentSelectionStrategy(this);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public  CoevolutionCompetitorList selectCompetitors(CoevolutionCompetitorList pool){
		CoevolutionCompetitorList opponents = new CoevolutionCompetitorList(numberOfOpponents);

		for(int i = 0; i < pool.getNumberOfLists(); ++i){
			List<Competitor> selectedOpponents = new ArrayList<Competitor>();
			int pID = -1;
			for(int o=0; o<numberOfOpponents;o++){
				int selected = (int)random.getUniform(0, pool.getNumberOfCompetitors(i));
				Competitor sel = pool.getCompetitor(i, selected);
				if(pID == -1)
					pID = sel.getPopulationID();
				selectedOpponents.add(new Competitor(sel.getEntityData(), sel.getPopulationID()));
				pool.removeCompetitor(i, selected);
			}
			if(selectedOpponents.size() > 0)
				opponents.addCompetitorList(pID, selectedOpponents);
		}
		return opponents;
	}

	public int getNumberOfOpponents() {
		return numberOfOpponents;
	}

	public void setNumberOfOpponents(int numberOfOpponents) {
		this.numberOfOpponents = numberOfOpponents;
	}
}
