package server.domain;

import java.util.Arrays;
import java.util.Comparator;
public class MyArray<T extends Numberic>{
    private static final double GROWTH_FACTOR = 1.5;

    private T[] data;
    private int size;

    public MyArray(int capacity, T fillValue)
    {
        if (capacity < 0) throw new IllegalArgumentException("capacity < 0");
        this.data = (T[]) new Numberic[Math.max(1, capacity)];
        this.size = (fillValue == null ? 0 : capacity);
        if (fillValue != null) Arrays.fill(this.data, fillValue);
    }

    public T get (int index)
    {
        checkRange(index);
        return data[index];
    };

    public T set (int index, T value)
    {
        checkRange(index);
        T old = this.data[index];
        this.data[index] = value;
        return old;
    }

    public int getSize() { return this.size; }


    public void add(T value)
    {
        this.regulateCapacity(size+1);
        data[size++] =  value;
    }

    public void resize(int newCapacity)
    {
        if (newCapacity < 0) throw new IllegalArgumentException("newCapacity < 0");
        if (newCapacity > data.length) {
            data = Arrays.copyOf(data, newCapacity);
            Arrays.fill(data, size, newCapacity, data[0].subtract(data[0]));
            size = newCapacity;
            return;
        }
        else if (newCapacity < data.length) {
            if (newCapacity < size){
                Arrays.fill(data, newCapacity, size, null);
                size = newCapacity;
            }
            data = Arrays.copyOf(data, newCapacity);
        }
        else return;
    }

    private void checkRange(int index)
    {
        if (index >= this.size || index < 0) throw new ArrayIndexOutOfBoundsException("invalid index");
    }

    private void regulateCapacity(int currSize)
    {
        if (currSize <= data.length) return;
        int newSize = data.length;
        while (newSize < currSize) {
            newSize = (int) Math.max(newSize * GROWTH_FACTOR, newSize + 1);
        }
        data = Arrays.copyOf(data, newSize);
    }

    private void sort(Comparator<Numberic> cmp) {
        Arrays.sort(data, 0, size, cmp);
    }
    public void sortAsc(Comparator<Numberic> cmp) { sort(cmp); }
    public void sortDesc(Comparator<Numberic> cmp) { sort(cmp.reversed()); }

    public T getAverage()
    {
        int n = getSize();
        if (n == 0) throw new ArrayStoreException();
        T sum = get(0);
        for (int i = 1; i < n; i++) { sum = (T) sum.add(data[i]); }
        return (T) sum.divide(n);
    }

    public T stdDev()
    {
        int n = getSize();
        if (n <= 1) throw new IllegalStateException("Для рассчета СКО нужно минимум 2 элемента");
        T average = getAverage();
        T sumOfSquares = null;
        for (int i = 0; i < n; i++) {
            T deviation = (T) average.subtract(get(i));
            T square = (T) deviation.multiply(deviation);

            if (sumOfSquares == null) sumOfSquares = square;
            else sumOfSquares = (T) sumOfSquares.add(square);
        }
        Numberic variance = sumOfSquares.divide(n-1);
        return (T) variance.sqrt();
    }

    @Override
    public String toString()
    {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < size; i++) {
            stringBuilder.append(data[i].toString()).append("  ");
        }
        return stringBuilder.toString();
    }
}