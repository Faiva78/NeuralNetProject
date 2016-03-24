package NeuralNet;

import java.util.ArrayList;

public class Layer extends  Cellula{
    public ArrayList<Neurone> layerNeurons = new ArrayList<>();
    public Neurone neuronBias = new Neurone();
    public double LayerError=0;
    public Layer() {
    
    neuronBias.A_activation=1;
    
    }
    

}
