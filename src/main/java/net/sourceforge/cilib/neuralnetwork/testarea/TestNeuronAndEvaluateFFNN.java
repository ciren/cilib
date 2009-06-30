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
package net.sourceforge.cilib.neuralnetwork.testarea;

import net.sourceforge.cilib.neuralnetwork.basicFFNN.FFNNTopology;
import net.sourceforge.cilib.neuralnetwork.foundation.NNPattern;
import net.sourceforge.cilib.neuralnetwork.generic.GenericTopology;
import net.sourceforge.cilib.neuralnetwork.generic.LayeredGenericTopology;
import net.sourceforge.cilib.neuralnetwork.generic.Weight;
import net.sourceforge.cilib.neuralnetwork.generic.datacontainers.StandardPattern;
import net.sourceforge.cilib.neuralnetwork.generic.neuron.SigmoidOutputFunction;
import net.sourceforge.cilib.neuralnetwork.generic.topologybuilders.FFNNgenericTopologyBuilder;
import net.sourceforge.cilib.neuralnetwork.generic.trainingstrategies.ErrorSignal;
import net.sourceforge.cilib.neuralnetwork.generic.trainingstrategies.FFNN_GD_TrainingStrategy;
import net.sourceforge.cilib.neuralnetwork.generic.trainingstrategies.SquaredErrorFunction;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.TypeList;
import net.sourceforge.cilib.type.types.container.Vector;


/**
 * @author stefanv
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public final class TestNeuronAndEvaluateFFNN {

    private TestNeuronAndEvaluateFFNN() {
    }

    public static void main(String[] args) {

        int[] sizes = new int[3];
        sizes[0] = 3;
        sizes[1] = 3;
        sizes[2] = 1;

        Weight base= new Weight(new Real(1));

        //GenericTopology topo = new GenericTopology();
        GenericTopology topo = new LayeredGenericTopology();
        FFNNgenericTopologyBuilder builder = new FFNNgenericTopologyBuilder();
        builder.addLayer(3);
        builder.addLayer(3);
        builder.addLayer(1);
        builder.setPrototypeWeight(base);
        builder.initialize();
        topo.setTopologyBuilder(builder);
        topo.initialize();


        //alternative weights setter------------------------------------
        Vector weights = new Vector();

        weights.add(new Real(0.1));
        weights.add(new Real(0.4));
        weights.add(new Real(0.7));

        weights.add(new Real(-0.8));
        weights.add(new Real(-0.1));
        weights.add(new Real(0.30));

        weights.add(new Real(0.2));
        weights.add(new Real(-0.9));
        weights.add(new Real(0.6));

        topo.setWeights(weights);

        //--------------------------------------------------------------

        //GenericTopology topo = new GenericTopology(new FFNNStaticTopologyBuilder());

        System.out.println("Sigmoid function test ===============================================");
        SigmoidOutputFunction s = new SigmoidOutputFunction();
        System.out.println("raw output for  0    = " + s.computeFunction(new Real(0)));
        System.out.println("raw output for  0.5  = " + s.computeFunction(new Real(0.5)));
        System.out.println("raw output for  1    = " + s.computeFunction(new Real(1)));
        System.out.println("raw output for  -0.5 = " + s.computeFunction(new Real(-0.5)));
        System.out.println("raw output for  -1   = " + s.computeFunction(new Real(-1)));
        System.out.println("raw output for  10   = " + s.computeFunction(new Real(10)));
        System.out.println("raw output for  -10  = " + s.computeFunction(new Real(-10)));

        //TestNeuronAndEvaluateFFNN test = new TestNeuronAndEvaluateFFNN();




        //NeuronPipeline neuron = new DotProductSigmoidPipeline(new SigmoidOutputFunction(1));

        Vector ins = new Vector();
        ins.add(new Real(-0.765));
        ins.add(new Real(0.112));

        Vector targt = new Vector();
        targt.add(new Real(0.7666));
        NNPattern p = new StandardPattern(ins, targt);

        TypeList result = new TypeList();

        ErrorSignal delta = new SquaredErrorFunction();

        FFNN_GD_TrainingStrategy trainer = new FFNN_GD_TrainingStrategy();
        trainer.setTopology(topo);
        trainer.setDelta(delta);
        trainer.setMomentum(0.9);
        trainer.setLearningRate(0.5);
        trainer.initialize();

        for (int counter = 0; counter < 100; counter++){

            result = topo.evaluate(p);

            Double real = ((Real) result.get(0)).getReal();

            System.out.println("-----------------------------------------------------");
            System.out.println("The final output result: " + real.toString());



            trainer.invokeTrainer(p);

        }

        //after training
             result = topo.evaluate(p);


        Double real = ((Real) result.get(result.size()-1)).getReal();


        System.out.println("The final output after training = result: " + real.toString());
        System.out.println("-----------------------------------------------------\n");

        //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

        FFNNTopology ffnn = new FFNNTopology(2, 2, 1, 0.5, 0.9);

        Vector weightsB = new Vector();
        weightsB.add(new Real(0.1));
        weightsB.add(new Real(0.4));
        weightsB.add(new Real(0.7));

        weightsB.add(new Real(-0.8));
        weightsB.add(new Real(-0.1));
        weightsB.add(new Real(0.3));

        weightsB.add(new Real(0.2));
        weightsB.add(new Real(-0.9));
        weightsB.add(new Real(0.6));

            ffnn.setWeights(weightsB);

        for (int counter = 0; counter < 100; counter++){

            result = new TypeList();
            result = ffnn.evaluate(p);
            real = ((Real) result.get(0)).getReal();

            System.out.println("__________________________________________________________");
            System.out.println("FFNNTopology---- output result: " + real.toString());


            ffnn.train();
            result = ffnn.evaluate(p);
            real = ((Real) result.get(0)).getReal();

        }

        System.out.println("FFNNTopology final output after training = result: " + real.toString());
        System.out.println("__________________________________________________________\n");


    }
}
