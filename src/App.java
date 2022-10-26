import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;

/*Main executable class. */
public class App {
    /**
     * @param args
     * @throws IOException
     * @throws FileNotFoundException
     */
    public static void main(String[] args) throws IOException, FileNotFoundException {
        
        String inputPath = "C:/Users/Stevin Sebastian/Desktop/sudoku/sudoku/lib/game.txt";
        String outputPath = "C:/Users/Stevin Sebastian/Desktop/sudoku/sudoku/lib/output.txt";

        //-----------Input Matrix
        BufferedReader buffer = new BufferedReader(new FileReader(inputPath));
        GridCell [][] sudokuGrid = new GridCell[9][9];
        int row=0;
        String line; 
        while((line = buffer.readLine())!=null && row<9) {
            String[] currentRow = line.trim().split(" ");

            for (int column=0; column<9; column++){
                int currentVal = Integer.parseInt(currentRow[column]);
                sudokuGrid[row][column] = new GridCell(currentVal);
            }
            row++;   
        }
        buffer.close();
        //-----------Input Matrix

        
        if(solve(sudokuGrid)){
            System.out.println("*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*");
            System.out.println("Sudoku solved.");
            writeMatrixOutput(outputPath, sudokuGrid);
        }
        else{
            System.out.println("*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*");
            System.out.println("No unique solution.");
        }
        System.out.println("*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*");

    }   

    /**
     * Writes the solved sudoku grid to a designated output file.
     * @param fileName - the output filepath
     * @param grid - the sudoku grid
     * @throws IOException
     */
    public static void writeMatrixOutput(String fileName, GridCell [][] grid) throws IOException{
        BufferedWriter buffer = new BufferedWriter(new FileWriter(fileName));

        for(int i=0; i<grid.length; i++){
            if(i%3==0){
                buffer.newLine();
            }
            for(int j=0; j<grid[i].length; j++){
                if(j%3!=0){
                    buffer.write(grid[i][j].getValue() + " ");
                }
                else {
                    buffer.write("\t");
                    buffer.write(grid[i][j].getValue() + " ");
                }
                

            }
            
            buffer.newLine();
        }
        buffer.flush();
        buffer.close();
    }//main

    /**
     * Attempts to solve the sudoku matrix using multiple techniques. 
     * <p><ul>
     * <li> Step 1: Checks if the input grid is playable. If not playable, return false.
     * <li> Step 2: If the grid is playable, revise each grid cell's option set and check for unique options. 
     * <p>
     *  Repeat Step 2 until no new solutions are found. If nothing new is found, continue.
     * <li> Step 3: Conduct naked pair removal on each row, column and block. Check for unique solutions. 
     * <p>
     * If new solutions are found, return to Step 2. If naked pair removal is run twice with no effect, continue.
     * <li> Step 4: If the grid is stuck after trying the previous approaches, run the brute force algorithm. 
     * <p> If the algorithm returns true, the sudoku is solved. If not, then the grid is unsolvable.
     * @param grid - the sudoku grid.
     * @return true - if the sudoku grid is solved; false if the grid is unsolvable.
     */
    public static boolean solve(GridCell[][] grid) {
        if(!isGridPlayable(grid)){
            return false;
        }
        int emptyCellCounter = reviseOptions(grid);
        //System.out.println("Process: Revise Options. (" + emptyCellCounter + " empty cells)");

        int EC_tracker = 0, NP_tracker=0, EC_change =0;
        boolean isSolved=false;
        
        while(!isSolved){
            EC_change = emptyCellCounter - EC_tracker;

            if(emptyCellCounter == 0){
                return true;
            }
            if(NP_tracker>1){
                //System.out.println("Process: Brute force.");
                return bruteForce(grid, 0, 0);
            }

            if(EC_change==0){
                nakedPairRemoval(grid);
                emptyCellCounter =  checkUniqueOptions(grid);
                NP_tracker++;
                //System.out.println("Process: Naked Pair Removal.");
            }

            if(EC_change!=0){
                EC_tracker = emptyCellCounter;
                emptyCellCounter = reviseOptions(grid);
                //System.out.println("Process: Revise Options. (" + emptyCellCounter + " empty cells)");

            }
        }
        return false;
    }
    
    /**
     * Prints the entire gridcell matrix to terminal.
     * @param grid - the sudoku grid
     */
    public static void printGrid(GridCell[][] grid){
        for(int k=0; k<9; k++){
            for(int j=0; j<9; j++){
                System.out.print(grid[k][j].getValue() + " ");
            }
            System.out.println();
        }
    }
    
    /**
     * Checks each gridcell's option set for any cell that 
    has only one potential value. If found, it sets that value to the cell.
     * @param grid
     * @return The number of empty cells left in the grid.
     */
    public static int checkUniqueOptions(GridCell[][] grid){
        int flag=0;
        for (GridCell[] row: grid) {
            for (GridCell cell : row) {
                if(cell.getValue()==0){
                    flag++;
                    if(cell.options.size()==1){
                        Integer value = cell.options.toArray(new Integer[0])[0];  
                        cell.setValue(value);
                        cell.options.removeAll(cell.options);
                        flag--;
                    }
                }
            }
        }
        return flag;
    }
    
    /**
     * Revises the option set of each cell in the grid, using a set of invalid options for that cell. 
     * @param grid - the sudoku grid
     * @return The number of empty cells that are left in the grid. 
     */
    public static int reviseOptions(GridCell[][] grid)
    {        
        int flag=0;
        HashSet<Integer> invalidOptions; 
        for (int row=0; row<9; row++){
            for (int col=0; col<9; col++){
                if (grid[row][col].getValue()==0) 
                {
                    flag++;
                    invalidOptions = getInvalidOptions(grid, row, col); 
                    grid[row][col].options.removeAll(invalidOptions);

                    if(grid[row][col].options.size()==1)
                    { 
                        Integer value = grid[row][col].options.toArray(new Integer[0])[0];
                        grid[row][col].setValue(value);
                        grid[row][col].options.removeAll(grid[row][col].options);
                    }
                }
                else {
                    continue;
                }
            } 
        }
        return flag;
    }

    /**
     * Given a row and column, return a set of invalid numbers for the respective cell,
     *  by checking its row, column and block.
     * @param grid - the sudoku grid
     * @param row - the row number
     * @param column - the column number
     * @return A set of invalid numbers for the cell of the respective row and column.
     */
    public static HashSet<Integer> getInvalidOptions(GridCell[][] grid, int row, int column) {
        HashSet<Integer> output = new HashSet<Integer>();

        for (int ctrl=0; ctrl<9; ctrl++){
            if(grid[row][ctrl].getValue()!=0){
                output.add(grid[row][ctrl].getValue());
            }
            
            if(grid[ctrl][column].getValue()!=0){
                output.add(grid[ctrl][column].getValue());
            }
        }

        int rowStart = row - (row%3);
        int columnStart = column - (column%3);

        for(int r=0+rowStart; r < 3+rowStart; r++) {
            for(int c=0+columnStart; c < 3+columnStart; c++) {
                if(grid[r][c].getValue()!=0){
                    output.add(grid[r][c].getValue());
                }
            }
        }
        return output;
    }

    /**
     * Checks if the grid contains no duplicate numbers within a block, column or row. 
     * @param grid - the sudoku grid
     * @return true - if there are no duplicates and the grid is playable.
     */
    public static boolean isGridPlayable(GridCell[][] grid){
        HashSet<Integer> checker = new HashSet<Integer>();
        for(int r=0; r<9; r++){
            for(int c =0; c<9; c++){
                if(grid[r][c].getValue()==0){
                    continue;
                }
                if(!checker.add(grid[r][c].getValue())){
                    return false;
                }
            }
            checker.removeAll(checker);
        }
        checker.removeAll(checker);

        for(int c=0; c<9; c++){
            for(int r=0; r<9; r++){
                if(grid[r][c].getValue()==0){
                    continue;
                }
                if(!checker.add(grid[r][c].getValue())){
                    return false;
                }
            }
            checker.removeAll(checker);
        }
        checker.removeAll(checker);
        int rowStart, columnStart, row=1, column=1, counter=0;


        while (row<9 && column<9){

            if(counter==3 && column>7){
                column=1;
                counter=0;
                row+=3;
            }
            rowStart = row - (row%3);
            columnStart = column - (column%3);


            for(int r=0+rowStart; r<3+rowStart; r++){
                for(int c=0+columnStart; c<3+columnStart; c++){
                    if(grid[r][c].getValue()==0){
                        continue;
                    }
                    if(!checker.add(grid[r][c].getValue())){
                        return false;
                    }
                }
                checker.removeAll(checker);
            }
            column+=3;
            counter++;
        }
        return true;
    }

    /**
     * Checks each row, column and block for any 2 cells with identical
     option sets of length 2.
     <p>
     * If so, then it removes the numbers in that naked pair 
     from the option sets of rest of the cells within that particular row, column or block. 
     <p>
     * This implements the naked pair removal strategy of sudoku.
     * @param grid - the sudoku grid
     */
    public static void nakedPairRemoval(GridCell[][] grid){
        ArrayList<HashSet<Integer>> rowOptions = new ArrayList<HashSet<Integer>>();
        ArrayList<HashSet<Integer>> columnOptions = new ArrayList<HashSet<Integer>>();
        ArrayList<HashSet<Integer>> blockOptions = new ArrayList<HashSet<Integer>>();

        int blockRow=1, blockColumn=1;
        int blockCounter=0;

        for(int i=0; i<9; i++){
            rowOptions = getRowOptions(i, grid);
            columnOptions = getColumnOptions(i, grid);

            if(blockCounter==3 || blockColumn>7)
            {
                blockCounter=0;
                blockRow+=3;
                blockColumn=1;
            }
            blockOptions = getBlockOptions(blockRow, blockColumn, grid);
        
            for(int j=0; j<9; j++){

                if(rowOptions.get(j).size()==2 && Collections.frequency(rowOptions, rowOptions.get(j))==2){
                    npRow(rowOptions.get(j), grid, i);
                }

                if(columnOptions.get(j).size()==2 && Collections.frequency(columnOptions, columnOptions.get(j))==2){
                    npColumn(columnOptions.get(j), grid, i);
                }

                if(blockOptions.get(j).size()==2 && Collections.frequency(blockOptions, blockOptions.get(j))==2){
                    npBlock(blockOptions.get(j), grid, blockRow, blockColumn);
                }
            }
            blockColumn+=3;
            blockCounter++;
        }
    }

    /**
     * Returns an array list containing all the option sets of cells within a particular row.
     * @param row - the row number
     * @param grid - the sudoku grid
     * @return An ArrayList containing every cell's option set within a particular row.
     */
    public static ArrayList<HashSet<Integer>> getRowOptions (int row, GridCell[][] grid) {
        ArrayList<HashSet<Integer>> rowList = new ArrayList<HashSet<Integer>>();
        for(int column=0; column<9; column++) {
            rowList.add(grid[row][column].options); 
        }
        return rowList;
    }

    /**     
     * Returns an array list containing all the option sets of cells within a particular column.
     * @param column - the column number
     * @param grid - the sudoku grid
     * @return An ArrayList containing every cell's option set within a particular column. 
     */
    public static ArrayList<HashSet<Integer>> getColumnOptions (int column, GridCell[][] grid) {
        ArrayList<HashSet<Integer>> columnList = new ArrayList<HashSet<Integer>>();
        for(int row=0; row<9; row++) {
            columnList.add(grid[row][column].options); 
        }
        return columnList;
    }

    /**
     * Returns an array list containing all the option sets of cells within a particular block.
     * @param row - the row number to calculate block
     * @param column - the column number to calculate block
     * @param grid - the sudoku grid
     * @return An ArrayList containing every cell's option set within a block.
     */
    public static ArrayList<HashSet<Integer>> getBlockOptions (int row, int column, GridCell[][] grid) {
        ArrayList<HashSet<Integer>> block = new ArrayList<HashSet<Integer>>();
        int rowStart = row - (row%3);
        int columnStart = column - (column%3);

        for(int r=0+rowStart; r < 3+rowStart; r++) {
            for(int c=0+columnStart; c < 3+columnStart; c++) {
                block.add(grid[r][c].options);
            }
        }
        return block;
    }

    /**
     * Removes the given naked pair values from the other cells of the given row.
     * @param nakedPair - the naked pair to be removed from the rest of the row
     * @param grid - the sudoku grid
     * @param row - the row number
     */
    public static void npRow (HashSet<Integer> nakedPair, GridCell[][] grid, int row) {
        for(int idx=0; idx<9; idx++) {
            if (!grid[row][idx].optionsEquals(nakedPair)){
                grid[row][idx].options.removeAll(nakedPair);
            }
        }
    }

    /** 
     * Removes the given naked pair values from the other cells of the given column.
     * @param nakedPair - the naked pair to be removed from the rest of the column
     * @param grid - the sudoku grid
     * @param column - the column number
     */
    public static void npColumn (HashSet<Integer> nakedPair, GridCell[][] grid, int column) {
        for(int idx=0; idx<9; idx++) {
            if (!grid[idx][column].optionsEquals(nakedPair)){
                grid[idx][column].options.removeAll(nakedPair);
            }
        }
    }

    /**
     * Removes the given naked pair values from the other cells of the given block.
     * @param nakedPair - the naked pair to be removed from the rest of the block
     * @param grid - the sudoku grid
     * @param row - the row number 
     * @param column - the column number
     */
    public static void npBlock (HashSet<Integer> nakedPair, GridCell[][] grid, int row, int column) {
        int rowStart = row - (row%3);
        int columnStart = column - (column%3);

        for(int r=0+rowStart; r < 3+rowStart; r++) {
            for(int c=0+columnStart; c < 3+columnStart; c++) {
                if(!grid[r][c].optionsEquals(nakedPair)){
                    grid[r][c].options.removeAll(nakedPair);
                }
            }
        }
    }

    /**
     * Uses a brute force approach to find the remaining solutions for each cell of the grid.
     *  It recursively iterates through each cell. 
     * <p>
     *  For example: 
     * <p>
     *  - Take the first cell, examine its options set, and place one option to the cell. Check grid validity:
     *  If invalid, try another option. If valid, continue.
     * <p> 
     *  - Based on the previous guess, move to the next empty cell and perform the same operation as above. 
     * <p>
     *  - If all options of a cell are invalid, then it must mean that a previous guess was wrong. Go to previous cell, and try the next option in the option set.
     * <p>
     * - If we have reached the end of the grid and the grid is still valid, them the guesses are valied and the grid is considered solved.
     * @param grid - the sudoku grid
     * @param row - control variable holding the row number for each recursive call
     * @param column - control variable holding the column number for each recursive call
     * @return true - if the grid is solved, and false if not possible to solve.
     */
    public static boolean bruteForce(GridCell[][] grid, int row, int column){
        
        if(row == 8 && column > 8){
            return true;
        }

        if(row<8 && column>8){
            row++;
            column=0;
        }

        if(!(grid[row][column].getValue()==0)){
            return bruteForce(grid, row, column+1);
        }

        Iterator<Integer> iOptions = grid[row][column].options.iterator();

        while(iOptions.hasNext()){
            
            int currentOption = iOptions.next(); 

            if(gridValidity(currentOption, row, column, grid)){
                grid[row][column].setValue(currentOption);
                if(bruteForce(grid, row, column+1)){
                    return true;
                }
            }

            grid[row][column].setValue(0);
        }
        return false;
    }

    /**
     * Checks if a potential value can be placed in a cell,
     by checking the validity of the value within the respective row, column and block. 
     * @param value - the potential value
     * @param row - the row number
     * @param column - the column number
     * @param grid - the sudoku grid
     * @return true - if the value is valid within the row, block and column.
     */
    public static boolean gridValidity(int value, int row, int column, GridCell[][] grid){
        for(int idx=0; idx<9; idx++) {
            if (grid[row][idx].getValue()==value){
                return false;
            }

            if (grid[idx][column].getValue()==value){
                return false;
            }
        }

        int rowStart = row - (row%3);
        int columnStart = column - (column%3);

        for(int r=0+rowStart; r < 3+rowStart; r++) {
            for(int c=0+columnStart; c < 3+columnStart; c++) {
                if(grid[r][c].getValue()==value){
                    return false;
                }
            }
        }
        return true;
    }
}