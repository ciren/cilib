/*
 * Created on Jun 21, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package net.sourceforge.cilib.Measurement;

import java.util.Iterator;

import net.sourceforge.cilib.Algorithm.Algorithm;
import net.sourceforge.cilib.PSO.PSO;
import net.sourceforge.cilib.PSO.Particle;

/**
 * @author espeer
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class ParticlePositions implements Measurement {

	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.Measurement.Measurement#getDomain()
	 */
	public String getDomain() {
		return "T";
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.Measurement.Measurement#getValue()
	 */
	public Object getValue() {
		StringBuffer tmp = new StringBuffer();
		
		PSO pso = (PSO) Algorithm.get();
		Iterator i = pso.getTopology().particles();
		while (i.hasNext()) {
			Particle particle = (Particle) i.next();
			tmp.append("\nParticle: ");
			tmp.append(particle.getId());
			tmp.append(" Current Fitness: ");
			tmp.append(particle.getFitness().getValue());
			tmp.append(" Best Fitness: ");
			tmp.append(particle.getBestFitness().getValue());
			tmp.append(" Position: ");
			for (int j = 0; j < particle.getDimension(); ++j) {
				tmp.append(particle.getPosition().getReal(j));
				tmp.append(" ");
			}
		}
		
		return tmp.toString();
	}

}
