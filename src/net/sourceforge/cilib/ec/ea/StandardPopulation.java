/*
 * StandardPopulation.java
 * 
 * Created on Jun 21, 2005
 * 
 * Copyright (C) 2003, 2004, 2005 - CIRG@UP 
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science 
 * University of Pretoria
 * South Africa
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * 
 */
package net.sourceforge.cilib.ec.ea;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.type.types.Vector;


/**
 * @author otter
 *
 * Extending the PopulationTopology interface.
 * This is a standard StandardPopulation implementation using an ArrayList as the data structure to host the individuals
 */
@Deprecated
public class StandardPopulation extends Topology<Individual> {
    
	protected String id; //for use in Co-evolution
	protected ArrayList<Individual> individuals;
    
	//FIXME: Evil hack to make the EP fits in EAOLD, together with the CloneCross and NextSelection thing...
    private int current = 0;

    
    /**
     * Creates a new instance of <code>StandardPopulation</code>.
     */
    public StandardPopulation() {
    	individuals = new ArrayList<Individual>();
    }

    public Iterator<Individual> iterator() {
    	return new PopulationIterator(this); 
    }
    
    public boolean add(Individual indiv) {
        return this.individuals.add(indiv);
    }
    
    public boolean addAll(Collection<? extends Individual> set) {
    	return this.individuals.addAll(set);
    }
    
    public void setAll(List<Individual> set) {
    	
    	if (set.size() != this.individuals.size()) {
    		this.individuals.clear();
    		this.individuals.ensureCapacity(set.size());
    		this.individuals.addAll(set);
    	}
    	else {  
    		int counter = 0;
    		for (Individual individual : set) {
    			this.individuals.set(counter, individual);
    			counter++;
    		}
    	}	
    	
    }
    
    public List<Individual> getAll() {
    	return this.individuals;
    }
    
    
    public boolean remove(Individual indiv) {
    	return this.individuals.remove(indiv);
    }

    public Individual get(int index) {
    	return this.individuals.get(index);
    }
    
    public Individual set(int index, Individual indiv) {
    	this.individuals.set(index,indiv);
    	return indiv;
    }
     
    public int size() {
        return this.individuals.size();
    }    
    
    public boolean isEmpty() {
    	return individuals.isEmpty();
    }
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

    /**
     * Important, this next is used when an Iterator can not be used. It's also important to note that
     * this method enforces wrap around...
     */
    public Individual next() { //based on a current index
        if(current >= this.size())
            current = 0;
        
        return this.individuals.get(current++);
    }    
    
    public Vector getCenter(int dimension) {
     /*   if (individuals.isEmpty()) {
            throw new RuntimeException("population has no individuals");
        }
        // calculate the center of the niche.
        Iterator iterator = individuals();
        double[] center = new double[dimension];

        // find the best individual in the population.
        Individual bestIndividual = (Individual) iterator.next();
        Gene[] chromosome = bestIndividual.getChromosome();
        for (int i = 0; i < chromosome.length; i++) {
            center[i] = chromosome[i].getGeneValue();
        }
        while (iterator.hasNext()) {
            // get the individual from the niche.
            Individual individual = (Individual) iterator.next();
            if (individual.getBestFitness().compareTo(bestIndividual.getBestFitness()) > 0) {
                bestIndividual = individual;

                // add the contribution of the individual to the center of the niche.
                chromosome = bestIndividual.getChromosome();
                for (int i = 0; i < chromosome.length; i++) {
                    center[i] = chromosome[i].getGeneValue();
                }
            }
        }
*/
        return null;
    }    
    

	/**
	 * @author otter
	 * 
	 * Stuff for the Iterator needed to traverse this population implementation...
	 */
    protected interface ArrayListIterator extends Iterator<Individual> {
        public int getIndex();
    }

    /**
     * @author otter
     *
     * Stuff for the Iterator needed to traverse this population implementation...
     */
    private class PopulationIterator implements ArrayListIterator {

    	public PopulationIterator(StandardPopulation topology) {
            this.poptop = topology;
            index = -1;
        }

        public int getIndex() {
            return index;
        }

        public boolean hasNext() {
            int lastIndex = this.poptop.individuals.size() - 1;
            return (index != lastIndex);
        	//return false;
        }

        public Individual next() {
            int lastIndex = poptop.individuals.size() - 1;
            if (index == lastIndex) {
                throw new NoSuchElementException();
            }

            ++index;
            //TODO:???? Moenie eintlik hier cast nie...
            return poptop.individuals.get(index);
        }

        public void remove() {
            if (index == -1) {
                throw new IllegalStateException();
            }

            poptop.individuals.remove(index);
            --index;
        }

        private int index;
        private StandardPopulation poptop;
    }

    
    
    
    
    
    
	@Override
	public void clear() {
		this.individuals.clear();
	}

	@Override
	public boolean contains(Object o) {
		return this.individuals.contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return this.individuals.containsAll(c);
	}

	@Override
	public boolean equals(Object o) {
		return this.individuals.equals(o);
	}

	@Override
	public int hashCode() {
		return this.individuals.hashCode();
	}

	@Override
	public boolean remove(Object o) {
		return this.individuals.remove(o);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return this.individuals.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return this.individuals.retainAll(c);
	}

	@Override
	public Object[] toArray() {
		return this.individuals.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return this.individuals.toArray(a);
	}

	public boolean addAll(int index, Collection<? extends Individual> c) {
		return this.individuals.addAll(index, c);
	}

	public void add(int index, Individual element) {
		this.individuals.add(index, element);		
	}

	public Individual remove(int index) {
		return this.individuals.remove(index);
	}

	public int indexOf(Object o) {
		return this.indexOf(o);
	}

	public int lastIndexOf(Object o) {
		return this.lastIndexOf(o);
	}

	public ListIterator<Individual> listIterator() {
		return this.individuals.listIterator();
	}

	public ListIterator<Individual> listIterator(int index) {
		return this.individuals.listIterator(index);
	}

	public List<Individual> subList(int fromIndex, int toIndex) {
		return this.individuals.subList(fromIndex, toIndex);
	}
	
	
	public Iterator<Individual> neighbourhood(Iterator<Individual> iterator) {
		return null;
	}
}
