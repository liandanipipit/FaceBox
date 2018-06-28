package com.pipitliandani.android.facebox;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.pipitliandani.android.facebox.FaceBoxModel;
import com.pipitliandani.android.facebox.fragments.BirthdayFragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by User on 27/06/2018.
 */

public class BirthDayJob extends JobService {
    DatabaseReference mDatabase;
    Query birthday;

    private static final String TAG = "SyncService";
    @Override
    public boolean onStartJob(JobParameters params) {
        mDatabase = FirebaseDatabase.getInstance().getReference().child("employee");
        String date = new SimpleDateFormat("dd/MM", Locale.getDefault()).format(new Date());
        birthday = mDatabase.orderByChild("dateMonthBirth").equalTo(date);

        birthday.keepSynced(true);
        mDatabase.keepSynced(true);

        birthday.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                FaceBoxModel currentModel = dataSnapshot.getValue(FaceBoxModel.class);
                
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
