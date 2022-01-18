package com.example.musicbook.login_register;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicbook.R;
import com.example.musicbook.ui.custom.CustomProgressDialog;
import com.example.musicbook.ui.home.HomeFragment;
import com.example.musicbook.ui.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class First_Login_Activity extends AppCompatActivity {
    private static final int IMAGE_PICK_GALLERY_REQUEST_CODE = 300;

    Uri uri_image,downloadUri;

    Button mButton;
    EditText mNameEdt, mPhoneEdt, mJobEdt, mSchoolEdt, mLocationEdt;
    RoundedImageView imageView;
    TextView mAddPicTv;
    FirebaseDatabase firebaseDatabase;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    CustomProgressDialog progressDialog;
    String storagePath = "User_Profile_Cover_Imgs/";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_login);

        init();
    }

    public void init() {

        imageView = findViewById(R.id.fl_addPic);
        mAddPicTv = findViewById(R.id.fl_text_addPic);
        mButton = findViewById(R.id.fl_btn_next);
        mNameEdt = findViewById(R.id.fl_edt_name);
        mPhoneEdt = findViewById(R.id.fl_edt_phone);
        mJobEdt = findViewById(R.id.fl_edt_job);
        mLocationEdt = findViewById(R.id.fl_edt_location);
        mSchoolEdt = findViewById(R.id.fl_edt_school);

        progressDialog=new CustomProgressDialog(First_Login_Activity.this);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("Users");
        storageReference= FirebaseStorage.getInstance().getReference();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickFromGallery();
            }
        });
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBlank()){
                    updateData();
                }
            }
        });

    }

    public boolean checkBlank() {
        boolean flag = true;

        if (mNameEdt.getText().toString().trim().isEmpty()) {
            mNameEdt.setError("Vui lòng nhập tên");
            mNameEdt.setFocusable(true);
            flag = false;
        }
        if (mPhoneEdt.getText().toString().trim().isEmpty()) {
            mPhoneEdt.setError("Vui lòng nhập tên");
            mPhoneEdt.setFocusable(true);
            flag = false;
        }
        if (mJobEdt.getText().toString().trim().isEmpty()) {
            mJobEdt.setError("Vui lòng nhập tên");
            mJobEdt.setFocusable(true);
            flag = false;
        }
        if (mSchoolEdt.getText().toString().trim().isEmpty()) {
            mSchoolEdt.setError("Vui lòng nhập tên");
            mSchoolEdt.setFocusable(true);
            flag = false;
        }
        if (mSchoolEdt.getText().toString().trim().isEmpty()) {
            mSchoolEdt.setError("Vui lòng nhập tên");
            mSchoolEdt.setFocusable(true);
            flag = false;

        } return flag;
    }
    public void updateData(){

        progressDialog.setMessage("Cập nhật");
        progressDialog.show();
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("name",mNameEdt.getText().toString());
        hashMap.put("phone",mPhoneEdt.getText().toString().trim());
        hashMap.put("image", downloadUri.toString());

        hashMap.put("job",mJobEdt.getText().toString());
        hashMap.put("school",mSchoolEdt.getText().toString());
        hashMap.put("location",mSchoolEdt.getText().toString());
        hashMap.put("cover","");
        databaseReference.child(firebaseUser.getUid()).updateChildren(hashMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                        Toast.makeText(First_Login_Activity.this, "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(First_Login_Activity.this,LoginActivity.class));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(First_Login_Activity.this, "Cập nhật thông tin thất bại", Toast.LENGTH_SHORT).show();
                    }
                });

    }
    private void pickFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, IMAGE_PICK_GALLERY_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode==RESULT_OK&&requestCode == IMAGE_PICK_GALLERY_REQUEST_CODE&& data.getData()!=null) {
            uri_image = data.getData();
            Picasso.get().load(uri_image).into(imageView);
            mAddPicTv.setVisibility(View.INVISIBLE);
            Log.d("onAR",String.valueOf(uri_image));

            uploadProfileCoverPhoto(uri_image);
        }
    }
    private void uploadProfileCoverPhoto(Uri uri) {
        //path and name of image to be stored in firebase storage
        String filePathAndName = storagePath + "" + "image" + "_" + firebaseUser.getUid();

        StorageReference storageReference2nd = storageReference.child(filePathAndName);
        storageReference2nd.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //image is upload to storage, now get its url and store in user's database
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful()) ;
                        downloadUri = uriTask.getResult();
                        //check if image is uploaded or not and uri is received
                        if (uriTask.isSuccessful()) {

                        } else {

                            Toast.makeText(getApplicationContext(), "Some error occured", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull @NotNull UploadTask.TaskSnapshot snapshot) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {

                        Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


    }
}