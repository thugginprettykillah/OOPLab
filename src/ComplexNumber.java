public final class ComplexNumber implements Number{
    private final TComplex value;

    public ComplexNumber(TComplex value) {
        this.value = value;
    }

    public TComplex getValue() {
        return value;
    }


    @Override
    public Number sqrt() {
        return new ComplexNumber(this.value.sqrt());
    }

    @Override
    public Number add(Number other) {
        if (!(other instanceof ComplexNumber)) throw new IllegalArgumentException("Must be complex");
        TComplex result = new TComplex(value.getRe(), value.getIm());
        result.add(((ComplexNumber)other).value);
        return new ComplexNumber(result);
    }

    @Override
    public Number subtract(Number other) {
        if (!(other instanceof ComplexNumber)) throw new IllegalArgumentException("Must be complex");
        TComplex result = new TComplex(value.getRe(), value.getIm());
        result.subtract(((ComplexNumber)other).value);
        return new ComplexNumber(result);
    }

    @Override
    public Number multiply(Number other) {
        if (!(other instanceof ComplexNumber)) throw new IllegalArgumentException("Must be complex");
        TComplex result = new TComplex(value.getRe(), value.getIm());
        result.multiply(((ComplexNumber)other).value);
        return new ComplexNumber(result);
    }

    @Override
    public Number divide(Number other) {
        if (!(other instanceof ComplexNumber)) throw new IllegalArgumentException("Must be complex");
        TComplex result = new TComplex(value.getRe(), value.getIm());
        result.divide(((ComplexNumber)other).value);
        return new ComplexNumber(result);
    }

    @Override
    public Number divide(double scalar) {
        TComplex result = new TComplex(value.getRe(), value.getIm());
        result.divide(scalar);
        return new ComplexNumber(result);
    }

    @Override
    public double normSq() {
        return value.normSq();
    }

    @Override
    public int compareTo(Number o) {
        if (!(o instanceof ComplexNumber)) throw new IllegalArgumentException("Must be complex");
        return value.compareTo(((ComplexNumber) o).value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
