package com.example.asuss.lokasyon;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    GoogleMap mMap;
    ArrayList<String> locDataX = new ArrayList<>();
    ArrayList<String> locDataY = new ArrayList<>();
    ArrayList<String> title = new ArrayList<>();
    ArrayList<Double> parseLocDataX = new ArrayList<>();
    ArrayList<Double> parseLocDataY = new ArrayList<>();
    ArrayList<Double> uzaklıkList = new ArrayList<>();
    Geometri geo = new Geometri();
    double currentX = 30.211;
    double currentY = 32.444;
    int indis;

    private Button descButton;
    LinearLayout desc_layout;
    ProgressDialog progressDialog;
    static String URL = "http://www.hastanebul.com.tr/eskisehir-nobetci-eczaneler";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        System.out.println("olusturulurken");
        descButton = (Button) findViewById(R.id.buttonDesc);
        descButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tetikle();
            }
        });


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    public void tetikle() {
        try {
            new FetchDescription().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        parseLocDataX = stringToDouble(locDataX, parseLocDataX);
        parseLocDataY = stringToDouble(locDataY, parseLocDataY);
        for (int i = 0; i < locDataX.size(); i++)
            System.out.println("loc " + (i + 1) + " :" + locDataX.get(i));
        for (int i = 0; i < locDataY.size(); i++)
            System.out.println("locy " + (i + 1) + " :" + locDataY.get(i));

        for (int i = 0; i < title.size(); i++)
            System.out.println("title :" + (i + 1) + title.get(i));

        System.out.println(currentX);
        System.out.println(currentY);
        System.out.println(parseLocDataX);
        System.out.println(parseLocDataY);

        uzaklıkList = geo.uzaklıkHesapla(currentX, currentY, parseLocDataX, parseLocDataY);
        for (double d : uzaklıkList)
            System.out.println(d);
        indis = geo.indisDöndür(uzaklıkList);
        System.out.println(indis);
    }

    public ArrayList<Double> stringToDouble(ArrayList<String> stringList, ArrayList<Double> doubleLocation) {


        for (String s : stringList) {
            doubleLocation.add(Double.parseDouble(s));

        }
        return doubleLocation;

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // LatLng sydney = new LatLng(locDataX.get(0), locDataX.get(0));
        LatLng sydney = new LatLng(parseLocDataX.get(indis), parseLocDataY.get(indis));
        mMap.addMarker(new MarkerOptions().position(sydney).title(""));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
    }


    public class FetchDescription extends AsyncTask<Void, Void, Void> {

        String loc;
        Elements elements;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(MapsActivity.this);
            progressDialog.setTitle("Lokasyon");
            progressDialog.setMessage("Lokasyon Bilgileri Çekiliyor...");
            progressDialog.setIndeterminate(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Elements elements = null;
            try {
                Document doc = Jsoup.connect(URL).get();
                elements = doc.select("div.container")
                        .select("div.row")
                        .select("div.col-lg-4")
                        .select("div.panel.panel-default")
                        .select("div.panel-body.pharmacyonduty")
                        .select("a");

                for (Element x : elements)
                    locDataX.add(x.attr("data-locationx"));
                for (Element y : elements)
                    locDataY.add(y.attr("data-locationy"));
                for (Element t : elements)
                    title.add(t.attr("data-title"));

            } catch (Exception e) {

                e.printStackTrace();
            }


            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            desc_layout = (LinearLayout) findViewById(R.id.desc_layout);
            TextView txt_desc = (TextView) findViewById(R.id.txt_desc);
            desc_layout.setVisibility(View.VISIBLE);
            txt_desc.setText("Description: " + "" + loc);


            progressDialog.dismiss();

        }
    }


}
