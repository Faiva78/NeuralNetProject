package NeuralNet;

import java.util.ArrayList;

public class Layer extends  Cellula{
    
    /** List of neurons in layer
     *
     */
    public ArrayList<Neurone> layerNeurons = new ArrayList<>();
    
    /** neuron of the bias of this layer
     *
     */
    public Neurone neuronBias = new Neurone();
    
    /** the error of the layer dErrTot/dOut
     *
     */
    public double LayerError=0;
    
    /** Initialize the neuron bias  activatiuon to 1
     *
     */
    public Layer() {
    
    neuronBias.A_activation=1;
    
    }
    

}
