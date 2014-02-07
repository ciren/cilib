/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.util.functions;

import com.google.common.collect.Lists;

import fj.F;
import fj.F2;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.MemoryBasedEntity;
import net.sourceforge.cilib.entity.SocialEntity;
import net.sourceforge.cilib.problem.Problem;
import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.type.types.container.StructuredType;
import net.sourceforge.cilib.util.Cloneable;

public final class Entities {
    private Entities() {}

    public static <E extends Cloneable> F<E, E> clone_() {
        return new F<E, E>() {
            @Override
            public E f(E e) {
                return (E) e.getClone();
            }
        };
    }

    public static <E extends Entity> F<E, StructuredType> getPosition() {
        return new F<E, StructuredType>() {
            @Override
            public StructuredType f(E a) {
                return a.getPosition();
            }
        };
    }

    public static <E, T extends Entity> java.util.List<E> getPositions(java.util.List<T> list) {
        return Lists.newArrayList(fj.data.List.iterableList(list).map(new F<T, E>() {
            @Override
            public E f(T e) {
                return (E) e.getPosition();
            }
        }));
    }

    public static <E extends Entity> F2<StructuredType, E, E> setPosition() {
        return new F2<StructuredType, E, E>() {
            @Override
            public E f(StructuredType a, E b) {
                b.setPosition(a);
                return b;
            }
        };
    }

    public static <E extends Entity> F<E, E> evaluate() {
        return new F<E, E>() {
            @Override
            public E f(E a) {
                a.updateFitness(a.getBehaviour().getFitnessCalculator().getFitness(a));
                return a;
            }
        };
    }

    public static <E extends Entity> F<E, Fitness> getFitness() {
        return new F<E, Fitness>() {
            @Override
            public Fitness f(E a) {
                return a.getFitness();
            }
        };
    }

    public static <E extends Entity> F<E, Fitness> getBestFitness() {
        return new F<E, Fitness>() {
            @Override
            public Fitness f(E a) {
                return a.getBestFitness();
            }
        };
    }

    public static <E extends Entity> F<E, E> reinitialise() {
        return new F<E, E>() {
            @Override
            public E f(E a) {
                a.reinitialise();
                return a;
            }
        };
    }

    public static <E extends Entity> F2<Problem, E, E> initialise() {
        return new F2<Problem, E, E>() {
            @Override
            public E f(Problem a, E b) {
                b.initialise(a);
                return b;
            }
        };
    }

    public static <E extends MemoryBasedEntity> F<E, StructuredType> getBestPosition() {
        return new F<E, StructuredType>() {
            @Override
            public StructuredType f(E a) {
                return a.getBestPosition();
            }
        };
    }

    public static <E extends SocialEntity> F<E, Fitness> getSocialFitness() {
        return new F<E, Fitness>() {
            @Override
            public Fitness f(E a) {
                return a.getSocialFitness();
            }
        };
    }
}
