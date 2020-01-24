package mj.examples;

class MainCFGexo {
    @SuppressWarnings("unused")
    public static void main(String[] a) {
	int x;
	x = new CFGexo().run(9);
    }
}

class CFGexo {

  int[] t;

  @SuppressWarnings("unused")
  public int run(int size) {
      int i;
      t = new int[size];
      i = 0;
      while (i<size) {
	  t[i] = size-i;
	  i = i+1;
      }
      System.out.println(t[0]);
      return 0;
  }
    
}
