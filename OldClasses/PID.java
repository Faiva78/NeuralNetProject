package NeuralNet;

/**
 *
 * @author Fiava78
 */
public class PID {

    public double kP = 0.002;
    public double kI = 0.0001;
    public double kD = 0.0001;
    public double T;

    private double P, I, D;
    private double P0 = 0;

    public double evaluate(double input) {

        P = T - input;
        D = P0 - P;
        I = I + P;
        P0 = P;
        double pid = (kP * P) + (kI * I) + (kD * D);
        return pid;

    }

}
