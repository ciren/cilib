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

class NicheParticle extends GCParticle {

  /**
   * The number of observations to calculate the standard the deviation with.
   **/
  int iObservations = 3;

  /**
   * The observations of the fitness, where the earliest observation is the
   * first element and the latest observation is the last element.
   */
  double dFitnessObservations[] = new double[iObservations];

  /**
   * The current observation.
   **/
  int iCurrentObservation = 0;

  public NicheParticle()
  {
    super();
  }

  public NicheParticle(int iNumberOfObservations)
  {
    super();
    iObservations = iNumberOfObservations;
    dFitnessObservations = new double[iObservations];
    iCurrentObservation = 0;
    reset();
  }

  public void addObservation(double fitness)
  {
    if (iCurrentObservation < iObservations)
    {
      dFitnessObservations[iCurrentObservation] = fitness;
      iCurrentObservation++;
    }
    else
    {
      // remove the 0 element from the array of observation
      // and move the 1..(iObservations-1) to 0..(iObservations-2)
      for (int i=1; i<iObservations; i++)
      {
        dFitnessObservations[i-1] = dFitnessObservations[i];
      }
      // copy the new fitness observation into the observation array at the last position.
      dFitnessObservations[iObservations-1] = fitness;
    }
  }

  public double calculateStandardDeviation()
  {
    if (iCurrentObservation < iObservations)
    {
      return -1.0;
    }

    // calculate the average.
    double average = 0.0;
    double total = 0.0;
    for (int i=0; i<iObservations; i++)
    {
      total += dFitnessObservations[i];
    }
    average = total/iObservations;

    // calculate the standard deviation.
    double sum = 0.0;
    for (int i=0; i<iObservations; i++)
    {
      sum += Math.pow(dFitnessObservations[i]-average, 2.0);
    }
    double c = 1.0/(iObservations-1.0);
    double variance = c*sum;
    double std_deviation = Math.pow(variance, 0.5);
    return std_deviation;
  }
}