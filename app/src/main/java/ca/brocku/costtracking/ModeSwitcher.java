package ca.brocku.costtracking;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.MenuItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ModeSwitcher {
    public static boolean handleMenuClicky(MenuItem item, Context from) throws ParseException {
        int selected=item.getItemId();

        if (selected==R.id.signOut) {
            //Insert signout code here, also remeber to delete all previous intents so that that cant press the back button
            //and redirect to mainactivity
        }

        return true;
    }
}
