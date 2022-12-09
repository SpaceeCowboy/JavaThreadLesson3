import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    public static AtomicInteger threeCharacters = new AtomicInteger(0);
    public static AtomicInteger fourCharacters = new AtomicInteger(0);
    public static AtomicInteger fiveCharacters = new AtomicInteger(0);


    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();

    }

    public static void main(String[] args) throws InterruptedException {

        Random random = new Random();
        String[] texts = new String[100_000];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }

        //палиндром
        Runnable palindrome = () -> {
            String reverse;
            for (int i = 0; i < texts.length; i++) {
                reverse = new StringBuilder(texts[i]).reverse().toString();
                if (texts[i].equals(reverse)) {
                    switch (reverse.length()) {
                        case 3:
                            threeCharacters.incrementAndGet();
                        case 4:
                            fourCharacters.incrementAndGet();
                        case 5:
                            fiveCharacters.incrementAndGet();
                    }
                }
            }
        };

        //одинаковые символы
        Runnable identicalCharacters = () -> {
            for (String name : texts) {
                int count = 0;
                for (int i = 0; i < name.length(); i++) {
                    if (name.charAt(0) == name.charAt(i)) {
                        count = name.length() == 3 ? ++count : count;
                        count = name.length() == 4 ? ++count : count;
                        count = name.length() == 5 ? ++count : count;

                        if (name.length() == count) {
                            count = 0;
                            switch (name.length()) {
                                case 3:
                                    threeCharacters.incrementAndGet();
                                    break;
                                case 4:
                                    fourCharacters.incrementAndGet();
                                    break;
                                case 5:
                                    fiveCharacters.incrementAndGet();
                                    break;
                            }
                        }
                    }
                }
            }
        };

        //по возрастанию
        Runnable ascending = () ->{
            for (String name : texts) {
                for (int i = 0; i < name.length(); i++) {
                    char[] chars = name.toCharArray();
                    Arrays.sort(chars);
                    String sorted = new String(chars);
                    if (name.length() == 3 && sorted.length() == 3) {
                        if (name.equals(sorted)) {
                            threeCharacters.incrementAndGet();
                        }
                    } else if (name.length() == 4 && sorted.length() == 4) {
                        if (name.equals(sorted)) {
                            fourCharacters.incrementAndGet();
                        }
                    } else {
                        if (name.equals(sorted)) {
                            fiveCharacters.incrementAndGet();
                        }
                    }
                }
            }
        };

        Thread threadOne = new Thread(palindrome);
        Thread threadTwo = new Thread(identicalCharacters);
        Thread threadThree = new Thread(ascending);

        List<Thread> threads = Arrays.asList(threadOne,threadTwo,threadThree);
        for(Thread thread : threads){
            thread.start();
            thread.join();
        }

        System.out.println("Красивых слов с длиной 3: " + threeCharacters.get());
        System.out.println("Красивых слов с длиной 4: " + fourCharacters.get());
        System.out.println("Красивых слов с длиной 5: " + fiveCharacters.get());


    }
}
