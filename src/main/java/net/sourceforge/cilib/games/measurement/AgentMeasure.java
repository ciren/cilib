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
package net.sourceforge.cilib.games.measurement;
import net.sourceforge.cilib.games.game.Game;
import net.sourceforge.cilib.games.states.GameState;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.util.Cloneable;

/**
 * @author leo
 * This class measures any game specific features observed by agents during play. This information can be used to describe the game player and/or
 * calculate fitness based on certain game specific requirements.
 */
public abstract class AgentMeasure implements Cloneable {
    public AgentMeasure(){
    }
    public AgentMeasure(AgentMeasure other){
    }

    /**
     * Measure game specific information and store it
     * @param game the game to measure information from
     */
    public abstract void measure(Game<GameState> game);
    /**
     * return the measured data
     * @return
     */
    public abstract Type getMeasuredData();
    /**
     * Clear all measured data.
     */
    public abstract void clearData();
    /**
     * {@inheritDoc}
     */
    @Override
    public abstract AgentMeasure getClone();
}
