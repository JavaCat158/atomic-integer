import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    private static final AtomicInteger beautifulWordsCount3 = new AtomicInteger(0);
    private static final AtomicInteger beautifulWordsCount4 = new AtomicInteger(0);
    private static final AtomicInteger beautifulWordsCount5 = new AtomicInteger(0);

    public static void main(String[] args) {
        Random random = new Random();
        String[] texts = new String[100_000];

        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }

        // Создание потоков для каждого критерия
        Thread palindromeThread = new Thread(() -> countBeautifulWords(texts));
        Thread singleLetterThread = new Thread(() -> countBeautifulWords(texts));
        Thread ascendingOrderThread = new Thread(() -> countBeautifulWords(texts));

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

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    // проверка на наличие является ли слово палиндром
    public static boolean isPalindrome(String text) {
        return new StringBuilder(text).reverse().toString().equals(text);
    }

    // проверка яв-ся ли слово из одной буквы
    public static boolean isSingleLetter(String text) {
        return text.chars().distinct().count() == 1;
    }

    // проверка яв-ся ли словa по возростанию
    public static boolean isAscendingOrder(String text) {
        for (int i = 0; i < text.length() - 1; i++) {
            if (text.charAt(i) > text.charAt(i + 1)) {
                return false;
            }
        }
        return true;
    }

    // подсчет красивых слов в зависимости от длины 3, 4 или 5 явно
    public static void countBeautifulWords(String[] texts) {
        for (String text : texts) {
            int length = text.length();

            if (length == 3 ) {
                if (isAscendingOrder(text) || isPalindrome(text) || isSingleLetter(text)) {
                    beautifulWordsCount3.incrementAndGet();
                }
            } else if (length == 4) {
                if (isAscendingOrder(text) || isPalindrome(text) || isSingleLetter(text)) {
                    beautifulWordsCount4.incrementAndGet();
                }
            } else if (length == 5) {
                if (isAscendingOrder(text) || isPalindrome(text) || isSingleLetter(text)) {
                    beautifulWordsCount5.incrementAndGet();
                }
            }
        }
    }
}

