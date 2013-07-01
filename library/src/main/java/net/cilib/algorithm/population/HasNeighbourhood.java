package net.cilib.algorithm.population;

import net.cilib.entity.topologies.Neighbourhood;

public interface HasNeighbourhood<E> {

    Neighbourhood<E> getNeighbourhood();
}
