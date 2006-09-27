/*
 * BeliefSpace.java
 * 
 * Created on Aug 1, 2005
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
 */
package net.sourceforge.cilib.ec.culturalevolution;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import net.sourceforge.cilib.entity.Topology;

/**
 * @author otter
 *
 * In fact this is just a wrapper. The PopulationTopology is wrapped in order to give a better
 * more understandeble interface to the user. The only thing that changes is the method name.
 * Everything stays exactly the same.
 * TODO: Maybe you can wrap an individual into a belief???
 * For now remember that a belief is also an individual... because it can be anything and a individual can be anything
 * confusing?;-)
 * 
 * A PopulationTopology and a BeliefSpace has the exact same strucuture,function and meaning.
 * It's just confusing to use the method names of PopTop for a belief space as well.
 * The same argument can be followed with Individual and beliefs.
 * 
 * 
 * 
 * With the new generic Population class this can change to...
 * BeliefSpace is in fact a kind of Topology and must therefore inherit it's interface, but it's structure is similar
 * to PopulationTopology so it can just extend from StandardPopulation to make things easier.
 * 
 * StandardPopulation is sufficient for now
 * TODO: This doesn't feel very generic... Change it to something more generic.
 */
@Deprecated
public class BeliefSpace extends Topology<Belief> {

	@Override
	public Iterator<Belief> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean add(Belief ent) {
		// TODO Auto-generated method stub
		return false;
		
	}

	@Override
	public boolean remove(Belief indiv) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Belief get(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Belief set(int index, Belief indiv) {
		// TODO Auto-generated method stub
		return indiv;
	}

	@Override
	public void setAll(List<Belief> set) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Belief> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	/*@Override
	public Belief next() {
		// TODO Auto-generated method stub
		return null;
	}*/

	@Override
	public boolean contains(Object o) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean remove(Object o) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Object[] toArray() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T[] toArray(T[] a) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean addAll(int index, Collection<? extends Belief> c) {
		// TODO Auto-generated method stub
		return false;
	}

	public void add(int index, Belief element) {
		// TODO Auto-generated method stub
		
	}

	public Belief remove(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	public int indexOf(Object o) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int lastIndexOf(Object o) {
		// TODO Auto-generated method stub
		return 0;
	}

	public ListIterator<Belief> listIterator() {
		// TODO Auto-generated method stub
		return null;
	}

	public ListIterator<Belief> listIterator(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Belief> subList(int fromIndex, int toIndex) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String getId() {
		return null;
	}
	
	public void setId(String id) {
		
	}

	@Override
	public Iterator<Belief> neighbourhood(Iterator<Belief> iterator) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean addAll(Collection<? extends Belief> c) {
		// TODO Auto-generated method stub
		return false;
	}
	
	
}
