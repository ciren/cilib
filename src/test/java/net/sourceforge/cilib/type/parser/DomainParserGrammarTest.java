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
