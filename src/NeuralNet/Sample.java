
package NeuralNet;

/**
 * base sample class for the data collection
 */
public class Sample {
    /**
     * input data
     */
    double[] inputData;
    /**
     * desidered test data
     */
    double[] testData;
    /**
     * output data of the net
     */
    double[] outputData;
    /**
     * sample sampleError
     */
    //double sampleError =1;
    public Rms sampleRms = new Rms();
    private final Data outer;

    public Sample(final Data outer) {
        this.outer = outer;
    }
    
}
