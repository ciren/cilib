package net.cilib.algorithm;

import fj.F;
import fj.P2;
import fj.Show;
import fj.data.Array;
import org.junit.Test;

/**
 *
 * @author gpampara
 */
public class CrossoverProviderTest {

    @Test
    public void functionalZapp() {
        Array<Integer> a1 = Array.array(1, 2, 3, 4, 5);
        Array<Integer> a2 = Array.array(6, 7, 8, 9, 10);

        F<Integer, Integer> f = new F<Integer, Integer>() {
            @Override
            public Integer f(Integer a) {
                return a*2;
            }
        };
        Array<Integer> result = a2.apply(Array.array(f));

//        Show.arrayShow(Show.intShow).println(a1.zip(a2));
        Show.arrayShow(Show.p2Show(Show.intShow, Show.intShow)).println(a1.zip(a2));

        F<P2<P2<Integer, Integer>, Integer>, Integer> check = new F<P2<P2<Integer, Integer>, Integer>, Integer>() {
            @Override
            public Integer f(P2<P2<Integer, Integer>, Integer> a) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
//        a1.zip(a2).zipIndex().toStream().apply()
    }
}
