/*
 * Copyright 2016 IBM Corp. All Rights Reserved
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ibm.simpleapp;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.ibm.helper.TaskReceiver;
import com.ibm.simpleapp.tasks.CheckLoginTask;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements TaskReceiver {

    public static String _parsingError = "Server response is not understood";
    private ProgressDialog _pDialogue = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set the view as defined in res/layout/activity_main.xml
        setContentView(R.layout.activity_main);
        //Retrieve the login button from the main view
        //and assign the task of checking login credential to it
        Button loginButton = ( Button ) findViewById( R.id.loginBtn );
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //on button click, this application will use a REST API
                //to access a resource on the back-end
                checkLogin();
            }
        });
    }

    public void checkLogin(){
        //retrieve the username and password values from user
        EditText userText = ( EditText ) findViewById( R.id.unameText );
        EditText passwordText = ( EditText ) findViewById( R.id.passwordText );
        EditText endPointText = ( EditText ) findViewById( R.id.endPoint );

        //the username or password field cannot be empty
        String user_name = userText.getText().toString();
        String user_password = passwordText.getText().toString();
        if( user_name.equals( "") || user_password.equals( "")){
            //show an error message
            TextView errView = (TextView) findViewById( R.id.txtError );
            errView.setText( "Username or Password cannot be empty" );
            return;
        }

        //else assume everything is right from the client side
        // pass the data to the server side for validation
        //this would cause the application to wait so start a
        // progress dialogue and then pass the data

        _pDialogue = new ProgressDialog( this );
        _pDialogue.setCancelable(false);
        _pDialogue.setTitle("Logging in...");
        _pDialogue.setMessage("Sending credentials");
        _pDialogue.show();

        //make sure there is no error message
        TextView errView = (TextView) findViewById( R.id.txtError );
        errView.setText("");

        //this is where the application makes a REST API call
        //to the URL provided in the field api end point
        String endPoint = endPointText.getText().toString();
        CheckLoginTask cl = new CheckLoginTask(endPoint);
        cl.setReceiver(this);
        cl.execute(user_name, user_password);
    }

    @Override
    public void receiveResult( String response, String source) {
        //so the result has arrived.
        //first, close the dialogue
        _pDialogue.dismiss();
        _pDialogue = null;
        //then check if this is the checklogin task
        if( source.equals( CheckLoginTask._taskID) ){
            try{
                JSONObject responseObject = new JSONObject( response );
                String result = responseObject.getString( "result" );
                if( result.equals( "success" ) ){
                    //collect the information
                    String record = responseObject.getString( "record" );
                    //navigate to the first page
                    Intent intent = new Intent( this, ResponsePage.class );
                    intent.putExtra( "record", record );
                    startActivity( intent );
                }
                else{ //if the login is not successful
                    //String desc = responseObject.getString( "description" );
                    //show the error
                    TextView errView = (TextView) findViewById( R.id.txtError );
                    errView.setText( "Incorrect credentials" );
                    //Toast.makeText( getApplicationContext() , desc, Toast.LENGTH_LONG ).show();
                }
            }
            catch( JSONException je ){
                Toast.makeText( getApplicationContext() , _parsingError, Toast.LENGTH_LONG )
                        .show();

            }
        }
    }
}
