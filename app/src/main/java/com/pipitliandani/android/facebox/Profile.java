package com.pipitliandani.android.facebox;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity {
    TextView name, nik, unit, workUnit,
    functionTitle, email, placeOfBirth, eduLevel, major, phone;
    CircleImageView image;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setTitle("Profile");
        name = (TextView) findViewById(R.id.name);
        nik = (TextView) findViewById(R.id.nik);
        unit = (TextView) findViewById(R.id.unit);
        workUnit = (TextView) findViewById(R.id.workUnit);
        functionTitle = (TextView) findViewById(R.id.functionTitle);
        email = (TextView) findViewById(R.id.email);
        placeOfBirth = (TextView) findViewById(R.id.placeBirth);
        eduLevel = (TextView) findViewById(R.id.eduLevel);
        major = (TextView) findViewById(R.id.major);
        phone = (TextView) findViewById(R.id.phone);
        image = (CircleImageView) findViewById(R.id.profileImage);

        reference = FirebaseDatabase.getInstance().getReference().child("data");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        })

    }
}
