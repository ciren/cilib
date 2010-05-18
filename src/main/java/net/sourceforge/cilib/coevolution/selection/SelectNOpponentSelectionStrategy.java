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
package net.sourceforge.cilib.coevolution.selection;

import java.util.ArrayList;
import java.util.List;
import net.sourceforge.cilib.coevolution.competitors.CoevolutionCompetitorList;
import net.sourceforge.cilib.coevolution.competitors.Competitor;
import net.sourceforge.cilib.math.random.ProbabilityDistributionFuction;
import net.sourceforge.cilib.math.random.UniformDistribution;

/**
 * Select N random opponents from the pool of competitors
 * @author Julien Duhain
 * @author leo
 */
public class SelectNOpponentSelectionStrategy extends OpponentSelectionStrategy {

    private static final long serialVersionUID = -7703414982437941424L;
    protected int numberOfOpponents;
    protected ProbabilityDistributionFuction random;

    public SelectNOpponentSelectionStrategy() {
        numberOfOpponents = 5;
        random = new UniformDistribution();
    }

    public SelectNOpponentSelectionStrategy(SelectNOpponentSelectionStrategy copy) {
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
    public CoevolutionCompetitorList selectCompetitors(CoevolutionCompetitorList pool) {
        CoevolutionCompetitorList opponents = new CoevolutionCompetitorList(numberOfOpponents);

        for (int i = 0; i < pool.getNumberOfLists(); ++i) {
            List<Competitor> selectedOpponents = new ArrayList<Competitor>();
            int pID = -1;
            for (int o = 0; o < numberOfOpponents; o++) {
                int selected = (int) random.getRandomNumber(0, pool.getNumberOfCompetitors(i));
                Competitor sel = pool.getCompetitor(i, selected);
                if (pID == -1) {
                    pID = sel.getPopulationID();
                }
                selectedOpponents.add(new Competitor(sel.getEntityData(), sel.getPopulationID()));
                pool.removeCompetitor(i, selected);
            }
            if (selectedOpponents.size() > 0) {
                opponents.addCompetitorList(pID, selectedOpponents);
            }
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
