package com.emozers.assistant2;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class get_user_data extends AppCompatActivity
{
    String location_provider = LocationManager.NETWORK_PROVIDER;
    LocationManager mLocationManager;
    LocationListener mLocationListener;
    String hobby,lat,lon;
    String email,password,username,gender,marital;
    String flag="notprofile";
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseReference,ref,mDatabaseReference2;
    final int requestcode=123;
    @Override
    protected void onResume()
    {
        super.onResume();
        workwithlocation();
    }

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
                geocoder = new Geocoder(get_user_data.this, Locale.getDefault());

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
                    //TextView location_set=(TextView)findViewById(R.id.location);
                    //location_set.setText(city);
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==requestcode)
        {
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                Log.d("xyz1234","Permission Granted");
                workwithlocation();
            }
            else
            {
                Log.d("xyz1234","Permission Denied");
            }
        }
    }

    Spinner mSpinner,mSpinner2,mSpinner3;
    ArrayAdapter<CharSequence> adapter,adapter2,adapter3;
    public void openNextActivity(View v)
    {
        TextView name_et=(TextView)findViewById(R.id.name_et);
        TextView age_et=(TextView)findViewById(R.id.age_et);
        TextView phone_et=(TextView)findViewById(R.id.ph_et);
        //TextView hobby_et=(TextView)findViewById(R.id.hobby_spinner);
        String name=name_et.getText().toString();
        String age=age_et.getText().toString();
        String phone=phone_et.getText().toString();
        user_information user=new user_information(name,age,hobby,phone,email,gender,marital);
        mDatabaseReference.child("Users").child(username).child("information").push().setValue(user);
        location loc=new location(lat,lon);
        mDatabaseReference.child("Users").child(username).child("information").child("Location").push().setValue(loc);
        /*if(!flag.equalsIgnoreCase("profile"))
        {
            user_information user=new user_information(name,age,hobby,phone,email,gender,marital);
            mDatabaseReference.child("Users").child(username).child("information").push().setValue(user);
            location loc=new location(lat,lon);
            mDatabaseReference.child("Users").child(username).child("information").child("Location").push().setValue(loc);
        }
        else
        {
            ref=FirebaseDatabase.getInstance().getReference();
            mDatabaseReference2=ref.child("Users").child(username).child("information");
            mDatabaseReference2.addValueEventListener(new ValueEventListener() {
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
                            email=it.getValue().getemail();
                        }
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError)
                {
                    // ...
                }
            });
            user_information user=new user_information(name,age,hobby,phone,email,gender,marital);
            mDatabaseReference.child("Users").child(username).child("information").push().setValue(user);
            location loc=new location(lat,lon);
            mDatabaseReference.child("Users").child(username).child("information").child("Location").push().setValue(loc);
        }*/
        Log.d("flag_check","I am here");
        if(flag.equalsIgnoreCase("profile"))
        {
            //Intent intent=new Intent(get_user_data.this,MessageListActivity.class);
            finish();
            //startActivity(intent);
    }
    else{
            attemptLogin();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_user_data);
        mAuth=FirebaseAuth.getInstance();
        Intent i=getIntent();
        flag=i.getStringExtra("Flag");
        if(flag==null)
            flag="notprofile";
        email= i.getStringExtra("Email");
        password=i.getStringExtra("Password");
        username=i.getStringExtra("Username");
        Log.d("xyz1234","Email ="+email+" "+password);
        mDatabaseReference=FirebaseDatabase.getInstance().getReference();
        mSpinner=(Spinner)findViewById(R.id.hobby_spinner);
        adapter=ArrayAdapter.createFromResource(this,R.array.hobby_list,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                //Toast.makeText(getBaseContext(),parent.getItemAtPosition(position)+"is selected",Toast.LENGTH_SHORT).show();
                hobby=parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
        mSpinner2=(Spinner)findViewById(R.id.gender_et);
        adapter2=ArrayAdapter.createFromResource(this,R.array.gender_list,android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner2.setAdapter(adapter2);
        mSpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                //Toast.makeText(getBaseContext(),parent.getItemAtPosition(position)+"is selected",Toast.LENGTH_SHORT).show();
                gender=parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
        mSpinner3=(Spinner)findViewById(R.id.marital_et);
        adapter3=ArrayAdapter.createFromResource(this,R.array.marital_list,android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner3.setAdapter(adapter3);
        mSpinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                //Toast.makeText(getBaseContext(),parent.getItemAtPosition(position)+"is selected",Toast.LENGTH_SHORT).show();
                marital=parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

    }
    private void attemptLogin()
    {
        Log.d("xyz1234","Email in login="+email+" "+password);
        if(email.equals("")||password.equals(""))
            return;
        Toast.makeText(this,"Login in progress",Toast.LENGTH_SHORT).show();
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d("xyz1234","signing with email "+task.isSuccessful());
                if(!task.isSuccessful()){
                    showErrorDialog(task.getException().getMessage());
                }
                else{
                    Intent intent = new Intent(get_user_data.this, MainActivity.class);
                    intent.putExtra("Email",email);
                    intent.putExtra("Username",username);
                    finish();
                    startActivity(intent);
                }
            }
        });

        // TODO: Use FirebaseAuth to sign in with email & password

    }
    void showErrorDialog(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Oops")
                .setMessage(message)
                .setPositiveButton("Ok", null)
                .show();
    }
}
