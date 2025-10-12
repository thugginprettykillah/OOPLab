package server.app;

import server.domain.ComplexNumber;
import server.domain.Numberic;

public interface NumberFactory<T extends Numberic> {
    T create (double re, double im);
    T parse(String str);
}

