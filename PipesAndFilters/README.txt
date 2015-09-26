////////////////////////////////////////////////////////////////////////////////
//Filename: README
//Class/Project: CS 490 (Fall 2015) / Project 1 (Pipes and Filters Pattern)
//Name: Amanda Donaldson
//Modified: 9/25/2015
//
//Description: An implementation of the Pipes and Filters Pattern. It uses 
//              multiple filters and passive pipes.
//
////////////////////////////////////////////////////////////////////////////////

INPUT:
There are two ways to enter input for this program.
Read from a file. Formatted as: "inFile.txt outFile.txt"
Entering the numbers yourself. The initial input is the first number you would like to be processed
	and then the output File. Formatted as: "11111111 out.txt"
	Then you will be prompted to enter the rest of the numbers one at a time. You will need to enter "0" to quit.
	
NOTE: All input and output files MUST be .txt file

OUTPUT:
The output will be printed to the specified output file. If the file already exists it will be deleted and created again.

INCLUDED:
There is a sample input file included called numfile.txt. This file included valid and invalid numbers.

PROBLEMS THAT I RAN INTO:
Since I haven't touched Java in 4 years and only knew the basics going into this project, I ran into a few bumps. I will 
tell them here just incase someone else runs into the same problems. The first problem was that my if statements were not 
accepting my poison pills ("0") on my comparisons and would just treat them as regular strings and process them. This was
because my comparison originally was if(str == "0"), and in Java that is not how you do string comparisons. Syntax errors 
are the worst. This is a statement in Java but not what I wanted. I needed if("0".equals(str) instead. Then the second problem
that I ran into was that my program would process properly if I did the command line input, but not reading from a csv file.
Once I debugged it a few times I noticed that my numbers had the space in front of them. So a str.trim() when I read in the 
numbers fixed that right up.
