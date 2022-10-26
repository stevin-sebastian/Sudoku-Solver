import java.util.HashSet;

/*Non-executable class, for the purpose of creating
 gridcell objects to ultimately create a gridcell [][] matrix.*/
public class GridCell {
    private int value;
    HashSet<Integer> options;

    /**
     * Class Constructor.
     * @param val
     */
    public GridCell(int val) {
        this.value = val;
        this.options = new HashSet<Integer>();
        if (val==0){  
            this.options.add(1);
            this.options.add(2);
            this.options.add(3);
            this.options.add(4);
            this.options.add(5);
            this.options.add(6);
            this.options.add(7);
            this.options.add(8);
            this.options.add(9);
        }

    }

    
    /**
     * Getter for the cell's value
     * @return Value
     */
    public int getValue() {
        return value;
    }
    
    /**
     * Setter for the cell's value
     * @param val - input value
     */
    public void setValue(int val) {
        this.value = val;
    }

    /**
     * Checks if two grid cells have equal option sets.
     * @param cell - second option set.
     * @return true - if the two option sets are equal.
     */
    public boolean optionsEquals(HashSet<Integer> cell){
        if(this.options.equals(cell)){
            return true;
        }
        return false;
    }
}
