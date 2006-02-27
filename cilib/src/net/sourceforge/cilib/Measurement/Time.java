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

package net.sourceforge.cilib.Measurement;

import net.sourceforge.cilib.Algorithm.AlgorithmEvent;
import net.sourceforge.cilib.Algorithm.AlgorithmListener;


/**
 * @author espeer
 */
public class Time implements Measurement, AlgorithmListener {

	public Time() {
		running = false;
		startTime = System.currentTimeMillis();
		endTime = startTime;
	}
	
	public String getDomain() {
		return "Z(0,)";
	}
	
	public Object getValue() {
		if (running) {
			return new Long(System.currentTimeMillis() - startTime);
		}
		else {
			return new Long(endTime - startTime);
		}
	}
	
	private boolean running = false;
	private long startTime;
	private long endTime;

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
