/*
 * ZiffGFSR4.java
 *
 * Created on January 16, 2003, 11:47 PM
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
 * source code is Copyright (C) 1998 James Theiler 
 * 
 */

package net.sourceforge.cilib.Random;

import java.util.*;

/**
 *
 * @author  espeer
 */
public class ZiffGFSR4 extends Random {
    
    public ZiffGFSR4() {
        super();
    }
    
    public ZiffGFSR4(long seed) {
        super(seed);
    }
    
    private long LCG(long n) {
        return (69069 * n) & 0xffffffffL;
    }

    public void setSeed(long seed) {
        ra = new long[M + 1];
        
        if (seed == 0) {
            seed = 4357;
        }
        
        long msb = 0x80000000L;
        long mask = 0xffffffffL;
        
        for (int i = 0; i <= M; ++i) {
            long t = 0;
            long bit = msb;
            for (int j = 0; j < 32; ++j) {
                seed = LCG(seed);
                if ((seed & msb) == msb) {
                    t |= bit;
                }
                bit >>>= 1;
            }
            ra[i] = t;
        }
        
        for (int i = 0; i < 32; ++i) {
            int k = 7 + i * 3;
            ra[k] &= mask;
            ra[k] |= msb;
            mask >>>= 1;
            msb >>>= 1;
        }
        
        nd = 32;
    }
    
    protected int next(int bits) {
        nd = (nd + 1) & M; 
        
        ra[nd] = ra[(nd + M + 1 - A) & M]
               ^ ra[(nd + M + 1 - B) & M]
               ^ ra[(nd + M + 1 - C) & M]
               ^ ra[(nd + M + 1 - D) & M];
        
        return (int) (ra[nd] >>> (32 - bits)) ;
    }
    
    private static final int A = 471;
    private static final int B = 1586;
    private static final int C = 6988;
    private static final int D = 9689;
    private static final int M = 16383;
    
    private int nd;
    private long[] ra;
    
}
