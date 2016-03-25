
package NeuralNet;

/**
 *
 * @author faiva78
 */
public final class Feedforward {
    
    public static final void evaluate(Net net, Data.Sample sample ){
   
    //clear the sample error data
    sample.sampleRms.clear();

        // for as many layers in the net
        for (int layerNum = 0; layerNum < net.LayerList.size(); layerNum++) {

            //get the  neuron layer list
            Layer layer = net.LayerList.get(layerNum);

            // for every neuron in the layer
            for (int neuronNum = 0; neuronNum < layer.layerNeurons.size(); neuronNum++) {
                Neurone neuron = layer.layerNeurons.get(neuronNum);

                // if is th first neuron layer so update the neuron outputs with input NetData
                if (layerNum == 0) {

                    // add the NetData from dataset inputData to input neuron values
                    layer.layerNeurons.get(neuronNum).A_activation = sample.inputData[neuronNum];

                } else { // else calculate the output

                    //evaluate the neuron output
                    neuron.A_activation = neuron.ActivationFunction.evaluate(neuron.getWeight());
                }
                // we are at the last layer neuron , update the last layer output sample Error
                if (layerNum == net.LayerList.size() - 1) {

                    // retrieve and add neuron output value to dataset
                    sample.outputData[neuronNum] = layer.layerNeurons.get(neuronNum).A_activation;
                    
                    // Calculate the neuron square mean deltaError 
                    sample.sampleRms.updateError(sample);
                }
                // finish parsing the layer neurons
            }
            // finished parsing the layer}
        }
    }
    
    
   public static final void  evaluate(Net net, Data data){
        for (Data.Sample sample : data.SampleList) {
            evaluate(net, sample);
        }
    }
    
}
