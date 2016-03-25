package NeuralNet;

import java.util.ArrayList;


public class Net  {


    /**
     * List of neuron layers
     */
    public ArrayList<Layer> LayerList = new ArrayList<>();


    /**
     * Activation function
     *
     */
    public Func AcivationFunction = new Sigmoid();

    /**
     * the topology of the net
     *
     */
    private final String Topology = "";

    /**
     * Create a new layer in the net
     *
     * @param NumNeurons number of neurons of the layer
     * @param ActivFunc the activation functyion
     */
    public void addLayer(int NumNeurons, Func ActivFunc) {
        
        //crete new layer
        Layer newLayer = new Layer();
        
        // for wow moch neuron desidered
        for (int i = 0; i < NumNeurons; i++) {
            
           //create a brand new neuron
            Neurone newNeuron = new Neurone();
            
            //sets the actiivation function
            newNeuron.ActivationFunction = ActivFunc;
            
            //add it to the neuronlist
            newLayer.layerNeurons.add(newNeuron);
        }
        //add the newly created layer to the layer list
        LayerList.add(newLayer);
        
        // if are more than one layer in layer list create the axon connection
        if (LayerList.size()>1) {
            
            // add connection from this layer neurons to bias neuron
            addAzonsFrom(newLayer.layerNeurons, newLayer.neuronBias);
            
            // get prev layer
            Layer PrevLay = LayerList.get(LayerList.size()-2);
            
            // for each neuron in previous layer
            for (Neurone prevLayerNeuron : PrevLay.layerNeurons) {
                
                // connect from this layer neuron to the previous
                addAzonsFrom(newLayer.layerNeurons, prevLayerNeuron);
            }
        }
        
    }

    /**
     * Create the layers and neuron from the topology array
     *
     * @param topology array with the topology of the net
     */
    private void createNet(int topology[]) {
        LayerList.clear();
        //for as many elements in topology
        for (int i = 0; i < topology.length; i++) {

            addLayer(topology[i], AcivationFunction);
        }
    }

    /**
     * Create the layers and neuron from the topology array
     *
     * @param TopologyString String with the topology of the net in the form of
     * "2,3,2"
     */
    public void createNet(String TopologyString) {
        int[] topology = Uti.str2ArrayInt(TopologyString);
        createNet(topology);
    }

    /**
     * add axon links from thne neuron N to all neurons of Nlist
     */
    private void addAzonsFrom(ArrayList<Neurone> Nlist, Neurone N) {

        for (Neurone neuron : Nlist) {
            //create a new axon
            Assone asson = new Assone();

            // set axons in neuron reference link
            asson.fromNeuron = N;

            //add the axon to the neuron from list
            neuron.axonFrom.add(asson);
        }

    }

   
    /**
     * get the net topology
     *
     * @return string of topology
     */
    public String getTopology() {
        if ("".equals(Topology)) {

            StringBuilder str = new StringBuilder("");
            for (int i = 0; i < LayerList.size() - 1; i++) {
                Layer get = LayerList.get(i);
                str.append(String.valueOf(get.layerNeurons.size()));
                str.append(",");
            }
            str.append(String.valueOf(LayerList.get(LayerList.size() - 1).layerNeurons.size()));
            return str.toString();

        } else {
            return Topology;
        }
    }
}
