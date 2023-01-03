package com.example.onenotebook;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LessonCreator extends AppCompatActivity {

    EditText newLessonName,newLessonDate;
    Button cancelButton, createButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lesson_creator);

        newLessonName = findViewById(R.id.newLessonName);
        newLessonDate = findViewById(R.id.newDate);


        cancelButton = findViewById(R.id.lessonCancel);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(Activity.RESULT_CANCELED, new Intent());
                finish();
            }
        });

        createButton = findViewById(R.id.lessonConfirm);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent returnIntent = new Intent();
                returnIntent.putExtra("name",newLessonName.getText().toString());
                returnIntent.putExtra("date",newLessonDate.getText().toString());

                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        });
    }
}