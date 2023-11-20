import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    // Счетчики для каждой длины слова
    private static AtomicInteger beautifulWordsCount3 = new AtomicInteger(0);
    private static AtomicInteger beautifulWordsCount4 = new AtomicInteger(0);
    private static AtomicInteger beautifulWordsCount5 = new AtomicInteger(0);

    public static void main(String[] args) {
        Random random = new Random();
        String[] texts = new String[100_000];

        // Генерация 100,000 текстов
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }

        // Создание потоков для каждого критерия
        Thread palindromeThread = new Thread(() -> countBeautifulWords(texts, 3, Main.beautifulWordsCount3, Main::isPalindrome));
        Thread singleLetterThread = new Thread(() -> countBeautifulWords(texts, 4, Main.beautifulWordsCount4, Main::isSingleLetter));
        Thread ascendingOrderThread = new Thread(() -> countBeautifulWords(texts, 5, Main.beautifulWordsCount5, Main::isAscendingOrder));

        // Запуск потоков
        palindromeThread.start();
        singleLetterThread.start();
        ascendingOrderThread.start();

        try {
            // Ожидание завершения всех потоков
            palindromeThread.join();
            singleLetterThread.join();
            ascendingOrderThread.join();

            // Вывод результатов
            System.out.println("Красивых слов с длиной 3: " + beautifulWordsCount3 + " шт");
            System.out.println("Красивых слов с длиной 4: " + beautifulWordsCount4 + " шт");
            System.out.println("Красивых слов с длиной 5: " + beautifulWordsCount5 + " шт");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Генератор текста
    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    // Метод для проверки, является ли слово палиндромом
    public static boolean isPalindrome(String text) {
        int left = 0;
        int right = text.length() - 1;

        while (left < right) {
            if (text.charAt(left) != text.charAt(right)) {
                return false;
            }
            left++;
            right--;
        }

        return true;
    }

    // Метод для проверки, состоит ли слово из одной и той же буквы
    public static boolean isSingleLetter(String text) {
        return text.chars().distinct().count() == 1;
    }

    // Метод для проверки, идут ли буквы в слове по возрастанию
    public static boolean isAscendingOrder(String text) {
        for (int i = 0; i < text.length() - 1; i++) {
            if (text.charAt(i) > text.charAt(i + 1)) {
                return false;
            }
        }
        return true;
    }

    // Метод для подсчета красивых слов определенной длины
    public static void countBeautifulWords(String[] texts, int length, AtomicInteger counter, BeatifulWordChecker checker) {
        for (String text : texts) {
            if (text.length() == length && checker.check(text)) {
                counter.incrementAndGet();
            }
        }
    }
}
