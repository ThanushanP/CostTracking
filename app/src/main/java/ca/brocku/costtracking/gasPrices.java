package ca.brocku.costtracking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.text.ParseException;

public class gasPrices extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gas_prices);

        getSupportActionBar().setTitle("Gas Prices");

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
        startActivity(new Intent(this, favourites.class));
    }

    public void toFuel(View view) {
        startActivity(new Intent(this, gasPrices.class));
    }

    public void toSpend(View view) {
        startActivity(new Intent(this, Spenditure.class));
    }
}