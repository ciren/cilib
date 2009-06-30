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
import net.sourceforge.cilib.games.items.GameItem;
import net.sourceforge.cilib.games.states.GameState;
import net.sourceforge.cilib.games.states.ListGameState;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Record each location an Agent has occupied at each timestep in the game. This could also be used
 * to measure locations a specific item type has occupied.
 * @author leo
 *
 */
public class RecordItemLocationMeasure extends SingleAgentMeasure {
    private static final long serialVersionUID = -7742916583743476119L;
    private Vector locations;

    public RecordItemLocationMeasure() {
        locations = new Vector();
    }

    public RecordItemLocationMeasure(Enum itemToken) {
        super(itemToken);
        locations = new Vector();
    }

    public RecordItemLocationMeasure(RecordItemLocationMeasure other) {
        super(other);
        locations = other.locations.getClone();
    }

    /**
     * {@inheritDoc}
     */
    public void clearData() {
        locations.clear();
    }

    /**
     * {@inheritDoc}
     */
    public RecordItemLocationMeasure getClone() {
        return new RecordItemLocationMeasure(this);
    }

    /**
     * {@inheritDoc}
     */
    public Type getMeasuredData() {
        return locations;
    }

    /**
     * {@inheritDoc}
     */
    public void measure(Game<GameState> game) {
        GameState state = game.getCurrentState();
        if(!(state instanceof ListGameState))
            throw new RuntimeException("Impliment for other state types");
        GameItem item = ((ListGameState)state).getItem(itemToken);
        locations.add((Numeric) item.getLocation().getClone());
    }

}
