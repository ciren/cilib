/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.io.pattern;

import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.Cloneable;

/**
 * Class represents a data pattern: A feature vector and it's corresponding target.
 * The target type is a cloneable object, thereby being very unrestricted.
 *
 */
public class StandardPattern implements Cloneable {

    private Vector vector;
    private Type target;

    /**
     * Default constructor.
     */
    public StandardPattern() {
        vector = Vector.of();
    }

    /**
     * Convenience constructor setting the vector and target.
     * @param vector the new pattern's vector.
     * @param target the new pattern's target.
     */
    public StandardPattern(Vector vector, Type target) {
        this.vector = Vector.copyOf(vector);
        this.target = target.getClone();
    }

    /**
     * Copy constructor.
     * @param orig the pattern to be copied.
     */
    public StandardPattern(StandardPattern orig) {
        this.vector = Vector.copyOf(orig.vector);
        this.target = orig.target.getClone();
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Object getClone() {
        return new StandardPattern(this);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final StandardPattern other = (StandardPattern) obj;
        if (this.vector != other.vector && (this.vector == null || !this.vector.equals(other.vector))) {
            return false;
        }
        if (this.target != other.target && (this.target == null || !this.target.equals(other.target))) {
            return false;
        }
        return true;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + (this.vector != null ? this.vector.hashCode() : 0);
        hash = 97 * hash + (this.target != null ? this.target.hashCode() : 0);
        return hash;
    }

    /**
     * Gets the pattern's target.
     * @return the pattern's target.
     */
    public Type getTarget() {
        return target;
    }

    /**
     * Sets the pattern's target.
     * @param target the pattern's new target.
     */
    public void setTarget(Type target) {
        this.target = target;
    }

    /**
     * Gets the pattern's feature vector.
     * @return the pattern's feature vector.
     */
    public Vector getVector() {
        return vector;
    }

    /**
     * Sets the pattern's feature vector.
     * @param vector the pattern's feature vector.
     */
    public void setVector(Vector vector) {
        this.vector = vector;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String toString() {
        return this.vector.toString() +" "+ this.target.toString();
    }


}
