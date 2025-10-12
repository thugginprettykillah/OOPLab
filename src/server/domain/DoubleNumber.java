package server.domain;

public final class DoubleNumber implements Numberic {

    private final double value;

    public DoubleNumber(double value) {
        this.value = value;
    }

    @Override
    public Numberic sqrt() {
        return new DoubleNumber(Math.sqrt(value));
    }

    @Override
    public Numberic add(Numberic other)
    {
        if (!(other instanceof DoubleNumber)) throw new IllegalArgumentException("Must be DoubleNumber");
        return new DoubleNumber(this.value + ((DoubleNumber) other).value);
    }

    @Override
    public Numberic subtract(Numberic other)
    {
        if (!(other instanceof DoubleNumber)) throw new IllegalArgumentException("Must be DoubleNumber");
        return new DoubleNumber(this.value - ((DoubleNumber) other).value);
    }

    @Override
    public Numberic multiply(Numberic other)
    {
        if (!(other instanceof DoubleNumber)) throw new IllegalArgumentException("Must be DoubleNumber");
        return new DoubleNumber(this.value * ((DoubleNumber) other).value);
    }

    @Override
    public Numberic divide(Numberic other)
    {
        if (!(other instanceof DoubleNumber)) throw new IllegalArgumentException("Must be DoubleNumber");
        return new DoubleNumber(this.value / ((DoubleNumber) other).value);
    }

    @Override
    public Numberic divide(double scalar)
    {
        if (scalar == 0) throw new ArithmeticException("Div by zero!");
        return new DoubleNumber(this.value/scalar);
    }

    @Override
    public double normSq() {
        return value * value;
    }
    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public int compareTo(Numberic o)
    {
        if (!(o instanceof DoubleNumber)) throw new IllegalArgumentException("Must be DoubleNumber");
        return Double.compare(this.value, ((DoubleNumber) o).value);
    }
}