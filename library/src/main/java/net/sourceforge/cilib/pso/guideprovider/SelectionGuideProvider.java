/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.guideprovider;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.type.types.container.StructuredType;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.selection.recipes.Selector;
import net.sourceforge.cilib.util.selection.recipes.TournamentSelector;

public class SelectionGuideProvider implements GuideProvider {
    
    private Selector<Particle> selector;
    private boolean perDimension;
    private GuideProvider component;
    
    public SelectionGuideProvider() {
        this.selector = new TournamentSelector();
        this.perDimension = true;
        this.component = new CurrentPositionGuideProvider();
    }

    private SelectionGuideProvider(SelectionGuideProvider other) {
        this.selector = other.selector;
        this.perDimension = other.perDimension;
        this.component = other.component.getClone();
    }

    @Override
    public SelectionGuideProvider getClone() {
        return new SelectionGuideProvider(this);
    }

    @Override
    public StructuredType get(Particle particle) {
        fj.data.List<Particle> topology = ((PSO) AbstractAlgorithm.get()).getTopology();
        
        if (perDimension) {
            Vector.Builder builder = Vector.newBuilder();
            for (int i = 0; i < particle.getDimension(); i++) {
                Vector v = (Vector) component.get(selector.on(topology).select());
                builder.add(v.get(i));
            }
            return builder.build();
        }
        
        return component.get(selector.on(topology).select());
    }

    public void setSelector(Selector<Particle> selector) {
        this.selector = selector;
    }

    public void setPerDimension(boolean perDimension) {
        this.perDimension = perDimension;
    }

    public void setComponent(GuideProvider component) {
        this.component = component;
    }

}
