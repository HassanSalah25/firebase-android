    package com.example.newone;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class MyAdapter extends BaseAdapter {
    String names[];
    String phones[];
    String keys[];
    String images[];
    Context cm;
    Activity act;
    MyAdapter(String x[],String y[],String z[],String i[], Context c,Activity act1){
        names = x;
        phones = y;
        keys = z;
        cm = c;
        images = i;
        act = act1;
    }
    @Override
    public int getCount() {
        return names.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null)
            convertView = LayoutInflater.from(cm).inflate(R.layout.designcard, parent, false);
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference r = db.getReference("contact");
        TextView x = convertView.findViewById(R.id.name);
        x.setText(names[position]) ;
        TextView y = convertView.findViewById(R.id.phone);
        y.setText(phones[position]);
        ImageView e = convertView.findViewById(R.id.imageView);
        Picasso.get().load(images[position]).into(e);
        Button edit = convertView.findViewById(R.id.editbtn);
        Button remove = convertView.findViewById(R.id.detletebtn);
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                r.child(keys[position]).removeValue();
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String a = "n";
                Intent i = new Intent(cm,MainActivity2.class)
                .putExtra("name",names[position])
                .putExtra("phone",phones[position])
                .putExtra("key",keys[position]);
                act.startActivity(i);
            }
        });
        return convertView;
    }
}
