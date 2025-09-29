package server.app;

import server.domain.*;

import java.util.List;

public class PolinomService implements PolinomServiceInterface{
    private Polinom polinom;

    public PolinomService()
    {
        MyArray roots = new MyArray(2, new ComplexNumber(1,0));
        roots.set(1, new ComplexNumber(3,0));
        Numberic leadCoeff = new ComplexNumber(2,0);
        polinom = new Polinom(leadCoeff, roots);
    }
    public PolinomService(Polinom polinom) {
        this.polinom = polinom;
    }


    @Override
    public void initPolinom(Numberic leadCoeff, List<Numberic> roots)
    {
        MyArray rootsArray = new MyArray(roots.size(), new ComplexNumber(0,0));
        for (int i = 0; i < roots.size(); i++) {
            rootsArray.set(i, roots.get(i));
        }
        polinom = new Polinom(leadCoeff, rootsArray);
    }

    @Override
    public void changeLeadCoeef(Numberic leadCoeff) {
        polinom.setLeadCoeff(leadCoeff);
    }

    @Override
    public void changeRoot(int index, Numberic newRoot) {
        polinom.setRoot(index, newRoot);
    }

    @Override
    public void resize(int newSize) {
        polinom.resizeRoots(newSize);
    }

    @Override
    public Numberic evaluate(Numberic x) {
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
    public Polinom getCurrentPolinom() {
        return polinom;
    }
}
