/* 
 * NichePSO.java
 *
 *
 * Copyright (C) 2003 - Clive Naicker
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
import net.sourceforge.cilib.Algorithm.InitialisationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class NicheTopology extends GBestTopology {

  /**
   * Default constructor.  Particles are not intialised.
   */
  public NicheTopology()
  {
    super();
  }

  public void initialise(Class particleClass) {
    int size = this.getSize();
    particles = new Particle[size];
    for (int i = 0; i < size; ++i) {
      try {
        //System.out.println("NicheTopology.initialise(Class) : " +
        //                   "particleClass[" + i + "] = " + particleClass.getName());
        particles[i] = (NicheParticle) particleClass.newInstance();
      }
      catch (Exception e) {
        System.err.println("NicheTopology.initialise(Class) : " + e);
        throw new InitialisationException("Could not instantiate particle");
      }
    }
    //System.out.println("NicheTopology.initialise(Class) : Completed...");
  }

  public Particle findClosestNeighbor(Particle p)
  {
    double smallestDistance = Double.MAX_VALUE;
    Particle p2 = null;
    // loop through the particles and find the closes particle to p.
    for (int i=0; i<particles.length; i++)
    {
      if (p != particles[i])
      {
        double distance = distance(p.getPosition(), particles[i].getPosition());
        if (distance < smallestDistance)
        {
          smallestDistance = distance;
          p2 = particles[i];
        }
      }
    }
    return p2;
  }

  private double distance(double p1[], double p2[]) {
    // Calculate the sum of the squares of the difference of each dimension.
    double sum = 0.0;
    for (int i=0; i<p1.length; i++)
    {
      sum += Math.pow(p1[i]-p2[i], 2.0);
    }
    // The distance is the square root of the of the sum.
    double distance = Math.sqrt(sum);
    return distance;
  }

  /**
   * Remove the particle from the Topology.  The particle is compared by
   * reference.  If the particle p has any references to the particles[], then
   * that particle is removed from the array.
   * @param p The particle to remove.  Particles are compared by reference.
   * @return boolean If the particle was successfully removed from the Topology,
   * true is returned, otherewise false.
   */
  public boolean removeParticle(Particle p)
  {
    for (int i=0; i<particles.length; i++)
    {
      if (particles[i] == p)
      {
        // remove the particle p from the array.
        return removeParticle(i);
      }
    }
    // The particle was not found in the Topology, so it was not removed.
    return false;
  }

  public boolean addParticle(NicheParticle p)
  {
    if (particles == null)
    {
      System.out.println("NicheTopology.addParticle() : particles == null");
    }
    // create a temporary array of particles.
    NicheParticle particles_t[] = new NicheParticle[particles.length+1];
    // add all the particles from the array particles[] to the array particles_t[].
    for (int i=0; i<particles.length; i++)
    {
      particles_t[i] = (NicheParticle)particles[i];
    }
    // add the new particle to the array particles_t[].
    particles_t[particles.length] = p;
    // set the particle[] to point to the temporary array of particles.
    particles = particles_t;
    // set the new size of the Topology.
    setSize(particles.length);
    return true;
  }

  private boolean removeParticle(int index)
  {
    if (index < 0 || index >= particles.length)
    {
      // index out of range.
      return false;
    }

    // try to remove the particle.
    try
    {
      // create a temporary array of particles.
      Particle particles_t[] = new Particle[particles.length-1];
      // add all the particles from the array particles[] except particles[index].
      int j = 0;
      for (int i=0; i<particles.length; i++)
      {
        if (i != index)
        {
          // particles are copied by reference.
          particles_t[j] = particles[i];
          j++;
        }
      }
      // set the particles[] to point to the particles_t[]
      particles = particles_t;
      // set the new size of the Topology.
      setSize(particles.length);
      return true;
    }
    catch(Exception e)
    {
      System.err.println("NicheTopology.removeParticle(int) : " + e);
      return false;
    }
  }

  public Iterator particles() {
      return new NicheTopologyIterator(this);
  }

  private class NicheTopologyIterator implements ArrayIterator {

    public NicheTopologyIterator(NicheTopology topology) {
      this.topology = topology;
      index = -1;
    }

    public int getIndex() {
      return index;
    }

    public boolean hasNext() {
      // System.out.println("NicheTopologyIterator.hasNext() : index = " + index);
      // System.out.println("NicheTopologyIterator.hasNext() : topology.size = " + topology.getSize());
      // System.out.println("NicheTopologyIterator.hasNext() : particles.length = "
      //                                          + topology.particles.length);
      if (index >= -1 && index < topology.getSize()-1)
      {
        // System.out.println("NicheTopologyIterator.hasNext() : return true");
        return true;
      }
      else
      {
        // System.out.println("NicheTopologyIterator.hasNext() : return false");
        return false;
      }
    }

    public Object next() {
      try
      {
        if (index < -1 || index >= topology.getSize()-1) {
          System.out.println("NicheTopologyIterator.next() : index = " + index);
          System.out.println("NicheTopologyIterator.next() : topology.size = " + topology.getSize());
          System.out.println("NicheTopologyIterator.next() : particles.length = "
                                                     + topology.particles.length);
          throw new NoSuchElementException();
        }
        ++index;
        return topology.particles[index];
      }
      catch (Exception e)
      {
        System.out.println("NicheTopologyIterator.next() : index = " + index);
        System.out.println("NicheTopologyIterator.next() : topology.size = " + topology.getSize());
        System.out.println("NicheTopologyIterator.next() : particles.length = "
                                                    + topology.particles.length);
        System.err.println(e);
        e.printStackTrace();
        System.exit(-1);
      }
      return null;
    }

    public void remove() {
      throw new UnsupportedOperationException();
    }

    private int index;
    private NicheTopology topology;
  }

}