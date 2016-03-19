/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NeuralNet;

/**
 *
 * @author Faiva78
 */
public class Cellula {
   private static int idStatic=0;  
   private int id;
   
   private static int idIncS(){
   idStatic++;
   return idStatic;
   }
   private static void idDecS(){
   idStatic++;
   }
   private static int idGetS(){
   return idStatic;
   }
   private void idSet(int i){
   id=i;
   }
   public int idGet(){
       return id;
   }

    public Cellula() {
        idSet(idIncS());         
    }

    @Override
    protected void finalize() throws Throwable {
        idDecS();
        super.finalize();
    }
   
}
