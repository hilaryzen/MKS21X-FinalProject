public class Driver {
  public static void main(String[] args) {
    //Cell testing
    /*
    Cell<String> cell1 = new Cell<String>("Hi");
    System.out.println(cell1.getValue());

    Cell<Integer> cell2 = new Cell<Integer>(5);
    System.out.println(cell2.getValue());

    cell2.setValue(6);
    System.out.println(cell2.getValue());
    System.out.println(cell2);

    Cell<Integer> cell3 = new Cell<Integer>(11);
    System.out.println(cell3.getValue());

    System.out.println(cell2.getValue() + cell3.getValue());
    System.out.println(cell2 + " " + cell3);
    */

    String filename = "test.txt";
    Sheet sheet1 = new Sheet(filename);
    /*
    try {
      Sheet sheet1 = new Sheet(filename);
    } catch (FileNotFoundException e) {
      System.out.println("File not found");
      e.printStackTrace();
      System.exit(1);
    }
    */

    System.out.println(sheet1.get(0,0));
    System.out.println(sheet1.get(3,3));
    System.out.println(sheet1.getRow(0));
    System.out.println(sheet1.getRow(2));
    System.out.println(sheet1.getRow(5));
    System.out.println(sheet1.getCol(0));
    System.out.println(sheet1.getCol(2));
    //System.out.println(sheet1.getCol(5));

    System.out.println("  ");

    System.out.println(sheet1.getTable());
  }
}
