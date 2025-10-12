package server.domain;

import server.app.NumberFactory;

public class Polinom<T extends Numberic> {
    private T leadCoeff ;
    private MyArray<T> roots;
    private MyArray<T> coeffs; // хранятся от младшего к старшему!
    private int degree = 0;
    private NumberFactory<T> factory;

    public Polinom(T leadCoeff, MyArray<T> roots, NumberFactory<T> factory)
    {
        this.leadCoeff = leadCoeff;
        this.roots = roots;
        this.factory = factory;
        degree = roots.getSize();
        computeCoeffsFromRoots();
    }

    public MyArray<T> getRoots() {
        return roots;
    }
    public MyArray<T> getCoeffs() {
        return coeffs;
    }
    public int getDegree() {
        return degree;
    }

    public void setRoot(int index, T root)
    {
        roots.set(index, root);
        computeCoeffsFromRoots();
    }
    public void setRoots(MyArray<T> roots)
    {
        this.roots = roots;
        degree = roots.getSize();
        computeCoeffsFromRoots();
    }

    public void setLeadCoeff(T leadCoeff)
    {
        this.leadCoeff = leadCoeff;
        computeCoeffsFromRoots();
    }

    private void computeCoeffsFromRoots()
    {
        coeffs = new MyArray<T>(1, leadCoeff);
        T zero = factory.create(0, 0);
        T minusOne = factory.create(-1, 0);
        for (int i = 0; i < degree; i++) {
            T root = roots.get(i);

            MyArray<T> newCoeffs = new MyArray<T>(coeffs.getSize() + 1,  zero);
            for (int j = 0; j < coeffs.getSize(); j++) {
                T term1 = (T) coeffs.get(j)
                        .multiply(root.multiply(minusOne));
                newCoeffs.set(j, (T) newCoeffs.get(j)
                        .add(term1));
                newCoeffs.set(j + 1, (T) newCoeffs.get(j + 1).add(coeffs.get(j)));
            }
            coeffs = newCoeffs;
        }
    }

    public T evaluate(T x)
    {
        if (degree == 0) {
            return leadCoeff;
        }
        T result = coeffs.get(degree);
        for (int i = degree - 1; i >= 0; i--) {
            result = (T) result.multiply(x).add(coeffs.get(i));
        }
        return result;
    }

    public void resizeRoots(int newSize)
    {
        roots.resize(newSize);
        degree = newSize;
        computeCoeffsFromRoots();
    }

    public String toStringWithDegree()
    {
        StringBuilder polinom = new StringBuilder();
        T zero = factory.create(0,0);

        for (int i = degree; i >= 0; i--)
        {
            if (coeffs.get(i).equals(zero)) continue;
            if (i == 0) {
                polinom.append(coeffs.get(i).toString());
            } else {
                polinom.append(coeffs.get(i).toString()).append("*x^").append(i).append(" ");
            }
        }
        return polinom.toString();
    }
    public String toStringWithBrackets()
    {
        StringBuilder polinom = new StringBuilder();
        polinom.append(leadCoeff.toString());
        int zeros = 0;
        T zero = factory.create(0, 0);
        T minusOne = factory.create(-1, 0);

        for (int i = 0; i < roots.getSize(); i++) {
            if (roots.get(i).equals(zero)) {
                zeros++;
                continue;
            }
            polinom.append("(x ").append(roots.get(i).multiply(minusOne).toString()).append(")");
        }
        if (zeros != 0) return polinom.append("* x^"+zeros).toString();
        return polinom.toString();
    }

}
