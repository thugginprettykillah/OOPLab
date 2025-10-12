package server.app;

import server.domain.DoubleNumber;

public class DoubleNumberFactory implements NumberFactory<DoubleNumber> {
    @Override
    public DoubleNumber create(double re, double im) {
        return new DoubleNumber(re);
    }

    @Override
    public DoubleNumber parse(String str) {
        return new DoubleNumber(Double.parseDouble(str));
    }
}
