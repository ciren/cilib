/**
 * Copyright (C) 2003 - 2009
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
package net.sourceforge.cilib.bioinf.rnaprediction;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.problem.OptimisationSolution;
import net.sourceforge.cilib.type.types.StringType;
import net.sourceforge.cilib.type.types.Type;

/**
 * @author mneethling
 *
 */
public class RNAStructureMeasurement implements Measurement {
    private static final long serialVersionUID = 5649656748412723897L;

    public RNAStructureMeasurement() {
    }

    public RNAStructureMeasurement(RNAStructureMeasurement copy) {
    }

    public RNAStructureMeasurement getClone() {
        return new RNAStructureMeasurement(this);
    }

    /* (non-Javadoc)
     * @see net.sourceforge.cilib.Measurement.Measurement#getDomain()
     */
    public String getDomain() {
        return "?";
    }

    /* (non-Javadoc)
     * @see net.sourceforge.cilib.Measurement.Measurement#getValue()
     */
    public Type getValue(Algorithm algorithm) {
        OptimisationSolution os = algorithm.getBestSolution();
        RNAConformation conf = (RNAConformation) os.getPosition();

        StringType t = new StringType();
        t.setString(new String(conf.getCharRepresentation())+"@");
        return t;
        /*
        //StemGenerator generator = StemGenerator.getInstance();
        char[] conf = new char[NucleotideString.getInstance().getNucleotideString().length()+2];
        int [] indexes = new int[NucleotideString.getInstance().getNucleotideString().length()];
        for (int i = 0; i < NucleotideString.getInstance().getNucleotideString().length();i++) {
            conf[i] = '.';
            indexes[i] = 0;
        }
        conf[conf.length-1] = '@';
        conf[conf.length-2] = '@';

        for (Iterator<?> it = stems.iterator(); it.hasNext(); ) {
            RNAStem s = (RNAStem) it.next();
            for (int i = s.getP5_index(); i < s.getP5_index()+s.getLength();i++ ) {
                conf[i] = '(';
            }
            for (int i = s.getP3_index(); i > s.getP3_index()-s.getLength();i--) {
                conf[i] = ')';
            }
        }
        return new String(conf);
        */
    }
}
