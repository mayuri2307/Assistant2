package com.emozers.assistant2;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class user_profile extends AppCompatActivity
{
    String username,lat,lon;
    String location_provider = LocationManager.NETWORK_PROVIDER;
    LocationManager mLocationManager;
    LocationListener mLocationListener;
    String email;
    DatabaseReference mDatabaseReference,ref;
    final int requestcode=123;
    public void workwithlocation() {
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location)
            {
                Log.d("xyz1234", "on location changed");
                //Log.d("xyz1234",location.toString());
                lat=Double.toString(location.getLatitude());
                lon=Double.toString(location.getLongitude());
                Double lat1=location.getLatitude();
                Double lon1=location.getLongitude();
                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(user_profile.this, Locale.getDefault());

                try {
                    addresses = geocoder.getFromLocation(lat1, lon1, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    String city = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    String postalCode = addresses.get(0).getPostalCode();
                    String knownName = addresses.get(0).getFeatureName();
                    Log.d("City",city);
                    Log.d("City",address);
                    Log.d("City",state);
                    Log.d("City",country);
                    Log.d("City",postalCode);
                    Log.d("City",knownName);
                    TextView location_set=(TextView)findViewById(R.id.location);
                    location_set.setText(city);
                } catch (IOException e) {
                    e.printStackTrace();
                }


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
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},requestcode);
            return;
        }
        mLocationManager.requestLocationUpdates(location_provider, 5000, 1000, mLocationListener);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile_2);
        Intent i=getIntent();
        username=i.getStringExtra("Username");
        TextView user=(TextView)findViewById(R.id.username);
        final TextView name_set=(TextView)findViewById(R.id.name_tv);
        final TextView email_set=(TextView)findViewById(R.id.email_tv);
        final TextView pno_set=(TextView)findViewById(R.id.mobilenumber_tv);
        final TextView hobby_set=(TextView)findViewById(R.id.hobby_tv);
        final TextView gender_set=(TextView)findViewById(R.id.gender_tv);
        final TextView married_set=(TextView)findViewById(R.id.marriage_tv);
        user.setText(username);
        findViewById(R.id.edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(user_profile.this,get_user_data.class);
                i.putExtra("Username",username);
                i.putExtra("Flag","profile");
                i.putExtra("Email",email);
                startActivity(i);
            }
        });
        ref=FirebaseDatabase.getInstance().getReference();
        //mDatabaseReference=ref.child("Users").child(username).child("information");
        Query lastQuery = ref.child("Users").child(username).child("information").orderByKey().limitToLast(2);
        final TextView age_tv_2=(TextView)findViewById(R.id.age_tv);
        lastQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                workwithlocation();
                Log.d("akkad",dataSnapshot.toString());
                Log.d("checking","I am in on data change");
                HashMap<String,user_information> hn=new HashMap<>();
                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    user_information user=snapshot.getValue(user_information.class);
                    hn.put(snapshot.getKey(),user);
                }
                Log.d("checking","Length="+hn.size());
                for(Map.Entry<String,user_information> it:hn.entrySet())
                {
                    Log.d("checking","Key = " + it.getKey() +
                            ", Value = " + it.getValue().getname()+"Age="+it.getValue().getage()+"Email"+it.getValue().getemail()+"Hobby"+it.getValue().gethobby()+"Pno"+it.getValue().getpno());
                    if(it.getValue().getage()!=null)
                    {
                        age_tv_2.setText(it.getValue().getage());
                        hobby_set.setText(it.getValue().gethobby());
                        email=it.getValue().getemail();
                        email_set.setText((it.getValue().getemail()));
                        name_set.setText(it.getValue().getname());
                        pno_set.setText(it.getValue().getpno());
                        gender_set.setText(it.getValue().getgender());
                        married_set.setText((it.getValue().getmarital()));
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                // ...
            }
        });
        //ref=FirebaseDatabase.getInstance().getReference();
        //mDatabaseReference=ref.child("Users").child(username).child("information");
    }
}
