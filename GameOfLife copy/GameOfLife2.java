package conwaygame;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

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

        File openFile = new File(file);

        try {
            Scanner scanner = new Scanner(openFile);
            int rows = scanner.nextInt();
            int columns = scanner.nextInt();

            grid = new boolean[rows][columns];

            for ( int i = 0; i < rows; i++){
                for ( int j = 0; j < columns; j++){
                    boolean cellValue = scanner.nextBoolean();
                    grid[i][j] = cellValue;
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } 
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

        int count = 0;
        // northwest neighbor 
        if (row > 0 && col > 0 && grid[row - 1][col - 1]){
            count++; 
        }
        // north neighbor
        if (row > 0 && grid[row - 1][col]){
            count++; 
        }
        // northeast neighbor
        if (row > 0 && col < width - 1 && grid[row - 1][col + 1]){
            count++; 
        }
        // west neighbor
        if (col > 0 && grid[row][col - 1]){
            count++; 
        }
        // east neighbor
        if (col < width - 1 && grid[row][col + 1]){
            count++; 
        }
        // southwest
        if (row < height - 1 && col > 0 && grid[row + 1][col - 1]){
            count++; 
        }
        // south
        if (row < height - 1 && grid[row + 1][col]){
            count++; 
        }
        // southeast
        if (row < height - 1 && col < width - 1 && grid[row + 1][col + 1]){
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

        for ( int i = 0; i < r; i++){
            for ( int j = 0; j < c; j++){
                if ( !grid[i][j] ){
                    continue;
                }
                if ( i > 0 && grid[i - 1][j] ){
                    uf.union( i , j , i - 1 , j ); // north
                }
                if ( j > 0 && grid[i][j - 1] ){
                    uf.union( i , j , i , j - 1 ); // west
                }
                if ( j < c - 1 && grid[i][j + 1] ){
                    uf.union( i , j , i , j + 1 ); // east
                }
                if ( i < r - 1 && grid[i + 1][j] ){
                    uf.union( i , j , i + 1 , j ); // south
                }
            }
        }

        Set<Integer> roots = new HashSet<Integer>(); 

        for ( int i = 0; i < r; i++){
            for ( int j = 0; j < c; j++){
                System.out.println(uf.find(i , j));

                roots.add( uf.find (i , j));
            }
        }

        int deadCells = (r * c) - totalAliveCells;

        return roots.size() - deadCells; 
    }
}