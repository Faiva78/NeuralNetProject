package NeuralNet;

import java.util.ArrayList;

/**
 * collection of sample data
 */
public class Data {

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
        
         public Rms sampleRms=new Rms();
        
        

    }

    /**
     * List of sample data
     */
    public ArrayList<Sample> SampleList = new ArrayList<>();


    
    /**
     * add a new sample data
     *
     * @param in array of double with the input data
     * @param test array of double with the expected data
     */
    public void addSample(double[] in, double[] test) {
        Sample sample = new Sample();
        sample.inputData = in;
        sample.testData = test;
        sample.outputData = new double[test.length];
        SampleList.add(sample);
    }

   
    /**
     * get the total error of all samples
     *  
     * @return sum of the total error
     */
    public double getDataError() {
        double t = 0;
        for (Sample sample : SampleList) {
            t = t + sample.sampleRms.getSampletError();
        }
        return t;
    }
}