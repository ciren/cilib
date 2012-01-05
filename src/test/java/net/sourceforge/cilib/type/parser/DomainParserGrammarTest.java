/**
 * Computational Intelligence Library (CIlib)
 * Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.cilib.type.parser;

import net.sourceforge.cilib.type.types.Real;
import org.junit.Assert;
import org.junit.Test;
import org.parboiled.Parboiled;
import org.parboiled.errors.ParseError;
import org.parboiled.parserunners.ReportingParseRunner;
import org.parboiled.support.ParsingResult;
import org.parboiled.support.ValueStack;

import static org.hamcrest.CoreMatchers.equalTo;

public class DomainParserGrammarTest {

    @Test
    public void twoBounds() {
        DomainParserGrammar.Parser parser = Parboiled.createParser(DomainParserGrammar.Parser.class);
        ReportingParseRunner<?> runner = new ReportingParseRunner(parser.Domain());
        ParsingResult<?> result = runner.run("R(1.0,2.0),R(1.0,5.0)");

        if (result.hasErrors()) {
            for (ParseError e : result.parseErrors) {
                System.out.println(e.getStartIndex());
            }
        }

        ValueStack<?> s = result.valueStack;
        System.out.println(s.size());
        while (!s.isEmpty()) {
            System.out.println(s.pop());
        }
    }

    @Test
    public void exponent() {
        DomainParserGrammar.Parser parser = Parboiled.createParser(DomainParserGrammar.Parser.class);
        ReportingParseRunner<?> runner = new ReportingParseRunner(parser.Domain());
        ParsingResult<?> result = runner.run("R(1.0,2.0)^6");

        ValueStack<?> s = result.valueStack;
        
        Assert.assertThat(s.size(), equalTo(6));
    }

    @Test
    public void sequence() {
        DomainParserGrammar.Parser parser = Parboiled.createParser(DomainParserGrammar.Parser.class);
        ReportingParseRunner<?> runner = new ReportingParseRunner(parser.Domain());
        ParsingResult<?> result = runner.run("R(-1.0,2.0),R(1.0,2.0)");

        ValueStack<Real> s = (ValueStack<Real>) result.valueStack;

        Assert.assertThat(s.size(), equalTo(2));
        Assert.assertThat(s.peek().getBounds().getLowerBound(), equalTo(1.0));
        s.pop();
        Assert.assertThat(s.peek().getBounds().getLowerBound(), equalTo(-1.0));
    }
    
    @Test
    public void combinedSequenceAndExponent() {
        DomainParserGrammar.Parser parser = Parboiled.createParser(DomainParserGrammar.Parser.class);
        ReportingParseRunner<?> runner = new ReportingParseRunner(parser.Domain());
        ParsingResult<?> result = runner.run("R(-1.0,2.0)^9,R(1.0,2.0)");

        ValueStack<?> s = result.valueStack;

        Assert.assertThat(s.size(), equalTo(10));
    }
    
    @Test
    public void boundless() {
        DomainParserGrammar.Parser parser = Parboiled.createParser(DomainParserGrammar.Parser.class);
        ReportingParseRunner<?> runner = new ReportingParseRunner(parser.Domain());
        ParsingResult<?> result = runner.run("Z");

        ValueStack<?> s = result.valueStack;

        Assert.assertThat(s.size(), equalTo(1));
    }
}
