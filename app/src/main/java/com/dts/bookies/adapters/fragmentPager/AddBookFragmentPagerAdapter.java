package com.dts.bookies.adapters.fragmentPager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class AddBookFragmentPagerAdapter extends FragmentPagerAdapter {

    // TODO: future implementation: Add book activity with TabLayout with ViewPager, fragment_add_with_api & fragment_add_manually + FragmentPagerAdapter. (not in this course scope).

    public AddBookFragmentPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if(position == 0) {
            return null; // fragment_add_with_api
        } else if(position == 1) {
            return null; // fragment_add_manually
        } else {
            return null;
        }
    }

    @Override
    public int getCount() {
        return 0;
    }


}
