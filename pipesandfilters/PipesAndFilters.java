////////////////////////////////////////////////////////////////////////////////
//Filename: PipesAndFilters.java
//Class/Project: CS 490 (Fall 2015) / Project 1 (Pipes and Filters Pattern)
//Name: Amanda Donaldson
//Modified: 9/25/2015
//
//Description: An implementation of the Pipes and Filters Pattern. It uses 
//              multiple filters and passive pipes. This program reads in numbers
//              and manipulates them, to display how multi-threading is done.
//              The numbers are read in and kept only if they are 1-8 digits long.
//              The first filter sorts the digits in the number into sets of integer
//              numbers in the range 0-9. The second filter sums the multiplicity
//              of the sets greater than 1. The third filter appends the digit
//              representating the multiplicity sum to the end of the orginal number.
//              And then write it to the output file.
//
//Input: 2 ways. Read in from command line number by number or reads in a csv .txt 
//       file. See README for details. 
//
////////////////////////////////////////////////////////////////////////////////

package pipesandfilters;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.FileReader;
import java.lang.StringBuilder;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.String;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author mandi
 */
public class PipesAndFilters {
    
    public static void main(String[] args) throws InterruptedException {
        
        //Fields
        String inFile = "";
        String outFile = "";
        BufferedReader buffR = null;
        BlockingQueue<String> blockQ1 = new ArrayBlockingQueue<String>(200);
        BlockingQueue<String> blockQ2 = new ArrayBlockingQueue<String>(200);
        BlockingQueue<String> blockQ3 = new ArrayBlockingQueue<String>(200);

        String line = "";
        String split = ",";
        String extension = ".txt";
        int DIGITS = 8;

        
        // args = [set] [out.txt]
        //If the first field is a string, then read from a file
        //If the first field is a number, then input is number-by-number
        //  If input set is larger than 8 digits - error        
        //  If input set is 0 - poison pill
        if(args.length == 2 ){ // valid arg amount
                inFile = args[0];
                outFile = args[1];
            if(args[0].contains(".txt") == true){ //Then input is a file
              try{
                    buffR = new BufferedReader(new FileReader(inFile));
                    
                    while((line = buffR.readLine()) != null){
                       String[] input = line.split(split);
                                           
                        for(int i = 0; i < input.length; i++){ // read input file into a blocking queue
                            try{
                                if(input[i].length() > 0 && input[i].length() <= DIGITS){// only allow sets with 1 - 8 digits
                                 blockQ1.put(input[i].trim());                                       
                                    
                                }//end if
                            }catch(InterruptedException e){
                                e.printStackTrace();
                            }finally{   
                            }//end try-catch
                        }//end for   
                    }//end while
                    startThreads(blockQ1, blockQ2, blockQ3, outFile);

                }catch(FileNotFoundException e){
                    e.printStackTrace();
                }catch(IOException e){
                    e.printStackTrace();
                }finally{
                    if(buffR != null){
                        try{
                            buffR.close();
                        }catch(IOException e){
                            e.printStackTrace();
                        }//end try-catch
                    }//end if
                }//end try-catch
     
        }
        else
        {//Then input is number-by-number
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));  
            String inSet = "";
            
            if("0".equals(args[0])){// if first entry in poison pill
                System.exit(0);// shut down program
            }else{
                blockQ1.put(args[0]); //add firt number to the pipe queue
            }//end if-else
            int count = 0;
            try{
                while(!"0".equals(inSet)){ //while not poison pill
                    
                    System.out.println("Enter next number set (1-8 digits long). Enter 0 to quit.");
                    inSet = in.readLine();
                        count = inSet.length(); // 1-8 digits long are valid input
                        if(count > 0 && count <= DIGITS){
                            blockQ1.put(inSet);
                        
                        }else{
                            System.out.println("INVALID number");
                        }//end if-else
                   // }//end if-else
                }//end while
                startThreads(blockQ1, blockQ2, blockQ3, outFile);

            }//end try
            catch(Exception e){
                    e.printStackTrace();
                    }
            finally{
                if(in != null){
                        try{
                            in.close();
                        }catch(IOException e){
                            e.printStackTrace();
                        }//end try-catch
                    }//end if
            }//end try-catch
        }//end if-else       
        }else{// invalid arg amount
            System.out.println("Invalid arguments. Exiting.");
            System.exit(0);
            }//end if-else
        
    }//end main
    
    public static void startThreads(BlockingQueue q1, BlockingQueue q2, BlockingQueue q3, String of) throws InterruptedException{
        SortFilter sortF = new SortFilter(q1, q2);
        Filter sumF  = new SumFilter(q2, q3);
        Filter appF = new AppendFilter(q3, of);

        Thread t1 = new Thread(sortF);
        Thread t2 = new Thread(sumF);
        Thread t3 = new Thread(appF);

        t1.start();
        t2.start();
        t3.start();

    }//end startThreads()
     
}//end class PipesAndFilters
    

