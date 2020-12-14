package com.dts.bookies.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.dts.bookies.activities.MainPageActivity;
import com.dts.bookies.logic.boundaries.UserBoundary;
import com.google.gson.Gson;

import java.util.regex.Matcher;

public class Functions {

    public static boolean isValidEmail(String email) {
        Matcher matcher = Constants.VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        return matcher.find();
    }

    /**
     * @param loggedIn user logged state indicator. 1 - logged in, 0 - logged out
     */
    public static void saveUserToPrefs(String userJson, MySharedPreferences prefs, int loggedIn) {
        prefs.putString(PrefsKeys.USER_BOUNDARY, userJson);
        prefs.putInt(PrefsKeys.LOGGED_STATE, loggedIn);
    }

    public static UserBoundary getUserBoundaryFromPrefs(MySharedPreferences prefs) {
        String myUserJson = prefs.getString(PrefsKeys.USER_BOUNDARY, null);
        if(myUserJson != null) {
            return new Gson().fromJson(myUserJson, UserBoundary.class);
        } else {
            return null;
        }
    }

}
