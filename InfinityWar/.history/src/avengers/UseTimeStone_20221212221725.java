package avengers;
import java.util.*;

/**
 * Given a starting event and an Adjacency Matrix representing a graph of all possible 
 * event once Thanos arrives on Titan, determine the total possible number of timelines 
 * that could occur AND the number of timelines with a total Expected Utility (EU) at 
 * least the threshold value.
 * 
 * 
 * Steps to implement this class main method:
 * 
 * Step 1:
 * UseTimeStoneInputFile name is passed through the command line as args[0]
 * Read from UseTimeStoneInputFile with the format:
 *    1. t (int): expected utility (EU) threshold
 *    2. v (int): number of event (vertices in the graph)
 *    3. v lines, each with 2 values: (int) event number and (int) EU value
 *    4. v lines, each with v (int) edges: 1 means there is a direct edge between two vertices, 0 no edge
 * 
 * Note 1: the last v lines of the UseTimeStoneInputFile is an ajacency matrix for a directed
 * graph. 
 * The rows represent the "from" vertex and the columns represent the "to" vertex.
 * 
 * The matrix below has only two edges: (1) from vertex 1 to vertex 3 and, (2) from vertex 2 to vertex 0
 * 0 0 0 0
 * 0 0 0 1
 * 1 0 0 0
 * 0 0 0 0
 * 
 * Step 2:
 * UseTimeStoneOutputFile name is passed through the command line as args[1]
 * Assume the starting event is vertex 0 (zero)
 * Compute all the possible timelines, output this number to the output file.
 * Compute all the posssible timelines with Expected Utility higher than the EU threshold,
 * output this number to the output file.
 * 
 * Note 2: output these number the in above order, one per line.
 * 
 * Note 3: use the StdIn/StdOut libraries to read/write from/to file.
 * 
 *   To read from a file use StdIn:
 *     StdIn.setFile(inputfilename);
 *     StdIn.readInt();
 *     StdIn.readDouble();
 * 
 *   To write to a file use StdOut:
 *     StdOut.setFile(outputfilename);
 *     //Call StdOut.print() for total number of timelines
 *     //Call StdOut.print() for number of timelines with EU >= threshold EU 
 * 
 * Compiling and executing:
 *    1. Make sure you are in the ../InfinityWar directory
 *    2. javac -d bin src/avengers/*.java
 *    3. java -cp bin avengers/UseTimeStone usetimestone.in usetimestone.out
 * 
 * @author Yashas Ravi
 * 
 */

public class UseTimeStone {

    public static void main (String [] args) {
    	
        if ( args.length < 2 ) {
            StdOut.println("Execute: java UseTimeStone <INput file> <OUTput file>");
            return;
        }

    	String useTimeStoneInputFile = args[0];
        String useTimeStoneOutputFile = args[1];
        StdIn.setFile(useTimeStoneInputFile);
        StdOut.setFile(useTimeStoneOutputFile);

        int threshold = StdIn.readInt();
        int nodes = StdIn.readInt();
        int[] event = new int[nodes];
        int[][] mat = new int[nodes][nodes];
        ArrayList<Integer> outputList = new ArrayList<Integer>();
        HashMap<Integer, ArrayList<Integer>> map = new HashMap<Integer, ArrayList<Integer>>();

        for(int i=0; i<nodes; i++)
        {
            int eventNum = StdIn.readInt();
            int funcVal = StdIn.readInt();
            event[eventNum] = funcVal;
        }

        for(int i = 0; i < nodes; i++)
        {
            if(!map.containsKey(i)){
                map.put(i, new ArrayList<Integer>());
            }
        }

        for(int i = 0; i < nodes; i++){
            for(int j=0; j<nodes; j++){
                mat[i][j] = StdIn.readInt();
                if(mat[i][j] == 1)
                {
                    map.get(i).add(j);
                }
            }
        }

        iterate(map, event, outputList,0, event[0]);
        for(int i = 0; i < outputList.size(); i++)
        {
            if(outputList.get(i) >= threshold){
                count++;
            }
        }
        StdOut.println(outputList.size());
        StdOut.print(count);
    }

    private static void iterate(HashMap<Integer, ArrayList<Integer>> map, int[] event, ArrayList<Integer> outputList, int node,int EU)
    {
        outputList.add(EU);
        if(map.get(node).size() > 0)
        {
            for(int i=0; i<map.get(node).size(); i++)
            {
                iterate(map, event, outputList,map.get(node).get(i), EU + event[map.get(node).get(i)]);
            }
        }
    }
}

