package com.aisd;

import java.util.Random;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        Fifo fifo = new Fifo();

        System.out.println("created a new fifo");
        for (int i = 0; i < 50; i++) {
            fifo.putIn(new Integer(i));
            System.out.println("put in: " + i);
        }
        // System.out.println("\ncurrent contents of fifo:");
        // fifo.printOut();

        System.out.println("\n taking all elements out:");
        for (int i = 0; i < 50; i++) {
            Object element = fifo.takeOut();
            System.out.println("took out: " + element.toString());
        }
        
        System.out.println("\n\ncreated a new lifo");
        Lifo lifo = new Lifo();

        for (int i = 0; i < 50; i++) {
            lifo.putIn(new Integer(i));
            System.out.println("put in: " + i);
        }
        Object element;
        System.out.println("\ntaking all elements out");
        do {
            element = lifo.takeOut();
            if (element != null) {
                System.out.println("took out: " + element.toString());   
            }
        } while (element != null);

        System.out.println("created a new one way list with values 10 to 19");
        OneWayList list = new OneWayList();
        for(int i = 10; i <= 19; i++){
            OneWayListHandler.insert(list, i);
        }
        OneWayListElement current = list.getStart();
        System.out.println("here are the first 10 elements on that list");
        for (int i = 0; i < 10; i++) {
            System.out.println(current.getValue());
            current = current.getNext();
        }

        System.out.println("created a second one way list with elements 20 to 29");
        OneWayList list2 = new OneWayList();
        for(int i = 20; i <= 29; i++){
            OneWayListHandler.insert(list2, i);
        }
        list = OneWayListHandler.merge(list, list2);
        current = list.getStart();
        System.out.println("merged the lists, here are first 20 elements of the result");
        for (int i = 0; i < 20; i++) {
            System.out.println(current.getValue());
            current = current.getNext();
        }

        System.out.println("\ncreated a new one way list and filled it with random values");
        OneWayList list3 = new OneWayList();
        Random random = new Random();
        int[] T = new int[10000];
        for (int i = 0; i < 10000; i++) {
            T[i] = random.nextInt(100000);
            OneWayListHandler.insert(list3, T[i]);
            //System.out.println("inserted random value: " + T[i]);
        }

        int sum = 0;
        for (int i = 0; i < T.length; i++) {
            sum += OneWayListHandler.find(T[random.nextInt(T.length)], list3);
        }
        System.out.println("average time to find elements from T: " + sum / T.length);
        sum = 0;
        for (int i = 0; i < 10000; i++) {
            sum += OneWayListHandler.find(random.nextInt(100000), list3);
        }
        System.out.println("average time to find random elements: " + sum / 10000);

        TwoWayList list4 = new TwoWayList();
        for (int i = 0; i < 10000; i++) {
            T[i] = random.nextInt(100000);
            TwoWayListHandler.insert(list4, T[i]);
            //System.out.println("inserted random value: " + T[i]);
        }

        System.out.println("created a new two way list with values 10 to 19");
        TwoWayList list5 = new TwoWayList();
        for(int i = 10; i <= 19; i++){
            TwoWayListHandler.insert(list5, i);
        }
        TwoWayListElement current2 = list5.getStart();
        System.out.println("here are the first 10 elements on that list");
        for (int i = 0; i < 10; i++) {
            System.out.println(current.getValue());
            current = current.getNext();
        }
        System.out.println("created a second two way list with elements 20 to 29");
        TwoWayList list6 = new TwoWayList();
        for(int i = 20; i <= 29; i++){
            TwoWayListHandler.insert(list6, i);
        }
        list5 = TwoWayListHandler.merge(list5, list6);
        current2 = list5.getStart();
        System.out.println("merged the lists, here are first 20 elements of the result");
        for (int i = 0; i < 20; i++) {
            System.out.println(current.getValue());
            current = current.getNext();
        }

        System.out.println("\ncreated a new two way list and filled it with random values");
        sum = 0;
        for (int i = 0; i < T.length; i++) {
            sum += TwoWayListHandler.find(T[random.nextInt(T.length)], list4);
        }
        System.out.println("average time to find elements from T: " + sum / T.length);
        sum = 0;
        for (int i = 0; i < 10000; i++) {
            sum += TwoWayListHandler.find(random.nextInt(100000), list4);
        }
        System.out.println("average time to find random elements: " + sum / 10000);
    }
}
