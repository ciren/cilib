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
package net.sourceforge.cilib.pso.dynamic;

import java.util.Iterator;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.velocityprovider.StandardVelocityProvider;
import net.sourceforge.cilib.pso.velocityprovider.VelocityProvider;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.Vectors;

/**
 * VelocityProvider that the so called Charged PSO makes use of.
 * This is an implementation of the original Charged PSO algorithm
 * developed by Blackwell and Bentley and then further improved by
 * Blackwell and Branke.
 *
 * @author Anna Rakitianskaia
 *
 */
public class ChargedVelocityProvider implements VelocityProvider {

    private static final long serialVersionUID = 365924556746583124L;
    
    private VelocityProvider delegate;
    private ControlParameter pCore; // lower limit
    private ControlParameter p; // upper limit

    public ChargedVelocityProvider() {
        this.delegate = new StandardVelocityProvider();
        this.pCore = new ConstantControlParameter(1);
        this.p = new ConstantControlParameter(30);
    }

    public ChargedVelocityProvider(ChargedVelocityProvider copy) {
        this.delegate = copy.delegate.getClone();
        this.pCore = copy.pCore.getClone();
        this.p = copy.p.getClone();
    }

    @Override
    public ChargedVelocityProvider getClone() {
        return new ChargedVelocityProvider(this);
    }

    @Override
    public Vector get(Particle particle) {
        Vector position = (Vector) particle.getPosition();

        PSO pso = (PSO) AbstractAlgorithm.get();
        Iterator<Particle> iter = null;
        // make iter point to the current particle
        for (Iterator<Particle> i = pso.getTopology().iterator(); i.hasNext();) {
            if (i.next().getId() == particle.getId()) {
                iter = i;
                break;
            }
        }

        // Calculate acceleration of the current particle
        Vector.Builder builder = Vector.newBuilder();
        for (int i = 0; i < particle.getDimension(); ++i) {
            double accSum = 0;
            for (Iterator<Particle> j = pso.getTopology().neighbourhood(iter); j.hasNext();) {
                ChargedParticle other = (ChargedParticle) j.next();
                if (particle.getId() == other.getId()) {
                    continue;
                }
                double qi = ((ChargedParticle) particle).getCharge();
                double qj = other.getCharge();
                Vector rij = position.subtract(other.getPosition());
                double magnitude = rij.norm();
                if (this.pCore.getParameter() <= magnitude && magnitude <= this.p.getParameter()) {
                    accSum += (qi * qj / Math.pow(magnitude, 3)) * rij.doubleValueOf(i);
                }
            }
            builder.add(accSum);
        }

        Vector acceleration = builder.build();

        Vector velocity = this.delegate.get(particle);

        return Vectors.sumOf(velocity, acceleration);
    }

    public void setDelegate(VelocityProvider delegate) {
        this.delegate = delegate;
    }

    public VelocityProvider getDelegate() {
        return this.delegate;
    }

    /**
     * @return the pCore
     */
    public ControlParameter getPCore() {
        return this.pCore;
    }

    /**
     * @param core the pCore to set
     */
    public void setPCore(ControlParameter core) {
        this.pCore = core;
    }

    /**
     * @return the p
     */
    public ControlParameter getP() {
        return this.p;
    }

    /**
     * @param p the p to set
     */
    public void setP(ControlParameter p) {
        this.p = p;
    }

    @Override
    public void updateControlParameters(Particle particle) {
        this.delegate.updateControlParameters(particle);
        this.pCore.updateParameter();
        this.p.updateParameter();
    }
}
