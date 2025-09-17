public class Main {
    public static void main(String[] args) {
        Number fillValueDouble = new DoubleNumber(0.0);
        MyArray arrayDouble = new MyArray(10, fillValueDouble);
        Application app = new Application(s -> new DoubleNumber(Double.parseDouble(s)));

        Number fillValueComplex = new ComplexNumber(0.0, 0.0);
        MyArray arrayComplex = new MyArray(10, fillValueComplex);
        Application appComplex = new Application(ComplexNumber.getParser());

        appComplex.run(arrayComplex);
    }
}