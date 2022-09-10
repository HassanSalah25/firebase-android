    package com.example.newone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.*;
    import com.google.firebase.storage.FirebaseStorage;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference x = db.getReference("contact");
        FirebaseStorage storage = FirebaseStorage.getInstance();
            x.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String names[] = new  String[(int) snapshot.getChildrenCount()];
                    String phones[] = new  String[(int) snapshot.getChildrenCount()];
                    String images[] = new  String[(int) snapshot.getChildrenCount()];
                    String keys[] = new  String[(int) snapshot.getChildrenCount()];
                    int i = 0;
                        for(DataSnapshot singleSnapshot : snapshot.getChildren()){
                            names[i] = singleSnapshot.child("name").getValue().toString();
                            phones[i] = singleSnapshot.child("phone").getValue().toString();
                            images[i] = singleSnapshot.child("image").getValue().toString();
                            keys[i] = singleSnapshot.getKey();
                            i++;
                        }
                    MyAdapter myadpt = new MyAdapter(names,phones,keys,images,getApplicationContext(),MainActivity.this);
                    ListView list = findViewById(R.id.lv);
                    list.setAdapter(myadpt);
                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            x.child(keys[position]).removeValue();
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        FloatingActionButton floatbtn = findViewById(R.id.floatingActionButton);
        floatbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent x = new Intent(getApplicationContext(),MainActivity2.class);
                startActivity(x);
            }
        });



    }


}