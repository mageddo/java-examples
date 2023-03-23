package vanillajavaexamples.sets;

import java.net.SocketException;
import java.util.TreeSet;

public class TreeSetItemUpdateMain {
  static class Wrapper implements Comparable<Wrapper> {

    private int v;

    public Wrapper(int v) {
      this.v = v;
    }

    public Wrapper setV(int v) {
      this.v = v;
      return this;
    }

    @Override
    public int compareTo(Wrapper o) {
      return Integer.compare(this.v, o.v);
    }

    @Override
    public String toString() {
      return String.valueOf(this.v);
    }

    @Override
    public int hashCode() {
      return Integer.hashCode(this.v);
    }
  }

  public static void main(String[] args) throws SocketException {

    final var set = new TreeSet<Wrapper>();
    final var second = new Wrapper(2);
    set.add(new Wrapper(3));
    set.add(new Wrapper(1));
    set.add(second);

    second.setV(-1);
    set.remove(second);
    set.add(second);

    System.out.println(set.first());
    System.out.println(set);
  }
}
