/**
 * Copyright (C) 2003 - 2008
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

package net.sourceforge.cilib.type;

import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author gpampara
 */
public class StringBasedDomainRegistryTest {

    @Test
    public void matrixCreation() {
        DomainRegistry registry = new StringBasedDomainRegistry();
        registry.setDomainString("[R(-5.0, 5.0)^10]^10");

        Vector matrix = (Vector) registry.getBuiltRepresenation();
        Assert.assertEquals(10, matrix.getDimension());
        Assert.assertEquals(10, ((Vector) matrix.get(0)).size());
        Assert.assertTrue(matrix.isInsideBounds());
    }
}
