package com.cerenerdem.mymapsproject;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;//Harita Objemiz
    LocationManager locationManager;//Bu sınıf ile kullanıcının yerini bulacağız.
    //CTRL + J tuşlarına basarsak kullanımıyla ilgili dokümantasyona ulaşırız.

    LocationListener locationListener;
    String Adres = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    //Harita açıldığında yapılacak işlemler
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap; //mMap objesi GOOGLEMAP tir denilerek atama yapılmış.

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) { //Kullanıcının yeri değiştiğinde ne yapacağız?
                //System.out.println("Lokasyon:" + location.toString()); //Konum değiştikçe bana lat - long bilgisini yazdır.


                //Lokasyon değiştikçe Kullanıcı lokasyonu olarak alır
                /*mMap.clear(); //Haritayı temizle
                LatLng KullaniciLokasyonu = new LatLng(location.getLatitude(),location.getLongitude());
                mMap.addMarker(new MarkerOptions().position(KullaniciLokasyonu).title("Kullanıcı Buradadır"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(KullaniciLokasyonu,15));*/



               /* //Lokasyonun adresini al
                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());//Kullanıcı hangi dilde cihaz kullanıyorsa ona uygun şekilde adresleri gösterir.
                //Geocoder bir sokak adresini enlem boylama dönüştürür.
                //Regeocoding enlem boylamı sokak adresine dönüştürür.

                try {

                    List<Address> AdresListesi = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                    if (AdresListesi != null && AdresListesi.size() > 0) {

                        geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        System.out.println("Adres " + AdresListesi);

                    }

                } catch (Exception e) {

                    e.toString();

                }*/


            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) { // Access fine location iznimin var mı? Kontrol et...

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            //Activity = MapsActivity
            //requestCode 1 -> izin verilirse dönecek odan kod

            locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0, 0, locationListener); //Her saniye yeni konum ver.
        }


        //Harita açık olduğunda görüntülenecek lokasyon ve market kodlaması
        // Add a marker in MyHouse and move the camera
        LatLng Location_MyHouse = new LatLng(40.727083, 29.798093); //Enlem (Latitude) ve Boylam (Longitude) bilgisi girilmiş.
        mMap.addMarker(new MarkerOptions().position(Location_MyHouse).title("My House"));//Enlem ve Boylam lokasyonunu üzerinde görünecek olan başlık.
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(Location_MyHouse)); //Haritayı bu belirlediğim lokasyona yönlendir.
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Location_MyHouse, 15)); //Haritayı bu belirlediğim lokasyona ZOOMLU bir şekilde yönlendir.


        if (Build.VERSION.SDK_INT >= 23) {

            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            } else {

                //Kullanıcının ilk açtığında son lokasyonunu almak...
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                Location SonrakiLokasyon = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
                System.out.println("Sonraki Lokasyon:" + SonrakiLokasyon);
                LatLng KullaniciLastLokasyon = new LatLng(SonrakiLokasyon.getLatitude(), SonrakiLokasyon.getLongitude());
                mMap.addMarker(new MarkerOptions().title("Kullanıcı Lokasyonu").position(KullaniciLastLokasyon));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(KullaniciLastLokasyon, 15));

            }

        } else {

            //Kullanıcının ilk açtığında son lokasyonunu almak...
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            Location SonrakiLokasyon = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
            System.out.println("Sonraki Lokasyon" + SonrakiLokasyon);
            LatLng KullaniciLastLokasyon = new LatLng(SonrakiLokasyon.getLatitude(), SonrakiLokasyon.getLongitude());
            mMap.addMarker(new MarkerOptions().title("Kullanıcı Lokasyonu").position(KullaniciLastLokasyon));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(KullaniciLastLokasyon, 15));

        }
    }


    //Kullanıcı izinleri verdiğinde yazılacak kodlama...
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (grantResults.length > 0) {

            if (requestCode == 1) {

                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                }
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    //OnMapLongClickListener implement in methodudur.
    @Override
    public void onMapLongClick(LatLng latLng) {

        //Tıklanılan Lokasyonun adresini al
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());//Kullanıcı hangi dilde cihaz kullanıyorsa ona uygun şekilde adresleri gösterir.
        //Geocoder bir sokak adresini enlem boylama dönüştürür.
        //Regeocoding enlem boylamı sokak adresine dönüştürür.


        try {
            List<Address> AdresListesi = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);

            if (AdresListesi != null && AdresListesi.size() > 0) {

                if (AdresListesi.get(0).getThoroughfare() != null) {
                    Adres += AdresListesi.get(0).getThoroughfare(); //Cadde Adını Al

                    if (AdresListesi.get(0).getThoroughfare() != null) {

                        Adres += AdresListesi.get(0).getSubThoroughfare(); //Cadde adı ve Numara

                    }
                }


            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        if (Adres.matches("")) {//bu string boş kaldıysa

            Adres = "Adres Bilgisi Yoktur";
        }

        mMap.addMarker(new MarkerOptions().position(latLng).title(Adres));//Adresi marker 'ın üzerine yazdır.
    }
}
