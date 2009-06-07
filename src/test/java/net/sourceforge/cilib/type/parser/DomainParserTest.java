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
package net.sourceforge.cilib.type.parser;

import net.sourceforge.cilib.type.types.container.StructuredType;
import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author gpampara
 */
public class DomainParserTest {

    @Test
    public void stringType() throws ParseException  {
        StructuredType vector = DomainParser.parse("T");

        Assert.assertEquals(1, vector.size());
    }

    @Test
    public void test() throws ParseException {
        Vector vector = (Vector) DomainParser.parse("R(-9.0, 9.0)^6");

        Assert.assertEquals(6, vector.size());
    }

    @Test
    public void infiniteBounds() throws ParseException {
        Vector vector = (Vector) DomainParser.parse("R^6");

        Assert.assertEquals(6, vector.size());
    }

    @Test
    public void singleInfiniteBounds() throws ParseException {
        Vector vector = (Vector) DomainParser.parse("R");

        Assert.assertEquals(1, vector.size());
    }

    @Test
    public void value() throws ParseException {
        Vector vector = (Vector) DomainParser.parse("R(8.0)^6");

        Assert.assertEquals(6, vector.size());
        for (int i = 0; i < vector.size(); i++)
            Assert.assertEquals(8.0, vector.get(0).getReal(), 0.001);
    }

    @Test
    public void singleValue() throws ParseException {
        Vector vector = (Vector) DomainParser.parse("R(8.0)");

        Assert.assertEquals(1, vector.size());
        Assert.assertEquals(8.0, vector.get(0).getReal(), 0.001);
    }

    @Test
    public void complex() throws ParseException {
        Vector vector = (Vector) DomainParser.parse("R(-9.0, 9.0),R^6,R(9.0),B,Z");

        Assert.assertEquals(10, vector.size());
    }
}
