/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NeuralNet;

import javafx.application.Application;

/**
 *
 * @author alessia
 */
public final class Timer {
    private  long timeOut;
    private  long time;
    public boolean check(){
    
       if ((System.currentTimeMillis() - time) > timeOut) {
           this.reset();
           return  true;
            }
    
    return false;
    }

    public Timer() {
    }
    
    public void reset(){
    time = System.currentTimeMillis();
    }

    public Timer(long timeout) {
        this.timeOut=timeout;
        this.reset();
    }

    public long getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(long timeOut) {
        this.timeOut = timeOut;
        reset();
    }
 

    
      
}
