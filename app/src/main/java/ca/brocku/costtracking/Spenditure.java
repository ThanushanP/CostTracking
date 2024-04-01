package ca.brocku.costtracking;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Spenditure extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spenditure);

        getSupportActionBar().setTitle("Spenditures");

    }
}