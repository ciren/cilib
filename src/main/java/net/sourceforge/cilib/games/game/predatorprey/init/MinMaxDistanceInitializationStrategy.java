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
package net.sourceforge.cilib.games.game.predatorprey.init;

import net.sourceforge.cilib.games.game.predatorprey.PredatorPreyGame;
import net.sourceforge.cilib.games.items.GridLocation;
import net.sourceforge.cilib.math.random.generator.Random;
import net.sourceforge.cilib.util.EuclideanDistanceMeasure;

/**
 * @author leo
 * This is a {@linkplain PredPreyPositionInitializationStrategy} that initializes the agents with a minimum and maximum distance constraint.
 */
public class MinMaxDistanceInitializationStrategy extends
        PredPreyPositionInitializationStrategy {

    private static final long serialVersionUID = 7005573437326699227L;
    int minDistance;
    int maxDistance;
    public MinMaxDistanceInitializationStrategy() {
        minDistance = 2;
        maxDistance = 5;
    }

    /**
     * Copy Constructor
     * @param other
     */
    public MinMaxDistanceInitializationStrategy(MinMaxDistanceInitializationStrategy other) {
        super(other);
        minDistance = other.minDistance;
        maxDistance = other.maxDistance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MinMaxDistanceInitializationStrategy getClone() {
        return new MinMaxDistanceInitializationStrategy(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initializePP(PredatorPreyGame game) {
        Random rand = game.getCurrentState().getRandomizer().getGenerator();
        double distance = 0;
        while(distance < minDistance || (maxDistance != -1 && distance > maxDistance)){
            for(int i = 0; i < game.getCurrentState().getSize(); ++i){
                GridLocation itemLoc = (GridLocation)game.getCurrentState().getItem(i).getLocation();
                itemLoc.setInt(0, rand.nextInt(game.getBoardWidth()));
                itemLoc.setInt(1, rand.nextInt(game.getBoardHeight()));
            }
            distance = game.getCurrentState().getItem(0).getLocation().getDistance(new EuclideanDistanceMeasure(), game.getCurrentState().getItem(1).getLocation());
        }
    }

    public int getMaxDistance() {
        return maxDistance;
    }

    public void setMaxDistance(int maxDistance) {
        this.maxDistance = maxDistance;
    }

    public int getMinDistance() {
        return minDistance;
    }

    public void setMinDistance(int minDistance) {
        this.minDistance = minDistance;
    }

}
