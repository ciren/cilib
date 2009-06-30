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
package net.sourceforge.cilib.games.items;

import net.sourceforge.cilib.games.game.Game;

/**
 * This interface should be implimented by all {@linkplain GameToken} Enum's
 * so that a textual description of each instance can easily be accessed
 *
 * @author leo
 *
 */
public interface GameEnum {
    /**
     * Return a description of a specific Enum, this description is
     * used in the {@linkplain Game}'s {@code display()} method to show
     * a {@linkplain GameItem}
     * @return the string description.
     */
    public String getDescription();
}
