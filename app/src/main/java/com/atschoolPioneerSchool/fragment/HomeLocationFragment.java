package com.atschoolPioneerSchool.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.atschoolPioneerSchool.ActivityLogin;
import com.atschoolPioneerSchool.ActivityMain;
import com.atschoolPioneerSchool.R;
import com.atschoolPioneerSchool.data.Constant;
import com.atschoolPioneerSchool.data.post_connection_json;
import com.atschoolPioneerSchool.model.Student;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by OmarA on 26/12/2017.
 */

import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
//753645	07536

public class HomeLocationFragment extends Fragment {

    HomeLocationFragment.GetHomeLocationTask myTask = null;

    private String json_code;

    private SharedPreferences sharedpref;
    private Button btn_savelocation;
    private ProgressBar progressBar;

    GoogleMap googleMap;
    ArrayList<Marker> markers = new ArrayList<>();
    Polyline line = null;
    double latitude = 0;
    double longitude = 0;
    String HomeLAT = "";
    String HomeLNG = "";
    LatLngBounds.Builder builder;
    MapFragment map1;
    static View view;
    Activity host;
    public static final String EXTRA_OBJCT = "com.atschoolPioneerSchool.ITEM";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_home_location, container, false);
        }

        host = ((Activity) view.getContext());
        map1 = ((MapFragment) host.getFragmentManager().findFragmentById(R.id.map1));

        sharedpref = getContext().getSharedPreferences("atSchool", Context.MODE_PRIVATE);

        map1.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap map1) {
                googleMap = map1;

                // Setting a click event handler for the map1
                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

                    @Override
                    public void onMapClick(LatLng latLng) {

                        // Creating a marker
                        MarkerOptions markerOptions = new MarkerOptions();

                        // Setting the position for the marker
                        markerOptions.position(latLng);

                        // Setting the title for the marker.
                        // This will be displayed on taping the marker
                        latitude = latLng.latitude;
                        longitude = latLng.longitude;
                        markerOptions.title(latLng.latitude + " : " + latLng.longitude);

                        // Clears the previously touched position
                        googleMap.clear();

                        // Animating to the touched position
                        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                        // Placing a marker on the touched position
                        googleMap.addMarker(markerOptions);
                    }
                });

                progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

                btn_savelocation = (Button) view.findViewById(R.id.btn_savelocation);
                btn_savelocation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (!isNetworkAvailable(getContext())) {
                            Toast.makeText(getContext(), R.string.msgInternetNotAvailable, Toast.LENGTH_SHORT).show();
                        } else {
                            new HomeLocationFragment.SaveLocation().execute("");
                        }
                    }
                });


                //fill home location
                if (!isNetworkAvailable(getActivity().getBaseContext())) {
                    Toast.makeText(getActivity().getBaseContext(), R.string.msgInternetNotAvailable, Toast.LENGTH_SHORT).show();
                } else {
                    myTask = new HomeLocationFragment.GetHomeLocationTask();
                    myTask.execute("");
                }
            }
        });

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (myTask != null) {
            myTask.cancel(true);
        }
        view = null;
        host.getFragmentManager().beginTransaction().remove(map1).commit();
    }

    public void SetHomeLocation(String s) {
        try {

            for (int i = 0; i < markers.size(); i++) {
                markers.get(i).remove();
            }

            markers.clear();

            if (line != null) {
                line.remove();
                line = null;
            }

            PolylineOptions line1 = new PolylineOptions();
            line1.width(3);
            Marker previousMarker = null;

            //marker with an icon
            int height = 100;
            int width = 100;


//            HomeLAT = sharedpref.getString("HomeLAT", "").trim();
//            HomeLNG = sharedpref.getString("HomeLNG", "").trim();

            HomeLAT = sharedpref.getString("HomeLAT", "").trim();
            HomeLNG = sharedpref.getString("HomeLNG", "").trim();

            LatLng ll = new LatLng(Double.valueOf(HomeLAT), Double.valueOf(HomeLNG));

            Marker marker = null;

            if (!HomeLAT.equals("") && !HomeLNG.equals("")) {
                //draw home location

                Bitmap mbitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.homelocation)).getBitmap();
                Bitmap imageRounded = Bitmap.createBitmap(mbitmap.getWidth(), mbitmap.getHeight(), mbitmap.getConfig());
                Canvas canvas = new Canvas(imageRounded);
                Paint mpaint = new Paint();
                mpaint.setAntiAlias(true);
                mpaint.setShader(new BitmapShader(mbitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
                canvas.drawRoundRect((new RectF(0, 0, mbitmap.getWidth(), mbitmap.getHeight())), 100, 100, mpaint);// Round Image Corner 100 100 100 100

                Bitmap b = imageRounded;
                Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);


                marker = googleMap.addMarker(new MarkerOptions()
                        .position(ll)
                        .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                        .title(getString(R.string.strHome)));
                marker.showInfoWindow();

            }
            previousMarker = marker;
            markers.add(marker);

            // line1.add(marker.getPosition());
            builder.include(ll);


            line = googleMap.addPolyline(line1);

            if (markers.size() == 0) {
                Toast.makeText(getContext(), "No Points Found", Toast.LENGTH_LONG).show();
            }


            LatLngBounds bounds = builder.build();

            googleMap.getUiSettings().setMapToolbarEnabled(true);

            googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 10));

            //   googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.valueOf( HomeLAT), Double.valueOf(  HomeLNG)), 17.0f));

        } catch (Exception x) {
            String c = x.getMessage();
        }
    }

    private class GetHomeLocationTask extends AsyncTask<String, String, String> {

        public GetHomeLocationTask() {

        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            builder = new LatLngBounds.Builder();
        }

        @Override
        protected String doInBackground(String... strings) {

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                SetHomeLocation("");

            } catch (Exception x) {
                String c = x.getMessage();
            }
            super.onPostExecute(s);
        }

    }

    private class SaveLocation extends AsyncTask<String, String, String> {

        String latHome = String.valueOf(latitude).replaceAll("٠", "0").replaceAll("١", "1").replaceAll("٢", "2").replaceAll("٣", "3").replaceAll("٤", "4")
                .replaceAll("٥", "5").replaceAll("٦", "6").replaceAll("٧", "7").replaceAll("٨", "8").replaceAll("٩", "9").trim();

        String lngHome = String.valueOf(longitude).trim().replaceAll("٠", "0").replaceAll("١", "1").replaceAll("٢", "2").replaceAll("٣", "3").replaceAll("٤", "4")
                .replaceAll("٥", "5").replaceAll("٦", "6").replaceAll("٧", "7").replaceAll("٨", "8").replaceAll("٩", "9").trim();

        private int cntall;

        //new Code
        final SharedPreferences.Editor edt = sharedpref.edit();

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            btn_savelocation.setVisibility(View.GONE);
            super.onPreExecute();
            json_code = "";

        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                Thread.sleep(1000);
                String loginUserMasterId = sharedpref.getString("USER_MASTER_Id", "").trim();

                try {

                    String tag[] = {"events", "USER_MASTER_Id", "lat", "lng"};
                    String value[] = {"44", loginUserMasterId, latHome, lngHome};


                    //http://schoolrootweb.controporal.com/API_Mobile.aspx?events=1&username=omar1&pass=omar1
                    String url = getResources().getString(R.string.Web_URL);
                    json_code = new post_connection_json().makePostRequest(url, tag, value);

                } catch (Exception e) {
                    e.printStackTrace();

                    return e.getMessage();
                }


                if (!json_code.equals("")) {

                    edt.putString("HomeLAT", latHome);
                    edt.putString("HomeLNG", lngHome);
                    edt.commit();

                    try {
                        SetHomeLocation("");

                    } catch (Exception x) {
                        String c = x.getMessage();
                    }

                } else {
                    return "not valid";
                }


            } catch (
                    InterruptedException e)

            {
                e.printStackTrace();
                return e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            progressBar.setVisibility(View.GONE);
            btn_savelocation.setVisibility(View.VISIBLE);

            //finish();
            super.onPostExecute(s);
        }

    }

    public boolean isNetworkAvailable(Context ctx) {
        if (ctx == null)
            return false;

        ConnectivityManager cm =
                (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }


}
