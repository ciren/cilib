/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cilib.pso;

import fj.data.List;
import static net.cilib.predef.Predef.plus;

/**
 *
 * @author gpampara
 */
public class StandardPositionProvider extends PositionProvider {

    @Override
    public List<Double> f(List<Double> a, List<Double> b) {
        return plus(a, b);
    }
}
