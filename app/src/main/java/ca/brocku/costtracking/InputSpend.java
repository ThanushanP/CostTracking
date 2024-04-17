package ca.brocku.costtracking;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class InputSpend extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{
    private Date selectedDate;
    private Date selectedTime;
    private String selectedDateText;
    private String selectedTimeText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_spend);

        getSupportActionBar().setTitle("Input Spending");
    }

    public void selectDate(View view) {
        Calendar today=Calendar.getInstance();
        new DatePickerDialog(
                this, //context (this activity)
                this, //what's the listener?
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

        selectedDate = today.getTime();
        new TimePickerDialog(
                this,
                this,
                today.get(Calendar.HOUR_OF_DAY),
                today.get(Calendar.MINUTE),
                true
        ).show();
    }
    public void onTimeSet(TimePicker view, int hour, int minute) {
        Calendar today = Calendar.getInstance();
        today.setTime(selectedDate);

        today.set(Calendar.HOUR_OF_DAY, hour);
        today.set(Calendar.MINUTE, minute);
        selectedTime = today.getTime();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

        selectedDateText = dateFormat.format(selectedDate);
        selectedTimeText = timeFormat.format(selectedTime);
    }
    public void save(View view) {
        EditText nameWidget=(EditText)findViewById(R.id.editName);
        EditText amountWidget=(EditText)findViewById(R.id.editAmount);


        DataHelper dh=new DataHelper(this);
        SQLiteDatabase datachanger=dh.getWritableDatabase();

        Editable text = amountWidget.getText();
        String amount;
        if (text.length() == 0) {
            amount = "0";
        } else {
            amount = amountWidget.getText().toString();
        }

        ContentValues newWisdom=new ContentValues();
        newWisdom.put("userName","UserName");//repalce with username
        newWisdom.put("locationName",nameWidget.getText().toString());
        Log.d("TAG",amount);
        newWisdom.put("amount",amount);
        newWisdom.put("date",selectedDateText);
        newWisdom.put("time",selectedTimeText);


        datachanger.insert(DataHelper.DB_TABLE,null,newWisdom);

        datachanger.close();
        startActivity(new Intent(this,Spenditure.class));
    }
}