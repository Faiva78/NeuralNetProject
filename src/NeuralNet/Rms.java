
package NeuralNet;

/**
 *
 * @author faiva78
 */
public final class Rms {
    
    private double error=0;
    private double size=0;
    
    /** update the error of the sample data
     *
     * @param sample
     */
    public void updateError(Sample sample){
        
        // for every output data (neuron number of last layer)
        for (int i = 0; i < sample.outputData.length; i++) {
            
            // get the delta of every output
            double delta = sample.testData[i]-sample.outputData[i];
            
            // add the square of the error
            error += Math.pow(delta,2); 
            size +=1;
        }   
    }
     
    /** return the error 
     *
     * @return
     */
    public double getSampletError(){
        double err=0;
        if (size==0) {
            return err;
        }
        err=(error/size);
        return err;
        //return  Math.sqrt(error/size); // XXX why... why.....
    
    }
    
    public void clear(){
    error=0;
    size=0;
    }
    
}
