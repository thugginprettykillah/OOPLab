public interface Number extends Comparable<Number>{

    Number sqrt();
    Number add(Number other);
    Number subtract(Number other);
    Number multiply(Number other);
    Number divide(Number other);
    Number divide(double scalar);
    double normSq();
    String toString();
    int compareTo(Number o);
}
