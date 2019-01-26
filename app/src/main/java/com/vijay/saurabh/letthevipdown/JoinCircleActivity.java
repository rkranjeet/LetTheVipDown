package com.vijay.saurabh.letthevipdown;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.goodiebag.pinview.Pinview;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.vijay.saurabh.letthevipdown.LoginRegister.CreateUsers;

public class JoinCircleActivity extends AppCompatActivity {

    Pinview pinview ;
    DatabaseReference databaseReference , currentdatabase , circlereference ;
    FirebaseUser user ;
    FirebaseAuth auth ;
    String current_user_id , join_userid;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_circle);
        pinview = (Pinview)findViewById(R.id.pinview);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        currentdatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());

        current_user_id = user.getUid();
//        circlereference = FirebaseDatabase.getInstance().getReference().child("Users").child(join_userid);









    }

    public void submitbuttonclick(View v)
    {
        Query query = databaseReference.orderByChild("code").equalTo(pinview.getValue());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //CreateUsers createUsers = null ;
                if(dataSnapshot.exists())
                {
                    CreateUsers createUsers = null ;
                    for (DataSnapshot childss : dataSnapshot.getChildren() )
                    {
                        createUsers = childss.getValue(CreateUsers.class) ;
                        join_userid = createUsers.getUserid();
                        circlereference = FirebaseDatabase.getInstance().getReference().child("Users").child(join_userid).child("CircleMembers");

                        CircleJoin circleJoin = new CircleJoin(current_user_id) ;
                        CircleJoin circleJoin1 = new CircleJoin(join_userid) ;

                        circlereference.child(user.getUid()).setValue(circleJoin).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if(task.isSuccessful())
                                {
                                    Toast.makeText(JoinCircleActivity.this, "User has Joined Circle Successfully", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

                        user = auth.getCurrentUser();
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
                        ref.child("OfficersAdded").child(join_userid).child("officerid").setValue(join_userid);



                    }
                }
                else
                {
                    Toast.makeText(JoinCircleActivity.this, "You Have entered the wrong pin", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }



}
