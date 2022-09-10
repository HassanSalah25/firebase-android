package com.example.newone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

    public class MainActivity2 extends AppCompatActivity {
    public ImageView imageView;
    public Uri filePath;
    public EditText editname;
    public String s;
    public EditText editphone;
    public FirebaseDatabase db;
    public FirebaseStorage storageReference = FirebaseStorage.getInstance();
    public StorageReference storageReference1 = storageReference.getReference();
    public DatabaseReference x;
    public String d;
    public StorageReference ref;
    public String Turi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        editname = findViewById(R.id.editname);
        editphone = findViewById(R.id.editphone);
        imageView = findViewById(R.id.imageView2);
        Button savebtn = findViewById(R.id.savebtn);
        Button bringimage = findViewById(R.id.bringimage);
        Button Donebtn = findViewById(R.id.button);
        db= FirebaseDatabase.getInstance();
        x = db.getReference("contact");
        Intent intent = getIntent();
        if(intent.getStringExtra("name") != null){
            String name = intent.getStringExtra("name");
            String phone = intent.getStringExtra("phone");
            String key = intent.getStringExtra("key");
            editname.setText(name);
            editphone.setText(phone);
            savebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (editname.getText().toString() == null || editphone.getText().toString() == null)
                        Toast.makeText(MainActivity2.this, "name or phone not filled", Toast.LENGTH_SHORT).show();
                    else {
                        x.child(key).child("name").setValue(editname.getText().toString());
                        x.child(key).child("phone").setValue(editphone.getText().toString());
                    }
                }
            });
        }
        else {
            Donebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DownloadImage();
                }
            });
            savebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    uploadData();
                }
            });
        }
        bringimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
    }
    void DownloadImage()
    {
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                Turi = uri.toString();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Toast.makeText(MainActivity2.this, "Image couldn't Uploaded!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void selectImage()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image from here..."), 1);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
                uploadImage();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    String name ;
    String phone;

    void uploadData()
    {
        phone = editphone.getText().toString();
        d = x.push().getKey();
        x.child(d).child("image").setValue(Turi);
        x.child(d).child("name").setValue(name);
        x.child(d).child("phone").setValue(phone);
    }
    UploadTask ut ;
    void uploadImage()
    {
        if (filePath != null) {
            ref = storageReference1.child("images/" + filePath.getLastPathSegment());
            ut = ref.putFile(filePath);
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            ut.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity2.this, "Image Uploaded!!", Toast.LENGTH_SHORT).show();

                }
            })
             .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });


        }
    }
}