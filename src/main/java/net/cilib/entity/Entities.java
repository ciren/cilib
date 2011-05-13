package net.cilib.entity;

import fj.data.Option;

/**
 *
 */
public final class Entities {

    private final static Entity DUMMY = new Entity() {
        @Override
        public CandidateSolution solution() {
            return CandidateSolution.empty();
        }

        @Override
        public int size() {
            return 0;
        }

        @Override
        public Option<Double> fitness() {
            return Option.none();
        }
    };

    private Entities() {
        throw new UnsupportedOperationException();
    }

    public static <A extends Entity> A dummy() {
        return (A) DUMMY;
    }
}
