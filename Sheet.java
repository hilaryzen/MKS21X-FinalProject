import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


public class Sheet {
  private ArrayList<ArrayList<Cell>> data;
  private int[] rows;
  private int[] cols;

  public Sheet(String filename) {
    ArrayList<ArrayList<Cell>> data = new ArrayList<ArrayList<Cell>>();
    int row = 0;
    try {
      File csv = new File(filename);
      Scanner in = new Scanner(csv);
      while(in.hasNext()) {
        String line = in.next();
        String[] entries = line.split(",");
        data.add(new ArrayList<Cell>());
        for (int i = 0; i < entries.length; i++) {
          Cell<String> newCell = new Cell(entries[i]);
          data.get(row).add(newCell);
        }
      }
    } catch (FileNotFoundException e) {
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
      ans = ans + data.get(index).get(i) + "  ";
    }
    return ans;
  }
}
