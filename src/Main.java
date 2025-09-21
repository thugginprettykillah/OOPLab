public class Main {
    public static void main(String[] args) {
        MyArray roots = new MyArray(2, new ComplexNumber(1,0));
        roots.set(1, new ComplexNumber(3,0));
        Polinom polinom = new Polinom(new ComplexNumber(2, 0), roots);
        Application appPolinom = new Application(ComplexNumber.getParser());
        appPolinom.run(polinom);
    }
}