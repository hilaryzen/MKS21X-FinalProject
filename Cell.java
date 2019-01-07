public class Cell<T> {
  private T value;

  public Cell(T start) {
    value = start;
  }

  public String toString() {
    return "" + getValue();
  }

  public T getValue() {
    return value;
  }

  public void setValue(T newValue) {
    value = newValue;
  }
}
