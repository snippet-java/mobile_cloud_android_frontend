package com.ibm.simpleapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ResponsePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set the view as defined in res/layout/first_page.xml
        setContentView( R.layout.first_page );

        //retrieve the information passed on from the login page
        //and display that
        Intent intent  = getIntent();
        String record = intent.getStringExtra( "record" );

        //display the record String in its correct place
        TextView recordView = (TextView) findViewById( R.id.txtRecords );
        recordView.setText( record );


    }
}
