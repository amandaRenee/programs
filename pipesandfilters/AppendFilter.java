////////////////////////////////////////////////////////////////////////////////
//Filename: AppendFilter.java
//Class/Project: CS 490 (Fall 2015) / Project 1 (Pipes and Filters Pattern)
//Name: Amanda Donaldson
//Modified: 9/25/2015
//
//Description: Filter 3: Appends the digit representing the multiplicity sum to
//              the end of the original number. And then prints the result to the 
//              output file. If the poison pill ("0") is read from the in queue,
//              then the current thread shuts down and the output file is closed.
//
////////////////////////////////////////////////////////////////////////////////

package pipesandfilters;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mandi
 */
public class AppendFilter extends Filter{
  
    //Fields
    private String set = "", outFile = "", orgNum = "";
    private BlockingQueue<String> inQ = new ArrayBlockingQueue<String>(200);
    private File oFile;
    private PrintWriter pw;
    private BufferedWriter bw;
    private FileWriter fw;
    private volatile boolean stop = false;
    
    //Methods
    public AppendFilter(BlockingQueue iQ, String of) {
        SetQ(iQ, null);
        outFile = of;
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
        return null;
    }//end GetOutQ

    @Override
    public void SetQ(BlockingQueue q, BlockingQueue p) {
        inQ = q;
    }//end SetQ
    
    @Override
    public void stopThread(){
        stop = true;
    }//end stopThread()
    
    @Override
    public void run() {
        oFile = new File(outFile);
        if(oFile.exists()) { // delete file if it exists
        oFile.delete();
        }//end if
        while(!stop){
            try {
                set = (String)inQ.take();
                orgNum = GetSet().substring(0, (GetSet().length() - 11));
                
                if("0".equals(orgNum)){ // poison pill
                    stopThread();
                }else{
                    String ans = AppendDigit(GetSet());
                    System.out.println(ans);
                    
                    /////////PRINT TO FILE//////////

                   try{
                        fw = new FileWriter(outFile, true);
                        bw = new BufferedWriter(fw);
                        pw = new PrintWriter(bw);

                       pw.println(ans);
                   }catch(FileNotFoundException e){
                       e.printStackTrace();
                   } catch(SecurityException e){
                       e.printStackTrace();
                   }catch(IOException e){
                       e.printStackTrace();
                   }finally{
                        if(pw != null){
                            pw.close();
                        }//end if
                   }//end try-catch
                }//end if-else

            }catch (InterruptedException ex) {
                Logger.getLogger(AppendFilter.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(AppendFilter.class.getName()).log(Level.SEVERE, null, ex);
            }//end try-catch
        }//end while
        return;
    }//end run()

}//end class AppendFilter
