import java.util.function.Function;

public final class ComplexNumber implements Number{
    private final double re;
    private final double im;

    public ComplexNumber(double re, double im) {
        this.re = re;
        this.im = im;
    }

    public double getRe() {
        return re;
    }

    public double getIm() {
        return im;
    }

    @Override
    public Number sqrt() {
        double r = Math.sqrt(this.normSq());
        double rePart = Math.sqrt((r + this.re) / 2);
        double imPart = Math.sqrt((r - this.re) / 2);

        if (this.im < 0) imPart = -imPart;
        return new ComplexNumber(rePart, imPart);
    }

    @Override
    public Number add(Number other) {
        if (!(other instanceof ComplexNumber)) throw new IllegalArgumentException("Must be complex");
        double newRe = this.re + ((ComplexNumber)other).re;
        double newIm = this.im + ((ComplexNumber)other).im;
        return new ComplexNumber(newRe, newIm);
    }

    @Override
    public Number subtract(Number other) {
        if (!(other instanceof ComplexNumber)) throw new IllegalArgumentException("Must be complex");
        double newRe = this.re - ((ComplexNumber)other).re;
        double newIm = this.im - ((ComplexNumber)other).im;
        return new ComplexNumber(newRe, newIm);
    }

    @Override
    public Number multiply(Number other) {
        if (!(other instanceof ComplexNumber)) throw new IllegalArgumentException("Must be complex");
        double im1 = this.im;
        double re1 = this.re;
        double im2 = ((ComplexNumber)other).im;
        double re2 = ((ComplexNumber)other).im;
        double newRe = re1 * re2 - im1 * im2;
        double newIm = re1 * im2 + im1 * re2;
        return new ComplexNumber(newRe, newIm);
    }

    @Override
    public Number divide(Number other) {
        if (!(other instanceof ComplexNumber)) throw new IllegalArgumentException("Must be complex");
        double denominator = ((ComplexNumber)other).re * ((ComplexNumber)other).re + ((ComplexNumber)other).im * ((ComplexNumber)other).im;
        if (denominator == 0) throw new ArithmeticException("div by aero");
        ComplexNumber sopr = new ComplexNumber(((ComplexNumber)other).re, -((ComplexNumber)other).im);
        multiply(sopr);
        divide(denominator);
        return this.multiply(sopr).divide(denominator);
    }

    @Override
    public Number divide(double scalar) {
        if (scalar == 0) throw new ArithmeticException("Div by zero");
        double newRe = this.re / scalar;
        double newIm = this.im / scalar;
        return new ComplexNumber(newRe, newIm);
    }

    @Override
    public double normSq() {
        return re * re + im * im;
    }

    @Override
    public int compareTo(Number o) {
        if (!(o instanceof ComplexNumber)) throw new IllegalArgumentException("Must be complex");
        double normThis = this.normSq();
        double normOther = o.normSq();
        if (normThis == normOther) {
            double fiThis = Math.atan2(this.im,this.re);
            double fiOther = Math.atan2(((ComplexNumber)o).im,((ComplexNumber)o).re);
            return Double.compare(fiThis,fiOther);
        }
        else {
            return Double.compare(normThis, normOther);
        }
    }

    @Override
    public String toString() {
        String sign = (im < 0) ? " - " : " + ";
        return re + sign + Math.abs(im) + "i";
    }

    public static Function<String, Number> getParser() {
        return s -> {
            String[] parts = s.trim().split("\\s+");
            if (parts.length == 1) {
                double re = Double.parseDouble(parts[0]);
                return new ComplexNumber(re, 0.0);
            } else if (parts.length == 2) {
                double re = Double.parseDouble(parts[0]);
                double im = Double.parseDouble(parts[1]);
                return new ComplexNumber(re, im);
            } else {
                throw new NumberFormatException("Для комплексного числа нужно ввести 1 или 2 вещественных числа через пробел");
            }

        };
    }
}
