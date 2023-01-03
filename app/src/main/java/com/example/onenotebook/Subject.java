package com.example.onenotebook;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;

public class Subject extends AppCompatActivity {

    ListView listView;
    ArrayList<LVAdapter.ListModel> listModelArrayList = new ArrayList<>();
    FloatingActionButton addLesson;
    String Name;
    String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/OneNotebook";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);




        Intent intent = getIntent();
        Name = intent.getStringExtra("subject");


        listModelArrayList = readFromFile(this);


        TextView SubjectName = findViewById(R.id.subjectName);

        addLesson = findViewById(R.id.addLesson);

        listView = findViewById(R.id.listView);

        SubjectName.setText(Name);
        final LVAdapter adapter = new LVAdapter(this,listModelArrayList);
        listView.setAdapter(adapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                listModelArrayList.remove(i);
                writeToFile(getApplicationContext());
                listView.setAdapter(new LVAdapter(getApplicationContext(),listModelArrayList));

                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent1 = new Intent(getApplicationContext(),Lesson.class);
                intent1.putExtra("Lesson",listModelArrayList.get(i).Name.trim());
                startActivity(intent1);
            }
        });

        addLesson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Subject.this, LessonCreator.class);
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
                        String name,date;
                        name = data.getStringExtra("name");
                        date = data.getStringExtra("date");

                        if(name != null && !name.equals("")) {

                            LVAdapter.ListModel listModel = new LVAdapter.ListModel(name,date);

                            listModelArrayList.add(listModel);

                            writeToFile(getApplicationContext());

                            listView.setAdapter(new LVAdapter(Subject.this, listModelArrayList));

                        }
                    }
                }
            });

    private void writeToFile(Context context) {

        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(Name+".txt", Context.MODE_PRIVATE));
            for(LVAdapter.ListModel S : listModelArrayList)
                outputStreamWriter.write(S.rawString()+";");
            outputStreamWriter.close();
        }
        catch (Exception e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }



    public ArrayList<LVAdapter.ListModel> readFromFile(Context context) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput(Name+".txt");

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

        ArrayList<LVAdapter.ListModel> returnList = new ArrayList<LVAdapter.ListModel>();
        String[] lessons = ret.split(";");

        for(String S : lessons){
            if(!Objects.equals(S, ""))
                returnList.add(new LVAdapter.ListModel(S));
        }

        return returnList;
    }


}