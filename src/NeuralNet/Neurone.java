package NeuralNet;

import java.util.ArrayList;

public class Neurone extends Cellula {

    /**
     * neuron output
     */
    public double A_activation = 0;
    public double dError_dOut, dOut_dNet;


    public Neurone() {
    }

    public Neurone(double biasActivation) {
        A_activation=biasActivation;
    }
    /**
     * activation function
     */
    public Func ActivationFunction;

    /**
     * list of axon where is connected from
     */
    public ArrayList<Assone> axonFrom = new ArrayList<>();

    //return the sum of the weights

    /** return the sum of the weight connected
     *
     * @return
     */
        public double getWeight() {

        //weighted sum of the inputData
        double weightedSum = 0;

        // for as many as input axons
        for (Assone axonFrom1 : axonFrom) {
            //multiplicate the neuron output value with the axon weight
            double weightMultActivation = axonFrom1.fromNeuron.A_activation * axonFrom1.weight;
            // sum over the partials 
            weightedSum = weightedSum + weightMultActivation;
        }

        //returnd the average the weighted sum W
        return weightedSum / axonFrom.size();
    }
}
