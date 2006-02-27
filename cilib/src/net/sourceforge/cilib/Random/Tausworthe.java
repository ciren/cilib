/*
 * Tausworthe.java
 *
 * Created on January 16, 2003, 11:09 PM
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
 * source code is Copyright (C) 1996, 1997, 1998, 1999, 2000 James Theiler 
 * and Brian Gough.
 * 
 */

package net.sourceforge.cilib.Random;

import java.util.*;

/**
 *
 * @author  espeer
 */
public class Tausworthe extends Random {
    
    public Tausworthe() {
        super();
    }
    
    public Tausworthe(long seed) {
        super(seed);
    }
    
    private long LCG(long n) {
        return (69069 * n) & 0xffffffffL;
    }
    
    public void setSeed(long seed) {
        if (seed == 0) {
            seed = 1;
        }
        
        s1 = LCG(seed);
        if (s1 < 2) {
            s1 += 2;
        }
        s2 = LCG(s1);
        if (s2 < 8) {
            s2 += 8;
        }
        s3 = LCG(s2);
        if (s3 < 16) {
            s3 += 16;
        }
        
        next(32);
        next(32);
        next(32);
        next(32);
        next(32);
        next(32);
    }
    
    protected int next(int bits) {
        s1 = (((s1 & 4294967294L) << 12) & 0xffffffffL) ^ ((((s1 << 13) & 0xffffffffL) ^ s1) >>> 19);
        s2 = (((s2 & 4294967288L) << 4) & 0xffffffffL) ^ ((((s2 << 2) & 0xffffffffL) ^ s2) >>> 25);
        s3 = (((s3 & 4294967280L) << 17) & 0xffffffffL) ^ ((((s3 << 3) & 0xffffffffL) ^ s3) >>> 11);
        
        return (int) ((s1 ^ s2 ^ s3) >>> (32 - bits));
    }
    
    private long s1;
    private long s2;
    private long s3;
}
