import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    static boolean optional = true; // это для user-friendly интерфейса, но можно выключить
    static boolean debug = false; // это для дебага, можно включить. Ниже настройки:
    static boolean signDebug = true; // показывает найденную арифметическую операцию (if debug = true)
    static boolean valueDebug = true; // показывает найденные введённые значения (if debug = true)
    static boolean romanDebug = true; // показывает, найдены ли римские символы во введённом выражении (if debug = true)

    public static void main(String[] args) {

        Scanner scannerInput = new Scanner(System.in);

        if (optional) System.out.print("Введите арифметическое выражение: ");
        String output = calc(scannerInput.nextLine().replaceAll(" ", "").toUpperCase());

        if (optional) System.out.print("Результат работы калькулятора: ");
        System.out.println(output);
    }

    public static String calc(String input) {

        Operations expression = new Operations();

        int resultArabic;
        String resultRoman;
        char sign = expression.signCheck(input);
        int[] values = expression.valueCheck(input);
        boolean roman = expression.romanCheck(input);

        switch (sign) { // непосредственный расчёт результата
            case '+' -> resultArabic = (values[0] + values[1]);
            case '-' -> resultArabic = (values[0] - values[1]);
            case '*' -> resultArabic = (values[0] * values[1]);
            case '/' -> resultArabic = (values[0] / values[1]);
            default -> throw new RuntimeException("Арифметическая операция не распознана");
        }
        if (roman) {
            resultRoman = expression.arabicToRoman(resultArabic);
            return resultRoman;
        } else {
            return String.valueOf(resultArabic);
        }
    }

    static boolean contains(@NotNull String content, String lookingFor) { // метод для поиска арифметического знака
        return content.contains(lookingFor);
    }

    enum RomanNumeral { // 'расшифровка' риских чисел в арабские
        I(1), IV(4), V(5), IX(9), X(10),
        XL(40), L(50), XC(90), C(100),
        CD(400), D(500), CM(900), M(1000);

        final int value;

        RomanNumeral(int value) {
            this.value = value;
        }

        static List<RomanNumeral> getReverseSortedValues() {
            return Arrays.stream(values())
                    .sorted(Comparator.comparing((RomanNumeral e) -> e.value).reversed())
                    .collect(Collectors.toList());
        }

        int getValue() {
            return value;
        }
    }

    static class Operations {

        char signCheck(String input) { // проверка арифметической операции (знака)
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
            if (debug && signDebug) {
                System.out.println("Арифметический оператор: " + sign);
                signDebug = false;
            }
            return sign;
        }

        boolean romanCheck(@NotNull String input) { // првоерка на наличие римских цифр (и ограничение ввода от 1 до 10)
            String[] stringValues = input.split("\\" + signCheck(input), 2);
            boolean check1;
            boolean check2;

            switch (stringValues[0]) {
                case "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X" -> check1 = true;
                default -> check1 = false;
            }
            switch (stringValues[1]) {
                case "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X" -> check2 = true;
                default -> check2 = false;
            }

            if (check1 && check2) { // + проверка смешивания римских и арабских цифр
                if (debug && romanDebug) System.out.println("Римские цифры: Да");
                romanDebug = false;
                return true;
            } else if (check1 == check2) {
                if (debug && romanDebug) System.out.println("Римские цифры: Нет");
                romanDebug = false;
                return false;
            } else {
                throw new RuntimeException("Неверный формат написания римских цифр");
            }
        }

        int[] romanToArabic(@NotNull String input) { // преобразование из римских в арабские с помощью Enum (он внизу)
            String[] stringValues = input.split("\\" + signCheck(input), 2);
            int[] intValues = {0, 0};
            int i = 0;

            List<RomanNumeral> romanNumerals = RomanNumeral.getReverseSortedValues();

            if (romanCheck(input)) {
                for (int k = 0; k < stringValues.length; k++) {

                    while ((stringValues[k].length() > 0) && (i < romanNumerals.size())) {
                        RomanNumeral symbol = romanNumerals.get(i);
                        if (stringValues[k].startsWith(symbol.name())) {
                            intValues[k] += symbol.getValue();
                            stringValues[k] = stringValues[k].substring(symbol.name().length());
                        } else {
                            i++;
                        }
                    }
                    i = 0;

                    if (stringValues[k].length() > 0) {
                        throw new RuntimeException("Неверный формат арифметического выражения");
                    }
                }
                return intValues;
            } else {
                try {
                    return new int[]{Integer.parseInt(stringValues[0]), Integer.parseInt(stringValues[1])};
                } catch (NumberFormatException e) {
                    throw new RuntimeException("Неверный формат арифметического выражения");
                }
            }

        }

        String arabicToRoman(int input) { // преобразование из арабских в римские с помощью Enum (он внизу)

            if (input < 1 || input > 3999) {
                throw new RuntimeException("Римские числа не могут принимать значения меньше 1 (I) " +
                                                   "и больше 3999 (MMMCMXCIX)");
            }

            List<RomanNumeral> romanNumerals = RomanNumeral.getReverseSortedValues();
            int i = 0;
            StringBuilder sb = new StringBuilder();

            while (input > 0 && i < romanNumerals.size()) {
                RomanNumeral currentSymbol = romanNumerals.get(i);
                if (currentSymbol.getValue() <= input) {
                    sb.append(currentSymbol.name());
                    input -= currentSymbol.getValue();
                } else {
                    i++;
                }
            }

            return sb.toString();
        }

        int[] valueCheck(String input) { // проверка входных значений
            int[] intValues = romanToArabic(input);

            if ((intValues[0] < 1 || intValues[0] > 10) || (intValues[1] < 1 || intValues[1] > 10)) {
                throw new RuntimeException("Входное значение не может быть меньше 1 или больше 10");
            } else {
                try {
                    if (debug && valueDebug) {
                        System.out.println("Обнаруженные операнды: " + intValues[0] + " и " + intValues[1]);
                        valueDebug = false;
                    }
                    return new int[]{intValues[0], intValues[1]};
                } catch (NumberFormatException e) {
                    throw new RuntimeException("Неверный формат арифметического выражения");
                }
            }

        }

    }

}