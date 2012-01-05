package net.sourceforge.cilib.type.parser;

import net.sourceforge.cilib.type.types.Type;
import org.parboiled.Action;
import org.parboiled.BaseParser;
import org.parboiled.Context;
import org.parboiled.Rule;
import org.parboiled.annotations.BuildParseTree;
import org.parboiled.support.ValueStack;
import org.parboiled.support.Var;

public final class DomainParserGrammar {

    private static final BoundsFactory bf = new BoundsFactory();
    
    public static TypeCreator getCreator(char c) {
        TypeCreator instance = null;
        try {
            // create an instance of the TypeCreator
            Class<?> creatorClass = Class.forName("net.sourceforge.cilib.type.parser." + c);
            instance = (TypeCreator) creatorClass.newInstance();
        } catch (ClassNotFoundException cne) {
            throw new UnsupportedOperationException("Cannot find class: net.sourceforge.cilib.type.creator." + c, cne);
        } catch (IllegalAccessException i) {
            throw new UnsupportedOperationException("Cannot access!", i);
        } catch (InstantiationException in) {
            throw new UnsupportedOperationException("Cannot instantiate class: net.sourceforge.cilib.type.creator." + c, in);
        }
        return instance;
    }
    
    @BuildParseTree
    public static class Parser extends BaseParser<Object> {
        Rule Domain() {
            final Var<Boolean> bounded = new Var<Boolean>(false);
            return Sequence(
                    Type(),
                    push(DomainParserGrammar.getCreator(matchedChar())),
                    Optional(Bounds(bounded)),
                    new Action() {
                        @Override
                        public boolean run(Context context) {
                            ValueStack<Object> s = context.getValueStack();
                            if (bounded.get()) {
                                s.swap();
                                Double b1 = (Double) s.pop();
                                Double b2 = (Double) s.pop();
                                TypeCreator c = (TypeCreator) s.pop();
                                s.push(c.create(bf.create(b1, b2)));
                            } else {
                                TypeCreator c = (TypeCreator) s.pop();
                                s.push(c.create());
                            }
                            return true;
                        }
                    },
                    Optional(Exponent()),
                    ZeroOrMore(Sequence(',', Domain())),
                    EOI
            );
        }

        Rule Bounds(final Var<Boolean> bounded) {
            return Sequence('(', Decimal(), push(Double.valueOf(matchOrDefault("x"))), ',', Decimal(), push(Double.valueOf(matchOrDefault("x"))), ')',
                    new Action() {
                        @Override
                        public boolean run(Context context) {
                            bounded.set(true);
                            return true;
                        }
                    });
        }

        Rule Type() {
            return AnyOf("RZBT");
        }
        
        Rule NonZeroDigit() {
            return CharRange('1', '9');
        }

        Rule Digit() {
            return CharRange('0', '9');
        }
        
        Rule Decimal() {
            return Sequence(Optional(Ch('-')), OneOrMore(Digit()), Optional(Sequence('.', OneOrMore(Digit()))));
        }

        Rule Exponent() {
            return Sequence('^', Sequence(NonZeroDigit(), ZeroOrMore(Digit())), push(Integer.parseInt(match())), new Action<Object>() {
                @Override
                public boolean run(Context<Object> booleanContext) {
                    ValueStack<Object> s = booleanContext.getValueStack();
                    Integer count = (Integer) s.pop();
                    Type t = (Type) s.peek();
                    for (int i = 0; i < count-1; i++) {
                        s.push(t.getClone());
                    }
                    return true;
                }
            });
        }
    }
}
