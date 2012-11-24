/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.type.types.container;

import com.google.common.base.Predicate;
import fj.F;
import fj.F2;
import static fj.data.Array.*;
import fj.data.List;
import static fj.data.List.*;
import java.util.Iterator;
import java.util.RandomAccess;
import net.sourceforge.cilib.type.types.Bit;
import net.sourceforge.cilib.type.types.Bounds;
import net.sourceforge.cilib.type.types.Int;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.util.functions.Numerics;
import net.sourceforge.cilib.util.functions.Utils;

public class FList implements Type, Iterable<Numeric>, RandomAccess {
 
    private List<Numeric> components;

    public static FList of() {
        return new FList(List.<Numeric>nil());
    }

    public static FList of(Number... numbers) {
        return copyOf(array(numbers).map(Numerics.numeric()));
    }

    public static FList of(Numeric... numerics) {
        return copyOf(array(numerics));
    }

    public static <N extends Number> FList of(Iterable<N> iterable) {
        return copyOf(iterableList(iterable).<Numeric>map(Numerics.<N>numeric()));
    }
    
    public static FList copyOf(Iterable<Numeric> list) {
        return new FList(iterableList(list));
    }

    public static FList fill(Numeric n, int size) {
    	return new FList(List.replicate(size, n));
    }

    public static FList fill(Number n, int size) {
    	return fill(Real.valueOf(n.doubleValue()), size);
    }

    private FList(List<Numeric> elements) {
        this.components = elements;
    }

    @Override
    public FList getClone() {
        return copyOf(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if ((obj == null) || (this.getClass() != obj.getClass())) {
            return false;
        }

        FList otherList = (FList) obj;
        return components.equals(otherList.components);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (this.components == null ? 0 : components.hashCode());
        return hash;
    }

    public FList copyOfRange(final int fromIndex, final int toIndex) {
        return new FList(components.splitAt(toIndex)._1().drop(fromIndex));
    }
    
    public Numeric[] toArray() {
        return components.array(Numeric[].class);
    }

    public boolean contains(Numeric o) {
        return components.exists(Numerics.equalF().f(o));
    }

    public boolean isEmpty() {
        return components.isEmpty();
    }

    @Override
    public Iterator<Numeric> iterator() {
        return components.iterator();
    }

    public int size() {
        return components.length();
    }

    /* ************************* Vector Math ******************************** 
    
    @Override
    public final FList plus(FList vector) {
        return new FList(components.map(Numerics.doubleValue())
                .zipWith(vector.map(Numerics.doubleValue()), Doubles.add)
                .map(Numerics.numeric()));
    }

    @Override
    public final FList subtract(FList vector) {
        if (this.components.length != vector.size()) {
            throw new UnsupportedOperationException("Cannot subtract vectors with differing dimensions");
        }
        Numeric[] result = new Numeric[components.length];
        for (int i = 0, n = components.length; i < n; i++) {
            result[i] = Real.valueOf(components[i].doubleValue() - vector.components[i].doubleValue(), components[i].getBounds());
        }
        return new FList(result);
    }

    @Override
    public final FList divide(double scalar) {
        if (scalar == 0.0) {
            throw new ArithmeticException("FList division by zero");
        }
        return this.multiply(1.0 / scalar);
    }

    public final FList multiply(double scalar) {
        return multiply(P.<Number>p(scalar));
    }

    public final FList multiply(P1<Number> supplier) {
        Numeric[] result = new Numeric[components.length];
        for (int i = 0, n = components.length; i < n; i++) {
            result[i] = Real.valueOf(components[i].doubleValue() * supplier._1().doubleValue(), components[i].getBounds());
        }
        return new FList(result);
    }

    @Override
    public final double norm() {
        return Math.sqrt(foldLeft(0, new F<Numeric, Double>() {
            @Override
            public Double f(Numeric x) {
                return x.doubleValue() * x.doubleValue();
            }
        }));
    }

    @Override
    public final double length() {
        return this.norm();
    }

    @Override
    public final FList normalize() {
        FList local = copyOf(this);
        double value = local.norm();

        // If the norm() of the vector is 0.0, then we are takling about the "normal vector"
        // (\vector{0}) and as a result the normal vector is it's own normal.
        return (Double.compare(value, 0.0) != 0) ? local.divide(value) : local;
    }

    @Override
    public final double dot(FList vector) {
        if (this.size() != vector.size()) {
            throw new ArithmeticException("Cannot perform the dot product on vectors with differing dimensions");
        }

        double result = 0.0;
        for (int i = 0, n = components.length; i < n; i++) {
            result += this.doubleValueOf(i) * vector.doubleValueOf(i);
        }
        return result;
    }

    @Override
    public final FList cross(FList vector) {
        if (this.size() != vector.size()) {
            throw new ArithmeticException("Cannot perform the dot product on vectors with differing dimensions");
        }

        if (this.size() != 3) { // implicitly checks that vector.size() == 3
            throw new ArithmeticException("Cannot determine the cross product on non 3-dimensional vectors.");
        }

        Numeric[] n = new Numeric[components.length];
        n[0] = Real.valueOf(this.doubleValueOf(1) * vector.doubleValueOf(2) - this.doubleValueOf(2) * vector.doubleValueOf(1));
        n[1] = Real.valueOf(-(vector.doubleValueOf(2) * this.doubleValueOf(0) - vector.doubleValueOf(0) * this.doubleValueOf(2)));
        n[2] = Real.valueOf(this.doubleValueOf(0) * vector.doubleValueOf(1) - this.doubleValueOf(1) * vector.doubleValueOf(0));
        return new FList(n);
    }

    public boolean isZero() {
        for (Numeric n : this) {
            if (Double.compare(n.doubleValue(), 0.0) != 0) {
                return false;
            }
        }

        return true;
    }

    public FList orthogonalize(Iterable<FList> vs) {
        FList u = copyOf(this);

        for (FList v : vs) {
            u = u.subtract(u.project(v));
        }

        return u;
    }

    public FList project(FList v) {
        return v.multiply(this.dot(v) / v.dot(v));
    }
    * */

    public double doubleValueOf(int index) {
        return components.index(index).doubleValue();
    }

    public int intValueOf(int index) {
        return components.index(index).intValue();
    }

    public boolean booleanValueOf(int index) {
        return components.index(index).booleanValue();
    }

    public Bounds boundsOf(int index) {
        return components.index(index).getBounds();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("[");
        if (components.isNotEmpty()) {
            builder.append(components.head());
        }        
        if (components.tail().isNotEmpty()) {
            for (Numeric n : components.tail()) {
                builder.append(",").append(n);
            }
        }
        builder.append("]");
        return builder.toString();
    }

    public FList map(F<Numeric, Numeric> function) {
        return new FList(components.map(function));
    }

    public FList filter(F<Numeric, Boolean> predicate) {
        return new FList(components.filter(predicate));
    }
    
    public FList filter(Predicate<Numeric> predicate) {
        return new FList(components.filter(Utils.predicate(predicate)));
    }

    public double foldLeft(double initial, F2<Double, Double, Double> function) {
        return components.map(Numerics.doubleValue()).foldLeft(function, initial);
    }

    public Double reduceLeft(F2<Double, Double, Double> function) {
        return components.map(Numerics.doubleValue()).foldLeft1(function);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {

        private List<Numeric> elements;

        private Builder() {
            this.elements = nil();
        }

        public Builder prepend(Numeric n) {
            elements.cons(n);
            return this;
        }

        public Builder add(double value) {
            elements.snoc(Real.valueOf(value));
            return this;
        }

        public Builder add(int value) {
            elements.snoc(Int.valueOf(value));
            return this;
        }

        public Builder add(boolean value) {
            elements.snoc(Bit.valueOf(value));
            return this;
        }

        public Builder add(Numeric numeric) {
            elements.snoc(numeric);
            return this;
        }

        public Builder addWithin(double value, Bounds bounds) {
            elements.snoc(Real.valueOf(value, bounds));
            return this;
        }

        public Builder addWithin(int value, Bounds bounds) {
            elements.snoc(Int.valueOf(value, bounds));
            return this;
        }

        public Builder copyOf(Iterable<Numeric> iterable) {
            elements.append(iterableList(iterable));
            return this;
        }

        public FList build() {
            return new FList(elements);
        }
    }
}
