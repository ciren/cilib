/*
 * GaussianMutator.java
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

package net.sourceforge.cilib.EC.Mutator;

import net.sourceforge.cilib.EC.EA.DoubleGene;
import net.sourceforge.cilib.EC.EA.Gene;

public class GaussianMutator extends Mutator {
    /**
     * The variance that used to determine the domain of the gaussian function.
     * The domain is determined as [-variance+mean, variance-mean]
     */
    //  private double variance = 1.0;
    /**
     * The width of the gaussian function, also known as the standard deviation.
     * This is the measure of how steep the gaussian curve is.  The higher the
     * value the LESS steep the gaussian becomes.
     */
    private double width = 0.1;
    /**
     * This is the mean of the gaussian function, or the center of the gaussian
     * function.
     */
    private double mean = 0.0;

    public GaussianMutator() {
    }

    public void mutate(Gene gene) {
        DoubleGene dGene = null;
        if (gene instanceof DoubleGene) {
            // cast the gene into DoubleGene.
            dGene = (DoubleGene) gene;
        }
        else {
            throw new RuntimeException("gene is not of type DoubleGene");
        }

        // calculate a random number in the range [-variance, variance].
        double r = (variance - mean) - 2.0 * (variance - mean) * Math.random();

        // calculate the gaussian function with this random number.
        double g = gaussian(r);

        // add the noise of the gaussian to the gene value.
        if (Math.random() < 0.5) {
            dGene.setGeneValue(dGene.getGeneValue() + g);
        }
        else {
            dGene.setGeneValue(dGene.getGeneValue() - g);
        }
    }

    private double gaussian(double x) {
        double x1 = Math.pow(x, 2.0);
        double x2 = 2.0 * Math.pow(width, 2.0);
        double y1 = Math.exp(- (x1 / x2));
        // double y2 = 1.0/(Math.sqrt(2.0*Math.PI)*width);
        double y2 = 1.0;
        double g = y1 * y2;
        return 1.0 - g;
    }

    public void setVariance(double variance) {
        this.variance = variance;
    }

    public double getVariance() {
        return variance;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getWidth() {
        return width;
    }

}
