package ca.brocku.costtracking;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Spenditure extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    private String user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spenditure);

        getSupportActionBar().setTitle("Expenditure");

        Intent intent = getIntent();
        if(intent != null) {
            String user = intent.getStringExtra("accEmail");
            if(user != null) {
                this.user= user;
            }
            else{
                this.user = null;
            }
        }

        query("L", false);

    }

    private void query(String date, boolean useDate) {
        String[] fields=new String[]{"rule","userName","date","amount","LocationName","time"};
        ListView listView = findViewById(R.id.Spendingslist);
        TextView textView = findViewById(R.id.totalcost);
        double total = 0;
        String selection;
        String[] selectionArgs;

        ArrayList<String> entries=new ArrayList<>();

        DataHelper dh=new DataHelper(this);
        SQLiteDatabase datareader=dh.getReadableDatabase();
        if (!useDate){
            selection = "userName = ?";
            selectionArgs = new String[]{user};//Change this for the name or email of the user
        }
        else{
            selection = "userName = ? AND date = ?";
            selectionArgs = new String[]{user, date};//Change this for the name or email of the user
        }
        Cursor cursor=datareader.query(DataHelper.DB_TABLE,fields,selection,selectionArgs,null,null,"date DESC, time DESC");

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            total+= Double.parseDouble(cursor.getString(3));
            entries.add(cursor.getString(4)+"\n$"+cursor.getString(3)+"\n"+cursor.getString(2)+" | "+cursor.getString(5));
            cursor.moveToNext();
        }

        if (!cursor.isClosed()) cursor.close();

        textView.setText("Total: $"+total);
        CustomCursorAdapter adapter = new CustomCursorAdapter(this, entries, this.user);
        listView.setAdapter(adapter);

        registerForContextMenu(listView);
        datareader.close();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.tasks,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return ModeSwitcher.handleMenuClicky(item, this) || super.onOptionsItemSelected(item);
    }


    public void newIntent(View view) {
        Intent intent = new Intent(this, InputSpend.class);
        intent.putExtra("accEmail",user);

        startActivity(intent);
    }

    public void toFav(View view) {
        Intent intent = new Intent(this, Favourites.class);
        intent.putExtra("accEmail",user);

        startActivity(intent);
    }

    public void toFuel(View view) {
        Intent intent = new Intent(this, GasPrices.class);
        intent.putExtra("accEmail",user);

        startActivity(intent);
    }

    public void toSpend(View view) {
        Intent intent = new Intent(this, Spenditure.class);
        intent.putExtra("accEmail",user);

        startActivity(intent);
    }

    public void changeDate(View view) {
        Calendar today=Calendar.getInstance();
        new DatePickerDialog(
                this,
                this,
                today.get(Calendar.YEAR),//year
                today.get(Calendar.MONTH),//month
                today.get(Calendar.DAY_OF_MONTH)//day
        ).show();
    }
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar today=Calendar.getInstance();
        today.set(Calendar.YEAR, year);
        today.set(Calendar.MONTH, monthOfYear);
        today.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        Date selectedDate = today.getTime();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String selectedDateText = dateFormat.format(selectedDate);

        query(selectedDateText,true);
    }
}