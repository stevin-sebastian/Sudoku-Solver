import java.util.ArrayList;
import java.util.HashSet;

import javax.print.DocFlavor.INPUT_STREAM;

/*Non-executable class, for the purpose of creating
 gridcell objects to ultimately create a gridcell [][] matrix.*/
public class GridCell {
    private int value;
    HashSet<Integer> options;

    /*Constructor */
    public GridCell(int v) {
        this.value = v;
        this.options = new HashSet<Integer>();
        if (v==0){  
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

    /*Getter */
    public int getValue() {
        return value;
    }
    /*Setter */
    public void setValue(int v) {
        this.value = v;
    }

    /*If only one element remains in options, this method sets it to <value>. */
    // public void setValueToFinalOption() {
    //     this.value = this.options.get(0);
    // }

    /*Removes all values from the ArrayList options of the cell. */
    public void resetOptions(){
        this.options.removeAll(options);
    }

    public boolean optionsEquals(HashSet<Integer> cell){
        if(this.options.equals(cell)){
            return true;
        }
        return false;
    }
}
