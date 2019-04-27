public class test{
  public static void main(String[] args) {
    Sheet file = new Sheet("TestCSV.csv");
    System.out.println(file);
    file.sortRows(0);
    System.out.println(file);
    file.sortRows(1);
    System.out.println(file);
    file.sortRows(2);
    System.out.println(file);
    file.sortRows(3);
    System.out.println(file);
    file.sortRows(4);
    System.out.println(file);
    file.sortRows(5);
    System.out.println(file);
  }
}