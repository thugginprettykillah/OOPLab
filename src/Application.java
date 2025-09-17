import java.io.PrintStream;
import java.util.Comparator;
import java.util.Locale;
import java.util.Scanner;
import java.util.function.Function;

public class Application{

    private static final Scanner in = new Scanner(System.in);
    private static final PrintStream out = System.out;
    private final Function<String, Number> parser;

    public Application(Function<String, Number> parser) {
        this.parser = parser;
    }

    public void run(MyArray array) {
        Locale.setDefault(Locale.ENGLISH);

        boolean running = true;
        while (running) {
            out.print("\n\n\n\n");
            printMenu();
            String cmd = in.nextLine().trim();

            try {
                switch (cmd) {
                    case "1":
                        readArray(array);
                        break;
                    case "2":
                        makeAvgAndDev(array);
                        break;
                    case "3":
                        makeSort(array);
                        break;
                    case "4":
                        makeResize(array);
                        break;
                    case "5":
                        makeSetElement(array);
                        break;
                    case "6":
                        printArray(array);
                        break;
                    case "0":
                        in.close();
                        out.close();
                        running = false;
                        break;
                    default:
                        out.println("Неизвестная команда...");
                        break;
                }
            } catch (Exception e) {
                out.println("Ошибка:" + e.getMessage());
                if (in.hasNextLine()) in.nextLine();
            }
        }
    }

    private void printMenu() {
        out.println("=======МЕНЮ=======");
        out.println("1. Ввод массива");
        out.println("2. Посчитать среднее и СКО");
        out.println("3. Сортировка (TimSort)");
        out.println("4. Изменить размерность");
        out.println("5. Изменить значение");
        out.println("6. Вывести массив");
        out.println("0. Выход");
    }

    private void readArray(MyArray array) {
        array.resize(0);
        out.println("Вводите элементы по одному, для завершения - Enter:");
        while (true) {
            try {
                String token = in.nextLine();
                if (token.trim().isEmpty()) break;
                Number value = parser.apply(token.trim());
                array.add(value);
            } catch (Exception e) {
                out.println("Неверный формат... " + e.getMessage());

            }
        }
        out.println("Готово! Size = " + array.getSize());
    }

    private void makeAvgAndDev(MyArray array) {
        Number average = array.getAverage();
        Number stdDev = array.stdDev();
        out.println("Среднее значение среди элементов: " + average.toString());
        out.println("СКО: " + stdDev.toString());
    }

    private void makeSort(MyArray array) {
        Comparator<Number> cmp = Comparator.naturalOrder();
        out.println("Выберите сортировку:\n1. По возрастанию\n2. По убыванию\n0. Назад");
        String answer = in.nextLine().trim();
        switch (answer) {
            case "1":
                array.sortAsc(cmp);
                out.println("Сортировка завершена!");
                break;
            case "2":
                array.sortDesc(cmp);
                out.println("Сортировка завершена!");
                break;
            case "0":
                break;
            default:
                out.println("Неизвестная команда...");
        }
    }

    private void makeResize(MyArray array) {
        boolean waiting = true;
        int newCapacity;
        out.println("Укажите новую размерность(Только натуральные числа и 0!)");
        while (waiting){
            try {
                newCapacity = in.nextInt();
                array.resize(newCapacity);
                waiting = false;
            } catch (Exception e) {
                out.println("Еще раз.");
                if (in.hasNextLine()) in.nextLine();
            }
        }
    }

    private void makeSetElement(MyArray array) {
        boolean waiting = true;
        while (waiting){
            try {
                out.println("Укажите индекс элемента: ");
                int index = readInteger();
                Number value = readValue();
                array.set(index, value);
                out.println("Элемент успешно вставлен!");
                return;
            } catch (Exception e) {
                out.println("Еще раз. " + e.getMessage());
            }
        }
    }

    private void printArray(MyArray array) {
        out.println();
        out.println("Текущий массив: ");
        out.println(array.toString());
    }

    private Number readValue() {
        out.println("Укажите значение элемента: ");
        boolean waiting = true;
        while (waiting){
            try {
                String token = in.nextLine();
                if (token.trim().isEmpty()) continue;
                Number value = parser.apply(token);
                return value;
            } catch (RuntimeException e) {
                out.println("Не удалось распарсить, попробуйте еще раз... " + e.getMessage());
            }
        }
        return null;
    }

    private int readInteger() {
        boolean waiting = true;
        while (waiting){
            try {
                int value = in.nextInt();
                return value;
            } catch (RuntimeException e) {
                out.println("Ошибка... " + e.getMessage());
                if (in.hasNextLine()) in.nextLine();
            }
        }
        return -1;
    }
}
