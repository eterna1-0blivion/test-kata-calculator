import org.jetbrains.annotations.NotNull;

import java.util.Scanner;

public class Main {
    static boolean optional = true; // это для user-friendly интерфейса, но можно выключить
    static boolean debug = false; // это для дебага (спамит в консоль, лучше не трогать)

    public static void main(String[] args) {

        Scanner scannerInput = new Scanner(System.in);

        if(optional) System.out.print("Введите арифметическое выражение: ");
        String output = calc(scannerInput.nextLine().replaceAll(" ", ""));

        if(optional) System.out.print("Результат работы калькулятора: ");
        System.out.println(output);
    }

    public static @NotNull String calc(String input){

        Operations expression = new Operations();

        int resultArabic;
        String resultRoman;

        switch (expression.signCheck(input)) { // непосредственный расчёт результата
            case '+' -> resultArabic = (expression.valueCheck(input)[0] + expression.valueCheck(input)[1]);
            case '-' -> resultArabic = (expression.valueCheck(input)[0] - expression.valueCheck(input)[1]);
            case '*' -> resultArabic = (expression.valueCheck(input)[0] * expression.valueCheck(input)[1]);
            case '/' -> resultArabic = (expression.valueCheck(input)[0] / expression.valueCheck(input)[1]);
            default -> throw new RuntimeException("Арифметическая операция не распознана");
        }
        if (expression.romanCheck(input)){
            resultRoman = expression.reverseConversion(resultArabic);
            return resultRoman;
        } else {
            return String.valueOf(resultArabic);
        }
    }

    static class Operations {

        char signCheck(@NotNull String input){ // проверка арифметической операции (знака)
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
            if(debug) {System.out.println("Арифметический оператор: " + sign);}
            return sign;
        }

        boolean romanCheck(@NotNull String input) { // првоерка на наличие римских цифр
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

            if (check1 && check2) { // + проверка смешивания римских и арабских цифр
                if(debug) System.out.println("Римские цифры: Да");
                return true;
            } else if (check1 == check2){
                if(debug) System.out.println("Римские цифры: Нет");
                return false;
            } else {
                throw new RuntimeException("Неверный формат написания римских цифр");
            }
        }

        int[] conversion(@NotNull String input) { // преобразование из римских в арабские с помощью Enum (он внизу)
            String[] stringValues = input.split("\\" + signCheck(input), 2);
            try {
                if (romanCheck(input)) {
                    return new int[]{RomanNumerals.valueOf(stringValues[0]).getArabic(), RomanNumerals.valueOf(stringValues[1]).getArabic()};
                } else {
                    return new int[]{Integer.parseInt(stringValues[0]), Integer.parseInt(stringValues[1])};
                }
            } catch (NumberFormatException e) {
                throw new RuntimeException("Неверный формат арифметического выражения");
            }

        }

        String reverseConversion(int input) { // преобразование из арабских в римские (Enum не принимает в себя int, к сожалению)
            String result;
            switch (input) {
                case 1 -> result = "I";
                case 2 -> result = "II";
                case 3 -> result = "III";
                case 4 -> result = "IV";
                case 5 -> result = "V";
                case 6 -> result = "VI";
                case 7 -> result = "VII";
                case 8 -> result = "VIII";
                case 9 -> result = "IX";
                case 10 -> result = "X";
                default -> throw new RuntimeException("Выходное значение римских цифр не может быть больше 10 или меньше 1");
            }
            return result;
        }

        int[] valueCheck(@NotNull String input) { // проверка входных значений
            int[] intValues = conversion(input);

            if (intValues[0] > 10 || intValues[0] < 1) {
                throw new RuntimeException("Входное значение не может быть больше 10 или меньше 1");
            } else if (intValues[1] > 10 || intValues[1] < 1) {
                throw new RuntimeException("Входное значение не может быть больше 10 или меньше 1");
            } else {
                try {
                    if(debug) { System.out.println("Обнаруженные операнды: " + intValues[0] + " и " + intValues[1]);}
                    return new int[]{intValues[0],intValues[1]};
                } catch (NumberFormatException e) {
                    throw new RuntimeException("Неверный формат арифметического выражения");
                }
            }
        }

    }

    static boolean contains(@NotNull String content, String lookingFor) { // метод для поиска арифметического знака (и не только)
        return content.contains(lookingFor);
    }

    enum RomanNumerals { // 'расшифровка' риских чисел в арабские
        I(1), II(2), III(3), IV(4), V(5), VI(6), VII(7), VIII(8), IX(9), X(10);

        final int arabic;
        RomanNumerals(int arabic) {
            this.arabic = arabic;
        }
        int getArabic() {
            return arabic;
        }
    }

}