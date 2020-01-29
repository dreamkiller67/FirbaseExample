package com.example.firestoreexample;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    EditText mtitleEdit , mDescription;
    Button mUploadBtn;
    Button mShowUploadBtn;
    ProgressBar pb;
    FirebaseFirestore db;
    private static final int PICK_IMAGE_REQUEST = 1;
     Button mButtonChooseImage;
     ProgressBar mProgressBar;
     ImageView mImageView;
     TextView mTextViewShowUploads;
     Uri mImageUri;
     private StorageReference mStorageRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // action bar and its contents
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Upload Data");

        mtitleEdit = findViewById(R.id.titleEdtxt);
        mDescription = findViewById(R.id.titleDescrptxt);
        mShowUploadBtn = findViewById(R.id.showUploadBtn);
        mUploadBtn = findViewById(R.id.uploadBtn);
        //mProgressBar = findViewById(R.id.progress_bar);
        mButtonChooseImage = findViewById(R.id.choose_file_Btn);
        mImageView = findViewById(R.id.uploadImage);
        db = FirebaseFirestore.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");

        // event when we click on choose file button
        mButtonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        mUploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = mtitleEdit.getText().toString().trim();
                String description = mDescription.getText().toString().trim();

                uploadData(title , description);
                uploadFile();
            }
        });
        mShowUploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagesActivity();
            }
        });
    }
 // method for uploading data to fireStore
    private  void uploadData(String title , String description){

        String id = UUID.randomUUID().toString();
        Map<String ,Object> doc = new HashMap<String,Object>();
        doc.put("id",id); // id of data
        doc.put("title",title);
        doc.put("description",description);

        // add this data
        db.collection("Documents").document(id).set(doc).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                    // this will called when data is sucessfully added
                Toast.makeText(MainActivity.this,"Sucees",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this,"error",Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void openFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }
    protected void onActivityResult(int requestCode , int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
         && data != null && data.getData() != null){
            mImageUri = data.getData();
            Glide.with(this).load(mImageUri).into(mImageView);
        }
    }
    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));

    }

    // method for uploading data to firebase Storage
    private void uploadFile(){
        if(mImageUri != null){
            StorageReference fileRefrence = mStorageRef.child(System.currentTimeMillis()
            + " . " + getFileExtension(mImageUri));
            fileRefrence.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mProgressBar.setProgress(0);
                                }
                            },5000);
                            Toast.makeText(MainActivity.this,"success",Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        else{
            Toast.makeText(this,"no file selected",Toast.LENGTH_SHORT).show();
        }
    }
    private void openImagesActivity(){
        Intent intent = new Intent(this,ImagesActivity.class);
        startActivity(intent);
    }
}

