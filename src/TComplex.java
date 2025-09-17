import java.util.function.Function;

public class TComplex implements Comparable<TComplex>{
    private double re;
    private double im;

    public TComplex(double re, double im) {
        this.re = re;
        this.im = im;
    }

    public double getRe() {
        return re;
    }

    public double getIm() {
        return im;
    }

    public void add(TComplex other) {
        this.re += other.re;
        this.im += other.im;
    }

    public void subtract(TComplex other) {
        this.re -= other.re;
        this.im -= other.im;
    }

    public void multiply(TComplex other) {
        double im1 = this.im;
        double re1 = this.re;
        double im2 = other.im;
        double re2 = other.im;
        this.re = re1 * re2 - im1 * im2;
        this.im = re1 * im2 + im1 * re2;
    }

    public void divide(TComplex other) {
        double denominator = other.re * other.re + other.im * other.im;
        if (denominator == 0) throw new ArithmeticException("div by aero");
        TComplex sopr = new TComplex(other.re, -other.im);
        multiply(sopr);
        divide(denominator);

    }

    public void divide(double scalar) {
        if (scalar == 0) throw new ArithmeticException("Div by zero");
        this.re /= scalar;
        this.im /= scalar;
    }

    public double normSq() {
        return re * re + im * im;
    }

    public TComplex sqrt() {
        double r = Math.sqrt(this.normSq());
        double rePart = Math.sqrt((r + this.re) / 2);
        double imPart = Math.sqrt((r - this.re) / 2);

        if (this.im < 0) imPart = -imPart;
        return new TComplex(rePart, imPart);
    }

    @Override
    public String toString() {
        String sign = (im < 0) ? "-" : "+";
        return re + sign + im + "i";
    }

    @Override
    public int compareTo(TComplex o) {
        double normThis = this.normSq();
        double normOther = o.normSq();
        return Double.compare(normThis, normOther);
    }

    public static Function<String, Number> getParser() {
        return s -> {
            String[] parts = s.trim().split("\\s+");
            if (parts.length == 1) {
                double re = Double.parseDouble(parts[0]);
                return new ComplexNumber(new TComplex(re, 0.0));
            } else if (parts.length == 2) {
                double re = Double.parseDouble(parts[0]);
                double im = Double.parseDouble(parts[1]);
                return new ComplexNumber(new TComplex(re, im));
            } else {
                throw new NumberFormatException("Для комплексного числа нужно ввести 1 или 2 вещественных числа через пробел");
            }

        };
    }
}
