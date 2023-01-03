package com.example.onenotebook;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.content.PackageManagerCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class Lesson extends AppCompatActivity {

    String currentPhotoPath;
    String LessonName;
    Button next;
    Button previous;
    FloatingActionButton takePic;

    ImageView lessonPage;


    ArrayList<Uri> image_URIs = new ArrayList<>();
    Uri image_uri;
    int lessonPageNumber = 0;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);



        LessonName = getIntent().getStringExtra("Lesson").trim();

        image_URIs = readFromFile(Lesson.this);
        lessonPage = findViewById(R.id.LessonPage);



        if(image_URIs.size()!=0){
            lessonPage.setImageURI(image_URIs.get(0));
        }

        next = findViewById(R.id.nextButton);
        previous = findViewById(R.id.previousButton);
        takePic = findViewById(R.id.takePic);


        lessonPage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(image_URIs.size()>0){
                    image_uri = image_URIs.get(lessonPageNumber);
                    image_URIs.remove(lessonPageNumber);
                    Toast.makeText(Lesson.this,"Deleted",Toast.LENGTH_SHORT).show();
                    writeToFile(Lesson.this);

                    if(lessonPageNumber > 0){
                        lessonPage.setImageURI(image_URIs.get(lessonPageNumber-1));
                        lessonPageNumber--;
                    }
                    else if(image_URIs.size() > 1){
                        lessonPage.setImageURI(image_URIs.get(lessonPageNumber));
                    }
                    else
                        lessonPage.setImageResource(R.drawable.no_photo);
                    File file = new File(String.valueOf(image_uri));
                    if(file.exists()){
                        file.delete();
                    }
                }

                return false;
            }
        });

        takePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED ||
                        checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                    String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                    requestPermissions(permission, 123);
                }
                else{
                    openCamera();
                }

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(lessonPageNumber < image_URIs.size()-1){
                    lessonPageNumber++;
                    lessonPage.setImageURI(image_URIs.get(lessonPageNumber));
                }
            }
        });
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(lessonPageNumber > 0){
                    lessonPageNumber--;
                    lessonPage.setImageURI(image_URIs.get(lessonPageNumber));
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 123:
               if(grantResults.length > 0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                   openCamera();
               }
               else{
                   Toast.makeText(this,"Permission denied!",Toast.LENGTH_SHORT).show();
               }
        }
    }

    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"new image");
        values.put(MediaStore.Images.Media.DESCRIPTION,"From Camera");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);


        Intent Cameraintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Cameraintent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(Cameraintent,124);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK){

            if(image_URIs.size() != 0)
                lessonPageNumber++;
            image_URIs.add(image_uri);
            writeToFile(Lesson.this);

            lessonPage.setImageURI(image_URIs.get(lessonPageNumber));
        }
        super.onActivityResult(requestCode, resultCode, data);

    }


    private void writeToFile(Context context){
        try {

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(LessonName+".txt", Context.MODE_PRIVATE));


            for(int a = 0;a < image_URIs.size();a++)
                outputStreamWriter.write(image_URIs.get(a).toString()+";");
            outputStreamWriter.close();
        }
        catch (Exception e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public ArrayList<Uri> readFromFile(Context context) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput(LessonName+".txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append("\n").append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayList<Uri> returnList = new ArrayList<Uri>();
        String[] images = ret.split(";");

        for(String S : images){
            if(!Objects.equals(S, ""))
                returnList.add(Uri.parse(S.trim()));
        }

        return returnList;
    }
}