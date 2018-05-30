package com.pipitliandani.android.facebox;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    RecyclerView rViewmain;
    DatabaseReference mDatabase;
    Query limit;
    ListAdapter adapter;
    ArrayList<FaceBoxModel> list;
    String currentID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rViewmain = findViewById(R.id.rViewMain);
        getSupportActionBar().setTitle("List of Employee");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("employee");
        limit = mDatabase.limitToFirst(10).orderByKey();
        limit.keepSynced(true);
        mDatabase.keepSynced(true);
        list = new ArrayList<>();

        rViewmain = (RecyclerView) findViewById(R.id.rViewMain);
        rViewmain.setHasFixedSize(true);
        rViewmain.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ListAdapter(this, list, rViewmain);
        adapter.setLoadMore(new onLoadMore() {
            @Override
            public void LoadMore() {
                Query query = mDatabase.orderByKey().startAt(currentID).limitToFirst(10);
                query.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        if ( !dataSnapshot.getKey().equals(currentID)) {
                            FaceBoxModel currentModel = dataSnapshot.getValue(FaceBoxModel.class);
                            currentID = dataSnapshot.getKey();
                            list.add(currentModel);
                            adapter.notifyDataSetChanged();
                            adapter.setLoaded();
                        }
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
            }
        });

        rViewmain.setAdapter(adapter);
        limit.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                FaceBoxModel currentModel = dataSnapshot.getValue(FaceBoxModel.class);
                currentID = dataSnapshot.getKey();
                list.add(currentModel);
                adapter.notifyDataSetChanged();
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
    }
//        FirebaseRecyclerAdapter<FaceBoxModel, FaceViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<FaceBoxModel, FaceViewHolder>
//                (FaceBoxModel.class, R.layout.list_item, FaceViewHolder.class, mDatabase) {
//
//            @Override
//            protected void populateViewHolder(FaceViewHolder viewHolder, FaceBoxModel model, int position) {
//                viewHolder.setName(model.getName());
//                viewHolder.setUnit(model.getUnit());
//                viewHolder.setImage(getApplicationContext(), model.getImage_url());
//                viewHolder.Profile(getApplicationContext());
//
//
//            }
//        };
//        rViewmain.setHasFixedSize(true);
//        rViewmain.setLayoutManager(new LinearLayoutManager(this));
//        rViewmain.setAdapter(firebaseRecyclerAdapter);
//    }


//    @Override
//    protected void onStart() {
//        super.onStart();
//    }
//    public static class FaceViewHolder extends RecyclerView.ViewHolder {
//        View view;
//        public FaceViewHolder(View itemView){
//            super(itemView);
//            view = itemView;
//        }
//        public void setName(String name){
//            TextView nameListItem = (TextView)view.findViewById(R.id.nameListItem);
//            nameListItem.setText(name);
//        }
//        public void setUnit(String unit){
//            TextView unitListItem = (TextView)view.findViewById(R.id.unitListItem);
//            unitListItem.setText(unit);
//        }
//        public void setImage(Context contx, String image){
//            CircleImageView imageView = (CircleImageView)view.findViewById(R.id.photo);
//            Picasso.with(contx).load(image).placeholder(R.color.grey)
//                    .error(R.mipmap.ic_launcher).transform(new CircleTransform()).into(imageView);
//        }
//        public void Profile(final Context cont){
//            LinearLayout layout = (LinearLayout)view.findViewById(R.id.layoutItemView);
//            layout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent i = new Intent(cont, Profile.class);
//                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    cont.startActivity(i);
//                }
//            });
//        }
//

//    }




}
