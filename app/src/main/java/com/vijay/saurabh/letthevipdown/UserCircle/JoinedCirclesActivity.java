package com.vijay.saurabh.letthevipdown.UserCircle;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.vijay.saurabh.letthevipdown.MyNavigationActivity;
import com.vijay.saurabh.letthevipdown.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class JoinedCirclesActivity extends AppCompatActivity {
    DatabaseReference mref;
    RecyclerView rv;
    FirebaseAuth auth;
    FirebaseUser user;
    String name,imageurl,lat,lang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joined_circles);
        Firebase.setAndroidContext(this);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        mref = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child("OfficersAdded");
        rv = findViewById(R.id.recycle_view);
        rv.setHasFixedSize(true);
        //to click on item with item
        rv.setItemAnimator(new DefaultItemAnimator());
        //to take the recycler view in linear form
        /*recyclerView.setLayoutManager(new LinearLayoutManager(this));*/
        //to take the recycler view in grid form
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setSmoothScrollbarEnabled(true);
        rv.setLayoutManager(llm);

        FirebaseRecyclerAdapter<JoinedCircle,MyHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<JoinedCircle, MyHolder>(JoinedCircle.class,R.layout.card_layout,MyHolder.class,mref) {
            @Override
            protected void populateViewHolder(final MyHolder viewHolder, JoinedCircle model, int position) {
                String circlememberid = model.getOfficerid();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(circlememberid);
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                         name = (String) dataSnapshot.child("name").getValue();
                         imageurl = (String) dataSnapshot.child("imageurl").getValue();
                         lat = (String) dataSnapshot.child("lat").getValue();
                         lang = (String) dataSnapshot.child("lng").getValue();
                        if(imageurl!=null && name!=null)
                        {
                            viewHolder.username.setText(name);
                            Picasso.get().load(imageurl).placeholder(R.drawable.tom).into(viewHolder.profpic);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(JoinedCirclesActivity.this, MyNavigationActivity.class);
                        i.putExtra("lat",lat);
                        i.putExtra("lang",lang);
                        i.putExtra("place",name);
                        startActivity(i);
                        finish();
                    }
                });
            }
        };

        rv.setAdapter(firebaseRecyclerAdapter);

    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        View mView;
        TextView username;
        ImageView profpic;



        public MyHolder(@NonNull View itemView) {
            //to typecast the widgets
            super(itemView);
            mView = itemView;
            username = mView.findViewById(R.id.item_title);
            profpic = mView.findViewById(R.id.item_img);

        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(JoinedCirclesActivity.this,MyNavigationActivity.class));
        finish();
    }
}
