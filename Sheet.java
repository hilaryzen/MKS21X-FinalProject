import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


public class Sheet {
  private ArrayList<ArrayList<Cell>> data = new ArrayList<ArrayList<Cell>>();
  private int[] rows;
  private int[] cols;

  public Sheet(String filename) {
    //ArrayList<ArrayList<Cell>> data = new ArrayList<ArrayList<Cell>>();
    ArrayList<String> lines = new ArrayList<String>();
    try {
      File csv = new File(filename);
      Scanner in = new Scanner(csv);
      while(in.hasNext()) {
        String line = in.nextLine();
        lines.add(line);
      }
    } catch (FileNotFoundException e) {
      System.out.println("File not found");
      e.printStackTrace();
      System.exit(1);
    }

    //Loops through lines to add rows
    for (int i = 0; i < lines.size(); i++) {
      String[] entries = lines.get(i).split(",");
      data.add(new ArrayList<Cell>());
      //Loops to add individual data cells
      for (int j = 0; j < entries.length; j++) {
        Cell<String> newCell = new Cell<String>(entries[j]);
        data.get(i).add(newCell);
        //System.out.println(data.get(i).get(j));
      }
      //System.out.println(data.get(i));
    }
  }

  //Returns the cell at the row and col given
  public Cell get(int row, int col) {
    //System.out.println(data.get(row).get(col).getValue());
    return data.get(row).get(col);
  }

  //Returns String contains the contents of the row at the index given
  public String getRow(int index) {
    String ans = "";
    for (int i = 0; i < data.get(index).size(); i++) {
      String entry = get(index, i).toString();
      int spaceLength = longestInCol(i);
      ans = ans + String.format("%-" + spaceLength + "." + spaceLength + "s", entry);
    }
    return ans;
  }

  //Returns String containing the contents of the column at the given index
  //Does not work if not all rows are the same length
  public String getCol(int index) {
    String ans = get(0, index).toString();
    for (int i = 1; i < data.size(); i++) {
      ans = ans + ", " + get(i, index).getValue();
    }
    return ans;
  }

  public int longestInCol(int index) {
    int longest = get(0, index).toString().length();
    int length = 0;
    for (int i = 1; i < data.size(); i++) {
      length = get(i, index).toString().length();
      if (length > longest) {
        longest = length;
      }
    }
    return longest;
  }

  //Returns String of the entire table
  public String getTable() {
    String ans = "";
    for (int i = 0; i < data.size(); i++) {
      ans = ans + getRow(i) + "\n";
    }
    return ans;
  }
}
