import org.jetbrains.annotations.NotNull;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scannerInput = new Scanner(System.in);

        System.out.print("Введите арифметическое выражение: "); // optional
        String output = calc(scannerInput.nextLine().replaceAll(" ", ""));

        System.out.print("Результат работы калькулятора: "); // optional
        System.out.println(output);
    }

    public static @NotNull String calc(String input){

        Operations expression = new Operations();

        char sign = expression.signCheck(input);
        System.out.println("Оператор: " + sign); // debug

        boolean roman = expression.romanCheck(input);
        System.out.println("Римские цифры : " + roman); // debug

        int[] numbers = expression.valueCheck(input);
        System.out.println("Числа: " + numbers[0] + ", " + numbers[1]); // debug


        int result;
        switch (sign) {
            case '+' -> result = (numbers[0] + numbers[1]);
            case '-' -> result = (numbers[0] - numbers[1]);
            case '*' -> result = (numbers[0] * numbers[1]);
            case '/' -> result = (numbers[0] / numbers[1]);
            default -> throw new RuntimeException("Арифметическая операция не распознана");
        }
        return String.valueOf(result);
    }

    static class Operations {

        char signCheck(@NotNull String input){
            char sign;
            if (contains(input, "-")) {
                sign = '-';
            } else if (contains(input, "+")) {
                sign = '+';
            } else if (contains(input, "*")) {
                sign = '*';
            } else if (contains(input, "/")) {
                sign = '/';
            } else {
                throw new RuntimeException("Арифметическая операция не распознана");
            }
            return sign;
        }

        boolean romanCheck(@NotNull String input) {
            String[] stringValues = input.split("\\" + signCheck(input), 2);
            boolean check1;
            boolean check2;

            switch (stringValues[0]){
                case "I", "II", "III", "IV", "V", "VI", "VII", "VIII","IX", "X" -> check1 = true;
                default -> check1 = false;
            }
            switch (stringValues[1]){
                case "I", "II", "III", "IV", "V", "VI", "VII", "VIII","IX", "X" -> check2 = true;
                default -> check2 = false;
            }
            if (check1 && check2) {
                return true;
            } else if (check1 == check2){
                return false;
            } else {
                throw new RuntimeException("Неверный формат арифметического выражения");
            }
        }

        int[] valueCheck(@NotNull String input) {
            String[] stringValues = input.split("\\" + signCheck(input), 2);

            try {
                return new int[]{Integer.parseInt(stringValues[0]), Integer.parseInt(stringValues[1])};
            } catch (NumberFormatException e) {
                throw new RuntimeException("Неверный формат арифметического выражения");
            }
        }

    }

    static boolean contains(@NotNull String content, String lookingFor) {
        return content.contains(lookingFor);
    }

}