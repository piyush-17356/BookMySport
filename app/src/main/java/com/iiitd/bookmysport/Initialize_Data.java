package com.iiitd.bookmysport;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.iiitd.bookmysport.bookings.Document;
import com.iiitd.bookmysport.other.Functions;

import java.util.ArrayList;

public class Initialize_Data extends AppCompatActivity {

    Button button1;
    Button button2;
    Button button3;
    Button button4;
    Button button5;
    Button button6;
    Button button7;
    Button button8;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initialize_data);

        db = FirebaseFirestore.getInstance();
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        button5 = findViewById(R.id.button5);
        button6 = findViewById(R.id.button6);
        button7 = findViewById(R.id.button7);
        button8 = findViewById(R.id.button8);


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click(1);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click(2);
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click(3);
            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click(4);
            }
        });

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click(5);
            }
        });

        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click(6);
            }
        });

        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click(7);
            }
        });

        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click(8);
            }
        });

    }


    public void click(int sportsID) {

        String col = "";
        String doc = "";

        col = Functions.getSportsName(sportsID - 1);
        doc = Functions.getSportsDocName(sportsID - 1);

        String d = "";

        for (int i = 1; i < 25; i++) {
            Document my = new Document(new ArrayList<String>(), i, 0);
            d = "" + i;

            db.collection(col)
                    .document(doc)
                    .collection("Today")
                    .document(d).set(my);
        }

        Toast.makeText(Initialize_Data.this, "Today DOCS Created", Toast.LENGTH_SHORT).show();

        for (int i = 1; i < 25; i++) {
            Document my = new Document(new ArrayList<String>(), i, 0);
            d = "" + i;

            db.collection(col)
                    .document(doc)
                    .collection("Tom")
                    .document(d).set(my);
        }


        Toast.makeText(Initialize_Data.this, "Tom DOCS Created", Toast.LENGTH_SHORT).show();

    }

}
