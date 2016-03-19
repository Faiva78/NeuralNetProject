package NeuralNet;

import java.io.FileWriter;
import java.io.IOException;
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
        double sampleError ;

        /**
         * mean sampleError function
         *
         * @deprecated
         */
        //MSE costFunction;
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
            t = t + sample.sampleError;
        }
        return t;
    }


    /**
     * print sample output data
     *
     * @deprecated ONLY FOR DEBUGGING
     */
    private void PrintData(Data.Sample sample) {

        for (int i = 0; i < sample.outputData.length; i++) {

            System.out.format("in:%.0f out:%.2f test:%.0f err:%.2f%n", sample.inputData[0], sample.outputData[0], sample.testData[0], sample.sampleError);
        }
    }

    /**
     * convert a function with 2 free parameters in a data format
     *
     * @param function
     * @param from
     * @param to
     * @param step
     */
    public void DataFunc2D(Func function, double from, double to, double step) {
        SampleList.clear();
        double dat1 = from;
        double dat2 = from;
        while (dat1 < to) {
            if ((to - from) > 0) {
                dat1 = dat1 + step;
            } else {
                dat1 = dat1 - step;
            }
            while (dat2 < to) {
                if ((to - from) > 0) {
                    dat2 = dat2 + step;
                } else {
                    dat2 = dat2 - step;
                }

            }
            addSample(new double[]{dat1, dat2}, new double[]{function.evaluate(dat1, dat2)});
        }
    }

    /**
     * convert a function with 1 free parameter in a data format
     *
     * @param function
     * @param from
     * @param to
     * @param step
     */
    public void DataFunc1P(Func function, double from, double to, double step) {
        SampleList.clear();
        double dat = from;
        while (dat <= to) {
            addSample(new double[]{dat}, new double[]{function.evaluate(dat)});
            if ((to - from) > 0) {
                dat = dat + step;
            } else {
                dat = dat - step;
            }

        }
    }

    /**
     * print all the sample in data
     *
     * @param data the collection of data
     * @deprecated ONLY FOR DEBUGGING
     */
    public void printSampleData(Data data) {

        int samNum = 0;
        for (Data.Sample sample : SampleList) {

            StringBuilder str = new StringBuilder();
            str.append(String.format("D%d ", samNum)).append((char) 9);
            for (int i = 0; i < sample.inputData.length; i++) {
                double data1 = sample.inputData[i];
                str.append(String.format("I%d=%.3f ", i, data1)).append((char) 9);
            }
            for (int i = 0; i < sample.testData.length; i++) {
                double data1 = sample.testData[i];
                str.append(String.format("T%d=%.3f ", i, data1)).append((char) 9);
            }

            for (int i = 0; i < sample.outputData.length; i++) {
                double data1 = sample.outputData[i];
                str.append(String.format("O%d=%.3f ", i, data1)).append((char) 9);
            }
            str.append(String.format("E=%.3f ", sample.sampleError));
            System.out.println(str);
            samNum++;
        }

    }

    /**
     * export all the sample data to a CSV file
     *
     * @param location where to save the file
     */
    public void exportSampleDataToCSV(String location) {
        char delimiter = (char) 9;
        char newLine = (char) 13;
        try (FileWriter filewriter = new FileWriter(location)) {
            filewriter.append("Datasample");
            filewriter.append(delimiter);

            filewriter.append("Input");
            filewriter.append(delimiter);

            filewriter.append("Test");
            filewriter.append(delimiter);

            filewriter.append("Output");
            filewriter.append(delimiter);

            filewriter.append("Error");
            filewriter.append(newLine);

            int samNum = 0;
            for (Sample sample : SampleList) {

                filewriter.append(String.valueOf(samNum));
                filewriter.append(delimiter);
                for (int i = 0; i < sample.inputData.length; i++) {
                    double data1 = sample.inputData[i];
                    filewriter.append(String.valueOf(data1));
                    filewriter.append(delimiter);
                }
                for (int i = 0; i < sample.testData.length; i++) {
                    double data1 = sample.testData[i];
                    filewriter.append(String.valueOf(data1));
                    filewriter.append(delimiter);
                }

                for (int i = 0; i < sample.outputData.length; i++) {
                    double data1 = sample.outputData[i];
                    filewriter.append(String.valueOf(data1));
                    filewriter.append(delimiter);
                }

                filewriter.append(String.valueOf(sample.sampleError));
                filewriter.append(newLine);

                samNum++;
            }
            filewriter.flush();
        } catch (IOException e) {
            System.out.println("Error in exporting CSV !!!");
        }

    }

}
