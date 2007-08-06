/*
 * MutatingParticle.java
 * 
 * Created on Jun 20, 2005
 *
 * Copyright (C) 2003 - 2006 
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
package net.sourceforge.cilib.pso.particle;

import java.util.Iterator;
import java.util.Vector;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.stoppingcondition.MaximumIterations;
import net.sourceforge.cilib.stoppingcondition.StoppingCondition;
import net.sourceforge.cilib.type.types.Real;

/**
 * @author Auralia Edwards
 * @deprecated 25/01/2006
 */
public class MutatingParticle extends StandardParticle {
	private static final long serialVersionUID = -135676600466549570L;
	
	private double mutationRate;
	private double startingMutationRate;
	private double endingMutationRate;

	private MaximumIterations maximum;

	public MutatingParticle() {
		super();

		startingMutationRate = 0.9;
		endingMutationRate = 0.9;
		mutationRate = startingMutationRate;
		maximum = null;
	}

	public void move() {
		
		net.sourceforge.cilib.type.types.container.Vector position = getPosition();
		net.sourceforge.cilib.type.types.container.Vector velocity = getVelocity();
		
		for (int i = 0; i < position.getDimension(); ++i) {
			double result = position.getReal(i) + velocity.getReal(i);
			position.setReal(i, result);
		}

		if (maximum == null)
			getStoppingConditionObjects();

		if (((PSO) Algorithm.get()).getIterations() < mutationRate*(double)maximum.getMaximumIterations())
			mutate();

	}

	private void getStoppingConditionObjects() {
		Vector<StoppingCondition> vector = (Algorithm.get()).getStoppingConditions();
		for (Iterator<StoppingCondition> i = vector.iterator(); i.hasNext(); ) {
			StoppingCondition condition = i.next();
			if (condition instanceof MaximumIterations) {
				maximum = (MaximumIterations) condition;
				break;
			}
		}
	}

	public void setStartingMutationRate(double newRate) {
		this.startingMutationRate = newRate;
		this.mutationRate = newRate;
	}

	public double getStartingMutationRate() {
		return this.startingMutationRate;
	}

	public void setEndingMutationRate(double newRate) {
		this.endingMutationRate = newRate;
	}

	public double getEndingMutationRate() {
		return this.endingMutationRate;
	}

	/*private void mutate() {
		PSO p = (PSO) Algorithm.get();
		OptimisationProblem prob = p.getOptimisationProblem();
		DomainComponent domain = prob.getDomain();

		double tempLower = 0.0;
		double tempUpper = 0.0;
		
		for (int i = 0; i < position.length; ++i) { // Mutation
			double number = strangeFunction(p, maximum);
		 	double r = ran.nextLorentzian(0.0,1.000); 
		while (r > 1 || r < 0){
			r = ran.nextLorentzian(0.0,1.000); 
		}
			if (r >=0.5){	
				
			int dimension = Double.valueOf(random.nextDouble()*position.length).intValue();
				Quantitative component = (Quantitative) domain.getComponent(dimension);
				double range = ((component.getUpperBound().doubleValue() - component.getLowerBound().doubleValue())* strangeFunction(p, maximum))/2;

				if ((position[i] - range) < component.getLowerBound().doubleValue())
					tempLower = component.getLowerBound().doubleValue();
				else
					tempLower = position[i] - range;

				if ((position[i] + range) > component.getUpperBound().doubleValue())
					tempUpper = component.getUpperBound().doubleValue();
				else
					tempUpper = position[i] + range;

				// Now reinitialis the number randomly between tempUpper and tempLower
			r = ran.nextLorentzian(0.0,1.000); 
		    while (r > 1 || r < 0){
			   r = ran.nextLorentzian(0.0,1.000); 
		    }
		   double tmp = r * (tempUpper-tempLower) + tempLower;
			 position[i] = tmp;
			// if (random.nextDouble() > 0.5) {
			//	position[i] += function(p.getIterations(),tmp);
			//	}
			//	else {
			//	position[i] -= function(p.getIterations(),tmp);
			//}
			//position[i] = tmp;
		}
		}
		
		mutationRate = (startingMutationRate - endingMutationRate) * (((double) maximum.getIterations() - p.getIterations()) / (double) maximum.getIterations())+endingMutationRate ;

	}*/

	private double strangeFunction(PSO p, MaximumIterations max) {
//		System.out.println();
		return Math.pow(1.0 - (double)p.getIterations()/(max.getMaximumIterations()*mutationRate),1.5);
	}

	/*private double function(double t, double y) {
		//double r = ran.nextLorentzian(0.0,1.000); 
		//while (r > 1 || r < 0){
		//	r = ran.nextLorentzian(0.0,1.000); 
		//}
		double r = random.nextDouble();
		double part1 = Math.pow((1.0-t/(double)maximum.getIterations()), 5.0);
		double part2 = Math.pow(r, part1);
		double part3 = 1.0 - part2;
		double result = y * part3;
		//System.out.println(result);
		return result;
	}*/

	/*	private double getCauchyNumber(double val) {
			double rand = 0;
	//while (rand > 1 | rand < 0){//System.out.println("R "+r);
	//      rand = ran.nextLorentzian(1.0,0.0001); 
	//}
	return ((0.2/Math.PI) * (1.0 / ((rand*rand)+(0.2*0.2))));
	}*/

/*************************** NEW CODE ***********************************/
	
	
	private void mutate() {
		PSO p = (PSO) Algorithm.get();
		//OptimisationProblem prob = p.getOptimisationProblem();
		//DomainRegistry domain = prob.getDomain();

		double tempLower = 0.0;
		double tempUpper = 0.0;
		
		net.sourceforge.cilib.type.types.container.Vector position = getPosition();
		
		//for (int i = 0; i < position.length; ++i) { // Mutation
		for (int i = 0; i < position.getDimension(); ++i) {
			double number = Math.pow((1.0 - (double)p.getIterations()/(maximum.getMaximumIterations()*mutationRate)),1.5);
			if (flip(number) == 1)
			{
				int dimension = RandomInt(0, position.getDimension());
				//Quantitative component = (Quantitative) domain.getComponent(dimension);
				Real real = (Real) position.get(dimension);
				//double range = ((component.getUpperBound().doubleValue() - component.getLowerBound().doubleValue())* strangeFunction(p, maximum))/2;
				double range = ((real.getUpperBound() - real.getLowerBound())* strangeFunction(p, maximum))/2.0;

				/*if ((position[i] - range) < component.getLowerBound().doubleValue())
					tempLower = component.getLowerBound().doubleValue();
				else
					tempLower = position[i] - range;

				if ((position[i] + range) > component.getUpperBound().doubleValue())
					tempUpper = component.getUpperBound().doubleValue();
				else
					tempUpper = position[i] + range;*/
				
				if ((real.getReal()-range) < real.getLowerBound())
					tempLower = real.getLowerBound();
				else
					tempLower = real.getReal()-range;
				
				if ((real.getReal()+range) > real.getUpperBound())
					tempUpper = real.getUpperBound();
				else
					tempUpper = real.getReal()+range;

				// Now reinitialis the number randomly between tempUpper and tempLower
				double tmp = RandomDouble(tempLower,tempUpper);
				//position[i] = tmp;
				position.setReal(i, tmp);
			}
		}
		
		mutationRate = (startingMutationRate - endingMutationRate) * (((double) maximum.getMaximumIterations() - p.getIterations()) / (double) maximum.getMaximumIterations())+endingMutationRate ;

	}

	double u [] = new double[97];
	double c,cd,cm;
	int i97,j97;
	boolean test = false;

/*
   This is the initialization routine for the random number generator.
   NOTE: The seed variables can have values between:    0 <= IJ <= 31328
                                                        0 <= KL <= 30081
   The random number sequences created by these two seeds are of sufficient
   length to complete an entire calculation with. For example, if sveral
   different groups are working on different parts of the same calculation,
   each group could be assigned its own IJ seed. This would leave each group
   with 30000 choices for the second seed. That is to say, this random
   number generator can create 900 million different subsequences -- with
   each subsequence having a length of approximately 10^30.
*/
void RandomInitialise(int ij,int kl)
{
   double s,t;
   int ii,i,j,k,l,jj,m;

   /*
      Handle the seed range errors
         First random number seed must be between 0 and 31328
         Second seed must have a value between 0 and 30081
   */
   if (ij < 0 || ij > 31328 || kl < 0 || kl > 30081) {
      ij = 1802;
      kl = 9373;
   }

   i = (ij / 177) % 177 + 2;
   j = (ij % 177)       + 2;
   k = (kl / 169) % 178 + 1;
   l = (kl % 169);

   for (ii=0; ii<97; ii++) {
      s = 0.0;
      t = 0.5;
      for (jj=0; jj<24; jj++) {
         m = (((i * j) % 179) * k) % 179;
         i = j;
         j = k;
         k = m;
         l = (53 * l + 1) % 169;
         if (((l * m % 64)) >= 32)
            s += t;
         t *= 0.5;
      }
      u[ii] = s;
   }

   c    = 362436.0 / 16777216.0;
   cd   = 7654321.0 / 16777216.0;
   cm   = 16777213.0 / 16777216.0;
   i97  = 97;
   j97  = 33;
   test = true;
}

/*
   This is the random number generator proposed by George Marsaglia in
   Florida State University Report: FSU-SCRI-87-50
*/
double RandomUniform()
{
   double uni;

   /* Make sure the initialisation routine has been called */
   if (!test)
      RandomInitialise(1802,9373);

   uni = u[i97-1] - u[j97-1];
   if (uni <= 0.0)
      uni++;
   u[i97-1] = uni;
   i97--;
   if (i97 == 0)
      i97 = 97;
   j97--;
   if (j97 == 0)
      j97 = 97;
   c -= cd;
   if (c < 0.0)
      c += cm;
   uni -= c;
   if (uni < 0.0)
      uni++;

   return(uni);
}

/*
  ALGORITHM 712, COLLECTED ALGORITHMS FROM ACM.
  THIS WORK PUBLISHED IN TRANSACTIONS ON MATHEMATICAL SOFTWARE,
  VOL. 18, NO. 4, DECEMBER, 1992, PP. 434-435.
  The function returns a normally distributed pseudo-random number
  with a given mean and standard devaiation.  Calls are made to a
  function subprogram which must return independent random
  numbers uniform in the interval (0,1).
  The algorithm uses the ratio of uniforms method of A.J. Kinderman
  and J.F. Monahan augmented with quadratic bounding curves.
*/
double RandomGaussian(double mean,double stddev)
{
   double  q,u,v,x,y;

   /*
      Generate P = (u,v) uniform in rect. enclosing acceptance region
      Make sure that any random numbers <= 0 are rejected, since
      gaussian() requires uniforms > 0, but RandomUniform() delivers >= 0.
   */
   do {
      u = RandomUniform();
      v = RandomUniform();
      if (u <= 0.0 || v <= 0.0) {
          u = 1.0;
          v = 1.0;
      }
      v = 1.7156 * (v - 0.5);

      /*  Evaluate the quadratic form */
      x = u - 0.449871;
      y = Math.abs(v) + 0.386595;
      q = x * x + y * (0.19600 * y - 0.25472 * x);

      /* Accept P if inside inner ellipse */
      if (q < 0.27597)
         break;

      /*  Reject P if outside outer ellipse, or outside acceptance region */
    } while ((q > 0.27846) || (v * v > -4.0 * Math.log(u) * u * u));

    /*  Return ratio of P's coordinates as the normal deviate */
    return (mean + stddev * v / u);
}

/*
   Return random integer within a range, lower -> upper INCLUSIVE
*/
int RandomInt(int lower, int upper)
{
   return((int)(RandomUniform() * (upper - lower + 1)) + lower);
}

/*
   Return random float within a range, lower -> upper
*/
double RandomDouble(double lower, double upper)
{
   return((upper - lower) * RandomUniform() + lower);
}


int  flip(double pf){
  if(RandomDouble(0.0,1.0)<=pf)return 1;else return 0;
}
}
















