package server.app;

import server.domain.*;

import java.util.List;

public class PolinomService<T extends Numberic> implements PolinomServiceInterface<T>{
    private Polinom<T> polinom;
    private NumberFactory<T> factory;

    public PolinomService(NumberFactory<T> factory)
    {
        this.factory = factory;
        T defaultRoot1 = factory.create(1,0);
        T defaultRoot2 = factory.create(3,0);
        T defaultLead = factory.create(2,0);

        MyArray<T> roots = new MyArray<>(2, defaultRoot1);
        roots.set(1, defaultRoot2);
        this.polinom = new Polinom<T>(defaultLead, roots, factory);
    }
    public PolinomService(Polinom<T> polinom) {
        this.polinom = polinom;
    }


    @Override
    public void initPolinom(T leadCoeff, List<T> roots)
    {
        MyArray<T> rootsArray = new MyArray<T>(roots.size(), factory.create(0,0));
        for (int i = 0; i < roots.size(); i++) {
            rootsArray.set(i, roots.get(i));
        }
        polinom = new Polinom<>(leadCoeff, rootsArray, factory);
    }

    @Override
    public void changeLeadCoeef(T leadCoeff) {
        polinom.setLeadCoeff(leadCoeff);
    }

    @Override
    public void changeRoot(int index, T newRoot) {
        polinom.setRoot(index, newRoot);
    }

    @Override
    public void resize(int newSize) {
        polinom.resizeRoots(newSize);
    }

    @Override
    public T evaluate(T x) {
        return polinom.evaluate(x);
    }

    @Override
    public String getAsText(boolean withBrackets)
    {
        return withBrackets ?
                polinom.toStringWithBrackets() :
                polinom.toStringWithDegree();
    }

    @Override
    public Polinom<T> getCurrentPolinom() {
        return polinom;
    }

    @Override
    public void resetToDefault() {
        T defaultRoot1 = factory.create(1,0);
        T defaultRoot2 = factory.create(3,0);
        T defaultLead = factory.create(2,0);

        MyArray<T> roots = new MyArray<>(2, defaultRoot1);
        roots.set(1, defaultRoot2);
        this.polinom = new Polinom<T>(defaultLead, roots, factory);
    }

    public T parseNumber(String str) {
        return factory.parse(str);
    }
}
