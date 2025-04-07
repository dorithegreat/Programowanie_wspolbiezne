package com.aisd;

public class OneWayListHandler {
    public static void insert(OneWayList list, int value){
        OneWayListElement newElement = new OneWayListElement(value);
        if (list.getStart() == null) {
            newElement.setNext(newElement);
            list.setStart(newElement);
            //System.out.println(newElement.getNext().getValue());
        }
        else{
            OneWayListElement current = list.getStart();
            for (int i = 0; i < list.getLength() - 1; i++) {
                //System.out.println(current.getValue());
                current = current.getNext();
            }
            current.setNext(newElement);
            newElement.setNext(list.getStart());
        }
        list.incrementLength();
    }

    public static OneWayList merge(OneWayList list1, OneWayList list2){
        OneWayListElement end1 = list1.getStart();
        for (int i = 0; i < list1.getLength() - 1; i++) {
            end1 = end1.getNext();
        }
        end1.setNext(list2.getStart());

        OneWayListElement end2 = list2.getStart();
        for (int i = 0; i < list2.getLength() - 1; i++) {
            end2 = end2.getNext();
        }
        end2.setNext(list1.getStart());
        return list1;
    }

    public static int find(int value, OneWayList list){
        OneWayListElement current = list.getStart();
        for (int i = 0; i < list.getLength(); i++) {
            if (current.getValue() == value) {
                //System.out.println("found: " + value);
                return i;
            }
            current = current.getNext();
        }
        return list.getLength();
    }


}
