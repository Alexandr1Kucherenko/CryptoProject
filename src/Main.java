import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Main {

    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean stopProgram = false;
        System.out.println("Что будем делать?\n");
        System.out.println("1. Шифрование с использованием ключа");
        System.out.println("2. Расшифровка с использованием ключа");
        System.out.println("3. Дешифровка с применением криптоанализа \"brute force\"");
        System.out.println("4. Дешифровка с применением криптоанализа \"статистический анализ\"");
        System.out.println("5. Выйти из программы");

        while (!stopProgram) {
            switch (interfacing()) {
                case 1 -> saveResultToFile(encode(askFile(), askKey()));
                case 2 -> saveResultToFile(decode(askFile(), askKey()));
                case 3 -> saveResultToFile(decodeByBruteForce(askFile()));
                case 4 -> saveResultToFile(decodeByStatistic(askFile()));
                case 5 -> {
                    stopProgram = true;
                    scanner.close();
                }
                case 0 -> {
                    System.out.println("Что-то пошло не так");
                    stopProgram = true;
                    scanner.close();
                }
            }
        }
        System.out.println("\nПрограмма завершилась");
    }

    public static int interfacing() {
        Scanner scanner = new Scanner(System.in);
        int answer_of_user;

        while (true) {
            System.out.println("\nВведите номер команды");
            if (scanner.hasNextInt()) {
                answer_of_user = scanner.nextInt();
                if (answer_of_user < 0 || answer_of_user > 5) {
                    System.out.println("Введена цифра вне диапазона!");
                } else {
                    break;
                }
            } else {
                System.out.println("Необходимо ввести числовое значение!");
                scanner.next();
            }
        }
        return answer_of_user;
    }

    public static int askKey() {
        System.out.println("Введите ключ");
        Scanner scannerKey = new Scanner(System.in);
        int key = 0;
        while (scannerKey.hasNext()) {
            if (scannerKey.hasNextInt()) {
                key = scannerKey.nextInt();
                return key;
                } else {
                System.out.println("Необходимо ввести числовое значение!");
            }
        } return key;
    }

    public static String askFile() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nВведите путь к файлу, с которым будем работать");
        Path inputFileName = Path.of(scanner.nextLine());
        StringBuilder stringBuilder = new StringBuilder();

        try {
            List<String> strings = Files.readAllLines(inputFileName, StandardCharsets.UTF_8);
            for (String string : strings) {
                stringBuilder.append(string);
            }
        } catch (IOException e) {
            System.out.println("Некорректный файл / путь к файлу");
        }

        String newString = stringBuilder.toString();
        stringBuilder.setLength(0);
        return newString;
    }

    public static void saveResultToFile(String result) {
        Scanner scannerOut = new Scanner(System.in);
        System.out.println("Введите имя и путь к файлу, в который нужно сохранить результат");
        String stringPath = scannerOut.nextLine();
        Path outputFileName = Path.of(stringPath);

        try {
            Files.createFile(outputFileName);
            FileWriter fileWriter = new FileWriter(String.valueOf(outputFileName));
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(result);
            bufferedWriter.close();
        } catch (IOException e) {
            System.out.println("Некорректный файл / путь к файлу");
        } catch (UnsupportedOperationException | SecurityException e) {
            System.out.println("Файл уже существует / недоступен");
        }
    }

    public static String encode(String text, int key) {
        StringBuilder encoded = new StringBuilder();

        for (char i : text.toCharArray()) {
            if (Character.isLetter(i)) {
                key = key % 33 + 33;
                if (Character.isUpperCase(i)) {
                    encoded.append((char) ('А' + (i - 'А' + key) % 33));
                } else {
                    encoded.append((char) ('а' + (i - 'а' + key) % 33));
                }
            } else if (i >= 33 && i < 66){
                encoded.append((char) (33 + (i - 33 + key) % 33));
            } else {
                encoded.append(i);
            }
        }
        return encoded.toString();
    }

    public static String decode(String encoded, int key) {
        return encode(encoded, 33 - key);
    }

    public static String decodeByBruteForce(String encoded) {

        int key = 0;
        while (true) {
            String textToCheck = decode(encoded, key);
            boolean isChecked = checking(textToCheck);
            if (isChecked) {
                return textToCheck;
            } else {
                key++;
            }
        }
    }
    public static boolean checking(String textToCheck) {
        char[] alphabet = {'Б', 'б', 'В', 'в', 'Г', 'г', 'Д', 'д', 'Ё', 'ё', 'Ж', 'ж', 'З', 'з', 'Й', 'й', 'К', 'к', 'Л', 'л', 'М', 'м', 'Н', 'н', 'П', 'п', 'Р', 'р', 'С', 'с', 'Т', 'т', 'Ф', 'ф', 'Х', 'х', 'Ц', 'ц', 'Ч', 'ч', 'Ш', 'ш', 'Щ', 'щ', 'Ъ', 'ъ', 'Ы', 'ы', 'Ь', 'ь',};
        int mistakes = 0;
        int countOfWhitespaces = 0;
        boolean isChecked;
        char[] textToChar = textToCheck.toCharArray();
        for (int i = 0; i < textToChar.length - 1; i++) {
            if (Character.isLetter(textToChar[i])) {
                for (char charOne : alphabet) {
                    if (textToChar[i] == charOne) {
                        for (char charTwo : alphabet) {
                            if (textToChar[i + 1] == charTwo) {
                                for (char charThree : alphabet) {
                                    if (textToChar[i + 2] == charThree) {
                                        for (char charFour : alphabet) {
                                            if (textToChar[i + 3] == charFour) {
                                                for (char charFive : alphabet) {
                                                    if (textToChar[i + 4] == charFive) {
                                                        mistakes++;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else if (Character.isWhitespace(textToChar[i])) {
                countOfWhitespaces++;
            }
        }
        isChecked = mistakes < 10 && countOfWhitespaces > 0;
        return isChecked;
    }

    public static String decodeByStatistic(String encoded) {
        char[] charArray = encoded.toCharArray();
        int size = charArray.length;

        HashMap<Character, Integer> encodedMap = getLettersMap(encoded, size);
        HashMap<Character, Integer> statisticMap = getLettersMap(askFile(), size);

        LinkedHashMap<Character, Integer> encodedSortedMap = sortByValue(encodedMap);
        LinkedHashMap<Character, Integer> statisticSortedMap = sortByValue(statisticMap);

        List<Character> list = new LinkedList<>(encodedSortedMap.keySet());
        List<Character> list2 = new LinkedList<>(statisticSortedMap.keySet());

        LinkedHashMap<Character, Character> decodedSortedMap = new LinkedHashMap<>();
        if (list2.size() > list.size()) {
            for (int i = 0; i < list.size(); i++) decodedSortedMap.put(list.get(i), list2.get(i));
        } else {
            for (int i = 0; i < list2.size(); i++) decodedSortedMap.put(list.get(i), list2.get(i));
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (char c : charArray) {
            if (decodedSortedMap.containsKey(c)) {
                stringBuilder.append(decodedSortedMap.get(c));
            }
        }
        return stringBuilder.toString();
    }

    private static HashMap<Character, Integer> getLettersMap(String fileForStatistic, int size) {
        HashMap<Character, Integer> letterMap = new HashMap<>();
        char[] arrayStata = fileForStatistic.toCharArray();
        if (arrayStata.length >= size) {
                arrayStata = fileForStatistic.substring(0, size).toCharArray();
            } else {
                arrayStata = fileForStatistic.toCharArray();
            }

        for (char letter : arrayStata) {
            if (letterMap.containsKey(letter)) {
                int value = letterMap.get(letter);
                letterMap.put(letter, value + 1);
            } else {
                letterMap.put(letter, 1);
            }
        }
        return letterMap;
    }

    public static LinkedHashMap<Character, Integer> sortByValue(HashMap<Character, Integer> map) {

        List<Map.Entry<Character, Integer>> list = new LinkedList<>(map.entrySet());
        list.sort(Map.Entry.comparingByValue());
        LinkedHashMap<Character, Integer> sortedHashMap = new LinkedHashMap<>();
        for (Map.Entry<Character, Integer> entry : list) {
            sortedHashMap.put(entry.getKey(), entry.getValue());
        }

    return sortedHashMap;
} 
}




