/*
 * MovingPeaks.java
 *
 * Created on 17 May 2006, 11:45
 * 
 * Copyright (C) 2004 - CIRG@UP 
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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 
 */
package net.sourceforge.cilib.functions.continuous.dynamic;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.functions.DynamicFunction;
import net.sourceforge.cilib.math.random.RandomNumber;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * @author csbmcd
 * @author Anna Rakitianskaia
 */
public class MovingPeaks extends ContinuousFunction implements DynamicFunction {
	private static final long serialVersionUID = 733952126255493620L;

	/** Creates a new instance of MovingPeaks */
	public MovingPeaks() {
		setDomain("R(0, 100)^5");
		exclusionthreshold = (maxcoordinate - mincoordinate) / (2 * (Math.pow(number_of_peaks, 1 / getDimension())));
		init_peaks();
	}
	
	public MovingPeaks getClone() {
		return new MovingPeaks();
	}

	public Object getMinimum() {
		return new Double(0);
	}

	public Object getMaximum() {
		return new Double(maxheight);
	}

	public double evaluate(Vector x) {
		double[] elements = new double[getDimension()];

		for (int i = 0; i < getDimension(); ++i) {
			elements[i] = x.getReal(i);

		}
		return eval_movpeaks(elements);
	}

	/** *** PARAMETER SETTINGS **** */

	/*
	 * number of evaluations between changes. change_frequency =0 means that function never changes
	 * (or only if function change_peaks is called) Scenario 1: 5000
	 */
	private int change_frequency = 0;

	public void setChangeFrequency(int cfr) {
		change_frequency = cfr;
	}

	public int getChangeFrequency() {
		return change_frequency;
	}

	/*
	 * seed for built-in random number generator
	 */
	@SuppressWarnings("unused")
	private long movrandseed = 1;

	/*
	 * number of dimensions, or the number of double valued genes Scenerio 1: 5
	 */

	/*
	 * distance by which the peaks are moved, severity Scenario 1: 1.0
	 */
	private double vlength = 1.0;

	/*
	 * severity of height changes, larger numbers mean larger severity Scenario 1: 7.0
	 */
	private double height_severity = 7.0;

	/*
	 * severity of width changes, larger numbers mean larger severity
	 */
	private double width_severity = 1;

	/*
	 * Random number generation
	 */
	private RandomNumber randomNumberGenerator = new RandomNumber();

	private double movrand() {
		return randomNumberGenerator.getUniform();
	}

	private double movnrand() {
		return randomNumberGenerator.getNormal();
	}

	/*
	 * lambda determines whether there is a direction of the movement, or whether they are totally
	 * random. For lambda = 1.0 each move has the same direction, while for lambda = 0.0, each move
	 * has a random direction
	 */
	protected double lambda = movrand();

	/*
	 * number of peaks in the landscape Scenario 1: 5
	 */
	private int number_of_peaks = 10;

	/*
	 * if set to 1, a static landscape (basis_function) is included in the fitness evaluation
	 */
	protected boolean use_basis_function = false;

	/* saves computation time if not needed and set to 0 */
	private boolean calculate_average_error = true; // int calculate_average_error = 1;

	/* saves computation time if not needed and set to 0 */
	private boolean calculate_offline_performance = true;
	// int calculate_offline_performance = 1;

	/* saves computation time if not needed and set to 0 */
	private boolean calculate_right_peak = true; // int calculate_right_peak = 1;

	/*
	 * minimum and maximum coordinate in each dimension Scenario 1: 0.0 and 100.0
	 */
	private double mincoordinate = 0.0;
	private double maxcoordinate = 100.0;

	/*
	 * minimum and maximum height of the peaks Scenario 1: 30.0 and 70.0
	 */

	private double minheight = 30.0;
	private double maxheight = 70.0;

	/*
	 * height chosen randomly when standardheight = 0.0 Scenario 1: 50.0
	 */
	private double standardheight = 50.0;

	/*
	 * Scenario 1: 0.0001
	 */
	private double minwidth = 1;

	/*
	 * Scenario 1: 0.2
	 */
	private double maxwidth = 12;

	/*
	 * width chosen randomly when standardwidth = 0.0 Scenario 1: 0.1
	 */
	private double standardwidth = 0.1;

	public Peak_Function pf = new Peak_Function_Cone(); // Scenario 1

	public Basis_Function bf = new Constant_Basis_Function();

	/*
	 * Value for the exclusion threshold
	 */

	private double exclusionthreshold;

	/** *** END OF PARAMETER SECTION **** */

	private boolean recent_change = true; /* indicates that a change has just ocurred */
	private boolean changed = false;
	private int current_peak; /* peak on which the current best individual is located */
	private int maximum_peak; /* number of highest peak */
	private double current_maximum; /* fitness value of currently best individual */
	private double offline_performance = 0.0;
	private double offline_error = 0.0;
	private double avg_error = 0; /* average error so far */
	private double current_error = 0; /* error of the currently best individual */
	private double global_max; /* absolute maximum in the fitness landscape */
	private int evals = 0; /* number of evaluations so far */

	/* data structure to store peak data */
	public double[][] peak; // double * * peak;

	private double[] shift; // double * shift;

	private double[] coordinates; // double * coordinates;

	/* which peaks are covered by the population ? */
	@SuppressWarnings("unused")
	private int[] covered_peaks; // int * covered_peaks;

	/* to store every peak's previous movement */
	private double[][] prev_movement; // double * * prev_movement;

	/*
	 * two variables needed in method change_stepsize_linear(). Perhaps it would be appropriate to
	 * put change_stepsize_linear() in its own class.
	 */
	private int counter = 1;
	private double frequency = 3.14159 / 20.0;

	final int PEAKFUNCTION1 = 0;
	final int PEAKFUNCTIONCONE = 1;
	final int PEAKFUNCTIONSPHERE = 2;

	public boolean didChange() {
		if (changed) {
			changed = false;
			return true;
		}
		else
			return false;
	}

	public double getExclusionThreshold() {
		return exclusionthreshold;
	}

	/* initialize all variables at the beginning of the program */
	public void init_peaks() {
		int i, j;
		double dummy;

		shift = new double[getDimension()];
		this.coordinates = new double[getDimension()];
		this.covered_peaks = new int[this.number_of_peaks];
		this.peak = new double[this.number_of_peaks][];
		this.prev_movement = new double[this.number_of_peaks][];

		for (i = 0; i < this.number_of_peaks; i++) {
			peak[i] = new double[getDimension() + 2];
			prev_movement[i] = new double[getDimension()];
		}

		for (i = 0; i < this.number_of_peaks; i++) {
			for (j = 0; j < getDimension(); j++) {
				peak[i][j] = 100.0 * this.movrand();
				this.prev_movement[i][j] = this.movrand() - 0.5;
			}
		}

		if (this.standardheight <= 0.0) {
			for (i = 0; i < this.number_of_peaks; i++) {
				peak[i][getDimension() + 1] = (this.maxheight - this.minheight) * this.movrand() + this.minheight;
			}
		}
		else {
			for (i = 0; i < this.number_of_peaks; i++) {
				peak[i][getDimension() + 1] = this.standardheight;
			} // for
		} // else

		if (this.standardwidth <= 0.0) {
			for (i = 0; i < this.number_of_peaks; i++) {
				peak[i][getDimension()] = (this.maxwidth - this.minwidth) * this.movrand() + this.minwidth;
			} // for
		}
		else {
			for (i = 0; i < this.number_of_peaks; i++) {
				peak[i][getDimension()] = this.standardwidth;
			} // for
		} // else

		if (this.calculate_average_error) {
			this.global_max = -100000.0;

			for (i = 0; i < this.number_of_peaks; i++) {
				for (j = 0; j < getDimension(); j++) {
					this.coordinates[j] = peak[i][j];
				} // for

				dummy = this.dummy_eval(coordinates);

				if (dummy > this.global_max) {
					this.global_max = dummy;
				}
			} // for
		} // if
	} // init_peaks

	/* dummy evaluation function allows to evaluate without being counted */
	public double dummy_eval(double[] gen) {
		int i;
		double maximum = -100000.0, dummy;

		for (i = 0; i < this.number_of_peaks; i++) {
			dummy = this.pf.calculate(gen, i);
			if (dummy > maximum)
				maximum = dummy;
		}

		if (this.use_basis_function) {
			dummy = this.bf.calculate(gen);
			/* If value of basis function is higher return it */
			if (maximum < dummy)
				maximum = dummy;
		}
		return (maximum);
	}

	/* evaluation function */
	public double eval_movpeaks(double[] gen) {
		double maximum = -100000.0;

		if ((this.change_frequency > 0) && (this.evals % this.change_frequency == 0)) {
			this.change_peaks();
		}

		maximum = dummy_eval(gen);

		// if(maximum < 0) { System.out.println("max " + maximum); }

		if (this.calculate_average_error) {
			this.avg_error += this.global_max - maximum;
		}

		if (calculate_offline_performance) {
			if (this.recent_change || (maximum > current_maximum)) {
				this.current_error = this.global_max - maximum;
				if (this.calculate_right_peak) {
					this.current_peak_calc(gen);
				}
				this.current_maximum = maximum;
				this.recent_change = false;
			}
			this.offline_performance += this.current_maximum;
			this.offline_error += this.current_error;
		}

		this.evals++; /* increase the number of evaluations by one */
		return (maximum);
	} // eval_movpeaks

	/* whenever this function is called, the peaks are changed */
	public void change_peaks() {
		int i, j;
		double sum, sum2, offset, dummy;

		for (i = 0; i < this.number_of_peaks; i++) {
			/* shift peak locations */
			sum = 0.0;
			for (j = 0; j < getDimension(); j++) {
				this.shift[j] = this.movrand() - 0.5;
				sum += this.shift[j] * this.shift[j];
			}

			if (sum > 0.0) {
				sum = this.vlength / Math.sqrt(sum);
			}
			else {/* only in case of rounding errors */
				sum = 0.0;
			}
			sum2 = 0.0;

			for (j = 0; j < getDimension(); j++) {
				this.shift[j] = sum * (1.0 - this.lambda) * this.shift[j] + this.lambda * this.prev_movement[i][j];
				sum2 += this.shift[j] * this.shift[j];
			}

			if (sum2 > 0.0) {
				sum2 = this.vlength / Math.sqrt(sum2);
			}
			else {/* only in case of rounding errors */
				sum2 = 0.0;
			}

			for (j = 0; j < getDimension(); j++) {
				this.shift[j] *= sum2;
				this.prev_movement[i][j] = this.shift[j];
				if ((peak[i][j] + this.prev_movement[i][j]) < this.mincoordinate) {
					peak[i][j] = 2.0 * this.mincoordinate - peak[i][j] - this.prev_movement[i][j];
					this.prev_movement[i][j] *= -1.0;
				}
				else if ((peak[i][j] + this.prev_movement[i][j]) > this.maxcoordinate) {
					peak[i][j] = 2.0 * this.maxcoordinate - peak[i][j] - this.prev_movement[i][j];
					this.prev_movement[i][j] *= -1.0;
				}
				else {
					peak[i][j] += prev_movement[i][j];
				}
			}

			/* change peak width */
			j = getDimension();
			offset = this.movnrand() * this.width_severity;

			if ((peak[i][j] + offset) < this.minwidth) {
				peak[i][j] = 2.0 * this.minwidth - peak[i][j] - offset;
			}
			else if ((peak[i][j] + offset) > this.maxwidth) {
				peak[i][j] = 2.0 * this.maxwidth - peak[i][j] - offset;
			}
			else {
				peak[i][j] += offset;
			}
			/* change peak height */
			j++;
			offset = this.height_severity * this.movnrand();

			if ((peak[i][j] + offset) < this.minheight) {
				peak[i][j] = 2.0 * this.minheight - peak[i][j] - offset;
			}
			else if ((peak[i][j] + offset) > this.maxheight) {
				peak[i][j] = 2.0 * this.maxheight - peak[i][j] - offset;
			}
			else {
				peak[i][j] += offset;
			}
		}
		if (this.calculate_average_error) {
			this.global_max = -100000.0;
			for (i = 0; i < this.number_of_peaks; i++) {
				for (j = 0; j < getDimension(); j++) {
					this.coordinates[j] = peak[i][j];
				}

				dummy = this.dummy_eval(coordinates);

				if (dummy > this.global_max) {
					this.global_max = dummy;
					this.maximum_peak = i;
				}
			}
		}
		this.recent_change = true;
		changed = true;
		// printPeakData();
	} // change_peaks

	/* current_peak_calc determines the peak of the current best individual */
	private void current_peak_calc(double[] gen) {
		int i;
		double maximum = -100000.0, dummy;

		this.current_peak = 0;
		maximum = this.pf.calculate(gen, 0);
		for (i = 1; i < this.number_of_peaks; i++) {
			dummy = this.pf.calculate(gen, i);
			if (dummy > maximum) {
				maximum = dummy;
				this.current_peak = i;
			} // if
		} // for
	} // current_peak_calc

	/* free disc space at end of program */
	public void free_peaks() {
		int i;

		for (i = 0; i < this.number_of_peaks; i++) {
			peak[i] = null;
			prev_movement[i] = null;
		}
		System.gc();
	} // free_peaks

	/* The following procedures may be used to change the step size over time */

	/* assigns vlength a value from a normal distribution */
	@SuppressWarnings("unused")
	private void change_stepsize_random() {
		this.vlength = this.movnrand();
	}

	/* sinusoidal change of the stepsize */
	@SuppressWarnings("unused")
	private void change_stepsize_linear() {
		// returns to same value after 20 changes
		this.vlength = 1 + Math.sin((double) counter * frequency);
		counter++;
	}

	/* returns the average error of all evaluation calls so far */
	public double get_avg_error() {
		return (this.avg_error / (double) this.evals);
	}

	/*
	 * returns the error of the best individual evaluated since last change To use this function,
	 * calculate_average_error and calculate_offline_performance must be set
	 */
	public double get_current_error() {
		return this.current_error;
	}

	/* returns offline performance */
	public double get_offline_performance() {
		return (this.offline_performance / (double) this.evals);
	}

	/* returns offline error */
	public double get_offline_error() {
		return (this.offline_error / (double) this.evals);
	}

	/* returns the number of evaluations so far */
	public int get_number_of_evals() {
		return this.evals;
	}

	/*
	 * returns 1 if current best individual is on highest peak, 0 otherwise
	 */
	public boolean get_right_peak() {
		if (this.current_peak == this.maximum_peak)
			return true;
		else
			return false;
	}

	public double[][] getPeakPositions() {
		double[][] positions = new double[this.number_of_peaks][getDimension()];

		for (int i = 0; i < this.number_of_peaks; i++) {
			for (int j = 0; j < getDimension(); j++) {
				positions[i][j] = peak[i][j];
			}
		}
		return positions;
	}

	public double[] getPeakHeights() {
		double[] temp = new double[this.number_of_peaks];
		int index = getDimension() + 1;

		for (int i = 0; i < number_of_peaks; i++) {
			temp[i] = peak[i][index];
		}
		return temp;
	}

	public void printPeakData() {
		double[] temp = getPeakHeights();

		System.out.print("Peak heights:\t");
		for (int i = 0; i < temp.length; i++) {
			System.out.print(temp[i] + "\t");
		}
		System.out.println();
	}

	public int getCurrentPeak() {
		return this.current_peak;
	}

	public int getMaximumPeak() {
		return this.maximum_peak;
	}

	public double getMinCoordinate() {
		return mincoordinate;
	}

	public double getMaxCoordinate() {
		return maxcoordinate;
	}

	public double getMaxHeight() {
		return maxheight;
	}

	// inner classes

	interface Peak_Function {
		public double calculate(double[] gen, int peak_number);
	} // Peak_Function

	class Peak_Function1 implements Peak_Function {
		public double calculate(double[] gen, int peak_number) {
			int j;
			double dummy;

			dummy = (gen[0] - peak[peak_number][0]) * (gen[0] - peak[peak_number][0]);
			for (j = 1; j < getDimension(); j++)
				dummy += (gen[j] - peak[peak_number][j]) * (gen[j] - peak[peak_number][j]);

			return peak[peak_number][getDimension() + 1] / (1 + (peak[peak_number][getDimension()]) * dummy);
		} // calculate
	}

	class Peak_Function_Cone implements Peak_Function {
		public double calculate(double[] gen, int peak_number) {
			int j;
			double dummy;

			dummy = (gen[0] - peak[peak_number][0]) * (gen[0] - peak[peak_number][0]);
			for (j = 1; j < getDimension(); j++)
				dummy += (gen[j] - peak[peak_number][j]) * (gen[j] - peak[peak_number][j]);
			// sqrt of dummy is the distance between gen and peak.
			return peak[peak_number][getDimension() + 1] // peak height
					- (peak[peak_number][getDimension()] * Math.sqrt(dummy));
		}
	}

	class Peak_Function_Hilly implements Peak_Function {
		public double calculate(double[] gen, int peak_number) {
			int j;
			double dummy;

			dummy = (gen[0] - peak[peak_number][0]) * (gen[0] - peak[peak_number][0]);
			for (j = 1; j < getDimension(); j++)
				dummy += (gen[j] - peak[peak_number][j]) * (gen[j] - peak[peak_number][j]);

			return peak[peak_number][getDimension() + 1] - (peak[peak_number][getDimension()] * dummy) - 0.01 * Math.sin(20.0 * dummy);
		}
	}

	class Peak_Function_Sphere implements Peak_Function {
		public double calculate(double[] gen, int peak_number) {
			int j;
			double dummy;

			dummy = (gen[0] - peak[peak_number][0]) * (gen[0] - peak[peak_number][0]);
			for (j = 1; j < getDimension(); j++)
				dummy += (gen[j] - peak[peak_number][j]) * (gen[j] - peak[peak_number][j]);

			return peak[peak_number][getDimension() + 1] - dummy;
		}
	}

	class Peak_Function_Twin implements Peak_Function {
		public double calculate(double[] gen, int peak_number) {
			int j;
			double maximum = -100000.0, dummy;
			/* difference to first peak */
			/* static */
			double[] twin_peak = {1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0};

			dummy = Math.pow(gen[0] - peak[peak_number][0], 2);
			for (j = 1; j < getDimension(); j++)
				dummy += Math.pow(gen[j] - peak[peak_number][j], 2);
			dummy = peak[peak_number][getDimension() + 1] - (peak[peak_number][getDimension()] * dummy);
			maximum = dummy;
			// System.out.println("j: "+j);
			dummy = Math.pow(gen[j] - (peak[peak_number][0] + twin_peak[0]), 2);
			for (j = 1; j < getDimension(); j++)
				dummy += Math.pow(gen[j] - (peak[peak_number][j] + twin_peak[0]), 2);
			dummy = peak[peak_number][getDimension() + 1] + twin_peak[getDimension() + 1] - ((peak[peak_number][getDimension()] + twin_peak[getDimension()]) * dummy);
			if (dummy > maximum)
				maximum = dummy;

			return maximum;
		}
	}

	interface Basis_Function {
		public double calculate(double[] gen);
	}

	class Constant_Basis_Function implements Basis_Function {
		public double calculate(double[] gen) {
			return 0.0;
		}
	}

	class Five_Peaks_Basis_Function implements Basis_Function {
		public double calculate(double[] gen) {
			int i, j;
			double maximum = -100000.0, dummy;
			double[][] basis_peak = { {8.0, 64.0, 67.0, 55.0, 4.0, 0.1, 50.0}, {50.0, 13.0, 76.0, 15.0, 7.0, 0.1, 50.0}, {9.0, 19.0, 27.0, 67.0, 24.0, 0.1, 50.0}, {66.0, 87.0, 65.0, 19.0, 43.0, 0.1, 50.0}, {76.0, 32.0, 43.0, 54.0, 65.0, 0.1, 50.0},};

			for (i = 0; i < 5; i++) {
				dummy = (gen[0] - basis_peak[i][0]) * (gen[0] - basis_peak[i][0]);
				for (j = 1; j < getDimension(); j++)
					dummy += (gen[j] - basis_peak[i][j]) * (gen[j] - basis_peak[i][j]);
				dummy = basis_peak[i][getDimension() + 1] - (basis_peak[i][getDimension()] * dummy);
				if (dummy > maximum)
					maximum = dummy;
			}
			return maximum;
		}
	}
}
