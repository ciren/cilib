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
package net.sourceforge.cilib.pso.velocityprovider;

import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 *
 * @author Wiehann Matthysen
 */
public class ClampingVelocityProvider implements VelocityProvider {

    private static final long serialVersionUID = -5995116445841750100L;

    private ControlParameter vMax;
    private VelocityProvider delegate;

    public ClampingVelocityProvider() {
        this.vMax = new ConstantControlParameter(Double.MAX_VALUE);
        this.delegate = new StandardVelocityProvider();
    }

    public ClampingVelocityProvider(ClampingVelocityProvider copy) {
        this.vMax = copy.vMax.getClone();
        this.delegate = copy.delegate.getClone();
    }

    @Override
    public ClampingVelocityProvider getClone() {
        return new ClampingVelocityProvider(this);
    }

    @Override
    public Vector get(Particle particle) {
        Vector velocity = this.delegate.get(particle);
        Vector.Builder builder = Vector.newBuilder();
        for (Numeric value : velocity) {
            if (value.doubleValue() < -vMax.getParameter()) {
                builder.add(-vMax.getParameter());
            } else if (value.doubleValue() > vMax.getParameter()) {
                builder.add(vMax.getParameter());
            } else {
                builder.add(value.doubleValue());
            }
        }
        return builder.build();
    }

    public void setVMax(ControlParameter vMax) {
        this.vMax = vMax;
    }

    public ControlParameter getVMax() {
        return this.vMax;
    }

    public void setDelegate(VelocityProvider delegate) {
        this.delegate = delegate;
    }

    public VelocityProvider getDelegate() {
        return this.delegate;
    }

    @Override
    public void updateControlParameters(Particle particle) {
        this.delegate.updateControlParameters(particle);
        this.vMax.updateParameter();
    }
}
