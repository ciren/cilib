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

import net.sourceforge.cilib.type.parser.ParseException;
import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.Assert;

import org.junit.Test;

/**
 *
 * @author Gary Pampara
 */
public class DomainParserTest {

    @Test
    public void testParseReal() throws ParseException {
        net.sourceforge.cilib.type.parser.DomainParser.parse("R(0.0,9.0)");
        net.sourceforge.cilib.type.parser.DomainParser.parse("R");
        net.sourceforge.cilib.type.parser.DomainParser.parse("R^6");
        net.sourceforge.cilib.type.parser.DomainParser.parse("R(-9.0,9.0)");
        net.sourceforge.cilib.type.parser.DomainParser.parse("R(-30.0,30.0)^6");
    }


    @Test
    public void testParseBit() throws ParseException {
        net.sourceforge.cilib.type.parser.DomainParser.parse("B");
        net.sourceforge.cilib.type.parser.DomainParser.parse("B^6");
    }


    @Test
    public void testParseInteger() throws ParseException {
        net.sourceforge.cilib.type.parser.DomainParser.parse("Z");
        net.sourceforge.cilib.type.parser.DomainParser.parse("Z(-1,0)");
        net.sourceforge.cilib.type.parser.DomainParser.parse("Z(0,1)");
        net.sourceforge.cilib.type.parser.DomainParser.parse("Z(-999,999)");
        net.sourceforge.cilib.type.parser.DomainParser.parse("Z^8");
        net.sourceforge.cilib.type.parser.DomainParser.parse("Z(0,1)^10");
    }


    @Test
    public void testParseString() throws ParseException {
        net.sourceforge.cilib.type.parser.DomainParser.parse("T^5");
    }


    @Test
    public void testParseComplexDomain() throws ParseException {
        Vector vector = (Vector) net.sourceforge.cilib.type.parser.DomainParser.parse("R(-30.0,30.0)^30,B,Z^6");

        Assert.assertEquals(37, vector.size());
    }

}
