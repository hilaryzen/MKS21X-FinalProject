import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class Sheet {
  private ArrayList<ArrayList<Cell>> data;
  private int[] rows;
  private int[] cols;

  public Sheet(String filename) throws FileNotFoundException {
    File csv = new File(filename);
    Scanner in = new Scanner(csv);
    while(in.hasNext()) {

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
      ans = ans + data.get(index).get(i) + "  ";
    }
    return ans;
  }
}
