package net.sourceforge.cilib.algorithm.population;

import net.sourceforge.cilib.entity.topologies.Neighbourhood;

public interface HasNeighbourhood<E> {

    Neighbourhood<E> getNeighbourhood();
}
