package info.lostandfound.firebase.Activity;


import android.content.ContentResolver;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ImageView;
import java.io.IOException;
import android.net.Uri;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.provider.MediaStore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import info.lostandfound.firebase.Model.Upload;
import info.lostandfound.firebase.R;


public class AddActivity extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemSelectedListener {

    //constant to track image chooser intent
    private static final int PICK_IMAGE_REQUEST = 234;


    //a constant to track the file chooser intent
    private EditText ProductName, ProductDesc;
    private Button btn_Upload, btn_Choose;
    private ImageView imageView;
    private Uri filePath, downloadUri;
    private StorageReference storageReference;
    public  DatabaseReference mDatabase;
    private String item, generatedFilePath, type;
    private Date date;
    DatePicker simpleDatePicker;
    public static DateFormat dateFormat1 = new SimpleDateFormat("dd/MM/yyyy");






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("ADD POST");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        Spinner spinner,spinner1;
        ProductName = findViewById(R.id.productname);
        ProductDesc = findViewById(R.id.productdesc);
        btn_Upload = findViewById(R.id.btn_upload);
        imageView = findViewById(R.id.imageView);
        btn_Choose = findViewById(R.id.btn_choose);
        spinner = findViewById(R.id.vspinner);
        spinner1 = findViewById(R.id.tspinner);
        DatePicker simpleDatePicker = (DatePicker) findViewById(R.id.simpleDatePicker); // initiate a date picker


        storageReference = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_UPLOADS);





        btn_Upload.setOnClickListener(this);
        btn_Choose.setOnClickListener(this);
        spinner.setOnItemSelectedListener(this);
        spinner1.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<>();
        categories.add("Lost");
        categories.add("Found");
        categories.add("Missing");
        categories.add("Wanted");

        List<String> types = new ArrayList<>();
        types.add("Books_documents");
        types.add("Clothing");
        types.add("Electronics");
        types.add("Persons");
        types.add("Accessories");
        types.add("Other");
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, types);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        spinner1.setAdapter(dataAdapter1);


    }



    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public String getFileExtension(Uri uri) {
        // Got the download URL for image

        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));


    }


    private void uploadFile() {

        //checking if file is available
        if (filePath != null) {
            //displaying progress dialog while image is uploading
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            //getting the storage reference
            StorageReference sRef = storageReference.child(Constants.STORAGE_PATH_UPLOADS + System.currentTimeMillis() + "." + getFileExtension(filePath));
            //adding the file to reference
            sRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override

                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                            //dismissing the progress dialog
                            progressDialog.dismiss();//displaying success toast
                            Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
                            //creating the upload object to store uploaded image details

                            /// Got the download URL for image
                            downloadUri = taskSnapshot.getMetadata().getDownloadUrl();
                            generatedFilePath = downloadUri.toString(); /// The string(file link) that you need

                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            if (user != null) {


                                final Calendar c = Calendar.getInstance();
                                int year = c.get(Calendar.YEAR);
                                int month = c.get(Calendar.MONTH) + 1;
                                int day = c.get(Calendar.DAY_OF_MONTH);


                                String date = day + "/" + month + "/" + year;

                                Upload posts = new Upload(user.getDisplayName(), user.getEmail(), ProductName.getText().toString(), ProductDesc.getText().toString(), generatedFilePath, item, date, type);                          //adding an upload to firebase database

                                String uploadId = mDatabase.push().getKey();

                                mDatabase.child(uploadId).setValue(posts);





                            }


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            System.out.println("Upload is " + progress + "% done");
                        }

                    });

        } else {
            Toast.makeText(getApplicationContext(), "Select a file! ", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        Spinner spinner = (Spinner) parent;
        if(spinner.getId() == R.id.vspinner) {
            item = parent.getItemAtPosition(position).toString();
        } else if (spinner.getId() == R.id.tspinner)
        {
            type = parent.getItemAtPosition(position).toString();
        }



        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }


    public void onNothingSelected(AdapterView<?> arg0) {

    }



    @Override
    public void onClick(View view) {


        if (view == btn_Choose) {
            showFileChooser();
        } else if (view == btn_Upload) {


            uploadFile();


        }
    }




}

