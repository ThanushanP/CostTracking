package ca.brocku.costtracking;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class CustomCursorAdapter extends ArrayAdapter<String> {

    private ArrayList<String> items;

    public CustomCursorAdapter(Context context, ArrayList<String> items) {
        super(context, 0, items);
        this.items = items;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.listview_layout, parent, false);
        }

        TextView textView = listItemView.findViewById(R.id.text_view);
        Button button = listItemView.findViewById(R.id.button);

        String currentItem = items.get(position);

        textView.setText(currentItem);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataHelper dh=new DataHelper(getContext());
                SQLiteDatabase datachanger=dh.getWritableDatabase();

                String text = textView.getText().toString();
                String[] lines = text.split("\n");


                ContentValues newWisdom=new ContentValues();
                newWisdom.put("userName","InsertUserNameHere");//Insert username here
                newWisdom.put("locationName",lines.length > 0 ? lines[0] : "");
                newWisdom.put("amount",lines.length > 1 ? lines[1] : "");
                newWisdom.put("date",lines.length > 2 ? lines[2] : "");

                datachanger.insert(DataHelper.DB_TABLE_FAV,null,newWisdom);

                datachanger.close();
            }
        });

        return listItemView;
    }
}