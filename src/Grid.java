import java.util.*;

/*Non-executable class, made for the purpose of making a 
gridcell[][]matrix object with methods that can be used to solve the matrix. */
public class Grid {
    GridCell [] [] grid;
    private boolean isSolved;
    private boolean isStuck;
    private static ArrayList<String> ref;

    /*ArrayList of Coordinates, this list stores the coordinates
    and values of newly found solutions that have been placed into 
    the grid. */
    ArrayList<Coordinates> newSolutionStore;

    /*Constructor. */
    public Grid(GridCell [][] g){
        this.grid = g;
        this.isSolved=false;
        this.isStuck=false;
        this.newSolutionStore = new ArrayList<Coordinates>();
        ref = new ArrayList<String>();
        ref.add("1");
        ref.add("2");
        ref.add("3");
        ref.add("4");
        ref.add("5");
        ref.add("6");
        ref.add("7");
        ref.add("8");
        ref.add("9");
    }

    /*Setter of boolean isSolved. */
    public void setSolvedState(boolean b){
        this.isSolved = true;
    }

    /*Getter of boolean isSolved. */
    public boolean getSolvedState(){
        return isSolved;
    }

    /*Setter of boolean isStuck */
    public void setStuckState(boolean b){
        this.isStuck = true;
    }

    /*Getter of boolean isStuck. */
    public boolean getStuckState(){
        return isStuck;
    }

    /*Getter of the gridcell matrix. */
    public GridCell[][] getGrid(){
        return grid;
    }

    /*Setter of the gridcell matrix. */
    public void setGrid(GridCell [][] g){
        grid = g;
    }

    /*Prints the entire gridcell matrix. */
    public void printGrid(){
        for(int k=0; k<9; k++){
            for(int j=0; j<9; j++){
                System.out.print(this.grid[k][j].getValue() + " ");
            }
            System.out.println();
        }
    }

    /*Populates every cell's initial list of finalOptions using
     the information from the input sudoku grid. If any list only 
     contains one possibility at this first stage, this method 
     also sets it to the grid.*/
    public void populateFinalOptions()
    {        
        int flag = 0;
        ArrayList<String> row; 
        ArrayList<String> column; 
        ArrayList<String> block; 
        ArrayList<String> all = new ArrayList<String>();
        for (int i=0; i<9; i++){
            for (int j=0; j<9; j++){
                if (this.grid[i][j].getValue().equals("0")) 
                {
                    flag++;
                    row = getRowValues(i);
                    row.removeIf(e -> (e.contains("0")));

                    column = getColumnValues(j);
                    column.removeIf(e -> (e.contains("0")));

                    block = getBlockValues(this.grid[i][j].getBlock());
                    block.removeIf(e -> (e.contains("0")));

                    all.addAll(row);
                    all.addAll(column);
                    all.addAll(block);
                    for (int k=0; k<ref.size(); k++) {
                        if(!all.contains(ref.get(k))) {
                            this.grid[i][j].finalOptions.add(ref.get(k));
                        }
                    }

                    if(this.grid[i][j].finalOptions.size()==1)
                    { 
                        newSolutionStore.add(new Coordinates(i, j, this.grid[i][j].finalOptions.get(0)));
                        this.grid[i][j].setValueToFinalOption();
                    }

                    all.removeAll(all);
                }
                else {
                    continue;
                }
            } 
        }
        if (flag==0){
            this.isSolved=true;
        }
    }

    /*Checks all the gridcell's finalOptions for any cell that 
    has a single potential value. If found, it sets it to the grid.*/
    public void checkUniqueOptions(){
        for (int i=0; i<9; i++) {
            for (int j=0; j<9; j++) {
                if(this.grid[i][j].finalOptions.size()==1){
                    newSolutionStore.add(new Coordinates(i, j, this.grid[i][j].finalOptions.get(0)));
                    this.grid[i][j].setValueToFinalOption();
                }
            }
        }
    }

    /*Returns true if the grid is full and there are no empty cells. */
    public boolean isFull(){
        for (int i=0; i<9; i++){
            for(int j=0; j<9; j++){
                if(this.grid[i][j].getValue().equals("0")){
                    return false;
                }
            }
        }
        this.isSolved=true;
        return true;
    }

    /*Checks each row, column and block for any 2 cells with identical
     final options, of the length 2. If so, then it removes those two numbers 
     from the rest of the cells within that particular row, column or block. 
     This is inline with the naked pair strategy of sudoku. */
    public void nakedPairRemoval(){        
        ArrayList<ArrayList<String>> rows;
        ArrayList<ArrayList<String>> columns;
        ArrayList<ArrayList<String>> blocks;
        for(int i=0; i<9; i++){
            rows = getRowOptions(i);
            columns = getColumnOptions(i);
            blocks = getBlockOptions(i+1);
            for(int j=0; j<9; j++){
                if(rows.get(j).size()==2 && !rows.get(j).isEmpty()){
                    if(findDuplicate(rows.get(j), rows)){
                        npRow(i, rows.get(j).get(0), rows.get(j).get(1), rows.get(j));
                    }
                }

                if(columns.get(j).size()==2 && !columns.get(j).isEmpty()){
                    if(findDuplicate(columns.get(j), columns)){

                        npColumn(i, columns.get(j).get(0), columns.get(j).get(1), columns.get(j));
                    }
                }

                if(blocks.get(j).size()==2 && !blocks.get(j).isEmpty()){
                    if(findDuplicate(blocks.get(j), blocks)){
                        npBlock(i+1, blocks.get(j).get(0), blocks.get(j).get(1), blocks.get(j));
                    }
                }
            }
        }
    }

    /*To return true if the list of all final options contains a
     duplicate of the given cell final options which is of length 2. 
     This is the check used to determine if there is a naked pair 
     within the row, column or block.  */
    public boolean findDuplicate(ArrayList<String> o, ArrayList<ArrayList<String>> list){
        int count =0;
        String [] original = new String[o.size()];
        original = o.toArray(original);
        String [] p;
        for(ArrayList<String> l : list ){
            p = new String[l.size()];
            p = l.toArray(p);
            if(p.length==2){
                if(Arrays.equals(original, p)==true){
                    count++;
                }
            }
        }
        if(count == 2)
            return true;
        else 
            return false;
    }

    /*To print the list of current final options of all the cells. */
    public void printFinalOptions(){
        for(int i=0; i<9; i++){
            for(int j=0; j<9; j++){
                System.out.print(this.grid[i][j].finalOptions + " \t");
                
            }
            System.out.println("");
        }
    }

    /*This method revises all the necessary cell options 
     within the grid, by referencing the list of new
     solutions that have been added to the grid recently. */
    public void reviseFinalOptions(){
        int[] coords = new int[2];
        for (Coordinates c : this.newSolutionStore){
            coords = c.getCoord();
            reviseRowOptions(coords[0], c.getVal());
            reviseColumnOptions(coords[1], c.getVal());
            reviseBlockOptions(this.grid[coords[0]][coords[1]].getBlock(), c.getVal());
        }
        this.newSolutionStore.removeAll(newSolutionStore);
    }

    /*This method uses a brute force approach to find the remaining 
     solutions for the grid. It goes through each cell, examines and
     tries each of its options, and goes down the grid until the grid is 
     invalid upon which it tries again, or the grid is filled.*/
    public boolean bruteForce(int row, int column){
        
        if(row == 8 && column ==9){
            this.printGrid();
            this.isSolved=true;
            return true;
        }

        if(column == 9){
            row++;
            column=0;
        }

        if(!this.grid[row][column].getValue().equals("0")){
            return bruteForce(row, column+1);
        }

        ArrayList<String> options = this.grid[row][column].finalOptions;
        for(int i=0; i<options.size(); i++){
            
            String o = options.get(i); 
            if(this.checkGrid(o, row, column, this.grid[row][column].getBlock())){
                this.grid[row][column].setValue(o);
                if(bruteForce(row, column+1)){
                    return true;
                }
            }

            this.grid[row][column].setValue("0");
        }
        return false;
    }

    /*This method checks if a potential value can be set to a cell,
     by checking its validity within the respective row, column and block. */
    public  boolean checkGrid(String val, int row, int column, int block){
        
        ArrayList<String> columnVals = getColumnValues(column);
        if(columnVals.contains(val)){
            return false;
        }
        ArrayList<String> rowVals = getRowValues(row);
        if(rowVals.contains(val)){
            return false;
        }
        ArrayList<String> blockVals = getBlockValues(block);
        if(blockVals.contains(val)){
            return false;
        }

        return true;
    }

    /*To return an ArrayList containing all 
    the cell values within a certain column. */
    public ArrayList<String> getColumnValues (int c) {
        ArrayList<String> column = new ArrayList<String>();

        for(int i=0; i<9; i++) {
            column.add(this.grid[i][c].getValue()); 
        }

        return column;
    }

    /*To return an ArrayList containing all 
    the cell values within a certain row. */
    public ArrayList<String> getRowValues (int r) {
        ArrayList<String> row = new ArrayList<String>();

        for(int i=0; i<9; i++) {
            row.add(this.grid[r][i].getValue()); 
        }

        return row;
    }

    /*To return an ArrayList containing all 
    the cell values within a certain block. */
    public ArrayList<String> getBlockValues(int b) {
        ArrayList<String> block = new ArrayList<String>();
        int [] x = blockCounters(b);
        for(int i=(0+x[0]); i<(3+x[0]); i++) {
            for(int j=(0+x[1]); j<(3+x[1]); j++){ 
                block.add(this.grid[i][j].getValue());
            } 
        }
        return block;
    }

    /*For a given column, erase the given value 
    from all the cells within that column. */
    public void reviseColumnOptions (int c, String val) {
        for(int i=0; i<9; i++) {
            this.grid[i][c].finalOptions.remove(val);
        }

    }

     /*For a given row, erase the given value 
    from all the cells within that row. */
    public void reviseRowOptions (int r, String val) {
        for(int i=0; i<9; i++) {
            this.grid[r][i].finalOptions.remove(val); 
        }
    }
    /*For a given block, erase the given value 
    from all the cells within that block. */
    public void reviseBlockOptions (int b, String val) {
        int [] x = blockCounters(b);
        for(int i=(0+x[0]); i<(3+x[0]); i++) {
            for(int j=(0+x[1]); j<(3+x[1]); j++){ 
                this.grid[i][j].finalOptions.remove(val);
            } 
        }
    }

    /*To return an array list containing all 
    option lists of cells within a particular column.*/
    public ArrayList<ArrayList<String>> getColumnOptions (int c) {
        ArrayList<ArrayList<String>> column = new ArrayList<ArrayList<String>>();
        for(int i=0; i<9; i++) {
            column.add(this.grid[i][c].finalOptions);
        }
        return column;

    }

    /*To return an array list containing all 
    option lists of cells within a particular row.*/
    public ArrayList<ArrayList<String>> getRowOptions (int r) {
        ArrayList<ArrayList<String>> row = new ArrayList<ArrayList<String>>();
        for(int i=0; i<9; i++) {
            row.add(this.grid[r][i].finalOptions); 
        }
        return row;
    }

    /*To return an array list containing all 
    option lists of cells within a particular block.*/
    public ArrayList<ArrayList<String>> getBlockOptions (int b) {
        ArrayList<ArrayList<String>> block = new ArrayList<ArrayList<String>>();
        int [] x = blockCounters(b);

        for(int i=(0+x[0]); i<(3+x[0]); i++) {
            for(int j=(0+x[1]); j<(3+x[1]); j++){ 
                block.add(this.grid[i][j].finalOptions);
            } 
        }

        return block;
    }    

    /*To remove the given naked pair values 
    from the other cells of the given column.*/
    public void npColumn (int c, String val1, String val2, ArrayList<String> skip) {
        for(int i=0; i<9; i++) {
            if (this.grid[i][c].finalOptions.equals(skip)){
                continue;
            }
            else{
                this.grid[i][c].finalOptions.remove(val1); 
                this.grid[i][c].finalOptions.remove(val2);
            }
        }

    }

    /*To remove the given naked pair values 
    from the other cells of the given row.*/
    public void npRow (int r, String val1, String val2, ArrayList<String> skip) {
        for(int i=0; i<9; i++) {
            if (this.grid[r][i].finalOptions.equals(skip)){
                continue;
            }
            else{
                this.grid[r][i].finalOptions.remove(val1); 
                this.grid[r][i].finalOptions.remove(val2);
            }
        }
    }

    /*To remove the given naked pair values 
    from the other cells of the given block.*/ 
    public void npBlock (int b, String val1, String val2, ArrayList<String> skip) {
        int [] x = blockCounters(b);
        for(int i=(0+x[0]); i<(3+x[0]); i++){
            for(int j=(0+x[1]); j<(3+x[1]); j++) {
                if (this.grid[i][j].finalOptions.equals(skip)){
                    continue;
                }
                else{
                    this.grid[i][j].finalOptions.remove(val1); 
                    this.grid[i][j].finalOptions.remove(val2);
                }
            }
        }
    }

    /*To retrieve the for loop variables when 
    searching through a particular block.*/
    public int [] blockCounters(int b){
        if(b==1) {
            return new int[]{0,0};
        }
        else if(b==2) {
            return new int[]{0,3};
        }
        else if(b==3) {
            return new int[]{0,6};
        }
        else if(b==4) {
            return new int[]{3,0};
        }
        else if(b==5) {
            return new int[]{3,3};
        }
        else if(b==6) {
            return new int[]{3,6};
        }
        else if(b==7) {
            return new int[]{6,0};
        }
        else if(b==8) {
            return new int[]{6,3};
        }
        else {
            return new int[]{6,6};
        }
    }
}