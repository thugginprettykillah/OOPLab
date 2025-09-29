package server.app;

import server.domain.*;


import java.util.List;

public interface PolinomServiceInterface {
    void initPolinom(Numberic leadCoeff, List<Numberic> roots);

    void changeLeadCoeef(Numberic leadCoeff);

    void changeRoot(int index, Numberic newRoot);

    void resize(int newSize);

    Numberic evaluate(Numberic x);

    String getAsText(boolean withBrackets);

    Polinom getCurrentPolinom();
}
