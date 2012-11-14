/**
 * Computational Intelligence Library (CIlib)
 * Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.cilib.pso.particle;

import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.pso.dynamic.ChargedParticle;
import net.sourceforge.cilib.pso.positionprovider.WeightDecayPositionProvider;

public class DecayingParticle extends ChargedParticle {
    private ControlParameter lambda;

    public DecayingParticle() {
        this.behavior.setPositionProvider(new WeightDecayPositionProvider());
        this.lambda = ConstantControlParameter.of(5e-6);
    }

     public DecayingParticle(DecayingParticle copy) {
        super(copy);
        this.lambda = copy.getLambda().getClone();
    }

    @Override
    public DecayingParticle getClone() {
        return new DecayingParticle(this);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if ((object == null) || (this.getClass() != object.getClass())) {
            return false;
        }

        DecayingParticle other = (DecayingParticle) object;
        return super.equals(object)
                && (Double.valueOf(this.lambda.getParameter()).equals(Double.valueOf(other.lambda.getParameter())));
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + super.hashCode();
        hash = 31 * hash + Double.valueOf(lambda.getParameter()).hashCode();
        return hash;
    }

    public ControlParameter getLambda() {
        return lambda;
    }

    public void setLambda(ControlParameter lambda) {
        this.lambda = lambda;
    }
}