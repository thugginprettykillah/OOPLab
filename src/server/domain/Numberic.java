package server.domain;

public interface Numberic extends Comparable<Numberic>{
    Numberic sqrt();
    Numberic add(Numberic other);
    Numberic subtract(Numberic other);
    Numberic multiply(Numberic other);
    Numberic divide(Numberic other);
    Numberic divide(double scalar);
    double normSq();
    String toString();
    int compareTo(Numberic o);
}
