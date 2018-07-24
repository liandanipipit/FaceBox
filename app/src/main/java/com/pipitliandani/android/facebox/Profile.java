package com.pipitliandani.android.facebox;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
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
    Context context;
    Button rem, edit;
    ProgressDialog pbDialog;
    String key;
    final FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.context = context;
        pbDialog = new ProgressDialog(this);
        Intent i = getIntent();
        key = i.getStringExtra("key");
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
        rem = findViewById(R.id.remove);
        edit = findViewById(R.id.editData);
        if (auth.getCurrentUser() == null){
            rem.setVisibility(View.GONE);
            edit.setVisibility(View.GONE);
        } else {
            rem.setVisibility(View.VISIBLE);
            edit.setVisibility(View.VISIBLE);
        }


        databaseReference = FirebaseDatabase.getInstance().getReference().child("employee");
        final Query query = databaseReference.child(key);
        final ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Iterable<DataSnapshot> ds = dataSnapshot.getChildren();
                //for(DataSnapshot data : ds) {
                final FaceBoxModel detail = dataSnapshot.getValue(FaceBoxModel.class);
//                    String msg = dataSnapshot.getValue(String.class);
//                    Log.d("PROFILE_CLASS", msg);

                String nameKey = detail.getName();
                String nikKey = detail.getNik();
                String unitKey = detail.getUnit();
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
                if (!detail.image_url.equals(""))
                    Picasso.with(context).load(imageUrl).placeholder(R.color.grey).into(image);
                else
                    Picasso.with(context).load(R.drawable.default_image).placeholder(R.color.grey).into(image);



                //}
//                for (DataSnapshot postSnapshoot : dataSnapshot.getChildren()){
//                    String msg = postSnapshoot.getValue(String.class);
//                    Log.d("PROFILE_CLASS", msg);
//
//
//                }

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());

            }
        };


        query.addValueEventListener(valueEventListener);
        //query.removeEventListener(valueEventListener);
        rem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                query.removeEventListener(valueEventListener);
                pbDialog.setMessage("Deleting...");
                pbDialog.setIndeterminate(true);
                pbDialog.show();
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        dataSnapshot.getRef().removeValue(new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                pbDialog.dismiss();
                                Toast.makeText(Profile.this, "Deleted", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, InputData.class);
                intent.putExtra("key", key);
                startActivity(intent);
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
