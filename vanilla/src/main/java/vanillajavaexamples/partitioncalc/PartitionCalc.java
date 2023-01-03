package vanillajavaexamples.partitioncalc;

public class PartitionCalc {
  public static int calcPartition(Object o, int n){
    return Math.abs(o.hashCode()) % n;
  }
}
