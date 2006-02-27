/*
 * VonNeumannTopology.java
 *
 * Created on January 18, 2003, 10:42 AM
 *
 * 
 * Copyright (C) 2003 - Edwin S. Peer
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
 *
 *    
 */

package net.sourceforge.cilib.PSO;

import java.lang.*;
import java.util.*;
import net.sourceforge.cilib.Algorithm.*;

/**
 * <p>
 * Implementation of the Von Neumann neighbourhood topology. The Von Neumann topology is 
 * a two dimensional grid of particles with wrap around.
 * </p><p>
 * Refereces:
 * </p><p><ul><li>
 * J. Kennedy and R. Mendes, "Population structure and particle swarm performance,"
 * in Proceedings of the IEEE Congress on Evolutionary Computation,
 * (Honolulu, Hawaii USA), May 2002.
 * </li></ul></p>
 *
 * @author  espeer
 */
public class VonNeumannTopology implements Topology {
    
    /**
     * Creates a new instance of <code>VonNeumannTopology</code>.
     */
    public VonNeumannTopology() {
        width = 5;
        height = 4;
    }
    
    public void initialise(Class particleClass) {
        particles = new Particle[width][height];
        for (int i = 0; i < width; ++i) {
            for (int j = 0; j < height; ++j) {
                try {
                    particles[i][j] = (Particle) particleClass.newInstance();
                }
                catch (Exception e) {
                    throw new InitialisationException("Could not instantiate particle");
                }
            }
        }
    }
    
    /** 
     * Sets the number of particles in the topology. The optimimal width and height
     * is determined to produce a grid of particles. Alternatively, use 
     * {@link #setWidth(int)} and {@link #setHeight(int)} to set the size. 
     * The default size is 20 particles.
     *
     * @param size The size of the topology.
     */
    public void setSize(int size) {
        height = (int) Math.sqrt(size);
        while((size % height) != 0) {
            --height;
        }
        width = size / height;
    }
    
    /**
     * Sets the width of the topology. The default is 5.
     *
     * @param width The width of the topology.
     */
    public void setWidth(int width) {
        this.width = width;
    }
    
    /**
     * Sets the height of the topology. The default is 4.
     *
     * @param height The height of the topology.
     */
    public void setHeight(int height) {
        this.height = height;
    }
    
    public Iterator neighbourhood(Iterator iterator) {
        MatrixIterator i = (MatrixIterator) iterator;
        return new VonNeumannNeighbourhoodIterator(this, i);
    }
    
    public Iterator particles() {
        return new VonNeumannTopologyIterator(this);
    }
    
    private Particle[][] particles;
    private int width;
    private int height;
    
    private interface MatrixIterator extends Iterator {
        public int getX();
        public int getY();
    }
    
    private class VonNeumannTopologyIterator implements MatrixIterator {
        
        public VonNeumannTopologyIterator(VonNeumannTopology topology) {
            this.topology = topology;
            x = -1;
            y = 0;
            lastX = topology.width - 1;
            lastY = topology.height - 1;
        }
        
        public boolean hasNext() {
            return (x != lastX || y != lastY);
        }
        
        public Object next() {
            if (x == lastX && y == lastY) {
                throw new NoSuchElementException();
            }
            ++x;
            if (x == topology.width) {
                x = 0;
                ++y;
            }
            return topology.particles[x][y];
        }
        
        public void remove() {
            throw new UnsupportedOperationException();
        }
        
        public int getX() {
            return x;
        }
        
        public int getY() {
            return y;
        }
        
        private int x;
        private int y;
        private int lastX;
        private int lastY;
        private VonNeumannTopology topology;
    }
    
    private class VonNeumannNeighbourhoodIterator implements MatrixIterator {
        
        public VonNeumannNeighbourhoodIterator(VonNeumannTopology topology, MatrixIterator iterator) {
            if (iterator.getX() == -1) {
                throw new IllegalStateException();
            }
            this.topology = topology;
            x = iterator.getX();
            y = iterator.getY();
            index = 0;
        }
        
        public boolean hasNext() {
            return (index != 5);
        }
        
        public Object next() {
            if (index == 5) {
                throw new NoSuchElementException();
            }
            x = (x + deltaX[index] + topology.width) % topology.width;
            y = (y + deltaY[index] + topology.height) % topology.height;
            ++index;
            return topology.particles[x][y];
        }
        
        public void remove() {
            throw new UnsupportedOperationException();
        }
        
        public int getX() {
            return x;
        }
        
        public int getY() {
            return y;
        }
        
        private int x;
        private int y;
        private int index;
        private VonNeumannTopology topology;
        private int[] deltaX = {0, -1, 1,  1, -1};
        private int[] deltaY = {0,  0, 1, -1, -1};
    }
}
