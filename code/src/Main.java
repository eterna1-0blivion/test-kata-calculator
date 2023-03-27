import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scannerInput = new Scanner(System.in);

        System.out.print("Введите арифметическое выражение: ");
        String output = calc(String.valueOf(scannerInput.nextLine()));

        System.out.print("Результат работы калькулятора: ");
        System.out.println(output);
    }

    public static String calc(String input){

        Operations expression = new Operations();

        char sign = expression.signCheck(input);
        System.out.println("Оператор: " + sign);

        int[] numbers = expression.valueCheck(input);
//        int[] numbers = {5, 10};
        System.out.println("Числа: " + numbers[0] + ", " + numbers[1]);

        int output = 0;
        switch (sign) {
            case '+' -> output = (numbers[0] + numbers[1]);
            case '-' -> output = (numbers[0] - numbers[1]);
            case '*' -> output = (numbers[0] * numbers[1]);
            case '/' -> output = (numbers[0] / numbers[1]);
            default -> throw new RuntimeException("Арифметическая операция не распознана: " + sign);
        }
        return String.valueOf(output);
    }

    static class Operations {

        char signCheck(String input){

            char sign;
            if (contains(input, "+")) {
                sign = '+';
            } else if (contains(input, "-")) {
                sign = '-';
            } else if (contains(input, "*")) {
                sign = '*';
            } else if (contains(input, "/")) {
                sign = '/';
            } else {
                throw new RuntimeException("Арифметическая операция не распознана");
            }
            return sign;
        }

        boolean romanCheck(String input) {
            
            return true;
        }

        int[] valueCheck(String input) {

            String[] stringValues = input.split("\\" + signCheck(input));

//            System.out.println(Arrays.toString(numbers));

            try {
                return new int[]{Integer.parseInt(stringValues[0]), Integer.parseInt(stringValues[1])};
            } catch (NumberFormatException e) {
                throw new RuntimeException("Неверный формат арифметического выражения");
            }
        }

    }

    static boolean contains(String content, String lookingFor) {
        return content.contains(lookingFor);
    }

}