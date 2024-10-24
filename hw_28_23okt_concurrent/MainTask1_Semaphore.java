package de.telran.hw_28_23okt_concurrent;

import java.util.concurrent.Semaphore;

public class MainTask1_Semaphore {
    //  1 уровень сложности: 1. У вас в магазине распродажа.
    //  К вам набежало 10 000 клиентов и вы пытаетесь корректно всех обслужить
    //  с учетом того,
    //что одновременно у вас внутри магазина может находиться не более 10 покупателей
    // (согласно карантинным нормам)
    //и время обслуживания одного покупателя занимает
    // 30 секунд(откорректируйте под себя). Сымитируйте данный процесс
    //работы и подсчитайте за какой период времени вы сможете обслужить данный объем
    // покупателей?
    //Отдельный покупатель - отдельный поток. Какой синхронизатор с библиотеки
    // concurrent Вы бы использовали?
    public static void main(String[] args) throws InterruptedException {

        Semaphore semaphore = new Semaphore(10, true); // будем добиваться справедливой очереди
        for (int i = 1; i <= 30; i++) { // для наглядности возьмем 30 покупателей (если поставить 10 000 ждать нужно будет долго)
            new Thread(new ConsumersInShop(semaphore), "Покупатель номер - " + i).start();
            Thread.sleep(200);

        }
    }
}

class ConsumersInShop implements Runnable {
    Semaphore semaphore;
    static int i = 0;

    public ConsumersInShop(Semaphore semaphore) {
        this.semaphore = semaphore;
    }

    public void run() {

        try {
            semaphore.acquire();
            System.out.println(Thread.currentThread().getName() + " зашел в магазин ");
            Thread.sleep(5000);  // пауза для наглядности, что в магазине находится одновременно не более  10 человек
            i++;  // внутренний счетчик для подсчета времени , необходимого на обслуживание ( 0,5 мин на каждого)
            System.out.println(Thread.currentThread().getName() + " выходит из магазина . С начала обслуживания прошло " + i * 0.5 + " минут ");
            semaphore.release();
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
        }
    }
}
