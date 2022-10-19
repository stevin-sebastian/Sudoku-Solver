import java.util.ArrayList;

/*Non-executable class, for the purpose of creating
 gridcell objects to ultimately create a gridcell [][] matrix.*/
public class GridCell {
    private String value;
    private int row;
    private int column;
    private int block;
    ArrayList<String> finalOptions;

    /*Constructor */
    public GridCell(String v, int r, int c, int b) {
        this.value = v;
        this.row = r;
        this.column = c;
        this.block = b;
        this.finalOptions = new ArrayList<String>();
    }

    /*Getter */
    public int getRow() {
        return row;
    }

    /*Getter */
    public int getColumn() {
        return column;
    }

    /*Getter */
    public int getBlock() {
        return block;
    }

    /*Getter */
    public String getValue() {
        return value;
    }

    /*If only one element remains in finalOptions, this method sets it to <value>. */
    public void setValueToFinalOption() {
        this.value = this.finalOptions.get(0);
    }

    /*Setter */
    public void setValue(String v) {
        this.value = v;
    }

    /*Removes all values from the ArrayList finalOptions of the cell. */
    public void resetFinalOptions(){
        this.finalOptions.removeAll(finalOptions);
    }
}
