public class Polinom {
    private Number leadCoeff ;
    private MyArray roots;
    private MyArray coeffs; // хранятся от младшего к старшему!
    private int degree = 0;

    public Polinom()
    {
        leadCoeff = new ComplexNumber(0,0);
        degree = 0;
    }
    public Polinom(Number leadCoeff, MyArray roots)
    {
        this.leadCoeff = leadCoeff;
        this.roots = roots;
        degree = roots.getSize();
        computeCoeffsFromRoots();
    }

    public MyArray getRoots() {
        return roots;
    }
    public MyArray getCoeffs() {
        return coeffs;
    }
    public int getDegree() {
        return degree;
    }

    public void setRoot(int index, Number root)
    {
        roots.set(index, root);
        computeCoeffsFromRoots();
    }
    public void setRoots(MyArray roots)
    {
        this.roots = roots;
        degree = roots.getSize();
        computeCoeffsFromRoots();
    }

    public void setLeadCoeff(Number leadCoeff)
    {
        this.leadCoeff = leadCoeff;
        computeCoeffsFromRoots();
    }

    private void computeCoeffsFromRoots()
    {
        coeffs = new MyArray(1, leadCoeff);

        for (int i = 0; i < degree; i++) {
            Number root = roots.get(i);

            MyArray newCoeffs = new MyArray(coeffs.getSize() + 1, new ComplexNumber(0,0));
            for (int j = 0; j < coeffs.getSize(); j++) {
                Number term1 = coeffs.get(j).multiply(root.multiply(new ComplexNumber(-1,0)));
                newCoeffs.set(j, newCoeffs.get(j).add(term1));
                newCoeffs.set(j + 1, newCoeffs.get(j + 1).add(coeffs.get(j)));
            }
            coeffs = newCoeffs;
        }
    }

    public Number evaluate(Number x)
    {
        if (degree == 0) {
            return leadCoeff;
        }
        Number result = coeffs.get(degree);
        for (int i = degree - 1; i >= 0; i--) {
            result = result.multiply(x).add(coeffs.get(i));
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
        for (int i = degree; i >= 0; i--)
        {
            if (coeffs.get(i).equals(new ComplexNumber(0,0))) continue;
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
        for (int i = 0; i < roots.getSize(); i++) {
            if (roots.get(i).equals(new ComplexNumber(0,0))) {
                zeros++;
                continue;
            }
            polinom.append("(x ").append(roots.get(i).multiply(new ComplexNumber(-1,0)).toString()).append(")");
        }
        if (zeros != 0) return polinom.append("* x^"+zeros).toString();
        return polinom.toString();
    }
}
