
package NeuralNet;

/**
 * base axon class
 */
public class Assone extends Cellula{
                
    /** neuron where this axon is connected from */
    public Neurone fromNeuron;
    
    /** weight of the axon initialized as a random number from 0 to 1*/
    public double weight=(Math.random()*2)-1;;
    //public double newWeight;
    
    /** delta error dalculated by backpopagation */
    public double deltaError;
    
    /** update the axon weight
     * @param learningRate */
    public void updateWeight(double learningRate){
        double x =weight-((learningRate* deltaError)*1);
        deltaError=0;
        weight=x;
    }
    
}
