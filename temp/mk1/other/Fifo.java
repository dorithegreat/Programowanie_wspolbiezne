package com.aisd;

public class Fifo {
    private FifoElement start = null;

    public void putIn(Object element){
        if (start == null) {
            start = new FifoElement(element);
        }
        else{
            start.putIn(element);
        }
    }

    public void printOut(){
        System.out.println("start:");
        start.printOut();
    }

    public Object takeOut() {
        if (start == null) {
            return null;
        }

        Object value = start.getValue();
        start = start.getNext();
        return value;
    }
}
