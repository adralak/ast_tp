package mj.examples;

class MPoint {
  public static void main(String[] a) {
    Point p1;
    Point p2;
    Point p3;
    int call;

    p1 = new Point(); p2 = new Point(); p3 = new Point();
    call = p1.setX(1); call = p1.setY(1);
    call = p2.setX(1); call = p2.setY(2);
    call = p3.setX(1); call = p3.setY(1);

    if (! p1.equals(p2)) {
      System.out.println(1);
    } else {
      System.out.println(0);
    }
    if (p1.equals(p3)) {
      System.out.println(2);
    } else {
      System.out.println(0);
    }
  }
}

class Point {

  int x;
  int y;

  public int setX(int nx) {
    x = nx;
    return 0;
  }

  public int setY(int ny) {
    y = ny;
    return 0;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }
    
  public boolean equals(Point that) {
    return ((((x < ((that.getX()) + 1)) &&
            ((that.getX()) < (x + 1))) &&
            (y < ((that.getY()) + 1))) &&
            ((that.getY()) < (y + 1)) );
  }
}
