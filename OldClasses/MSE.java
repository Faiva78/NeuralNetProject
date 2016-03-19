package NeuralNet;

public class MSE {

    /**
     * number of samples
     */
    public double count = 0;

    private double sum = 0;

    /**
     * totoal error
     */
    public double error = 0;

    /**
     * add the error to the sum
     * @param x
     * @param fx
     * @return 
     */
    public double add(double x, double fx) {
        count++;
        sum = sum + Func.meanError(x, fx);
        error = sum / count;
        return error;
    }

}
