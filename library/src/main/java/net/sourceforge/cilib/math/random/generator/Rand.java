/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.math.random.generator;

import net.sourceforge.cilib.math.random.generator.seeder.Seeder;

public class Rand {
    
    private static ThreadLocal<MersenneTwister> random = new ThreadLocal<MersenneTwister>() {
        @Override
        protected MersenneTwister initialValue() {
            return new MersenneTwister(Seeder.getSeed());
        }
    };

    public static boolean nextBoolean() {
        return Rand.random.get().nextBoolean();
    }

    public static int nextInt() {
        return Rand.random.get().nextInt();
    }

    public static int nextInt(int n) {
        return Rand.random.get().nextInt(n);
    }

    public static long nextLong() {
        return Rand.random.get().nextLong();
    }

    public static float nextFloat() {
        return Rand.random.get().nextFloat();
    }

    public static double nextDouble() {
        return Rand.random.get().nextDouble();
    }

    public static void nextBytes(byte[] bytes) {
        Rand.random.get().nextBytes(bytes);
    }
    
    public static void setSeed(long seed) {
        random.set(new MersenneTwister(seed));
    }
    
    public static long getSeed() {
        return random.get().getSeed();
    }
    
    public static void reset() {
        setSeed(Seeder.getSeed());
    }

}
