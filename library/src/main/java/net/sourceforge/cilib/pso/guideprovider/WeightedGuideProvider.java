/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.guideprovider;

import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.type.types.container.StructuredType;
import net.sourceforge.cilib.type.types.container.Vector;

public class WeightedGuideProvider implements GuideProvider {
    
    private ControlParameter c1;
    private ControlParameter c2;
    private GuideProvider component1;
    private GuideProvider component2;
    
    public WeightedGuideProvider() {
        this.c1 = ConstantControlParameter.of(1.496180);
        this.c2 = ConstantControlParameter.of(1.496180);
        this.component1 = new PBestGuideProvider();
        this.component2 = new GBestGuideProvider();
    }

    private WeightedGuideProvider(WeightedGuideProvider other) {
        this.c1 = other.c1.getClone();
        this.c2 = other.c2.getClone();
        this.component1 = other.component1.getClone();
        this.component2 = other.component2.getClone();
    }

    public WeightedGuideProvider getClone() {
        return new WeightedGuideProvider(this);
    }

    public StructuredType get(Particle particle) {
        Vector v1 = (Vector) component1.get(particle);
        Vector v2 = (Vector) component2.get(particle);
        
        return v1.multiply(c1.getParameter())
                .plus(v2.multiply(c2.getParameter()))
                .divide(c1.getParameter() + c2.getParameter());
    }

    public void setComponent2(GuideProvider component2) {
        this.component2 = component2;
    }

    public void setComponent1(GuideProvider component1) {
        this.component1 = component1;
    }

    public void setC2(ControlParameter c2) {
        this.c2 = c2;
    }

    public void setC1(ControlParameter c1) {
        this.c1 = c1;
    }
}
