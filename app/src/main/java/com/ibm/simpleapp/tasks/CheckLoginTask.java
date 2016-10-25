package com.ibm.simpleapp.tasks;

import android.util.Log;

import com.ibm.helper.AsyncServiceTask;
import com.ibm.utility.http.PostService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;

public class CheckLoginTask extends AsyncServiceTask{

    //These keys will be used to create the JSON
    //that will be sent to the server. Make sure
    //the keys match with what the server is expecting
    public static String _userNameKey = "user_name";
    public static String _passwordKey = "user_password";

    public static String _taskID = "Check Log in detail";

    private String restLocation;
    public CheckLoginTask(String checkloginURL ){

        super( _taskID );
        restLocation  = checkloginURL;
    }

    @Override
    protected String performTask(String... params) throws IOException, JSONException, URISyntaxException {

        //the first parameter is the username
        //the second parameter is the password
        String user = params[0];
        String password = params[1];


        //create a JSON object
        JSONObject dataObject = new JSONObject();
        dataObject.put( _userNameKey, user );
        dataObject.put( _passwordKey, password );

        //finally post the data as string
        PostService ps = new PostService( null );

        return ps.performPost( restLocation, dataObject.toString() );
    }
}
