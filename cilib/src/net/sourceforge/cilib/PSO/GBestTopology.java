/*
 * GBestTopology.java
 *
 * Created on January 17, 2003, 8:07 PM
 *
 *
 * Copyright (C) 2003, 2004 - CIRG@UP 
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

package net.sourceforge.cilib.PSO;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * <p>
 * Implementation of the gbest neighbourhood topology
 * </p><p>
 * References:
 * </p><p><ul><li>
 * R.C. Eberhart, P. Simpson, and R. Drobbins, "Computational Intelligence PC Tools,"
 * chapter 6, pp. 212-226. Academic Press Professional, 1996.
 * </li></ul></p>
 *
 * @author  espeer
 */
public class GBestTopology implements Topology {

    /**
     * Creates a new instance of <code>GBestTopology</code>.
     */
    public GBestTopology() {
        particles = new ArrayList();
    }
    
    public Iterator particles() {
        return new GBestTopologyIterator(this);
    }

    public Iterator neighbourhood(Iterator iterator) {
        return new GBestTopologyIterator(this);
    }
    
    public void add(Particle particle) {
        particles.add(particle);
    }

    public int getSize() {
        return particles.size();
    }

    protected ArrayList particles;

    protected interface ArrayIterator extends Iterator {
        public int getIndex();
    }

    private class GBestTopologyIterator implements ArrayIterator {

    	public GBestTopologyIterator(GBestTopology topology) {
            this.topology = topology;
            index = -1;
        }

        public int getIndex() {
            return index;
        }

        public boolean hasNext() {
            int lastIndex = topology.particles.size() - 1;
            return (index != lastIndex);
        }

        public Object next() {
            int lastIndex = topology.particles.size() - 1;
            if (index == lastIndex) {
                throw new NoSuchElementException();
            }

            ++index;

            return topology.particles.get(index);
        }

        public void remove() {
            if (index == -1) {
                throw new IllegalStateException();
            }

            topology.particles.remove(index);
            --index;
        }

        private int index;
        private GBestTopology topology;
    }
}

