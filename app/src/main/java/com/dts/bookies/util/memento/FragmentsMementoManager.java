package com.dts.bookies.util.memento;

import android.util.Log;

import androidx.fragment.app.Fragment;

import java.util.List;

public class FragmentsMementoManager {

    private Fragment currentFragment;
    private Originator originator;
    private CareTaker careTaker;

    public FragmentsMementoManager() {
        this.currentFragment = null;
        this.originator = new Originator();
        this. careTaker = new CareTaker();
    }

    public Fragment getCurrentFragment() {
        return currentFragment;
    }

    public void setCurrentFragment(Fragment currentFragment) {
        this.currentFragment = currentFragment;
    }

    public Originator getOriginator() {
        return originator;
    }

    public void setOriginator(Originator originator) {
        this.originator = originator;
    }

    public CareTaker getCareTaker() {
        return careTaker;
    }

    public void setCareTaker(CareTaker careTaker) {
        this.careTaker = careTaker;
    }

    public List<Memento> getMementoList() {
        return this.getCareTaker().getMementoList();
    }

    public String getOriginatorState() {
        return originator.getState();
    }

    public void setOriginatorState(Memento memento) {
        this.originator.getStateFromMemento(memento);
    }

    public Memento getAndRemoveLastMemento() {
        return this.careTaker.getAndRemove(getMementoList().size() - 1);
    }

    public void saveToMemento(Fragment offFocusFragment, Fragment newFocusFragment) {
        String offFocusName = offFocusFragment.getClass().getSimpleName();
        String newFocusName = newFocusFragment.getClass().getSimpleName();

//        if new focused fragment already exist in the list - delete it.
        if(newFocusName.equals("ProfileFragment") && careTaker.getMementoList().contains(new Memento(MementoStates.PROFILE_STATE))) {
            careTaker.getMementoList().remove(new Memento(MementoStates.PROFILE_STATE));
        } else if(newFocusName.equals("MapFragment") && careTaker.getMementoList().contains(new Memento(MementoStates.MAP_STATE))) {
            careTaker.getMementoList().remove(new Memento(MementoStates.MAP_STATE));
        } else if(newFocusName.equals("SearchFragment") && careTaker.getMementoList().contains(new Memento(MementoStates.SEARCH_STATE))) {
            careTaker.getMementoList().remove(new Memento(MementoStates.SEARCH_STATE));
        }

//        add off focused fragment to memento list.
        if(offFocusName.equals("ProfileFragment")) {
            originator.setState(MementoStates.PROFILE_STATE);   // generate string value by Originator
            careTaker.add(originator.saveStateToMemento());     // export value as Memento with Originator and save in Memento list by CareTaker
        } else if(offFocusName.equals("MapFragment")) {
            originator.setState(MementoStates.MAP_STATE);
            careTaker.add(originator.saveStateToMemento());
        } else if(offFocusName.equals("SearchFragment")) {
            originator.setState(MementoStates.SEARCH_STATE);
            careTaker.add(originator.saveStateToMemento());
        }

        Log.d("mem", "(FragmentMementoManager)added: " + careTaker.getLastMemento().getState());
    }

}
