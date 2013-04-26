/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.nn.components;

import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.nn.architecture.NeuralInputSource;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Class represents a {@link NeuralInputSource} that decorates a {@link StandardPattern}
 * such that the pattern can be used as an input source for a neuron.
 */
public class PatternInputSource implements NeuralInputSource {

    private StandardPattern pattern;
    private Vector inputVector;

    /**
     * Constructor taking the {@link StandardPattern} to decorate as argument.
     * @param pattern the pattern to decorate.
     */
    public PatternInputSource(StandardPattern pattern) {
        this.pattern = pattern;
        this.inputVector = pattern.getVector();
    }

    /**
     * Gets the neural input by indexing into the decorated pattern's input vector.
     * @param index the index of the input to retrieve.
     * @return the neural input.
     */
    @Override
    public double getNeuralInput(int index) {
        return inputVector.doubleValueOf(index);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public int size() {
        return pattern.getVector().size();
    }

	/**
     * {@inheritDoc }
     */
    @Override
    public Neuron getNeuron(int index) {
        Neuron neuron = new Neuron();
        neuron.setActivationFunction(null);
        neuron.setActivation(this.getNeuralInput(index));
        return neuron;
    }


    /**
     * Gets the pattern's input vector.
     * @return the input vector of the pattern.
     */
    public Vector getVector() {
        return inputVector;
    }

    /**
     * Gets the pattern's target.
     * @return the target of the pattern.
     */
    public Type getTarget() {
        return pattern.getTarget();
    }
}
