/*
 * Copyright (C) 2003 - 2008
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

	/** 
	 * Creates a new instance of MovingPeaks. 
	 */
	public MovingPeaks() {
		setDomain("R(0, 100)^5");
		exclusionthreshold = (maxcoordinate - mincoordinate) / (2 * (Math.pow(numberOfPeaks, 1 / getDimension())));
		initPeaks();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public MovingPeaks getClone() {
		return new MovingPeaks();
	}

	/**
	 * {@inheritDoc}
	 */
	public Object getMinimum() {
		return new Double(0);
	}

	/**
	 * {@inheritDoc}
	 */
	public Object getMaximum() {
		return new Double(maxheight);
	}

	/**
	 * {@inheritDoc}
	 */
	public double evaluate(Vector x) {
		double[] elements = new double[getDimension()];

		for (int i = 0; i < getDimension(); ++i) {
			elements[i] = x.getReal(i);

		}
		return evaluateMovpeaks(elements);
	}

	/** *** PARAMETER SETTINGS **** */

	/*
	 * number of evaluations between changes. change_frequency =0 means that function never changes
	 * (or only if function change_peaks is called) Scenario 1: 5000
	 */
	private int changeFrequency = 0;

	/**
	 * Set the change frequency.
	 * @param cfr The given frequency. 
	 */
	public void setChangeFrequency(int cfr) {
		changeFrequency = cfr;
	}

	/**
	 * Get the current change frequency.
	 * @return The current change frequency.
	 */
	public int getChangeFrequency() {
		return changeFrequency;
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
	private double heightSeverity = 7.0;

	/*
	 * severity of width changes, larger numbers mean larger severity
	 */
	private double widthSeverity = 1;

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
	private int numberOfPeaks = 10;

	/*
	 * if set to 1, a static landscape (basis_function) is included in the fitness evaluation
	 */
	protected boolean useBasisFunction = false;

	/* saves computation time if not needed and set to 0 */
	private boolean calculateAverageError = true; // int calculate_average_error = 1;

	/* saves computation time if not needed and set to 0 */
	private boolean calculateOfflinePerformance = true;
	// int calculate_offline_performance = 1;

	/* saves computation time if not needed and set to 0 */
	private boolean calculateRightPeak = true; // int calculate_right_peak = 1;

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

	public PeakFunction pf = new PeakFunctionCone(); // Scenario 1

	public BasisFunction bf = new ConstantBasisFunction();

	/*
	 * Value for the exclusion threshold
	 */

	private double exclusionthreshold;

	/** *** END OF PARAMETER SECTION **** */

	private boolean recentChange = true; /* indicates that a change has just ocurred */
	private boolean changed = false;
	private int currentPeak; /* peak on which the current best individual is located */
	private int maximumPeak; /* number of highest peak */
	private double currentMaximum; /* fitness value of currently best individual */
	private double offlinePerformance = 0.0;
	private double offlineError = 0.0;
	private double avgError = 0; /* average error so far */
	private double currentError = 0; /* error of the currently best individual */
	private double globalMax; /* absolute maximum in the fitness landscape */
	private int evals = 0; /* number of evaluations so far */

	/* data structure to store peak data */
	public double[][] peak; // double * * peak;

	private double[] shift; // double * shift;

	private double[] coordinates; // double * coordinates;

	/* which peaks are covered by the population ? */
	@SuppressWarnings("unused")
	private int[] coveredPeaks; // int * covered_peaks;

	/* to store every peak's previous movement */
	private double[][] prevMovement; // double * * prev_movement;

	/*
	 * two variables needed in method change_stepsize_linear(). Perhaps it would be appropriate to
	 * put change_stepsize_linear() in its own class.
	 */
	private int counter = 1;
	private double frequency = 3.14159 / 20.0;

	final int peakFunction1 = 0;
	final int peakFunctionCone = 1;
	final int peakFunctionSphere = 2;

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
	public void initPeaks() {
		int i, j;
		double dummy;

		shift = new double[getDimension()];
		this.coordinates = new double[getDimension()];
		this.coveredPeaks = new int[this.numberOfPeaks];
		this.peak = new double[this.numberOfPeaks][];
		this.prevMovement = new double[this.numberOfPeaks][];

		for (i = 0; i < this.numberOfPeaks; i++) {
			peak[i] = new double[getDimension() + 2];
			prevMovement[i] = new double[getDimension()];
		}

		for (i = 0; i < this.numberOfPeaks; i++) {
			for (j = 0; j < getDimension(); j++) {
				peak[i][j] = 100.0 * this.movrand();
				this.prevMovement[i][j] = this.movrand() - 0.5;
			}
		}

		if (this.standardheight <= 0.0) {
			for (i = 0; i < this.numberOfPeaks; i++) {
				peak[i][getDimension() + 1] = (this.maxheight - this.minheight) * this.movrand() + this.minheight;
			}
		}
		else {
			for (i = 0; i < this.numberOfPeaks; i++) {
				peak[i][getDimension() + 1] = this.standardheight;
			} // for
		} // else

		if (this.standardwidth <= 0.0) {
			for (i = 0; i < this.numberOfPeaks; i++) {
				peak[i][getDimension()] = (this.maxwidth - this.minwidth) * this.movrand() + this.minwidth;
			} // for
		}
		else {
			for (i = 0; i < this.numberOfPeaks; i++) {
				peak[i][getDimension()] = this.standardwidth;
			} // for
		} // else

		if (this.calculateAverageError) {
			this.globalMax = -100000.0;

			for (i = 0; i < this.numberOfPeaks; i++) {
				for (j = 0; j < getDimension(); j++) {
					this.coordinates[j] = peak[i][j];
				} // for

				dummy = this.dummyEval(coordinates);

				if (dummy > this.globalMax) {
					this.globalMax = dummy;
				}
			} // for
		} // if
	} // init_peaks

	/* dummy evaluation function allows to evaluate without being counted */
	public double dummyEval(double[] gen) {
		int i;
		double maximum = -100000.0, dummy;

		for (i = 0; i < this.numberOfPeaks; i++) {
			dummy = this.pf.calculate(gen, i);
			if (dummy > maximum)
				maximum = dummy;
		}

		if (this.useBasisFunction) {
			dummy = this.bf.calculate(gen);
			/* If value of basis function is higher return it */
			if (maximum < dummy)
				maximum = dummy;
		}
		return (maximum);
	}

	/* evaluation function */
	public double evaluateMovpeaks(double[] gen) {
		double maximum = -100000.0;

		if ((this.changeFrequency > 0) && (this.evals % this.changeFrequency == 0)) {
			this.changePeaks();
		}

		maximum = dummyEval(gen);

		// if(maximum < 0) { System.out.println("max " + maximum); }

		if (this.calculateAverageError) {
			this.avgError += this.globalMax - maximum;
		}

		if (calculateOfflinePerformance) {
			if (this.recentChange || (maximum > currentMaximum)) {
				this.currentError = this.globalMax - maximum;
				if (this.calculateRightPeak) {
					this.currentPeakCalculation(gen);
				}
				this.currentMaximum = maximum;
				this.recentChange = false;
			}
			this.offlinePerformance += this.currentMaximum;
			this.offlineError += this.currentError;
		}

		this.evals++; /* increase the number of evaluations by one */
		return (maximum);
	} // eval_movpeaks

	/* whenever this function is called, the peaks are changed */
	public void changePeaks() {
		int i, j;
		double sum, sum2, offset, dummy;

		for (i = 0; i < this.numberOfPeaks; i++) {
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
				this.shift[j] = sum * (1.0 - this.lambda) * this.shift[j] + this.lambda * this.prevMovement[i][j];
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
				this.prevMovement[i][j] = this.shift[j];
				if ((peak[i][j] + this.prevMovement[i][j]) < this.mincoordinate) {
					peak[i][j] = 2.0 * this.mincoordinate - peak[i][j] - this.prevMovement[i][j];
					this.prevMovement[i][j] *= -1.0;
				}
				else if ((peak[i][j] + this.prevMovement[i][j]) > this.maxcoordinate) {
					peak[i][j] = 2.0 * this.maxcoordinate - peak[i][j] - this.prevMovement[i][j];
					this.prevMovement[i][j] *= -1.0;
				}
				else {
					peak[i][j] += prevMovement[i][j];
				}
			}

			/* change peak width */
			j = getDimension();
			offset = this.movnrand() * this.widthSeverity;

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
			offset = this.heightSeverity * this.movnrand();

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
		if (this.calculateAverageError) {
			this.globalMax = -100000.0;
			for (i = 0; i < this.numberOfPeaks; i++) {
				for (j = 0; j < getDimension(); j++) {
					this.coordinates[j] = peak[i][j];
				}

				dummy = this.dummyEval(coordinates);

				if (dummy > this.globalMax) {
					this.globalMax = dummy;
					this.maximumPeak = i;
				}
			}
		}
		this.recentChange = true;
		changed = true;
		// printPeakData();
	} // change_peaks

	/* current_peak_calc determines the peak of the current best individual */
	private void currentPeakCalculation(double[] gen) {
		int i;
		double maximum = -100000.0, dummy;

		this.currentPeak = 0;
		maximum = this.pf.calculate(gen, 0);
		for (i = 1; i < this.numberOfPeaks; i++) {
			dummy = this.pf.calculate(gen, i);
			if (dummy > maximum) {
				maximum = dummy;
				this.currentPeak = i;
			} // if
		} // for
	} // current_peak_calc

	/* free disc space at end of program */
	public void freePeaks() {
		int i;

		for (i = 0; i < this.numberOfPeaks; i++) {
			peak[i] = null;
			prevMovement[i] = null;
		}
		System.gc();
	} // free_peaks

	/* The following procedures may be used to change the step size over time */

	/* assigns vlength a value from a normal distribution */
	@SuppressWarnings("unused")
	private void changeStepsizeRandom() {
		this.vlength = this.movnrand();
	}

	/* sinusoidal change of the stepsize */
	@SuppressWarnings("unused")
	private void changeStepsizeLinear() {
		// returns to same value after 20 changes
		this.vlength = 1 + Math.sin((double) counter * frequency);
		counter++;
	}

	/* returns the average error of all evaluation calls so far */
	public double getAvgError() {
		return (this.avgError / (double) this.evals);
	}

	/*
	 * returns the error of the best individual evaluated since last change To use this function,
	 * calculate_average_error and calculate_offline_performance must be set
	 */
	public double getCurrentError() {
		return this.currentError;
	}

	/* returns offline performance */
	public double getOfflinePerformance() {
		return (this.offlinePerformance / (double) this.evals);
	}

	/* returns offline error */
	public double getOfflineError() {
		return (this.offlineError / (double) this.evals);
	}

	/* returns the number of evaluations so far */
	public int getNumberOfEvals() {
		return this.evals;
	}

	/*
	 * returns 1 if current best individual is on highest peak, 0 otherwise
	 */
	public boolean getRightPeak() {
		return (this.currentPeak == this.maximumPeak);
	}

	public double[][] getPeakPositions() {
		double[][] positions = new double[this.numberOfPeaks][getDimension()];

		for (int i = 0; i < this.numberOfPeaks; i++) {
			for (int j = 0; j < getDimension(); j++) {
				positions[i][j] = peak[i][j];
			}
		}
		return positions;
	}

	public double[] getPeakHeights() {
		double[] temp = new double[this.numberOfPeaks];
		int index = getDimension() + 1;

		for (int i = 0; i < numberOfPeaks; i++) {
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
		return this.currentPeak;
	}

	public int getMaximumPeak() {
		return this.maximumPeak;
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

	/**
	 * TODO: Complete this javadoc.
	 */
	interface PeakFunction {
		public double calculate(double[] gen, int peakNumber);
	} // Peak_Function

	/**
	 * TODO: Complete this javadoc.
	 */
	class PeakFunction1 implements PeakFunction {
		public double calculate(double[] gen, int peakNumber) {
			int j;
			double dummy;

			dummy = (gen[0] - peak[peakNumber][0]) * (gen[0] - peak[peakNumber][0]);
			for (j = 1; j < getDimension(); j++)
				dummy += (gen[j] - peak[peakNumber][j]) * (gen[j] - peak[peakNumber][j]);

			return peak[peakNumber][getDimension() + 1] / (1 + (peak[peakNumber][getDimension()]) * dummy);
		} // calculate
	}

	/**
	 * TODO: Complete this javadoc.
	 */
	class PeakFunctionCone implements PeakFunction {
		public double calculate(double[] gen, int peakNumber) {
			int j;
			double dummy;

			dummy = (gen[0] - peak[peakNumber][0]) * (gen[0] - peak[peakNumber][0]);
			for (j = 1; j < getDimension(); j++)
				dummy += (gen[j] - peak[peakNumber][j]) * (gen[j] - peak[peakNumber][j]);
			// sqrt of dummy is the distance between gen and peak.
			return peak[peakNumber][getDimension() + 1] -// peak height
				(peak[peakNumber][getDimension()] * Math.sqrt(dummy));
		}
	}

	/**
	 * TODO: Complete this javadoc.
	 */
	class PeakFunctionHilly implements PeakFunction {
		public double calculate(double[] gen, int peakNumber) {
			int j;
			double dummy;

			dummy = (gen[0] - peak[peakNumber][0]) * (gen[0] - peak[peakNumber][0]);
			for (j = 1; j < getDimension(); j++)
				dummy += (gen[j] - peak[peakNumber][j]) * (gen[j] - peak[peakNumber][j]);

			return peak[peakNumber][getDimension() + 1] - (peak[peakNumber][getDimension()] * dummy) - 0.01 * Math.sin(20.0 * dummy);
		}
	}

	/**
	 * TODO: Complete this javadoc.
	 */
	class PeakFunctionSphere implements PeakFunction {
		public double calculate(double[] gen, int peakNumber) {
			int j;
			double dummy;

			dummy = (gen[0] - peak[peakNumber][0]) * (gen[0] - peak[peakNumber][0]);
			for (j = 1; j < getDimension(); j++)
				dummy += (gen[j] - peak[peakNumber][j]) * (gen[j] - peak[peakNumber][j]);

			return peak[peakNumber][getDimension() + 1] - dummy;
		}
	}

	/**
	 * TODO: Complete this javadoc.
	 */
	class PeakFunctionTwin implements PeakFunction {
		public double calculate(double[] gen, int peakNumber) {
			int j;
			double maximum = -100000.0, dummy;
			/* difference to first peak */
			/* static */
			double[] twin_peak = {1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0};

			dummy = Math.pow(gen[0] - peak[peakNumber][0], 2);
			for (j = 1; j < getDimension(); j++)
				dummy += Math.pow(gen[j] - peak[peakNumber][j], 2);
			dummy = peak[peakNumber][getDimension() + 1] - (peak[peakNumber][getDimension()] * dummy);
			maximum = dummy;
			// System.out.println("j: "+j);
			dummy = Math.pow(gen[j] - (peak[peakNumber][0] + twin_peak[0]), 2);
			for (j = 1; j < getDimension(); j++)
				dummy += Math.pow(gen[j] - (peak[peakNumber][j] + twin_peak[0]), 2);
			dummy = peak[peakNumber][getDimension() + 1] + twin_peak[getDimension() + 1] - ((peak[peakNumber][getDimension()] + twin_peak[getDimension()]) * dummy);
			if (dummy > maximum)
				maximum = dummy;

			return maximum;
		}
	}

	/**
	 * TODO: Complete this javadoc.
	 */
	interface BasisFunction {
		public double calculate(double[] gen);
	}

	/**
	 * TODO: Complete this javadoc.
	 */
	class ConstantBasisFunction implements BasisFunction {
		public double calculate(double[] gen) {
			return 0.0;
		}
	}

	/**
	 * TODO: Complete this javadoc.
	 */
	class FivePeaksBasisFunction implements BasisFunction {
		public double calculate(double[] gen) {
			int i, j;
			double maximum = -100000.0, dummy;
			double[][] basisPeak = { 
					{8.0, 64.0, 67.0, 55.0, 4.0, 0.1, 50.0},
					{50.0, 13.0, 76.0, 15.0, 7.0, 0.1, 50.0},
					{9.0, 19.0, 27.0, 67.0, 24.0, 0.1, 50.0},
					{66.0, 87.0, 65.0, 19.0, 43.0, 0.1, 50.0},
					{76.0, 32.0, 43.0, 54.0, 65.0, 0.1, 50.0},
				};

			for (i = 0; i < 5; i++) {
				dummy = (gen[0] - basisPeak[i][0]) * (gen[0] - basisPeak[i][0]);
				for (j = 1; j < getDimension(); j++)
					dummy += (gen[j] - basisPeak[i][j]) * (gen[j] - basisPeak[i][j]);
				dummy = basisPeak[i][getDimension() + 1] - (basisPeak[i][getDimension()] * dummy);
				if (dummy > maximum)
					maximum = dummy;
			}
			return maximum;
		}
	}
}
