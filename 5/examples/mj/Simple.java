package mj.examples;

class Simple {
    @SuppressWarnings("unused")
    public static void main(String[] a) {
    T x;
    int y;
    x = new T();
    y = x.init(10);
    System.out.println(x.size());
  }
}

class T {

  int s;
  int[] t;

  @SuppressWarnings("unused")
  public int init(int size) {
    s = size;
    t = new int[size];
    return 0;
  }

  @SuppressWarnings("unused")
  public int size() {
    return t.length;
  }
    
}
