package com.example.flashcards;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

@RequiresApi(api = Build.VERSION_CODES.O)
public class MainActivity extends AppCompatActivity {
    //XML Elements
    private Button btnNext;
    private Button btnPrev;
    private Button btnAdd;
    private Button btnFlip;
    private Button btnReset;
    private TextView textArea;
    private ImageButton btnDelete;

    //array of all the keys
    public static ArrayList<String> titles = new ArrayList<String>();
    public static int index = 0;


    //Current card data
    public static String currTitle;
    public static String currDefinition;
    public static boolean flipped = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //HIDES THE TITLE BAR AT TOP
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //LINK THE XML COMPONENTS
        linkXML();

        updateCards(index, flipped);


        //LISTENER FOR SWITCHING PAGES
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextPage();
            }
        });

        //LISTENER FOR GOING TO NEXT CARD
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveNext();
            }
        });

        //Listener for going to prev card
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                movePrev();
            }
        });

        //listener for flipping the card
        btnFlip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flipped = !flipped;
                updateCards(index, flipped);
            }
        });

        //listener for resetting
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences cardDefs = getSharedPreferences("Cards", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = cardDefs.edit();
                editor.clear();
                editor.apply();

                SharedPreferences arrTitles = getSharedPreferences("arrTitles", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor2 = arrTitles.edit();
                editor2.clear();
                editor2.apply();
                titles.clear();
            }
        });

        //listener to delete
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCard();
            }
        });
    }

    private void moveNext(){
        if (index < titles.size()){
            index++;
            updateCards(index, false);
        }
    }
    private void movePrev(){
        if (index > 0){
            index--;
            updateCards(index, false);
        }
    }

    private void loadArr(){
        SharedPreferences arrTitles = getSharedPreferences("arrTitles", Context.MODE_PRIVATE);
        String together = arrTitles.getString("Title", "");
        if (together.equals("")){
            Toast.makeText(getApplicationContext(),"Please create at least one card", Toast.LENGTH_SHORT).show();
            nextPage();
        }
        else {
            String[] arrSep = together.split("\\|");

            titles.clear();
            for (String i : arrSep) {
                titles.add(i);
            }
        }
    }

    private void updateCards(int index, boolean flipped){
        loadArr();
        System.out.println("size: " + titles.size());
        System.out.println("index: " + index);
        btnPrev.setEnabled(true);
        btnNext.setEnabled(true);
        if (titles.size() == 1){
            btnPrev.setEnabled(false);
            btnNext.setEnabled(false);
        }
        else if (index == 0){
            btnPrev.setEnabled(false);
        }
        else if ((index + 1) == titles.size()){
            btnNext.setEnabled(false);
            btnPrev.setEnabled(true);
        }
        else{
            btnPrev.setEnabled(true);
            btnNext.setEnabled(true);
        }
        SharedPreferences cardDefs = getSharedPreferences("Cards", Context.MODE_PRIVATE);
        String title = titles.get(index);//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
        String definition = cardDefs.getString(title,"");


        if (flipped == true){
            textArea.setText(definition);
        }
        else{
            textArea.setText(title);
        }
    }

    private void deleteCard(){
        if (index == 0 && titles.size() == 0) {
            Toast.makeText(getApplicationContext(), "Empty", Toast.LENGTH_SHORT).show();
        }
        else{
            SharedPreferences cardDefs = getSharedPreferences("Cards", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = cardDefs.edit();
            editor.remove(titles.get(index));
            editor.apply();
            titles.remove(index);
            System.out.println("size: " + titles.size() + "  index: " + index);

            if (index != 0){
                index--;
            }
            saveArr();
            updateCards(index, flipped);
        }

    }

    private void saveArr(){
        String arrStr = String.join("|", MainActivity.titles);

        SharedPreferences arrTitles = getSharedPreferences("arrTitles", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = arrTitles.edit();
        editor.clear();
        editor.putString("Title", arrStr);
        editor.apply();
    }

    //GOES TO THE NEXT PAGE
    private void nextPage() {
        Intent myIntent = new Intent(this, AddPage.class);
        //myIntent.putExtra("key1", roundsNum);
        startActivity(myIntent);
    }

    private void linkXML(){
        btnNext = findViewById(R.id.btnNext);
        btnPrev = findViewById(R.id.btnPrev);
        btnAdd = findViewById(R.id.btnAdd);
        btnFlip = findViewById(R.id.btnFlip);
        btnReset = findViewById(R.id.btnReset);
        textArea = findViewById(R.id.textArea);
        btnDelete = findViewById(R.id.btnDelete);
    }


}