package com.example.onenotebook;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SubjectCreator extends AppCompatActivity {

    Button cancelButton, createButton;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subject_creator);


        editText = findViewById(R.id.newElementName);


        cancelButton = findViewById(R.id.Cancel);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(Activity.RESULT_CANCELED, new Intent());
                finish();
            }
        });

        createButton = findViewById(R.id.Confirm);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent returnIntent = new Intent();
                returnIntent.putExtra("result",editText.getText().toString());

                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        });

    }
}