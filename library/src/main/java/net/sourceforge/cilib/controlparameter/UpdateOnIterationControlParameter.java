/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
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
