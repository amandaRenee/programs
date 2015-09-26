////////////////////////////////////////////////////////////////////////////////
//Filename: Filter.java
//Class/Project: CS 490 (Fall 2015) / Project 1 (Pipes and Filters Pattern)
//Name: Amanda Donaldson
//Modified: 9/7/2015
//
//Description: An implementation of the Pipes and Filters Pattern. It uses 
//              multiple filters and passive pipes.
//
////////////////////////////////////////////////////////////////////////////////

package pipesandfilters;

import java.lang.String;
import java.lang.Integer;
import java.lang.Thread;
import java.util.concurrent.BlockingQueue;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author mandi
 */
public abstract class Filter implements Runnable {
    
    //FIELDS
    private int DIGITS = 8; //max # of digits in set
    private int[] bucketArray = new int[10];
    private BlockingQueue<String> blockQ = new ArrayBlockingQueue<String>(200);
    private String bucket = "", set = "";
    private int num = 0;
    private String[] arr = new String[10];

    
   //METHODS
    //Constructor: takes original set numbers and puts in set array   
    public Filter(){};
    
    public abstract String GetSet();
    public abstract void SetSet(String s);
    public abstract BlockingQueue GetInQ();
    public abstract BlockingQueue GetOutQ();
    public abstract void SetQ(BlockingQueue q, BlockingQueue p);
    public abstract void stopThread();
    
    //Counts the occurances of each integer [0,9] and places the count into 
    // the corresponding place of the output integer. The output integer is 
    // formatted as so, 0123456789.
    // Ex. 11044378 input set would become the output set of 1201200110.
    public String SortSet(String sNum){
        System.out.println("enter sortSet");
        System.out.println(sNum);
        bucket = ""; set = ""; //clear the values
        Arrays.fill(bucketArray, 0);
        for(int i = 0; i < sNum.length(); i++){
            switch(sNum.charAt(i)){
                case '0': bucketArray[0] = bucketArray[0]+1;
                        break;
                case '1': bucketArray[1] = bucketArray[1]+1;
                        break;
                case '2': bucketArray[2] = bucketArray[2]+1;
                        break;
                case '3': bucketArray[3] = bucketArray[3]+1;
                        break;
                case '4': bucketArray[4] = bucketArray[4]+1;
                        break;
                case '5': bucketArray[5] = bucketArray[5]+1;
                        break;
                case '6': bucketArray[6] = bucketArray[6]+1;
                        break;
                case '7': bucketArray[7] = bucketArray[7]+1;
                        break;
                case '8': bucketArray[8] = bucketArray[8]+1;
                        break;
                case '9': bucketArray[9] = bucketArray[9]+1;
                        break;
                default: break;
            }//end switch
        }//end for
        for(int i = 0; i < 10; i++){
            bucket = bucket.concat(Integer.toString(bucketArray[i]));
        }//end for
        set = sNum.concat(bucket); // last 10 digits are the bucket and the first part is the original number
        System.out.println("exit sortSet");
        return set;
    }//end SortSet()
    
    //Adds all the counts from the output set of SortSet() that are greater
    // than 1, and outputs the sum.
    // Ex. 1201200110 would calculate 2+2=4.
    public String SumSet(String s){
        System.out.println("enter sumSet");
        System.out.println(s);
        int count = s.length();
        int sum = 0, n = 0, j = 0;
        Arrays.fill(arr, null); // empty array
        String b = s.substring(count-10); //returns the last 10 digits - the bucket
        
        arr = b.split("(?!^)");
        for(int i = 0; i < 10; i++){
            j = Integer.parseInt(arr[i]);
            if(j > 1){
                sum = sum + j;
            }//end if
        }//end for
        
        s = s.concat(Integer.toString(sum));
        System.out.println("exit sumSet");
        return s;
    }//end SumSet()
    
    //Takes the original value of the set and appends the sum of the set.
    // Ex. 110443784
    public String AppendDigit(String s) throws IOException{
        System.out.println("enter AppendDigit");
        String answer = "";
        int count = s.length();
        int sum = 0, n = 0;
        String o = s.substring(0, count-11); // original #
        String sn = s.substring(s.length()-1); // get sum         
        answer = o.concat(sn); // add sum to original number
        System.out.println("Exit AppendDigit");
        return answer;

    }//end AppendDigit()   
    
}//end abstract class Filter
