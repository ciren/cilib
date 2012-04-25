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
package net.sourceforge.cilib.controlparameter;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.algorithm.AlgorithmEvent;
import net.sourceforge.cilib.algorithm.AlgorithmListener;

/**
 * A Control parameter that updates itself at the end of each iteration.
 */
public class UpdateOnIterationControlParameter implements ControlParameter {
    
    private double parameter;
    private ControlParameter delegate;
    private boolean attached;
    private AlgorithmListener listener = new AlgorithmListener() {

        @Override
        public void algorithmStarted(AlgorithmEvent e) {
            parameter = delegate.getParameter();
        }

        @Override
        public void algorithmFinished(AlgorithmEvent e) {
        }

        @Override
        public void algorithmTerminated(AlgorithmEvent e) {
        }

        @Override
        public void iterationCompleted(AlgorithmEvent e) {
            parameter = delegate.getParameter();
        }

        @Override
        public AlgorithmListener getClone() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    };
    
    public UpdateOnIterationControlParameter() {
        this(ConstantControlParameter.of(0.0));
    }

    public UpdateOnIterationControlParameter(ControlParameter delegate) {
        this.parameter = 0.0;
        this.delegate = delegate;
        this.attached = false;
    }
    
    public UpdateOnIterationControlParameter(UpdateOnIterationControlParameter copy) {
        this.parameter = copy.parameter;
        this.delegate = copy.delegate.getClone();
        this.attached = true;
        AbstractAlgorithm.get().addAlgorithmListener(listener);
    }
    
    @Override
    public UpdateOnIterationControlParameter getClone() {
        return new UpdateOnIterationControlParameter(this);
    }

    @Override
    public double getParameter() {
        if (!attached) {
            AbstractAlgorithm.get().addAlgorithmListener(listener);
            attached = true;
        }
        
        return parameter;
    }

    @Override
    public double getParameter(double min, double max) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setDelegate(ControlParameter delegate) {
        this.delegate = delegate;
    }

    public ControlParameter getDelegate() {
        return delegate;
    }
}
