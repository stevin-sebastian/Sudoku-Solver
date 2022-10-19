/*Non-executable class used as a datatype to create the
  class variable <newSolutionStore> for the class Grid.*/
public class Coordinates {
    private int x; 
    private int y;
    private String val;

    /*Constructor */
    public Coordinates(int a, int b, String v){
        this.x = a;
        this.y = b;
        this.val = v;
    }

    /*Getter */
    public int[] getCoord(){
        int[] op = {x,y};
        return op;
    }

    /*Getter */
    public String getVal(){
        return val;
    }

}
