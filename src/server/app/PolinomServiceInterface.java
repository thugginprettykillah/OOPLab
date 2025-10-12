package server.app;

import server.domain.*;


import java.util.List;

public interface PolinomServiceInterface<T extends Numberic>{
    void initPolinom(T leadCoeff, List<T> roots);

    void changeLeadCoeef(T leadCoeff);

    void changeRoot(int index, T newRoot);

    void resize(int newSize);

    T evaluate(T x);

    String getAsText(boolean withBrackets);

    Polinom<T> getCurrentPolinom();

    void resetToDefault();
}
