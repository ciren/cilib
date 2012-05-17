/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.cilib.measurement.clustervalidity;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.CentroidHolder;

/**
 *
 * @author Kristina
 */
public class RayTuriFavouringValidityIndex extends ValidityIndex{
    private ControlParameter scalingConstant;
    private RayTuriValidityIndex delegate;
    private ControlParameter mean;
    private ControlParameter standardDeviation;
    
    public RayTuriFavouringValidityIndex() {
        scalingConstant = ConstantControlParameter.of(20.0);
        delegate = new RayTuriValidityIndex();
        mean = ConstantControlParameter.of(0.0);
        standardDeviation = ConstantControlParameter.of(1.0);
    }
    
    public RayTuriFavouringValidityIndex(RayTuriFavouringValidityIndex copy) {
        scalingConstant = copy.scalingConstant;
        delegate = copy.delegate;
        mean = copy.mean;
        standardDeviation = copy.standardDeviation;
    }
    
    @Override
    public RayTuriFavouringValidityIndex getClone() {
        return new RayTuriFavouringValidityIndex(this);
    }
    
    @Override
    public Real getValue(Algorithm algorithm) {
        CentroidHolder holder = (CentroidHolder) algorithm.getBestSolution().getPosition();
        double result = delegate.getValue(algorithm).doubleValue() * ((scalingConstant.getParameter() * getGaussianValue(holder)) + 1);
        
        return Real.valueOf(result);
    }
    
    protected double getGaussianValue(CentroidHolder holder) {
        double power = -1 * (Math.pow((holder.size() - mean.getParameter()), 2) / (2 * Math.pow(standardDeviation.getParameter(), 2)));
        double bottomOfEquation = Math.sqrt(2 * Math.PI * Math.pow(standardDeviation.getParameter(), 2));
        double result = (1 / bottomOfEquation) * Math.exp(power);
        
        return result;
    }
}
