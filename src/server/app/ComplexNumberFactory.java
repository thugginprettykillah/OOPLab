package server.app;

import server.domain.ComplexNumber;

public class ComplexNumberFactory implements NumberFactory<ComplexNumber> {

    @Override
    public ComplexNumber create(double re, double im) {
        return new ComplexNumber(re, im);
    }

    @Override
    public ComplexNumber parse(String str) {
        return (ComplexNumber) ComplexNumber.getParser().apply(str);
    }
}
