package com.example.onenotebook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {


    GridView coursesGV;
    FloatingActionButton button;

    ArrayList<GVAdapter.GridModel> gridModelArrayList = new ArrayList<>();
    String[] subjects;
    String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/OneNotebook";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        coursesGV = findViewById(R.id.gridView);

        subjects = readFromFile(this);

        for(String S : subjects){
            if(!S.equals(""))
                gridModelArrayList.add(new GVAdapter.GridModel(S.trim()));
        }

        final GVAdapter adapter = new GVAdapter(this, gridModelArrayList);
        coursesGV.setAdapter(adapter);

        coursesGV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent subject = new Intent(MainActivity.this,Subject.class);
                subject.putExtra("subject", gridModelArrayList.get(i).getCourse_name().toString());
                startActivity(subject);
            }
        });

        coursesGV.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                File path = getFilesDir();
                File file = new File(path,gridModelArrayList.get(i).getCourse_name()+".txt");
                file.delete();
                gridModelArrayList.remove(i);
                writeToFile("",getApplicationContext());


                coursesGV.setAdapter(new GVAdapter(MainActivity.this, gridModelArrayList) );
                return true;
            }
        });

        button = findViewById(R.id.addSubject);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SubjectCreator.class);
                someActivityResultLauncher.launch(intent);

            }
        });


    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();

                        Bundle extras = data.getExtras();
                        String s = extras.getString("result");

                        if(s != null && !s.equals("")){
                            for(String S : subjects)
                                if(S.equals(s)) {
                                    Toast.makeText(MainActivity.this,"Subject already exists",Toast.LENGTH_LONG).show();
                                    return;
                                }
                            writeToFile(s,getApplicationContext());
                            gridModelArrayList.add(new GVAdapter.GridModel(s));
                            coursesGV.setAdapter(new GVAdapter(MainActivity.this, gridModelArrayList) );
                        }
                        else{
                            Toast.makeText(MainActivity.this,"Error with creating new subject",Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });

    private void writeToFile(String data,Context context) {


        try {

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("Subjects.txt", Context.MODE_PRIVATE));
            for(GVAdapter.GridModel S : gridModelArrayList){

                outputStreamWriter.write(S.getCourse_name()+";");
            }
            if(!data.equals(""))
                outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private String[] readFromFile(Context context) {
        String ret = "";

        try {
            InputStream inputStream = context.openFileInput("Subjects.txt");

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
        return ret.split(";");
    }


}