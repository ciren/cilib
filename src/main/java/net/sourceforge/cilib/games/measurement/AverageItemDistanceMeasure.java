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

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.cilib.games.game.Game;
import net.sourceforge.cilib.games.states.GameState;
import net.sourceforge.cilib.games.states.ListGameState;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.util.EuclideanDistanceMeasure;

/**
 * @author leo
 *
 */
public class AverageItemDistanceMeasure extends AgentMeasure {
    /**
     *
     */
    private static final long serialVersionUID = 8680949683153569536L;
    List<Double> vals;

    /**
     *
     */
    public AverageItemDistanceMeasure() {
        vals = new ArrayList<Double>();
    }

    public AverageItemDistanceMeasure(AverageItemDistanceMeasure other) {
        super(other);
        vals = other.vals;
    }

    /* (non-Javadoc)
     * @see net.sourceforge.cilib.games.game.measurement.AgentMeasure#clearData()
     */
    public void clearData() {
        vals.clear();
    }

    /* (non-Javadoc)
     * @see net.sourceforge.cilib.games.game.measurement.AgentMeasure#getClone()
     */
    public AgentMeasure getClone() {
        return new AverageItemDistanceMeasure(this);
    }

    /* (non-Javadoc)
     * @see net.sourceforge.cilib.games.game.measurement.AgentMeasure#getMeasuredData()
     */
    public Type getMeasuredData() {
        double av = 0;
        for(Double d: vals)
            av += d;
        return new Real(av / (double)vals.size());
    }

    /* (non-Javadoc)
     * @see net.sourceforge.cilib.games.game.measurement.AgentMeasure#measure(net.sourceforge.cilib.games.game.Game)
     */
    public void measure(Game<GameState> game) {
        GameState state = game.getCurrentState();
        if(!(state instanceof ListGameState))
            throw new RuntimeException("Impliment for other state types");

        if(((ListGameState)state).getSize() != 2)
            throw new RuntimeException("Impliment for more than 2 items");
        double d =  ((ListGameState)state).getItem(0).getLocation().getDistance(new EuclideanDistanceMeasure(), ((ListGameState)state).getItem(1).getLocation());
        vals.add(d);
    }

}
