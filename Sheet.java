import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


public class Sheet {
  private ArrayList<ArrayList<Cell>> data;
  private int[] rows;
  private int[] cols;

  public Sheet(String filename) {
    ArrayList<ArrayList<Cell>> data = new ArrayList<ArrayList<Cell>>(); //array
    try {

      File csv = new File(filename);
      Scanner in = new Scanner(csv); //import value
      int Row = 0; //counts index rows
      while(in.hasNextLine()) {
        data.add(new ArrayList<Cell>()); // initialize this row
        String[] line = in.nextLine().split(",");
        for (int x = 0; x < line.length; x++) {
          //System.out.println(line[x]);
          data.get(Row).add(new Cell(line[x])); //adds Cells at this Row
        }
        System.out.println(data.get(Row));
        //System.out.println(this.get(0,0));
        Row++;
      }
    }
    catch (FileNotFoundException e) {
      System.out.println("File not found");
      e.printStackTrace();
      System.exit(1);
    }

  }

  //Returns the cell at the row and col given
  public Cell get(int row, int col) {
    return data.get(row).get(col);
  }

  //Returns String contains the contents of the row at the index given
  public String getRow(int index) {
    String ans = "";
    for (int i = 0; i < data.get(index).size(); i++) {
      ans = ans + data.get(index).get(i).getValue() + "  ";
    }
    return ans;
  }
}
