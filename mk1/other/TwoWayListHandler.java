package com.aisd;

public class TwoWayListHandler {
    public static void insert(TwoWayList list, int value){
        TwoWayListElement newElement = new TwoWayListElement(value);
        if (list.getStart() == null) {
            newElement.setNext(newElement);
            newElement.getPrev();
            list.setStart(newElement);
            //System.out.println(newElement.getNext().getValue());
        }
        else{
            TwoWayListElement current = list.getStart();
            for (int i = 0; i < list.getLength() - 1; i++) {
                //System.out.println(current.getValue());
                current = current.getNext();
            }
            current.setNext(newElement);
            newElement.setPrev(current);
            newElement.setNext(list.getStart());
        }
        list.incrementLength();
    }

    public static TwoWayList merge(TwoWayList list1, TwoWayList list2){
        TwoWayListElement end1 = list1.getStart();
        while(end1.getNext() != list1.getStart()){
            end1 = end1.getNext();
        }
        end1.setNext(list2.getStart());
        list2.getStart().setPrev(end1);

        TwoWayListElement end2 = list2.getStart();
        while(end2.getNext() != list2.getStart()){
            end2 = end2.getNext();
        }
        end2.setNext(list1.getStart());
        list1.getStart().setPrev(end2);
        return list1;
    }

    public static int find(int value, TwoWayList list){
        TwoWayListElement current = list.getStart();
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
