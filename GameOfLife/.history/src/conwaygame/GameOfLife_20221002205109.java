package conwaygame;
import java.util.ArrayList;
/**
 * Conway's Game of Life Class holds various methods that will
 * progress the state of the game's board through it's many iterations/generations.
 *
 * Rules 
 * Alive cells with 0-1 neighbors die of loneliness.
 * Alive cells with >=4 neighbors die of overpopulation.
 * Alive cells with 2-3 neighbors survive.
 * Dead cells with exactly 3 neighbors become alive by reproduction.

 * @author Seth Kelley 
 * @author Maxwell Goldberg
 */
public class GameOfLife {

    // Instance variables
    private static final boolean ALIVE = true;
    private static final boolean  DEAD = false;

    private boolean[][] grid;    // The board has the current generation of cells
    private int totalAliveCells; // Total number of alive cells in the grid (board)

    /**
    * Default Constructor which creates a small 5x5 grid with five alive cells.
    * This variation does not exceed bounds and dies off after four iterations.
    */
    public GameOfLife() {
        grid = new boolean[5][5];
        totalAliveCells = 5;
        grid[1][1] = ALIVE;
        grid[1][3] = ALIVE;
        grid[2][2] = ALIVE;
        grid[3][2] = ALIVE;
        grid[3][3] = ALIVE;
    }

    /**
    * Constructor used that will take in values to create a grid with a given number
    * of alive cells
    * @param file is the input file with the initial game pattern formatted as follows:
    * An integer representing the number of grid rows, say r
    * An integer representing the number of grid columns, say c
    * Number of r lines, each containing c true or false values (true denotes an ALIVE cell)
    */
    public GameOfLife (String file) {
        StdIn.setFile(file);
        
        int rows = StdIn.readInt();
        int cols = StdIn.readInt();

        grid = new boolean[rows][cols];

        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                grid[i][j] = StdIn.readBoolean();
            }
        }
    }

    /**
     * Returns grid
     * @return boolean[][] for current grid
     */
    public boolean[][] getGrid () {
        return grid;
    }
    
    /**
     * Returns totalAliveCells
     * @return int for total number of alive cells in grid
     */
    public int getTotalAliveCells () {
        return totalAliveCells;
    }

    /**
     * Returns the status of the cell at (row,col): ALIVE or DEAD
     * @param row row position of the cell
     * @param col column position of the cell
     * @return true or false value "ALIVE" or "DEAD" (state of the cell)
     */
    public boolean getCellState (int row, int col) {

        // WRITE YOUR CODE HERE
        return grid[row][col]; // update this line, provided so that code compiles
    }

    /**
     * Returns true if there are any alive cells in the grid
     * @return true if there is at least one cell alive, otherwise returns false
     */
    public boolean isAlive () {

        // WRITE YOUR CODE HERE
        for(int i = 0; i < grid.length; i++){
            for(int j = 0; j < grid[0].length; j++){
                if(grid[i][j] == true)
                    return true;
            }
        }
        return false; // update this line, provided so that code compiles
    }

    /**
     * Determines the number of alive cells around a given cell.
     * Each cell has 8 neighbor cells which are the cells that are 
     * horizontally, vertically, or diagonally adjacent.
     * 
     * @param col column position of the cell
     * @param row row position of the cell
     * @return neighboringCells, the number of alive cells (at most 8).
     */
    public int numOfAliveNeighbors (int row, int col) {
        int alive = 0;
        // WRITE YOUR CODE HERE
        int rowLength = grid.length;
        int colLength = grid[0].length;

        for(int i = 0; i < rowLength; i++){
            for(int j = 0; j < colLength; j++){
                if(Math.abs(i-row) <= 1||Math.abs(i-row) == rowLength-1){
                    if(Math.abs(j-col) <= 1||Math.abs(j-col)==colLength-1){
                        if(i != row || j != col){
                            if(grid[i][j]==true){
                                alive++;
                            }
                        }
                    }
                }
            }
        }

        return alive; // update this line, provided so that code compiles
    }

    /**
     * Creates a new grid with the next generation of the current grid using 
     * the rules for Conway's Game of Life.
     * 
     * @return boolean[][] of new grid (this is a new 2D array)
     */
    public boolean[][] computeNewGrid () {
        int num = 0;
        boolean[][] grid2 = new boolean[grid.length][grid[0].length];
        for(int i = 0; i < grid2.length; i++){
            for(int j = 0; j < grid2[i].length; j++){
                num = numOfAliveNeighbors(i, j);
                if(num <= 1)
                    grid2[i][j] = false;
                else if((num == 3)&&(grid[i][j] == false))
                    grid2[i][j] = true;
                else if(((num == 2)||(num == 3))&&(grid[i][j] == true)){
                    grid2[i][j] = true;
                }
                else if(num >= 4){
                    grid2[i][j] = false;
                }
            }
        }
        return grid2;// update this line, provided so that code compiles
    }

    /**
     * Updates the current grid (the grid instance variable) with the grid denoting
     * the next generation of cells computed by computeNewGrid().
     * 
     * Updates totalAliveCells instance variable
     */
    public void nextGeneration () {
        grid = computeNewGrid();
        // WRITE YOUR CODE HERE
    }

    /**
     * Updates the current grid with the grid computed after multiple (n) generations. 
     * @param n number of iterations that the grid will go through to compute a new grid
     */
    public void nextGeneration (int n) {
        for(int i = 0; i < n; i++){
            nextGeneration();
        }
    }

    /**
     * Determines the number of separate cell communities in the grid
     * @return the number of communities in the grid, communities can be formed from edges
     */
    public int numOfCommunities() {
        int num = 0;
        int rowLength = grid.length;
        int colLength = grid[0].length;

        WeightedQuickUnionUF unions = new WeightedQuickUnionUF(rowLength, colLength);
        ArrayList<Integer> list = aliveCells();
        ArrayList<Integer> ids = new ArrayList<Integer>();
        ArrayList<Integer> uniqueIds = new ArrayList<Integer>();

        for(int i = 0; i < list.size(); i+=2){
            for(int j = i+2; j < list.size(); j+=2){
                if(isNeighbor(list.get(i), list.get(i+1), list.get(j),list.get(j+1))){
                    unions.union(list.get(i), list.get(i+1), list.get(j),list.get(j+1));
                }   
            }
        }

        for(int i = 0; i < list.size(); i+=2){
            ids.add(unions.find(list.get(i),list.get(i+1)));
        }

        for(int i = 0; i < ids.size(); i++){
            
        }

        return num; // update this line, provided so that code compiles
    }

    private ArrayList<Integer> aliveCells(){
        ArrayList<Integer> list = new ArrayList<Integer>();
        int rowLength = grid.length;
        int colLength = grid[0].length;
        for(int i = 0; i < rowLength; i++){
            for(int j = 0; j < colLength; j++){
                if(grid[i][j] == true){
                    list.add(i);
                    list.add(j);
                }
            }
        }
        return list;
    }

    private boolean isNeighbor(int x1, int y1, int x2, int y2){
        int rowLength = grid.length;
        int colLength = grid[0].length;
        if(Math.abs(x1-x2) <= 1||Math.abs(x1-x2) == rowLength-1){
            if(Math.abs(y1-y2) <= 1||Math.abs(y1-y2) == colLength-1){
                return true;
            }
        }
        return false;
    }
}
