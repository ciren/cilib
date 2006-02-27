/*
 * MersenneTwister.java
 *
 * Created on January 16, 2003, 4:53 PM
 *
 * 
 * Copyright (C) 2003 - Edwin S. Peer
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
 *  
 * This code is based on the implementation in GSL (GNU Scientific Library) 
 * which is also covered by the GNU General Public License. The original C 
 * source code is Copyright (C) 1998 Brian Gough.
 * 
 */

package net.sourceforge.cilib.Random;

import java.util.*;

/**
 *
 * @author  espeer
 */
public class MersenneTwister extends Random {
    
    public MersenneTwister() {
        super();
    }
    
    public MersenneTwister(long seed) {
        super(seed);
    }
    
    public void setSeed (long seed) {
        data = new long[N];
        
        if (seed == 0) {
            seed = 4357;
        }

        data[0] = seed & 0xffffffffL;

        for (index = 1; index < N; ++index) {
            data[index] = (1812433253L * (data[index - 1] ^ (data[index - 1] >>> 30)) + index);
            data[index] &= 0xffffffffL;
        }
    }

    protected int next(int bits) {
        if (index >= N) {
            int i;
            for (i = 0; i < N - M; ++i) {
                long y = (data[i] & UPPER_MASK) | (data[i + 1] & LOWER_MASK);
                data[i] = data[i + M] ^ (y >>> 1);
                if ((y & 1L) == 1L) {
                    data[i] ^= 0x9908b0dfL;
                }
            }
            for (; i < N - 1; ++i) {
                long y = (data[i] & UPPER_MASK) | (data[i + 1] & LOWER_MASK);
                data[i] = data[i + (M - N)] ^ (y >>> 1);
                if ((y & 1L) == 1L) {
                    data[i] ^= 0x9908b0dfL;
                }
            }
            long y = (data[N - 1] & UPPER_MASK) | (data[0] & LOWER_MASK);
            data[N - 1] = data[M - 1] ^ (y >>> 1);
            if ((y & 1L) == 1L) {
                data[N - 1] ^= 0x9908b0dfL;
            }
            index = 0;
        }
        
        long k = data[index];
        k ^= (k >>> 11);
        k ^= (k << 7) & 0x9d2c5680L;
        k ^= (k << 15) & 0xefc60000L;
        k ^= (k >>> 18);
        
        ++index;
        
        return (int) ((k & 0xffffffffL) >>> (32 - bits));
    }
    
    private long[] data;
    private int index;
    
    private static final int N = 624;
    private static final int M = 397;
    private static final long UPPER_MASK = 0x80000000L;
    private static final long LOWER_MASK = 0x7fffffffL;
}
