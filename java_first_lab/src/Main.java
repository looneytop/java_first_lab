import java.util.Scanner;
import java.util.Locale;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;

public class Main {
    public static ComplexMatrix readMatrix(Scanner scanner) {
        System.out.print("Число строк: ");
        int rows = scanner.nextInt();

        System.out.print("Число столбцов: ");
        int cols = scanner.nextInt();

        ComplexMatrix matrix = new ComplexMatrix(rows, cols);

        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                System.out.printf("Элемент [%d][%d] (re im): ", i + 1, j + 1);
                double re = scanner.nextDouble();
                double im = scanner.nextDouble();
                matrix.set(i, j, new Complex(re, im));
            }
        }
        return matrix;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        scanner.useLocale(Locale.US);

        try {
            System.out.println("Введите первую матрицу:");
            ComplexMatrix first = readMatrix(scanner);

            System.out.println("\nВыберите операцию:");
            System.out.println("1. Сложить");
            System.out.println("2. Вычесть");
            System.out.println("3. Умножить");
            System.out.println("4. Разделить");
            System.out.println("5. Транспонировать");
            System.out.println("6. Вычислить определитель");
            System.out.print("Номер операции: ");
            int choice = scanner.nextInt();

            if (choice < 1 || choice > 6) {
                System.out.println("Неизвестная операция.");
                return;
            }

            ComplexMatrix second = null;
            if (choice <= 4) {
                System.out.println("Введите вторую матрицу:");
                second = readMatrix(scanner);
            }

            ComplexMatrix result;
            switch (choice) {
                case 1:
                    result = first.add(second);
                    break;
                case 2:
                    result = first.subtract(second);
                    break;
                case 3:
                    result = first.multiply(second);
                    break;
                case 4:
                    result = first.divide(second);
                    break;
                case 5:
                    result = first.findMatrixTransposition();
                    break;
                case 6:
                    System.out.println("Определитель: " + first.findDeterminant().get_string_format());
                    return;
                default:
                    throw new IllegalStateException("Неизвестная операция");
            }

            System.out.println("Результат:");
            result.printMatrix();
        } catch (InputMismatchException e) {
            System.out.println("Ошибка: ожидалось число. Для дробной части используйте точку.");
        } catch (NoSuchElementException e) {
            System.out.println("Ошибка: ввод закончился до завершения операции.");
        } catch (IllegalArgumentException | IllegalStateException | ArithmeticException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }


};