package ca.brocku.costtracking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.text.ParseException;
import java.util.ArrayList;

public class Favourites extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        getSupportActionBar().setTitle("Favourites");
        query();
    }

    private void query() {
        String[] fields=new String[]{"rule","userName","dateTime","amount","LocationName"};
        ListView listView = findViewById(R.id.Favlist);

        ArrayList<String> entries=new ArrayList<>();

        DataHelper dh=new DataHelper(this);
        SQLiteDatabase datareader=dh.getReadableDatabase();

        String selection = "userName = ?";
        String[] selectionArgs = { "UserName" };//Change this for the name or email of the user

        Cursor cursor=datareader.query(DataHelper.DB_TABLE_FAV,fields,selection,selectionArgs,null,null,null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            entries.add(cursor.getString(0)+". "+cursor.getString(4)+"\n$"+cursor.getString(3)+"\n"+cursor.getString(2));
            cursor.moveToNext();
        }
        if (!cursor.isClosed()) cursor.close();
        CustomFav adapter = new CustomFav(this, entries);
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
        try {
            return ModeSwitcher.handleMenuClicky(item,this);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
    public void toFav(View view) {
        startActivity(new Intent(this, Favourites.class));
    }

    public void toFuel(View view) {
        startActivity(new Intent(this, GasPrices.class));
    }

    public void toSpend(View view) {
        startActivity(new Intent(this, Spenditure.class));
    }
}