package net.cilib.predef;

import fj.data.List;
import com.google.common.base.Supplier;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Test;
import static net.cilib.predef.Predef.*;

/**
 *
 */
public class PredefTest {

    @Test
    public void addition() {
        List<Double> solution = solution(1.0, 3.0);
        List<Double> result = plus(solution, solution); // z = x + y

        Assert.assertTrue(Iterables.elementsEqual(result, Lists.newArrayList(2.0, 6.0)));
    }

    @Test
    public void subtraction() {
        List<Double> solution = solution(1.0, 3.0);
        List<Double> result = subtract(solution, solution); // z = x - y

        Assert.assertTrue(Iterables.elementsEqual(result, Lists.newArrayList(0.0, 0.0)));
    }

    @Test
    public void multiplication() {
        List<Double> solution = solution(1.0, 3.0);
        List<Double> result = multiply(2.0, solution); // z = x * y

        Assert.assertTrue(Iterables.elementsEqual(Lists.newArrayList(2.0, 6.0), result));
    }

    @Test
    public void multiplySupplier() {
        List<Double> solution = solution(1.0, 3.0);
        Supplier<Double> supplier = new Supplier<Double>() {
            private double value = 1.0;

            @Override
            public Double get() {
                value *= 2.0;
                return value;
            }
        };

        List<Double> result = multiply(supplier, solution);

        Assert.assertTrue(Iterables.elementsEqual(Lists.newArrayList(2.0, 12.0), result));
    }

    @Test(expected = ArithmeticException.class)
    public void illegalDivide() {
        List<Double> solution = solution(1.0);
        divide(0.0, solution);
    }

    @Test
    public void division() {
        List<Double> solution = solution(1.0, 3.0);
        List<Double> result = divide(1.0, solution); // z = x / y

        Assert.assertTrue(Iterables.elementsEqual(Lists.newArrayList(1.0, 3.0), result));
    }

    @Test
    public void divideSupplier() {
        List<Double> solution = solution(1.0, 3.0);
        List<Double> result = divide(new Supplier<Double>() {
            @Override
            public Double get() {
                return 1.0;
            }
        }, solution); // z = x / y

        Assert.assertTrue(Iterables.elementsEqual(Lists.newArrayList(1.0, 3.0), result));
    }

    @Test
    public void complexFunctionalOperation() {
        List<Double> solution = solution(1.0, 2.0);
        List<Double> result = plus(multiply(4.0, solution), solution);

        Assert.assertTrue(Iterables.elementsEqual(Lists.newArrayList(5.0, 10.0), result));
    }
}
