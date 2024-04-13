package ca.brocku.costtracking;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomFav extends ArrayAdapter<String> {

    private ArrayList<String> items;

    public CustomFav(Context context, ArrayList<String> items) {
        super(context, 0, items);
        this.items = items;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.listview_layoutfav, parent, false);
        }

        TextView textView = listItemView.findViewById(R.id.text_viewFav);
        Button button = listItemView.findViewById(R.id.buttonFav);

        String currentItem = items.get(position);

        textView.setText(currentItem);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataHelper dh=new DataHelper(getContext());
                SQLiteDatabase datareader = dh.getReadableDatabase();

                String text = textView.getText().toString();
                String selection = "rule = ?";
                Log.d("TAG", text);
                Pattern pattern = Pattern.compile("(\\d+)\\.");

                Matcher matcher = pattern.matcher(text);
                if (matcher.find()) {
                    String matchedNumber = matcher.group(1);
                    String[] selectionArgs = { String.valueOf(matchedNumber) };

                    datareader.delete(DataHelper.DB_TABLE_FAV, selection, selectionArgs);
                }


                datareader.close();

                Toast.makeText(getContext(), "Removed from favorites", Toast.LENGTH_SHORT).show();
                getContext().startActivity(new Intent(getContext(),favourites.class));
                ((Activity)getContext()).finish();
            }
        });

        return listItemView;
    }
}
