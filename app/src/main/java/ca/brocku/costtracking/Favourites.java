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
import android.widget.TextView;

import java.text.ParseException;
import java.util.ArrayList;

public class Favourites extends AppCompatActivity {
    private String user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        getSupportActionBar().setTitle("Favourites");

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

        query();
    }

    private void query() {
        String[] fields=new String[]{"rule","userName","dateTime","amount","LocationName"};
        ListView listView = findViewById(R.id.Favlist);
        TextView textView = findViewById(R.id.totalFav);
        double total = 0;

        ArrayList<String> entries=new ArrayList<>();

        DataHelper dh=new DataHelper(this);
        SQLiteDatabase datareader=dh.getReadableDatabase();

        String selection = "userName = ?";
        String[] selectionArgs = { user };//Change this for the name or email of the user

        Cursor cursor=datareader.query(DataHelper.DB_TABLE_FAV,fields,selection,selectionArgs,null,null,null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            total+= Double.parseDouble(cursor.getString(3));
            entries.add(cursor.getString(0)+". "+cursor.getString(4)+"\n$"+cursor.getString(3)+"\n"+cursor.getString(2));
            cursor.moveToNext();
        }
        if (!cursor.isClosed()) cursor.close();
        textView.setText("Total: $"+total);
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
        return ModeSwitcher.handleMenuClicky(item,this);
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
}