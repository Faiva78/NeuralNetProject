package NeuralNet;

import java.util.ArrayList;

/**
 * collection of sample data
 */
public class Data {


    /**
     * List of sample data
     */
    public ArrayList<Sample> SampleList = new ArrayList<>();
    public ErrorTrack errorTrack = new ErrorTrack();

    
    /**
     * add a new sample data
     *
     * @param in array of double with the input data
     * @param test array of double with the expected data
     */
    public void addSample(double[] in, double[] test) {
        Sample sample = new Sample(this);
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
    public double getMaxError(){
        double maxErr=0;
    
        for (Sample sample : SampleList) {
            double thisErr= sample.sampleRms.getSampletError();
            if (thisErr>maxErr) {
                maxErr=thisErr;
            }
        }
        return maxErr;
    }
    
}