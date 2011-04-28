package net.cilib.algorithm;

import fj.F;

/**
 *
 */
public abstract class Predicate<T> extends F<T, Boolean> {
    
    private static final Predicate<Boolean> ALWAYS_TRUE = new Predicate<Boolean>() {
        @Override
        public Boolean f(Boolean a) {
            return true;
        }
    };
    
    private static final Predicate<Boolean> ALWAYS_FALSE = new Predicate<Boolean>() {
        @Override
        public Boolean f(Boolean a) {
            return false;
        }
    };
    
    public static Predicate<Boolean> alwaysTrue() {
        return ALWAYS_TRUE;
    }
    
    public static Predicate<Boolean> alwaysFalse() {
        return ALWAYS_FALSE;
    }
}
