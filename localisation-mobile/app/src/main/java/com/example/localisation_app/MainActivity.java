package com.example.localisation_app;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String SERVER_IP = "192.168.163.237";
    private RequestQueue requestQueue;
    private TextInputEditText latitudeInput;
    private TextInputEditText longitudeInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        latitudeInput = findViewById(R.id.latitudeInput);
        longitudeInput = findViewById(R.id.longitudeInput);
        Button addPositionButton = findViewById(R.id.addPositionButton);
        Button mapButton = findViewById(R.id.button);

        // Initialize request queue
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        // Set click listener for map button
        mapButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);
        });

        // Set click listener for add position button
        addPositionButton.setOnClickListener(v -> {
            String latitudeStr = latitudeInput.getText().toString().trim();
            String longitudeStr = longitudeInput.getText().toString().trim();

            if (latitudeStr.isEmpty() || longitudeStr.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                double latitude = Double.parseDouble(latitudeStr);
                double longitude = Double.parseDouble(longitudeStr);

                // Validate latitude and longitude ranges
                if (latitude < -90 || latitude > 90) {
                    Toast.makeText(this, "La latitude doit être entre -90 et 90", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (longitude < -180 || longitude > 180) {
                    Toast.makeText(this, "La longitude doit être entre -180 et 180", Toast.LENGTH_SHORT).show();
                    return;
                }

                addPosition(latitude, longitude);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Veuillez entrer des valeurs numériques valides", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addPosition(final double lat, final double lon) {
        String insertUrl = "http://" + SERVER_IP + "/localisation/createPosition.php";

        StringRequest request = new StringRequest(Request.Method.POST, insertUrl, response -> {
            try {
                JSONObject jsonResponse = new JSONObject(response);
                String status = jsonResponse.getString("status");
                String message = jsonResponse.getString("message");

                if ("success".equals(status)) {
                    Toast.makeText(MainActivity.this, "Position ajoutée avec succès", Toast.LENGTH_SHORT).show();
                    // Clear input fields after successful submission
                    latitudeInput.setText("");
                    longitudeInput.setText("");
                } else {
                    Toast.makeText(MainActivity.this, "Erreur : " + message, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                Log.e("MAIN_DEBUG", "JSONException: ", e);
                Toast.makeText(MainActivity.this, "Erreur lors de la récupération de la réponse", Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            Toast.makeText(MainActivity.this, "Erreur de connexion : " + error.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("MAIN_DEBUG", "VolleyError: ", error);
        }) {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> params = new HashMap<>();
                DateTimeFormatter formater = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    formater = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                }
                params.put("latitude", String.valueOf(lat));
                params.put("longitude", String.valueOf(lon));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    params.put("date", LocalDateTime.now().format(formater));
                }
                params.put("imei", android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID));
                return params;
            }
        };

        requestQueue.add(request);
    }
}
