import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/*Main executable class. */
public class App {
    static boolean isSolved = false;
    /**
     * @param args
     * @throws IOException
     * @throws FileNotFoundException
     */
    public static void main(String[] args) throws IOException, FileNotFoundException {
        
        File sd_input = new File("C:/Users/Stevin Sebastian/Desktop/sudoku/sudoku/lib/game.txt");

        Scanner scan = new Scanner(sd_input);
        

        GridCell [][] cellMatrix = new GridCell[9][9];
        String [][] stringMatrix = new String [9][9];
        int i=0;
        while(scan.hasNextLine() && i<9) {
            stringMatrix[i] = scan.nextLine().split(" ");
            int block=0;

            for (int j=0; j<9; j++){
                if((i>=0 && i<=2) && (j>=0 && j<=2))  {
                    block = 1;
                }
                else if((i>=0 && i<=2) && (j>=3 && j<=5)) {
                    block = 2;
                }
                else if((i>=0 && i<=2) && (j>=6 && j<=8)) {
                    block = 3;
                }
                else if((i>=3 && i<=5) && (j>=0 && j<=2)) {
                    block = 4;
                }
                else if((i>=3 && i<=5) && (j>=3 && j<=5)) {
                    block = 5;
                }
                else if((i>=3 && i<=5) && (j>=6 && j<=8)) {
                    block = 6;
                }
                else if((i>=6 && i<=8) && (j>=0 && j<=2)) {
                    block = 7;
                }
                else if((i>=6 && i<=8) && (j>=3 && j<=5)) {
                    block = 8;
                }
                else if((i>=6 && i<=8) && (j>=6 && j<=8)) {
                    block = 9;
                }

                cellMatrix[i][j] = new GridCell(stringMatrix[i][j], i, j, block);
            }
            i++;   
        }
        scan.close();

        Grid inputGrid = new Grid(cellMatrix);

        inputGrid.populateFinalOptions();

        //System.out.println("----------POPULATED-------------");
        //inputGrid.printFinalOptions();
        int solStoreSize = inputGrid.newSolutionStore.size();
        int flag=99;
        boolean checker=false;
        while(!inputGrid.getSolvedState()){
            if(flag-solStoreSize==0 && inputGrid.newSolutionStore.isEmpty()){
                inputGrid.setStuckState(true);
            }
            flag = solStoreSize;
            solStoreSize = inputGrid.newSolutionStore.size();

            if (inputGrid.getStuckState()){
                checker = inputGrid.bruteForce(0, 0);
                //System.out.println("----------BRUTE FORCE--------------");
                //inputGrid.printFinalOptions();
                break;
            }


            if(!inputGrid.newSolutionStore.isEmpty()){
                inputGrid.reviseFinalOptions();
                inputGrid.checkUniqueOptions();
                checker = inputGrid.isFull();
                //System.out.println("------------REVISED & UNIQUE CHECK------------");
                //inputGrid.printFinalOptions();
            }
            
            if (inputGrid.newSolutionStore.isEmpty())
            {
                inputGrid.nakedPairRemoval();
                inputGrid.checkUniqueOptions();
                //System.out.println("----------NAKED PAIR--------------");
                //inputGrid.printFinalOptions();
                
            }
            
        }

        if(checker){
            System.out.println("Sudoku Solved!");
            writeMatrixOutput("C:/Users/Stevin Sebastian/Desktop/sudoku/sudoku/lib/output.txt", inputGrid.getGrid());
        }
        else{
            System.out.println("No Unique Solution..");
        }
        
        
    }

    /**
     * @param fileName
     * @param m
     * @throws IOException
     */
    public static void writeMatrixOutput(String fileName, GridCell [][] m) throws IOException{
        BufferedWriter b = new BufferedWriter(new FileWriter(fileName));

        for(int i=0; i<m.length; i++){
            for(int j=0; j<m[i].length; j++){
                if(j%3!=0){
                    b.write(m[i][j].getValue() + " ");
                }
                else {
                    b.write(m[i][j].getValue() + " ");
                }
                

            }
            b.newLine();
        }
        b.flush();
        b.close();
    }
}
