package com.example.flashcards;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

@RequiresApi(api = Build.VERSION_CODES.O)
public class AddPage extends AppCompatActivity {
    private Button btnAdd;
    private Button btnBack;
    private EditText txtDef;
    private EditText txtTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_page);
        //HIDES THE TITLE BAR AT TOP
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //Link the xml components
        linkXML();


        // LISTENER FOR IF THE USER PRESSES ENTER IT WILL MOVE TO NEXT FIELD
        txtTitle.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                txtDef.requestFocus();
                return true;
            }
        });


        //LISTENER FOR SWITCHING PAGES
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NextPage();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCard();
                txtTitle.setText("");
                txtDef.setText("");
                saveArr();
                txtTitle.requestFocus();
            }
        });


    }

    private void saveArr(){
        String arrStr = String.join("|", MainActivity.titles);
        System.out.println(arrStr);
        SharedPreferences arrTitles = getSharedPreferences("arrTitles", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = arrTitles.edit();
        editor.clear();
        editor.putString("Title", arrStr);
        editor.apply();
    }


    private void addCard(){
        // Adds the title to the arraylist in the main
        MainActivity.titles.add(txtTitle.getText().toString());
        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ want to save the arraylist here
        saveArr();

        SharedPreferences cardDefs = getSharedPreferences("Cards", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = cardDefs.edit();

        // adds the definiton to the shared preferences
        editor.putString(txtTitle.getText().toString(), txtDef.getText().toString());
        editor.apply();
    }


    //GOES TO THE BACK to the main PAGE
    private void NextPage() {
        Intent myIntent = new Intent(this, MainActivity.class);
        //myIntent.putExtra("key1", roundsNum);
        startActivity(myIntent);
    }

    private void linkXML(){
        btnAdd = findViewById(R.id.btnAdd);
        btnBack = findViewById(R.id.btnBack);
        txtDef = findViewById(R.id.txtDef);
        txtTitle = findViewById(R.id.txtTitle);
    }
}