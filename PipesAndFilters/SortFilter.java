////////////////////////////////////////////////////////////////////////////////
//Filename: SortFilter.java
//Class/Project: CS 490 (Fall 2015) / Project 1 (Pipes and Filters Pattern)
//Name: Amanda Donaldson
//Modified: 9/25/2015
//
//Description: Filter 1: Sort the digits in the number into sets of integer numbers
//              If the poison pill ("0") is read from the in queue,
//              then the pill is passed to the next thread in the out queue and
//              the current thread shuts down.
//
////////////////////////////////////////////////////////////////////////////////

package pipesandfilters;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mandi
 */
public class SortFilter extends Filter{
    
    //FIELDS
    private int DIGITS = 8; //max # of digits in set
    private String set = "";
    private BlockingQueue<String> inQ = new ArrayBlockingQueue<String>(200);
    private BlockingQueue<String> outQ = new ArrayBlockingQueue<String>(200);
    String line = "";
    String split = ",";
    String extension = ".txt";
    private volatile boolean stop = false;
    //private String pill = "0";

    //METHODS
    public SortFilter(BlockingQueue iQ, BlockingQueue oQ){
        SetQ(iQ, oQ);
    }//end constructor 

    @Override
    public String GetSet() {
        return set;
    }//GetSet()

    @Override
    public void SetSet(String s) {
        set = s;
    }//end SetSet()

    @Override
    public BlockingQueue GetInQ() {
        return inQ;
    }//end GetQ()
    
    @Override
    public BlockingQueue GetOutQ() {
        return outQ;
    }//end GetQ()

    @Override
    public void SetQ(BlockingQueue iQ, BlockingQueue oQ) {
        inQ = iQ;
        outQ = oQ;
    }// end SetQ()
   
    @Override
    public void stopThread(){
        stop = true;
    }//end stopThread()
    
    @Override
    public void run() {        
        int setNum = 0;
        String str = "";
        while( !stop){
            try {
                str = (String)GetInQ().take();
                if(!"0".equals(str)){ //poison pill
                    SetSet(SortSet(str));   //sort numbers into buckets and concats it to the orig # //Works 
                    GetOutQ().put(GetSet()); // put set on out buffer                    
                }else{
                    GetOutQ().put("0"); //pass the pill
                    stopThread();
                }//end if-else
                
            } catch (InterruptedException ex) {
                Logger.getLogger(SortFilter.class.getName()).log(Level.SEVERE, null, ex);
            }//end try-catch
        }//end while
    }//end run()

}//end class SortFilter


