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
package net.sourceforge.cilib.neuralnetwork.generic.datacontainers;

import java.util.ArrayList;

import net.sourceforge.cilib.neuralnetwork.foundation.Initializable;
import net.sourceforge.cilib.neuralnetwork.foundation.NNError;
import net.sourceforge.cilib.neuralnetwork.foundation.NNPattern;
import net.sourceforge.cilib.neuralnetwork.foundation.NeuralNetworkDataIterator;
import net.sourceforge.cilib.neuralnetwork.generic.GenericTopology;
import net.sourceforge.cilib.type.types.container.TypeList;


/**
 * @author stefanv
 *
 */
public class DynamicPatternSelectionData extends GenericData implements Initializable {

    protected GenericTopology topology;
    protected NNError prototypeError;
    protected ArrayList<NNPattern> mostInformative;
    protected ArrayList<NNError> informativeness;
    private int nrUpdates;



    public DynamicPatternSelectionData() {
        topology = null;
        this.prototypeError = null;
        this.mostInformative = null;
        this.informativeness = null;
        this.nrUpdates = 1;
    }

    public void initialize(){

        super.initialize();

        if ((this.topology == null) || (this.prototypeError == null)) {
            throw new IllegalArgumentException("Required object was null during initialization");
        }

        //assign this as a permanent case as we are testing one pattern at a time.
        prototypeError.setNoPatterns(1);

        this.mostInformative = new ArrayList<NNPattern>();
        this.informativeness = new ArrayList<NNError>();
        for (int i = 0; i < nrUpdates; i++){
            mostInformative.add(null);
            informativeness.add(prototypeError.getClone());
            informativeness.get(i).setValue(new Double(-999999));
        }
    }


    protected void prioritisePattern(NNPattern p, NNError inform){

        //search for input position, index 0 being the best ie highest informativeness value.
        for (int i = 0; i < informativeness.size(); i++){

            if (informativeness.get(i).getValue() < inform.getValue()){
                informativeness.add(i, inform);
                mostInformative.add(i, p);
                break;
            }
        }

        //trim the list to size, if an insertion was made
        if (informativeness.size() > this.nrUpdates){
            informativeness.remove(this.nrUpdates);
            mostInformative.remove(this.nrUpdates);
        }

    }



    public void activeLearningUpdate(Object input) {
        //At every update interval, determine pattern informativeness of all
        //patterns in Candidate set, select most informative one
        //to remove from candidate set and add to training set.


        if(candidateSet.size() != 0){

            //Iterate over each pattern p in the Candidate set Dc
            NeuralNetworkDataIterator dcIter = this.getCandidateSetIterator();

            while(dcIter.hasMore()){

                NNPattern p = dcIter.value();

                //Evaluate p against current NN topology.
                TypeList output = this.topology.evaluate(p);

                //determine informativeness of p
                NNError patternError = prototypeError.getClone();
                patternError.computeIteration(output, p);
                patternError.finaliseError();

                this.prioritisePattern(p, patternError);

                dcIter.next();
            }//end iterate Dc


            //remove mostInformative patterns from candidate set Dc and add to training set Dt.
            while ((candidateSet.size() != 0) && (this.mostInformative.size() > 0)) {
                this.candidateSet.remove(mostInformative.get(0));
                this.trainingSet.add(mostInformative.get(0));
                this.mostInformative.remove(0);
                this.informativeness.remove(0);
            }

            //reset list
            this.mostInformative = new ArrayList<NNPattern>();
            this.informativeness = new ArrayList<NNError>();
            for (int i = 0; i < nrUpdates; i++){
                mostInformative.add(null);
                informativeness.add(prototypeError.getClone());
                informativeness.get(i).setValue(new Double(-999999));
            }
        }

    }

    public NNError getPrototypeError() {
        return prototypeError;
    }

    public void setPrototypeError(NNError baseError) {
        this.prototypeError = baseError;
    }

    public void setTopology(GenericTopology topology) {
        this.topology = topology;
    }

    public int getNrUpdates() {
        return nrUpdates;
    }

    public void setNrUpdates(int nrUpdates) {
        this.nrUpdates = nrUpdates;
    }






}
