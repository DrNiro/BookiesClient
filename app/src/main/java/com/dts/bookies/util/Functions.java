package com.dts.bookies.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.dts.bookies.activities.MainPageActivity;
import com.dts.bookies.logic.boundaries.UserBoundary;
import com.dts.bookies.logic.boundaries.subboundaries.LocationBoundary;
import com.dts.bookies.util.memento.Memento;
import com.google.gson.Gson;

import java.util.List;
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

    public static Object getResponseToClassWithGson(Object response, Class<?> type) {
        return new Gson().fromJson(new Gson().toJsonTree(response).getAsJsonObject(), type);

    }


    public static void printMementoList(List<Memento> memList) {
        if(memList.size() == 0) {
            Log.d("mem","empty list");
            return;
        }
        for(int i = 0; i < memList.size(); i++) {
            Log.d("mem", "mem #" + (i+1) + ": " + memList.get(i));
        }
    }

}
