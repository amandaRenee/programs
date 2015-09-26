////////////////////////////////////////////////////////////////////////////////
//Filename: SumFilter.java
//Class/Project: CS 490 (Fall 2015) / Project 1 (Pipes and Filters Pattern)
//Name: Amanda Donaldson
//Modified: 9/25/2015
//
//Description: Filter 2: Sums the multiplicity of the sets greater than 1.
//             If the poison pill ("0") is read from the in queue,
//              then the pill is passed to the next thread in the out queue and
//              the current thread shuts down.
//
////////////////////////////////////////////////////////////////////////////////

package pipesandfilters;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mandi
 */
public class SumFilter extends Filter{

    //Fields
    private String set = "", orgNum = "";
    private BlockingQueue<String> inQ = new ArrayBlockingQueue<String>(200);
    private BlockingQueue<String> outQ = new ArrayBlockingQueue<String>(200);
    private volatile boolean stop = false;
    
    //Methods
    public SumFilter(BlockingQueue iQ, BlockingQueue oQ){
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
    }//end GetInQ()

    @Override
    public BlockingQueue GetOutQ() {
        return outQ;
    }//end GetOutQ()
    
    @Override
    public void SetQ(BlockingQueue q, BlockingQueue p) {
        inQ = q;
        outQ = p;
    }
    @Override
    public void stopThread(){
        stop = true;
    }//end stopThread()
    
    @Override
    public void run() {
        int sumNum = 0;
        String setNum = "";
        while(!stop){
            try {
                set = (String)GetInQ().take();
                if("0".equals(set)){ //poison pill
                    GetOutQ().put("0"); //pass pill
                    stopThread();
                }else{
                    SetSet(SumSet(set)); // sum up bucket and append sum to end of string
                    GetOutQ().put(GetSet());     // put set on out buffer
                    
                }//end if-else
            } catch (InterruptedException ex) {
                Logger.getLogger(SumFilter.class.getName()).log(Level.SEVERE, null, ex);
            }//end try-catch
        }//end while
        
    }//end run()  
  
}// end class SumFilter
