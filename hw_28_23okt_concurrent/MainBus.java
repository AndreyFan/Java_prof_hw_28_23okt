package de.telran.hw_28_23okt_concurrent;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

public class MainBus {
    // 2*. Вы едете на экскурсии. Каждый человек, при входе в автобус,
    // называет свою фамилию.
    //Экскурсовод ставит у себя в блокноте птичку и если количество людей
    // по списку совпадает
    //автобус уезжает на экскурсию. Сымитируйте данный процесс работы.
    //Какой синхронизатор с библиотеки concurrent Вы бы использовали для
    // данного процесса?

    public static void main(String[] args) throws InterruptedException {

        // список людей, кто желает ехать на экскурсию
        String[] persons = new String[]{"Peter", "Hella", "Max", "Anna", "Leo", "Martin", "Olga"};

        CountDownLatch countDownLatch = new CountDownLatch(persons.length); // ограничим счетчик кол-вом людей в списке

        new Thread(new Bus(" Gabriela", persons, countDownLatch)).start(); // имитация пассажира которого нет в списке
        for (int i = 0; i < persons.length; i++) {
            new Thread(new Bus(persons[i], persons, countDownLatch)).start();
            Thread.sleep(1000);
        }
        System.out.println();
        System.out.println("*** Все на месте и наполненный автобус отъезжает *** ");
        System.out.println();
        System.out.println(" Главный поток завершился ");

    }
}

class Bus implements Runnable {
    private String personName;
    private CountDownLatch countDownLatch;
    private String[] persons;

    public Bus(String personName, String[] persons, CountDownLatch countDownLatch) {
        this.personName = personName;
        this.countDownLatch = countDownLatch;
        this.persons = persons;
    }

    @Override
    public void run() {
        Set<String> set = new HashSet<>(Arrays.asList(persons)); // заносим весь список в хэш -сет для удобства проверки
        try {
            System.out.println("-> " + personName + " заходит в автобус и называет имя ");

            if (set.contains(personName)) { // проверка , есть ли пассажир  в списке
                Thread.sleep(300);
                System.out.println("[!] " + personName + " есть в списке и водитель ставит птичку ");
                System.out.println();
                countDownLatch.countDown(); //-1

            } else {
                System.out.println(personName + " такого пассажира нет в списке ");
                System.out.println(personName + " покидает автобус ");
            }
            countDownLatch.await(); // все ждут остальных по списку

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
