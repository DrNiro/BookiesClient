package com.dts.bookies.util.memento;

import java.util.ArrayList;
import java.util.List;

public class CareTaker {

    private List<Memento> mementoList = new ArrayList<>();

//    list values are unique. every time existing value added - change its index to be the last index.
    public void add(Memento state) {
        if(this.mementoList.contains(state)) {
            this.mementoList.remove(state);
        }
        mementoList.add(state);
    }

    public Memento getAndRemove(int index) {
        Memento memento = mementoList.get(index);
        mementoList.remove(index);
        return memento;
    }

    public Memento get(int index) {
        return mementoList.get(index);
    }

    public List<Memento> getMementoList() {
        return this.mementoList;
    }

    public Memento getLastMemento() {
        return this.mementoList.get(this.mementoList.size() - 1);
    }

    public void removeLastState() {
        this.mementoList.remove(this.mementoList.size() - 1);
    }

}
