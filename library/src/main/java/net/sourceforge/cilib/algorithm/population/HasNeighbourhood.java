package net.sourceforge.cilib.algorithm.population;

import net.sourceforge.cilib.entity.topologies.Neighbourhood;

public interface HasNeighbourhood<E> {

//    public void setNeighbourhood(Neighbourhood<E> f) {
//    	this.neighbourhood = f;
//    }

    Neighbourhood<E> getNeighbourhood();
}
