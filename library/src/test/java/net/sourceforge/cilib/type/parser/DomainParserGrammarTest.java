/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
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
        DomainParserGrammar.DomainGrammar parser = Parboiled.createParser(DomainParserGrammar.DomainGrammar.class);
        ReportingParseRunner<?> runner = new ReportingParseRunner(parser.Domain());
        ParsingResult<?> result = runner.run("R(1.0:2.0),R(1.0:5.0)");

        if (result.hasErrors()) {
            for (ParseError e : result.parseErrors) {
                Assert.fail();
            }
        }
    }

    @Test
    public void sequence() {
        DomainParserGrammar.DomainGrammar parser = Parboiled.createParser(DomainParserGrammar.DomainGrammar.class);
        ReportingParseRunner<?> runner = new ReportingParseRunner(parser.Domain());
        ParsingResult<?> result = runner.run("R(-1.0:2.0),R(1.0:2.0)");

        ValueStack<Real> s = (ValueStack<Real>) result.valueStack;

        Assert.assertThat(s.size(), equalTo(2));
        Assert.assertThat(s.peek().getBounds().getLowerBound(), equalTo(1.0));
        s.pop();
        Assert.assertThat(s.peek().getBounds().getLowerBound(), equalTo(-1.0));
    }
    
    @Test
    public void boundless() {
        DomainParserGrammar.DomainGrammar parser = Parboiled.createParser(DomainParserGrammar.DomainGrammar.class);
        ReportingParseRunner<?> runner = new ReportingParseRunner(parser.Domain());
        ParsingResult<?> result = runner.run("Z");

        ValueStack<?> s = result.valueStack;

        Assert.assertThat(s.size(), equalTo(1));
    }
}
