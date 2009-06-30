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
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sourceforge.cilib.type.types;

/**
 * Interface to define bounds. These bounds are generally / currently defined
 * to be a single upper and lower bound. There may be situations whereby this
 * functionality may need to be extended to an arbitrary amount of bounds. This
 * is still very much in debate.
 *
 * @author gpampara
 */
public interface BoundedType {

    public Bounds getBounds();

    public void setBounds(Bounds bounds);

    public void setBounds(double lower, double upper);

}
