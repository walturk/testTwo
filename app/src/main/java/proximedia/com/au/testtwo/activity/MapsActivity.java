package proximedia.com.au.testtwo.activity;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import proximedia.com.au.testtwo.util.Constants;
import proximedia.com.au.testtwo.model.Place;
import proximedia.com.au.testtwo.R;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, AdapterView.OnItemSelectedListener {

    private GoogleMap mMap;

    private ArrayList<JSONObject> jsonPlaces = new ArrayList<>();
    private JSONObject currentPlace = new JSONObject();
    private ArrayList<Place> places = new ArrayList<Place>();
    private List<String> placesnames = new ArrayList<>();
    Place selectedPlace;
    static final LatLng HAMBURG = new LatLng(53.558, 9.927);
    static final LatLng KIEL = new LatLng(53.551, 9.993);
    private GoogleMap map;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // populate pinner
        spinner = (Spinner) findViewById(R.id.spinner);

        // set spinner click listener
        spinner.setOnItemSelectedListener(this);





        Button navigate = (Button) this.findViewById(R.id.navigate);
        navigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (selectedPlace != null) {
                    LatLng location = selectedPlace.getLocation();

                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location,15), 2000, null);

                }

                //Log.d(Constants.TAG, view.getTag().toString());
            }
        });

    }

    public class spinnerArrayAdapter<String> extends ArrayAdapter
    {

        public spinnerArrayAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List objects) {
            super(context, resource, textViewResourceId, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            view.setTag(places.get(position));
            return view;
        }
    }

    public String readFully(InputStream stream) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[stream.available()];
        int length = 0;

        try {

            while ((length = stream.read(buffer)) != -1) {
                baos.write(buffer, 0, length);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String sbaos = baos.toString();
        sbaos  = sbaos.replace("\r", "");
        sbaos  = sbaos.replace("\n", "");

        return sbaos;

    }


    private class initSpinner extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(MapsActivity.this, R.string.data_loading, Toast.LENGTH_LONG).show();

        }

        @Override
        protected Void doInBackground(Void... voids) {

            loadJSONData();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            // when data is loaded, set the adapter
            spinnerArrayAdapter<String> adapter = new spinnerArrayAdapter<String>(getApplicationContext(), R.layout.spinner_row, R.id.place, placesnames);
            // Drop down layout style - list view with radio button
            adapter.setDropDownViewResource(R.layout.spinner_row);
            spinner.setAdapter(adapter);

        }
    }

    public void loadJSONData() {

        String json = "";
        Gson gson = new Gson();

        try {
            InputStream inputStream = getAssets().open("sample.json");
            int s = inputStream.available();

            json = readFully(inputStream);
            JSONArray jsonArray = new JSONArray(json);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jobj = jsonArray.getJSONObject(i);
                Place place = gson.fromJson(jobj.toString(), Place.class);
                places.add(place);
                placesnames.add(place.getName());

                //jsonPlaces.add(jobj);
                //Log.d("testtwotag", jsonPlaces.get(i).getString("location"));
            }

            //inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }



    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        // load spinner data
        //
        new initSpinner().execute();
        // --

        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        Log.d(Constants.TAG, "view: "+ view.getTag().toString());
        selectedPlace = (Place) view.getTag();

        AppCompatTextView tvCar = (AppCompatTextView) findViewById(R.id.car);
        AppCompatTextView tvTrain = (AppCompatTextView) findViewById(R.id.train);
        tvCar.setText(getString(R.string.transport_mode, "Car", "-"));
        tvTrain.setText(getString(R.string.transport_mode, "Train", "-"));

        HashMap<String, String> fromcentral = selectedPlace.getFromcentral();
        if (fromcentral.containsKey("car")) {
            tvCar.setText(getString(R.string.transport_mode, "Car", fromcentral.get("car")));
        }
        if (fromcentral.containsKey("train")) {
            tvTrain.setText(getString(R.string.transport_mode, "Train", fromcentral.get("train")));
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
