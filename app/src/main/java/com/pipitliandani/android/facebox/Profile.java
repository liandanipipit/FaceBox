package com.pipitliandani.android.facebox;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.security.Key;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity {
    TextView name, nik, unit, workUnit,
    functionTitle, email, placeOfBirth, eduLevel, major, phone, birthday;
    CircleImageView image;
    DatabaseReference databaseReference;
    private StorageReference mStorage;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.context = context;
        Intent i = getIntent();
        Long key = i.getLongExtra("key", 0);

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
        birthday = (TextView) findViewById(R.id.birthday);
        image = (CircleImageView) findViewById(R.id.profileImage);


        databaseReference = FirebaseDatabase.getInstance().getReference().child("employee");
        Query query = databaseReference.orderByChild("id").equalTo(key);


       query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshoot : dataSnapshot.getChildren()){
                    FaceBoxModel detail = postSnapshoot.getValue(FaceBoxModel.class);

                    String nameKey = detail.getName().toString();
                    String nikKey = detail.getNik().toString();
                    String unitKey = detail.getUnit().toString();
                    String workUnitKey = detail.getWorkUnit();
                    String functionTitleKey = detail.getFunctionTitle();
                    String emailKey = detail.getEmail();
                    String birthdateKey = detail.getBirthDate();
                    String placeKey = detail.getPlaceOfBirth();
                    String eduLevelKey = detail.getEduLevel();
                    String majorKey = detail.getMajor();
                    String phoneKey = detail.getPhone();
                    String imageUrl = detail.getImage_url();

                    name.setText(nameKey);
                    nik.setText(nikKey);
                    unit.setText(unitKey);
                    workUnit.setText(workUnitKey);
                    functionTitle.setText(functionTitleKey);
                    email.setText(emailKey);
                    birthday.setText(birthdateKey);
                    placeOfBirth.setText(placeKey);
                    eduLevel.setText(eduLevelKey);
                    major.setText(majorKey);
                    phone.setText(phoneKey);
                    Picasso.with(context).load(imageUrl).placeholder(R.color.grey).error(R.mipmap.ic_launcher).into(image);


                }

            }



            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());


            }
        });


    }
}
