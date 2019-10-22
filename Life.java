import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class Life {
  public static final int MAX_ROWS = 40;
  public static final int MAX_COLUMNS = 60;
  private boolean[][] grid;     // MAX_ROWS by MAX_COLUMNS
  private int generation = 0;   // "generation" counts the number of times passTime() has been called

  /** Default constructor that creates an empty grid. */
  public Life () {
    grid = new boolean[MAX_ROWS][MAX_COLUMNS];
  }

  /** This constructor loads the starting configuration of the grid from "filename".
    * A "*" in the file indicates a "true" location in
    * the grid.  The file should load so that it is centered horizontally
    * and vertically in the grid.
    *
    * This method should not throw an exception.  If the file is not found,
    * the method should print something like: File, xxxxxx, not found.
    */
  public Life (String fileName) {
    try{
      grid = new boolean[MAX_ROWS][MAX_COLUMNS];
      Scanner file = new Scanner(new FileReader(fileName));
      
      int longestRowLength = 0;
      ArrayList<String> arr = new ArrayList<>();
      while(file.hasNextLine()){
        String n = file.nextLine();
        arr.add(n);
        if(n.length() > longestRowLength)
          longestRowLength = n.length();
      }
      int rowNum = arr.size();
      int centerCol = (MAX_COLUMNS - longestRowLength) / 2;
      int centerRow = (MAX_ROWS - rowNum) / 2;

      for(int i = 0; i < arr.size(); i++){
        for(int j = 0; j < arr.get(i).length(); j++){
          if(arr.get(i).charAt(j) == '*'){
            grid[i + centerRow][j + centerCol] = true;
          }
          else{
            grid[i + centerRow][j + centerCol] = false;
          }
        }
      }
    }
    
    catch(Exception ex){
      ex.printStackTrace();
      //System.err.println();
    }
  }


  /** Pass time by 1 generation:
    *
    * For a given cell, determine the number of neighbors that are alive. (All 8 cells
    * touching the given cell are its neighbors.) Note that the grid wraps around the edge
    * of the screen, so cells on the edges of the grid have neighbors on the far side of
    * the grid.
    *
    * For a given cell that is currently alive(true) and has exactly 2 or 3 neighbors that
    * are alive, the cell will live in the next generation.  Otherwise, it will die.
    *
    * For a given cell that is currently dead(false) if it has exactly 3 neighbors that are
    * alive, it will come alive in the next generation.  Otherwise, it remains dead.
    *
    * It is important to note that all changes from time = t to time = t + 1 are
    * considered to occur simultaneously, so that, for instance, if a cell is going
    * to die at the next time step, it still counts as "alive" when computing
    * any of its neighbors� state changes.
    */
  public void passTime() {
    boolean[][] futureGrid= new boolean[MAX_ROWS][MAX_COLUMNS];
    for(int i = 0; i < MAX_ROWS; i++){
      for(int j = 0; j < MAX_COLUMNS; j++){
        int neighborsAlive = friendsAlive(i, j);
        if(isAlive(i, j)){
          if(neighborsAlive != 2 && neighborsAlive != 3)
            futureGrid[i][j] = false;
          else
            futureGrid[i][j] = true;
        }
        else{
          if(neighborsAlive == 3)
            futureGrid[i][j] = true;
          else
            futureGrid[i][j] = false;
        }
      }
    }
    
    for(int i = 0; i < MAX_ROWS; i++){
      for(int j = 0; j < MAX_COLUMNS; j++){
        grid[i][j] = futureGrid[i][j];
      }
    }
    generation++;
    
  }

  /** Accessor method for locations on the grid.  LifeController uses this method to
    * determine if a current cell is alive or dead. */
  public boolean isAlive (int row, int column) {
    if(grid[row][column] == true){
      return true;
    }
    else{
      return false;
    }
  }
  
  public int friendsAlive (int row, int column){
    int alive = 0;
    if(row == 0 && column == 0){
      if(isAlive(row + 1, column))
        alive++;
      if(isAlive(row, column + 1))
        alive++;
      if(isAlive(row + 1, column+1))
        alive++;
      if(isAlive(row, MAX_COLUMNS-1))
        alive++;
      if(isAlive(row + 1, MAX_COLUMNS-1))
        alive++;
      if(isAlive(MAX_ROWS-1, MAX_COLUMNS-1))
        alive++;
      if(isAlive(MAX_ROWS-1, column))
        alive++;
      if(isAlive(MAX_ROWS-1, column+1))
        alive++;
    }
    else if(row == 0 && column == MAX_COLUMNS-1){
      if(isAlive(row, MAX_COLUMNS - 2))
        alive++;
      if(isAlive(row + 1, MAX_COLUMNS - 1))
        alive++;
      if(isAlive(row + 1, MAX_COLUMNS - 2))
        alive++;
      if(isAlive(row, 0))
        alive++;
      if(isAlive(row + 1, 0))
        alive++;
      if(isAlive(MAX_ROWS-1, 0))
        alive++;
      if(isAlive(MAX_ROWS-1, MAX_COLUMNS-1))
        alive++;
      if(isAlive(MAX_ROWS-1, MAX_COLUMNS-2))
        alive++;
    }
    else if(row == MAX_ROWS - 1 && column == 0){
      if(isAlive(MAX_ROWS - 2, column))
        alive++;
      if(isAlive(MAX_ROWS - 1, column + 1))
        alive++;
      if(isAlive(MAX_ROWS - 2, column + 1))
        alive++;
      if(isAlive(MAX_ROWS - 1, MAX_COLUMNS - 1))
        alive++;
      if(isAlive(MAX_ROWS - 1, MAX_COLUMNS - 1))
        alive++;
      if(isAlive(0, MAX_COLUMNS - 1))
        alive++;
      if(isAlive(0, column))
        alive++;
      if(isAlive(0, column + 1))
        alive++;
    }
    else if(row == MAX_ROWS - 1 && column == MAX_COLUMNS -1){
      if(isAlive(MAX_ROWS - 2, MAX_COLUMNS - 1))
        alive++;
      if(isAlive(MAX_ROWS - 1, MAX_COLUMNS - 2))
        alive++;
      if(isAlive(MAX_ROWS - 2, MAX_COLUMNS - 2))
        alive++;
      if(isAlive(MAX_ROWS - 1, 0))
        alive++;
      if(isAlive(MAX_ROWS - 2, 0))
        alive++;
      if(isAlive(0, 0))
        alive++;
      if(isAlive(0, MAX_COLUMNS - 1))
        alive++;
      if(isAlive(0, MAX_COLUMNS - 2))
        alive++;
    }
    else if(row == 0){
      if(isAlive(row, column - 1))
        alive++;
      if(isAlive(row, column + 1))
        alive++;
      if(isAlive(row + 1, column + 1))
        alive++;
      if(isAlive(row +1, column))
        alive++;
      if(isAlive(row +1, column - 1))
        alive++;
      if(isAlive(MAX_ROWS - 1, column))
        alive++;
      if(isAlive(MAX_ROWS - 1, column + 1))
        alive++;
      if(isAlive(MAX_ROWS - 1, column - 1))
        alive++;
    }
    else if(row == MAX_ROWS-1){
      if(isAlive(MAX_ROWS - 2, column))
        alive++;
      if(isAlive(MAX_ROWS - 1, column + 1))
        alive++;
      if(isAlive(MAX_ROWS - 1, column - 1))
        alive++;
      if(isAlive(MAX_ROWS - 2, column + 1))
        alive++;
      if(isAlive(MAX_ROWS - 2, column-1))
        alive++;
      if(isAlive(0, column - 1))
        alive++;
      if(isAlive(0, column + 1))
        alive++;
      if(isAlive(0, column))
        alive++;
    }
    else if(column == 0){
      if(isAlive(row - 1, column))
        alive++;
      if(isAlive(row + 1, column))
        alive++;
      if(isAlive(row, column + 1))
        alive++;
      if(isAlive(row - 1, column + 1))
        alive++;
      if(isAlive(row + 1, column + 1))
        alive++;
      if(isAlive(row, MAX_COLUMNS-1))
        alive++;
      if(isAlive(row - 1, MAX_COLUMNS-1))
        alive++;
      if(isAlive(row + 1, MAX_COLUMNS-1))
        alive++;
    }
    else if (column == MAX_COLUMNS-1){
      if(isAlive(row - 1, MAX_COLUMNS-1))
        alive++;
      if(isAlive(row + 1, MAX_COLUMNS-1))
        alive++;
      if(isAlive(row, MAX_COLUMNS-2))
        alive++;
      if(isAlive(row-1, MAX_COLUMNS-2))
        alive++;
      if(isAlive(row+1, MAX_COLUMNS-2))
        alive++;
      if(isAlive(row, 0))
        alive++;
      if(isAlive(row-1, 0))
        alive++;
      if(isAlive(row+1, 0))
        alive++;
    }
    else{
      if(isAlive(row, column-1))
        alive++;
      if(isAlive(row, column+1))
        alive++;
      if(isAlive(row-1, column))
        alive++;
      if(isAlive(row+1, column))
        alive++;
      if(isAlive(row-1, column-1))
        alive++;
      if(isAlive(row+1, column+1))
        alive++;
      if(isAlive(row-1, column+1))
        alive++;
      if(isAlive(row+1, column-1))
        alive++;
    }
    
    return alive;
  }

  /** Accessor method -- returns the number of times passTime has been called. */
  public int getGeneration() {
    return generation;
  }

  /** Resets the generation count to 0. */
  public void resetGeneration() {
    generation = 0;
  }

  /** Inverts the value of the grid at the given location. */
  public void toggleGridValue(int row, int column) {
    if(grid[row][column]){
      grid[row][column] = false;
    }
    else{
      grid[row][column] = true;
    }
  }
}
