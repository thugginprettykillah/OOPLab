import java.io.PrintStream;
import java.util.Locale;
import java.util.Scanner;
import java.util.function.Function;

public class ApplicationConsole {

    private static final Scanner in = new Scanner(System.in);
    private static final PrintStream out = System.out;
    private final Function<String, Number> parser;

    public ApplicationConsole(Function<String, Number> parser) {
        this.parser = parser;
    }

    public void run(Polinom polinom)
    {
        Locale.setDefault(Locale.ENGLISH);

        boolean running = true;
        while (running) {
            out.print("\n\n\n\n");
            printMenu();
            String cmd = in.nextLine().trim();

            try {
                switch (cmd) {
                    case "1":
                        readPolinom(polinom);
                        break;
                    case "2":
                        setCoeffOrRoot(polinom);
                        break;
                    case "3":
                        makeResize(polinom);
                        break;
                    case "4":
                        makeEvaluate(polinom);
                        break;
                    case "5":
                        printPolinom(polinom);
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

    private void printMenu()
    {
        out.println("============МЕНЮ============");
        out.println("1. Ввести новый полином");
        out.println("2. Изменить старший коэффициент или корень по выбору");
        out.println("3. Изменить размерность");
        out.println("4. Вычислить полином в точке x");
        out.println("5. Вывести полином");
        out.println("0. Выход");
    }

    private MyArray readArray()
    {
        MyArray array = new MyArray(0, null);
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
        return array;
    }

    private void readPolinom(Polinom polinom)
    {
        out.print("Введите старший коэффициент: ");
        Number leadCoeff = readValue();
        out.println("\nСтарший коэффициент зафиксирован, сейчас нужно ввести корни по одному, для завершения - Enter:");
        MyArray roots = readArray();
        polinom.setLeadCoeff(leadCoeff);
        polinom.setRoots(roots);
        out.println("Новый полином создан!");
    }

    private void setCoeffOrRoot(Polinom polinom)
    {
        out.println("Что изменить?\n1.Старший коэффициент\n2.Корень\n0.Назад");
        int choice = readInteger();
        switch (choice) {
            case 1:
                out.println("Введите новый коэффициент:");
                polinom.setLeadCoeff(readValue());
                System.out.println("Коэффициент изменен!");
                break;
            case 2:
                if (polinom.getRoots().getSize() == 0) {
                    out.println("Нет корней для изменений");
                    break;
                }
                out.println("Какой корень изменить? (1.." + polinom.getDegree() + ")");
                int index = readInteger() - 1;
                out.println("Введите новый корень: ");
                Number value = readValue();
                polinom.setRoot(index, value);
                break;
            default:
                out.println("Выход...");
        }
    }

    private void makeResize(Polinom polinom)
    {
        boolean waiting = true;
        int newCapacity;
        out.println("Укажите новую размерность полинома: ");
        while (waiting){
            try {
                newCapacity = readInteger();
                polinom.resizeRoots(newCapacity);
                waiting = false;
            } catch (Exception e) {
                out.println("Еще раз.");
                if (in.hasNextLine()) in.nextLine();
            }
        }
    }

    private void makeEvaluate(Polinom polinom)
    {
        out.println("Введите x: ");
        Number x = readValue();
        out.println(polinom.evaluate(x).toString());
    }

    private void printPolinom(Polinom polinom)
    {
        out.println("Выберите режим вывода\n1. В раскрытом виде\n2. Со скобками\n0. Назад");
        int choice = readInteger();
        switch (choice) {
            case 1:
                out.println("Текущий полином:\n" + polinom.toStringWithDegree());
                break;
            case 2:
                out.println("Текущий полином:\n" + polinom.toStringWithBrackets());
            default:
                break;
        }
    }

    private Number readValue()
    {
        while (true){
            try {
                String token = in.nextLine();
                if (token.trim().isEmpty()) continue;
                Number value = parser.apply(token);
                return value;
            } catch (RuntimeException e) {
                out.println("Не удалось распарсить, попробуйте еще раз... " + e.getMessage());
            }
        }
    }

    private int readInteger()
    {
        while (true){
            try {
                int value = in.nextInt();
                if (in.hasNextLine()) in.nextLine();
                return value;
            } catch (RuntimeException e) {
                out.println("Ошибка... " + e.getMessage());
                if (in.hasNextLine()) in.nextLine();
            }
        }
    }
}
