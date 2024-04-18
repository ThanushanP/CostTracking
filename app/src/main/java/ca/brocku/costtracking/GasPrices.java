package ca.brocku.costtracking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;

public class GasPrices extends AppCompatActivity {

    private TextView resultTextView;
    private Button fetch;
    private EditText LocationInput;

    private String user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gas_prices);

        resultTextView = findViewById(R.id.resultTextView);
        fetch = findViewById(R.id.fetchButton);
        LocationInput = findViewById(R.id.zipCodeEditText);
        getSupportActionBar().setTitle("Gas Prices");

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

        fetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String zipCode = LocationInput.getText().toString();
                // Example usage: Fetch gas prices for ZIP code "12345"
                new FetchGasPricesTask().execute(zipCode);
            }
        });
    }

    private class FetchGasPricesTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String zipCode = params[0];
            return getGasPrices(zipCode);
        }
        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                // Parse JSON response and format data
                try {
                    JSONObject jsonResponse = new JSONObject(result);
                    String lowestPrice = jsonResponse.getString("lowest_price");
                    String averagePrice = jsonResponse.getString("average_price");
                    JSONArray stationsArray = jsonResponse.getJSONArray("stations");

                    StringBuilder formattedResult = new StringBuilder();
                    formattedResult.append("Lowest Price: ").append(lowestPrice).append("\n");
                    formattedResult.append("Average Price: ").append(averagePrice).append("\n\n");
                    for (int i = 0; i < stationsArray.length(); i++) {
                        JSONObject stationObject = stationsArray.getJSONObject(i);
                        String price = stationObject.getString("price");
                        String station = stationObject.getString("station");
                        String location = stationObject.getString("location");
                        formattedResult.append("Station: ").append(station).append("\n");
                        formattedResult.append("Price: ").append(price).append("\n");
                        formattedResult.append("Location: ").append(location).append("\n\n");
                    }
                    resultTextView.setText(formattedResult.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                    resultTextView.setText("Failed to parse gas prices.");
                }
            } else {
                resultTextView.setText("Failed to fetch gas prices.");
            }




        }
    }

    /**
     * Asynchronously fetches gas prices from the API based on the provided ZIP code.
     * @param zipCode The ZIP code for which gas prices are to be fetched.
     * @return A JSON string containing gas prices data, or null if fetching fails.
     */
    private String getGasPrices(String zipCode) {
        // Construct the API URL using the provided ZIP code
        String baseUrl = "https://adrian.tpgc.me:443//gas_prices?zip_code=";
        String apiUrl = baseUrl + zipCode;
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                return response.toString();
            } else {
                // Handle HTTP error response
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.tasks,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return ModeSwitcher.handleMenuClicky(item, this);
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