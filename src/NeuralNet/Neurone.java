package NeuralNet;

import java.util.ArrayList;


public class Neurone extends Cellula {

    /** neuron output*/
    public double A_activation = 0;
    public double dError_dOut,dOut_dNet;
    
    public boolean isBias=false;
    
    /** neuron net error */
    //public double E_error = 0;
    
    /**activation function */
    //public Func ActivationFunction = new Func(" 1 / ( 1 + ( e ^ ( - 1 * ( x - 1 ) ) ) )");
    public Func ActivationFunction ; // more fast function computation
    
    /** list of axon where is connected from */
    public ArrayList<Assone> axonFrom = new ArrayList<>();

    
    
    
    
    //return the sum of the weights
    public double getWeight() {

        //weighted sum of the inputData
        double weightedSum = 0;

        // for as many as input axons
        for (Assone axonFrom1 : axonFrom) {
            //Assone ax = axonFrom.get(y);
            //multiplicate the neuron output value with the axon weight
            double weightMultActivation = axonFrom1.fromNeuron.A_activation * axonFrom1.weight;
            // sum over the partials 
            weightedSum = weightedSum + weightMultActivation;
        }

        //returnd the average the weighted sum W
        return weightedSum / axonFrom.size();
    }

 

    /**
     * update the neuron axon bias weight
     * @param learningRate
     */
    public void updateFromAxonWeights(double learningRate) {
        for (Assone axon : axonFrom) {
            axon.updateWeight(learningRate);
        }

    }

}
