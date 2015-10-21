package com.example.isa.diebstahlschutz;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Startseite extends Activity implements SeekBar.OnSeekBarChangeListener {


    private  Location locationA = new Location("point A");
    private  Location locationB = new Location("point B");
    private TextView textHome, textProgress, textPosition;
    private TextView entfernung;
    private TextView position;
    private TextView hinweis;
    private  SeekBar bar;
    private MediaPlayer mp = null;
    private String alarm = "Alarm!";
    private String test = "Test!";
    private LocationManager locationManager;
    private LocationListener listener;
  //  private float distance;


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bar = (SeekBar) findViewById(R.id.seekBar);
        bar.setOnSeekBarChangeListener(this);

        textProgress = (TextView) findViewById(R.id.textProgress);
        textHome = (TextView) findViewById(R.id.textHome);
        textPosition = (TextView) findViewById(R.id.textPosition);
        hinweis = (TextView) findViewById(R.id.textHinweis);
        position = (TextView) findViewById(R.id.textPosition);
        entfernung = (TextView) findViewById(R.id.textEntfernung);

       final Button buttonMusik = (Button) findViewById(R.id.buttonMusik);
        buttonMusik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                managerOfSound(test);
            } // END onClick()
        }); // END buttonHello*/

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);


      //  Location l = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
      //  System.out.println("Aktuelle Location ist: " +l);
        listener = new LocationListener() {


            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }

            //Interruped wird genutzt hier ! Nichr das Polling!!
            public void onLocationChanged(Location location) {
                System.out.println("location changed to " + location);

                locationA.setLatitude(location.getLatitude());
                locationA.setLongitude(location.getLongitude());

                position.setText("Aktuelle Position: " + "long: " + locationA.getLongitude() + " Lat: " + locationA.getLatitude());
                entfernung.setText("Die Entfernung betraegt:" + locationA.distanceTo(locationB));
                System.out.println("Entfernung: " + locationA.distanceTo(locationB));


                if (locationA.distanceTo(locationB)>0){
                    hinweis.setText("Du hast dich bewegt!");
                    managerOfSound(alarm);
                }
            }


        };
        //Man kann  dennoch jederzeit die Position abfragen dazu diesen Listener
        //GPS nicht allzu genau
        try {
            locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0, 0, listener);
            System.out.println(locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER));
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }

    }

    public void setHome(View view){
        try {
            locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0, 0, listener);
            locationB = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        textHome.setText("Dein aktuelles zu Hause: " + "long: " + locationB.getLongitude() + " Lat: " + locationB.getLatitude());
    }


    protected void managerOfSound(String theText) {
        if (mp != null) {
            mp.reset();
            mp.release();
        }
        if (theText == alarm)
            mp = MediaPlayer.create(this, R.raw.alarm);
        else if (theText == test)
            mp = MediaPlayer.create(this, R.raw.test);
        mp.start();
    }





    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        textProgress.setText("Die Eingabe lautet: " +progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}

