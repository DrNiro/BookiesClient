package com.dts.bookies.util.memento;

import android.util.Log;

import androidx.fragment.app.Fragment;

import com.dts.bookies.activities.fragments.MapFragment;
import com.dts.bookies.activities.fragments.ProfileFragment;
import com.dts.bookies.activities.fragments.SearchFragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentsMementoManager {

    private Fragment currentFragment;
    private Originator originator;
    private CareTaker careTaker;

    private Map<String, Memento> mementoMap;

    public FragmentsMementoManager() {
        this.currentFragment = null;
        this.originator = new Originator();
        this.careTaker = new CareTaker();
        initMap();
    }

    public void initMap() {
        this.mementoMap = new HashMap<>();
        this.mementoMap.put(ProfileFragment.class.getSimpleName(), new Memento(MementoStates.PROFILE_STATE));
        this.mementoMap.put(MapFragment.class.getSimpleName(), new Memento(MementoStates.MAP_STATE));
        this.mementoMap.put(SearchFragment.class.getSimpleName(), new Memento(MementoStates.SEARCH_STATE));
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
        Memento nextFragMemento = mementoMap.get(newFocusName);
        careTaker.getMementoList().remove(nextFragMemento);

//        add off focused fragment to memento list.
        Memento currentFragMemento = mementoMap.get(offFocusName);
        originator.setState(currentFragMemento.getState());   // generate string value by Originator
        careTaker.add(originator.saveStateToMemento());     // export value as Memento with Originator and save in Memento list by CareTaker

        Log.d("mem", "(FragmentMementoManager)added: " + careTaker.getLastMemento().getState());
    }

}
