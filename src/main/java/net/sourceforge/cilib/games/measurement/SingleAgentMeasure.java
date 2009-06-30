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


import net.sourceforge.cilib.games.items.GameToken;


/**
 * This defines an {@linkplain AgentMeasure} that only records information for a specified {@linkplain Agent}
 * As specified by the given {@linkplain GameToken}
 * @author leo
 *
 */
public abstract class SingleAgentMeasure extends AgentMeasure {
    Enum itemToken;
    public SingleAgentMeasure() {
        itemToken = GameToken.DEFAULT;
    }

    public SingleAgentMeasure(Enum itemToken) {
        super();
        this.itemToken = itemToken;
    }

    public SingleAgentMeasure(SingleAgentMeasure other) {
        super(other);
        itemToken = other.itemToken;
    }

    public Enum getItemToken() {
        return itemToken;
    }

    public void setItemToken(Enum itemToken) {
        this.itemToken = itemToken;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public abstract SingleAgentMeasure getClone();

}
