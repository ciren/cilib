/**
 * Copyright (C) 2003 - 2009
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
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
 */
package net.sourceforge.cilib.neuralnetwork.testarea;

import java.util.Random;

/**
 * TODO: Complete this javadoc.
 */
public final class tester {

    private tester() {
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        Random ran1 = new Random(System.currentTimeMillis());
        Random ran2 = new Random(System.currentTimeMillis());

        ran1.setSeed(100);
        ran2.setSeed(100);

        for (int i = 0; i < 20; i++){
            System.out.println("Ran1 = " + ran1.nextDouble() + ", ran2 = " + ran2.nextDouble());
        }
    }

}
