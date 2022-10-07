package conwaygame;
import java.util.ArrayList;


/**
 * Conway's Game of Life Class holds various methods that will
 * progress the state of the game's board through it's many
 * iterations/generations.
 *
 * Rules
 * Alive cells with 0-1 neighbors die of loneliness.
 * Alive cells with >=4 neighbors die of overpopulation.
 * Alive cells with 2-3 neighbors survive.
 * Dead cells with exactly 3 neighbors become alive by reproduction.
 * 
 * @author Seth Kelley
 * @author Maxwell Goldberg
 */

public class GameOfLife {

    // Instance variables
    private static final boolean ALIVE = true;
    private static final boolean DEAD = false;

    private boolean[][] grid; // The board has the current generation of cells
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
     * Constructor used that will take in values to create a grid with a given
     * number
     * of alive cells
     * 
     * @param file is the input file with the initial game pattern formatted as
     *             follows:
     *             An integer representing the number of grid rows, say r
     *             An integer representing the number of grid columns, say c
     *             Number of r lines, each containing c true or false values (true
     *             denotes an ALIVE cell)
     */
    public GameOfLife(String file) {
          

            StdIn.setFile(file);
            int rows = StdIn.readInt();
            int columns = StdIn.readInt();
            int count = 0;

            grid = new boolean[rows][columns];

            for ( int i = 0; i < rows; i++){
                for ( int j = 0; j < columns; j++){
                    boolean cellValue = StdIn.readBoolean();
                    grid[i][j] = cellValue;
                    if (cellValue){
                        count++;
                    }
                }
            }

            totalAliveCells = count;
    }

    /**
     * Returns grid
     * 
     * @return boolean[][] for current grid
     */
    public boolean[][] getGrid() {
        return grid;
    }

    /**
     * Returns totalAliveCells
     * 
     * @return int for total number of alive cells in grid
     */
    public int getTotalAliveCells() {
        return totalAliveCells;
    }

    /**
     * Returns the status of the cell at (row,col): ALIVE or DEAD
     * 
     * @param row row position of the cell
     * @param col column position of the cell
     * @return true or false value "ALIVE" or "DEAD" (state of the cell)
     */
    public boolean getCellState(int row, int col) {

        boolean currentValue = grid[row][col];

        return currentValue; 
    }

    /**
     * Returns true if there are any alive cells in the grid
     * 
     * @return true if there is at least one cell alive, otherwise returns false
     */
    public boolean isAlive() {

        for ( int i = 0; i < grid.length; i++){
            for ( int j = 0; j < grid[0].length; j++){
                if ( grid[i][j]){
                   return true;
                } 
            }
        }
        return false; 
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
    public int numOfAliveNeighbors(int row, int col) {
        int height = grid.length;
        int width = grid[0].length;
        int neighborRow;
        int neighborColumn;

        int count = 0;
        // northwest neighbor 
        neighborRow = row > 0 ? row - 1: height - 1;
        neighborColumn = col > 0 ? col - 1: width - 1;

        if (grid[neighborRow][neighborColumn]){
            count++; 
        }
        // north neighbor
        neighborRow = row > 0 ? row - 1: height - 1;
        
        if (grid[neighborRow][col]){
            count++; 
        }
        // northeast neighbor
        neighborRow = row > 0 ? row - 1: height - 1;
        neighborColumn = col < width - 1 ? col + 1: 0;

        if (grid[neighborRow][neighborColumn]){
             count++; 
        }
        
        // west neighbor
        neighborColumn = col > 0 ? col - 1: width - 1;

        if (grid[row][neighborColumn]){
            count++; 
        }

        // east neighbor
        neighborColumn = col < width - 1 ? col + 1: 0;

        if (grid[row][neighborColumn]){
            count++; 
        }
        // southwest
        neighborRow = row < height - 1 ? row + 1: 0;
        neighborColumn = col > 0 ? col - 1: width - 1;

        if (grid[neighborRow][neighborColumn]){
            count++; 
        }
        // south
        neighborRow = row < height - 1 ? row + 1: 0;

        if (grid[neighborRow][col]){
            count++; 
        }
        // southeast
        neighborRow = row < height - 1 ? row + 1: 0;
        neighborColumn = col < width - 1 ? col + 1: 0;

        if (grid[neighborRow][neighborColumn]){
            count++; 
        }

        return count;
    }

    /**
     * Creates a new grid with the next generation of the current grid using
     * the rules for Conway's Game of Life.
     * 
     * @return boolean[][] of new grid (this is a new 2D array)
     */
    public boolean[][] computeNewGrid() {

        int height = grid.length;
        int width = grid[0].length;

        boolean [][] newGrid = new boolean[height][width];

        for ( int i = 0; i < height; i++){
            for ( int j = 0; j < width; j++){
                int numberOfNeighbors = numOfAliveNeighbors(i , j);
                boolean isAlive = (grid[i][j] && (numberOfNeighbors == 2 || numberOfNeighbors == 3)) || 
                                    (!grid[i][j] && numberOfNeighbors == 3);
                newGrid[i][j] = isAlive;
            }
        }

        return newGrid;
    }

    /**
     * Updates the current grid (the grid instance variable) with the grid denoting
     * the next generation of cells computed by computeNewGrid().
     * 
     * Updates totalAliveCells instance variable
     */
    public void nextGeneration() {

        grid = computeNewGrid();
        int count = 0;
        for ( int i = 0; i < grid.length; i++){
            for ( int j = 0; j < grid[0].length; j++){
               if ( grid[i][j] ){
                count++;
               } 
            }
        }
        totalAliveCells = count;
    }

    /**
     * Updates the current grid with the grid computed after multiple (n)
     * generations.
     * 
     * @param n number of iterations that the grid will go through to compute a new
     *          grid
     */
    public void nextGeneration(int n) {

        for ( int i = 0; i < n; i++){
            nextGeneration();
        }
    }

    /**
     * Determines the number of separate cell communities in the grid
     * 
     * @return the number of communities in the grid, communities can be formed from
     *         edges
     */
    public int numOfCommunities() {

        int r = grid.length;
        int c = grid[0].length;

        WeightedQuickUnionUF uf = new WeightedQuickUnionUF(r , c);
       
        for ( int i1 = 0; i1 < r; i1++){
            for ( int j1 = 0; j1 < c; j1++){
                if ( !grid[i1][j1] ){
                    continue;
                }

                for (int i2 = 0; i2 < r; i2++){
                    for (int j2 = 0; j2 < c; j2++) {
                        if (areNeighbors(i1, j1, i2, j2) && grid[i2][j2])
                        {
                            uf.union( i1 , j1 , i2, j2);
                        }
                    }
                }
            }
        }

        // Using a set to account for duplicates
        ArrayList<Integer> roots = new ArrayList<>();

        for ( int i = 0; i < r; i++){
            for ( int j = 0; j < c; j++){
                int union = uf.find(i , j);
                if( !roots.contains(union)){
                    roots.add( union );
                }
            }
        }

        int deadCells = (r * c) - totalAliveCells;

        int numberOfCommunities = roots.size() - deadCells;
        return numberOfCommunities;
    }

    private boolean areNeighbors(int row1, int col1, int row2, int col2)
    {
        int height = grid.length;
        int width = grid[0].length;

        boolean rowsClose = Math.abs(row1 - row2) <= 1 || Math.abs(row1 - row2) == height - 1;
        boolean colsClose = Math.abs(col1 - col2) <= 1 || Math.abs(col1 - col2) == width - 1;

        return rowsClose && colsClose;
    }
}
