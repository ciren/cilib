/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.nn.architecture;

import com.google.common.collect.AbstractIterator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.sourceforge.cilib.nn.architecture.builder.ArchitectureBuilder;
import net.sourceforge.cilib.nn.architecture.builder.FeedForwardArchitectureBuilder;
import net.sourceforge.cilib.nn.architecture.visitors.ArchitectureVisitor;

/**
 * Represents a neural network architecture and encapsulates a
 * {@link ArchitectureBuilder} and a list of {@link Layer}s.
 */
public class Architecture {

    private ArchitectureBuilder architectureBuilder;
    private List<Layer> layers;

    /**
     * Default constructor. Default {@link ArchitectureBuilder} is a
     * {@link FeedForwardArchitectureBuilder}.
     */
    public Architecture() {
        layers = new ArrayList<Layer>();
        architectureBuilder = new FeedForwardArchitectureBuilder();
    }

    public Architecture(Architecture rhs) {
        layers = new ArrayList<Layer>();
        for (Layer curLayer : rhs.layers)
            layers.add(curLayer.getClone());

        architectureBuilder = rhs.architectureBuilder.getClone();
    }

    /**
     * Initialises the architecture by calling the {@link ArchitectureBuilder}'s
     *  build method on 'this' object.
     */
    public void initialise() {
        architectureBuilder.buildArchitecture(this);
    }

    /**
     * Accepts an {@link ArchitectureVisitor} and calls its visit method on
     * 'this' object.
     * @param visitor
     */
    public void accept(ArchitectureVisitor visitor) {
        visitor.visit(this);
    }

    /**
     * Gets the architecture builder.
     * @return the architecture builder.
     */
    public ArchitectureBuilder getArchitectureBuilder() {
        return architectureBuilder;
    }

    /**
     * Sets the architecture builder.
     * @param architectureBuilder the new architecture builder.
     */
    public void setArchitectureBuilder(ArchitectureBuilder architectureBuilder) {
        this.architectureBuilder = architectureBuilder;
    }

    /**
     * Gets the number of layers the architecture contains.
     * @return the number of layers in the architecture.
     */
    public int getNumLayers() {
        return layers.size();
    }

    /**
     * Gets the list containing the layers.
     * @return list containing the layers.
     */
    public List<Layer> getLayers() {
        return layers;
    }

    /**
     * Sets the list containing the layers.
     * @param layers the new list containing the layers.
     */
    public void setLayers(List<Layer> layers) {
        this.layers = layers;
    }

    /**
     * Iterable for all hidden and output layers.
     */
    public Iterable<Layer> getActivationLayers() {
        return new Iterable<Layer>() {
            @Override
            public Iterator<Layer> iterator() {
                return new ActivationLayerIterator();
            }
        };
    }

    private class ActivationLayerIterator extends AbstractIterator<Layer> {

        private int idx = 1;

        @Override
        protected Layer computeNext() {
            if (idx >= getNumLayers()) {
                return endOfData();
            }
            return getLayers().get(idx++);
        }
    }
}
