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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    RecyclerView rViewmain;
    DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rViewmain = findViewById(R.id.rViewMain);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("employee");
        mDatabase.keepSynced(true);

        rViewmain = (RecyclerView)findViewById(R.id.rViewMain);
        rViewmain.setHasFixedSize(true);
        rViewmain.setLayoutManager(new LinearLayoutManager(this));

    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<FaceBoxModel, FaceViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<FaceBoxModel, FaceViewHolder>
                (FaceBoxModel.class, R.layout.list_item, FaceViewHolder.class, mDatabase) {

            @Override
            protected void populateViewHolder(FaceViewHolder viewHolder, FaceBoxModel model, int position) {
                viewHolder.setName(model.getName());
                viewHolder.setUnit(model.getUnit());
                viewHolder.setImage(getApplicationContext(), model.getImage_url());
                viewHolder.Profile(getApplicationContext());


            }
        };
        rViewmain.setAdapter(firebaseRecyclerAdapter);
    }
    public static class FaceViewHolder extends RecyclerView.ViewHolder {
        View view;
        public FaceViewHolder(View itemView){
            super(itemView);
            view = itemView;
        }
        public void setName(String name){
            TextView nameListItem = (TextView)view.findViewById(R.id.nameListItem);
            nameListItem.setText(name);
        }
        public void setUnit(String unit){
            TextView unitListItem = (TextView)view.findViewById(R.id.unitListItem);
            unitListItem.setText(unit);
        }
        public void setImage(Context contx, String image){
            CircleImageView imageView = (CircleImageView)view.findViewById(R.id.photo);
            Picasso.with(contx).load(image).placeholder(R.color.grey)
                    .error(R.mipmap.ic_launcher).transform(new CircleTransform()).into(imageView);
        }
        public void Profile(final Context cont){
            LinearLayout layout = (LinearLayout)view.findViewById(R.id.layoutItemView);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(cont, Profile.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    cont.startActivity(i);
                }
            });
        }


    }







}
