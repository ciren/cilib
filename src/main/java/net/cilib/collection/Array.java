package net.cilib.collection;

import com.google.common.collect.Lists;
import com.google.common.collect.UnmodifiableIterator;
import com.google.common.primitives.Doubles;
import fj.Function;
import fj.F2;
import fj.Unit;
import fj.F;
import static java.lang.Math.*;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * This is an attempt to create a lazy-array
 *
 * @author gpampara
 */
public final class Array extends Seq {

    private final double[] a;

    public static Array array(double[] d) {
        return new Array(d);
    }

    public static Array array(Array delegate) {
        double[] b = new double[delegate.length()];
        System.arraycopy(delegate.a, 0, b, 0, b.length);
        return new Array(b);
    }

    public static Array empty() {
        return new Array(new double[0]);
    }

    private Array(double[] array) {
        this.a = array;
    }

    public int length() {
        return a.length;
    }

    private double get(int index) {
        return a[index];
    }

    private Unit set(int index, double b) {
        a[index] = b;
        return Unit.unit();
    }

    public Array plus(Seq other) {
        return zipWith(other.delegate(), new F2<Double, Double, Double>() {

            @Override
            public Double f(Double a, Double b) {
                return a + b;
            }
        });
    }

    public Array subtract(Seq other) {
        return zipWith(other.delegate(), new F2<Double, Double, Double>() {

            @Override
            public Double f(Double a, Double b) {
                return a - b;
            }
        });
    }

    public Array zipWith(final Array bs, final F<Double, F<Double, Double>> f) {
        final int len = min(a.length, bs.length());
        final Array x = new Array(new double[len]);

        for (int i = 0; i < len; i++) {
            x.set(i, f.f(get(i)).f(bs.get(i)));
        }

        return x;
    }

    public Array zipWith(final Array bs, final F2<Double, Double, Double> f) {
        return zipWith(bs, Function.curry(f));
    }

    public Array map(final F<Double, Double> f) {
        final double[] bs = new double[a.length];

        for (int i = 0; i < a.length; i++) {
            bs[i] = f.f(a[i]);
        }

        return new Array(bs);
    }

    public double[] copyOfInternal() {
        double[] b = new double[a.length];
        System.arraycopy(a, 0, b, 0, a.length);
        return b;
    }

    public Iterator<Double> iterator() {
        return new UnmodifiableIterator<Double>() {

            private final double[] inner = copyOfInternal();
            private int i = 0;

            @Override
            public boolean hasNext() {
                return i < inner.length;
            }

            @Override
            public Double next() {
                return inner[i++];
            }
        };
    }

    public boolean isEmpty() {
        return a.length == 0;
    }

    public Array take(int n) {
        final ArrayBuffer b = new ArrayBuffer();
        int i = 0;
        Array these = this;

        while (!these.isEmpty() && i < n) {
            b.append(these.get(i));
            these = these.tail();
        }
        if (these.isEmpty()) {
            return this;
        } else {
            return b.toArray();
        }
    }

    public Array drop(int n) {
        Array these = this;
        int count = n;

        while (!these.isEmpty() && count > 0) {
            these = these.tail();
            count -= 1;
        }

        return these;
    }

    public Array tail() {
        double[] tail = new double[a.length - 1];
        System.arraycopy(a, 1, tail, 0, tail.length);
        return new Array(tail);
    }

    public double head() {
        return get(0);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Array)) {
            return false;
        }

        Array a2 = (Array) obj;
        return Arrays.equals(a, a2.a);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(a);
    }

    @Override
    public String toString() {
        return Arrays.toString(a);
    }

    @Override
    protected Array delegate() {
        return this;
    }

    private static class ArrayBuffer {

        private final List<Double> buffer = Lists.newArrayList();

        private void append(double d) {
            buffer.add(d);
        }

        private Array toArray() {
            return Array.array(Doubles.toArray(buffer));
        }
    }
}
