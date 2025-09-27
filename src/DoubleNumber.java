public final class DoubleNumber implements Number {
    private final double value;

    public DoubleNumber(double value) {
        this.value = value;
    }

    @Override
    public Number sqrt() {
        return new DoubleNumber(Math.sqrt(value));
    }

    @Override
    public Number add(Number other)
    {
        if (!(other instanceof DoubleNumber)) throw new IllegalArgumentException("Must be DoubleNumber");
        return new DoubleNumber(this.value + ((DoubleNumber) other).value);
    }

    @Override
    public Number subtract(Number other)
    {
        if (!(other instanceof DoubleNumber)) throw new IllegalArgumentException("Must be DoubleNumber");
        return new DoubleNumber(this.value - ((DoubleNumber) other).value);
    }

    @Override
    public Number multiply(Number other)
    {
        if (!(other instanceof DoubleNumber)) throw new IllegalArgumentException("Must be DoubleNumber");
        return new DoubleNumber(this.value * ((DoubleNumber) other).value);
    }

    @Override
    public Number divide(Number other)
    {
        if (!(other instanceof DoubleNumber)) throw new IllegalArgumentException("Must be DoubleNumber");
        return new DoubleNumber(this.value / ((DoubleNumber) other).value);
    }

    @Override
    public Number divide(double scalar)
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
    public int compareTo(Number o)
    {
        if (!(o instanceof DoubleNumber)) throw new IllegalArgumentException("Must be DoubleNumber");
        return Double.compare(this.value, ((DoubleNumber) o).value);
    }

}