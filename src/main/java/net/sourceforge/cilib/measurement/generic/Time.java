/* Time.java
 * 
 * Created on Feb 16, 2004
 *
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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA 
 */

package net.sourceforge.cilib.measurement.generic;

import net.sourceforge.cilib.algorithm.AlgorithmEvent;
import net.sourceforge.cilib.algorithm.AlgorithmListener;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.type.types.Int;
import net.sourceforge.cilib.type.types.Type;


/**
 * @author Edwin Peer
 */
public class Time implements Measurement, AlgorithmListener {
	private static final long serialVersionUID = -3516066813688827758L;
	
	private boolean running = false;
	private long startTime;
	private long endTime;

	public Time() {
		running = false;
		startTime = System.currentTimeMillis();
		endTime = startTime;
	}
	
	public Time(Time copy) {
		running = copy.running;
		startTime = copy.startTime;
		endTime = copy.endTime;
	}
	
	public Time getClone() {
		return new Time(this);
	}
	
	public String getDomain() {
		return "Z";
		//return "T";
	}
	
	public Type getValue() {
		if (running) {
			Int t = new Int();
			t.setReal(Long.valueOf(System.currentTimeMillis() - startTime));
			//return new Long(System.currentTimeMillis() - startTime);
			return t;
		}
		else {
			Int i = new Int();
			i.setReal(endTime - startTime);
			//return new Integer((int) (endTime - startTime));
			return i;
		}
	}
	
	public void algorithmStarted(AlgorithmEvent e) {
		running = true;
		startTime = System.currentTimeMillis();
	}

	public void algorithmFinished(AlgorithmEvent e) {
		endTime = System.currentTimeMillis();
		running = false;
	}

	public void algorithmTerminated(AlgorithmEvent e) {
		endTime = System.currentTimeMillis();
		running = false;
	}

	public void iterationCompleted(AlgorithmEvent e) {
		
	}
	
}
