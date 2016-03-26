/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NeuralNet;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author alessia
 */
public class BackPropagation extends Feedforward{
  
    public  double ETA=1;
    public  double MOMENTUM=0.1;
    private final Random rand = new Random();

    public BackPropagation(Net net, Data data) {
        super(net, data);
    }

    
    public void train(Net net,Data data){

        // for as many as samples
        for (int i = 0; i < data.SampleList.size(); i++) {

            // get a random sample
            Data.Sample sample = data.SampleList.get(rand.nextInt(data.SampleList.size()));
            
            //evaluate
            evaluate(net, sample);

            //learn
            train(net, sample);
   
        }
        
             trainingBatches++;
    }
    
    
    public void train(Net net, Data.Sample sample){
        
        // declare the derivations variables NOT WORKING WELL!!!
        // work first on the last output layer
        Layer outlayer = net.LayerList.get(net.LayerList.size() - 1);

        //set the last layer total error
        outlayer.LayerError = 0;

        //for every neuron in the last layer CALCULATE OUTPUT ERROR and new weighs
        for (int n = 0; n < outlayer.layerNeurons.size(); n++) {

            double dError_dOut, dOut_dNet;
            double dNet_dWeight, dError_dWeight;

            // get the neuron
            Neurone neuron = outlayer.layerNeurons.get(n);

            //get the neuron axons connected from
            ArrayList<Assone> assoneInList = neuron.axonFrom;

            // differentiate the totale deltaError with respect to neuron output
            dError_dOut = +(sample.testData[n] - neuron.A_activation);
            neuron.dError_dOut = dError_dOut;

            // differentiate the output with respect to net totoal weights
            dOut_dNet = neuron.ActivationFunction.derivation(neuron.getWeight());
            neuron.dOut_dNet = dOut_dNet;

            // for each axon in neuron input
            for (Assone assoneIn : assoneInList) {

                //differentiate the net total weights with respect to axon weight
                dNet_dWeight = assoneIn.fromNeuron.A_activation; //out of prevous neuron

                //  partial derivative of the total error with respect to weight
                dError_dWeight = dError_dOut * dOut_dNet * dNet_dWeight;

                //  set the axon deltaError delta( to be updated)
                assoneIn.deltaError = dError_dWeight;   //assoneIn.newWeight = assoneIn.weight-(learningRate * dError_dW);

                /// the new weights MUST BE updated after the full propagation
            }

            //update the last layer total error
            outlayer.LayerError = outlayer.LayerError + (dError_dOut * dOut_dNet);

        }
        // end of last layer

        // ...................   SECOND PASS ,;;;;;;;;;;;;;;;;;;;;;
        //start from the penultimaate layer thru the second
        //calculate the new weights
        // for every layer starting from second from last
        for (int layerNum = net.LayerList.size() - 2; layerNum > 0; layerNum--) {

            double dOutH_dNetH, dNet_dEighth, dError_dWeightH;

            // get the layer
            Layer layer = net.LayerList.get(layerNum);

            //for as many layers in layerslist
            for (Neurone layerNeuron : layer.layerNeurons) {
                //get the neuron list
                ArrayList<Neurone> layerNeurons = layer.layerNeurons;

                //for as many as neurons in neuron list
                for (Neurone neuron : layerNeurons) {

                    //get the neuron axons connected from
                    ArrayList<Assone> assoneInList = neuron.axonFrom;

                    // differentiate the output with respect to net totoal weights
                    dOutH_dNetH = neuron.ActivationFunction.derivation(neuron.getWeight());

                    // for each azon in neuron input
                    for (Assone assoneIn : assoneInList) {

                        //differentiate the net total weights with respect to axon weight
                        dNet_dEighth = assoneIn.fromNeuron.A_activation; //out of prevous neuron

                        //  partial derivative of the total error with respect to weight
                        //dError_dWeightH = dErrotTot_OutH * dOutH_dNetH * dNet_dEighth;
                        dError_dWeightH = outlayer.LayerError * dOutH_dNetH * dNet_dEighth;

                        //  set the axon deltaError delta( to be update)
                        assoneIn.deltaError = dError_dWeightH;   //assoneIn.newWeight = assoneIn.weight-(learningRate * dError_dW);

                    }
                }
            }
        }
        /// the new weights MUST BE updated after the full propagation
        updateAllWeights(net);
    
    
    }
    
    /**
     * Update all the weight with the eta learning rate
     */
    private  void updateAllWeights(Net net) {

        //for every layer excluded the first (no weights availables)
        for (int i = 1; i < net.LayerList.size(); i++) {

            //get the current layer
            Layer layer = net.LayerList.get(i);

            //for every neuron in layer
            for (Neurone neuron : layer.layerNeurons) {

                //foe everu axon from in ineuron
                for (Assone axonFrom : neuron.axonFrom) {

                    // update the weight with eta and momentum
                    axonFrom.weight = axonFrom.weight - ((ETA * axonFrom.deltaError) + (MOMENTUM * axonFrom.deltaError_1));

                    //update the last deltaerror with current
                    axonFrom.deltaError_1 = axonFrom.deltaError;
                }

            }
        }

    }
    
    
    
}



