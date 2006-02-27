/*
 * Neuron.java
 *
 * Created on June 24, 2003, 21:00 PM
 *
 *
 * Copyright (C) 2003, 2004 - CIRG@UP 
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
 *
 */

package net.sourceforge.cilib.NeuralNetwork;

public class Neuron {
    int size;

    Neuron(int size) {
        this.size = size;
    }

    double getOutput(double input[], double wieght[], int r1, int r2) {
        double output = 0.0;
        if (r2 - r1 != size) {
            throw new RuntimeException("r1 - r2 != Size");
        }
        double net = 0.0;
        for (int i = r1; i < r2 - 1; i++) {
            net += wieght[i] * input[i - r1];
        }

        net -= wieght[r2 - 1];
        output = 1.0 / (1.0 + ((double) Math.exp(-net)));

        return output;
    }
}