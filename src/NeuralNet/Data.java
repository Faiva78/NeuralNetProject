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

    public double getMaxError() {
        double maxErr = 0;

        for (Sample sample : SampleList) {
            double thisErr = sample.sampleRms.getSampletError();
            if (thisErr > maxErr) {
                maxErr = thisErr;
            }
        }
        return maxErr;
    }

    public void normalizeSamples() {

        // get max
        double maxIn = 0;
        double maxOut = 0;

        double minIn = 0;
        double minOut = 0;

        
        
        
        for (int i = 0; i < SampleList.size(); i++) {

            double[] in = SampleList.get(i).inputData;
            double[] test = SampleList.get(i).testData;
            if ((in[0] > maxIn)|(i==0)) {
                maxIn = in[0];
            }
            if ((in[0] < minIn)|(i==0)) {
                minIn = in[0];
            }

            if ((test[0] > maxOut)||(i==0)) {
                maxOut = test[0];
            }
            if ((test[0] < minOut)|(i==0)) {
                minOut = test[0];
            }

        }

        for (int i = 0; i < SampleList.size(); i++) {
            double[] in = SampleList.get(i).inputData;
            double[] test = SampleList.get(i).testData;
            

            double[] normIn = {Uti.normalize(minIn, maxIn, in[0])};
            double[] normTest = {Uti.normalize(minOut, maxOut, test[0])};
           

            Sample sample = new Sample(this);
            sample.inputData = normIn;
            sample.testData = normTest;
            sample.outputData = new double[normTest.length];
            SampleList.set(i, sample);

        }

    }

}
