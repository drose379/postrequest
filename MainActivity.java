package dylanrose60.postrequest;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONStringer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EmptyStackException;


public class MainActivity extends ActionBarActivity {

    public MediaType type = MediaType.parse("application/json;charset=utf-8");
    public OkHttpClient HttpClient = new OkHttpClient();
    public String radioSelection = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get button
        Button button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    validateFields();
                    String input = getUserInput();
                    makeRequest(input);
                    getUserInfo();
                } catch (EmptyFieldException e) {
                    TextView errorText = (TextView) findViewById(R.id.errorBox);
                    ArrayList errors = e.getErrors();
                    
                    /*
                    TextView errorText = (TextView) findViewById(R.id.errorBox);
                    EditText field1 = (EditText) findViewById(R.id.textBox1);
                    field1.setBackgroundColor(Color.parseColor("#fa4652"));
                    errorText.setText("Make sure all fields are filled out");
                    */
                }
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);
        return true;
    }

   @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                //Call method that starts new activity here
                return true;
            case R.id.myInfo:
                getUserInfo();
                return true;
            default:
                return false;
        }
    }

    public void validateFields() throws EmptyFieldException {
        EditText textBox1 = (EditText) findViewById(R.id.textBox1);
        EditText textBox2 = (EditText) findViewById(R.id.textBox2);
        EditText[] varArray = {textBox1,textBox2};

        ArrayList<View> errorArray = new ArrayList();

        for (EditText field : varArray) {
            if (field.length() == 0) {
                errorArray.add(field);
            }
        }
        if (radioSelection == null) {
            //Handle radioSelection error
        }
        if (errorArray.size() != 0) {
            throw new EmptyFieldException(errorArray);
        }
    }

    public String getUserInput() {
        EditText textEntry = (EditText) findViewById(R.id.textBox1);
        EditText textEntry2 = (EditText) findViewById(R.id.textBox2);
        Editable editInterface = textEntry.getText();
        Editable editInterface2 = textEntry2.getText();
        String userString1 = editInterface.toString();
        String userString2 = editInterface2.toString();

        JSONStringer buildString = new JSONStringer();
    try {
        buildString.object();
        buildString.key("name");
        buildString.value(userString1);
        buildString.key("email");
        buildString.value(userString2);
        buildString.key("priority");
        buildString.value(radioSelection);
        buildString.endObject();
        return buildString.toString();
    }
    catch (JSONException e) {
        throw new RuntimeException(e);
    }

    }
//Method is called when a Radio Button in the view is clicked.
    public void setRadioSelect(View view) {

        switch (view.getId()) {
            case R.id.highCheckBox:
                radioSelection = "0";
                break;
            case R.id.neutralCheckBox:
                radioSelection = "1";
                break;
            case R.id.lowCheckBox:
                radioSelection = "2";
                break;
        }
    }



    public void makeRequest(String userInput) {

        //Create the post request body
        RequestBody requestBody = RequestBody.create(type,userInput);

        Request.Builder builder = new Request.Builder();
        builder.url("http://codeyourweb.net/httpTest/index.php/add"); //MAKE SURE TO ADD LIVE URL HERE!!
        //Add the userString to the request
        builder.post(requestBody);
        //Build the request, returning a usable Request object
        Request readyRequest = builder.build();
        //Run the request using the client
        Call readyCall = HttpClient.newCall(readyRequest);
        readyCall.enqueue(new Callback() {
        //Listen for response/failure from the server
            @Override
            public void onFailure(Request request, IOException e) {
                //Handle a failure
            }

            @Override
            public void onResponse(Response response) throws IOException {
                //Handle a response
            }
        });
    }

    public void getUserInfo() {
        Intent starter = new Intent(getApplicationContext(),GetUserInfo.class);
        startActivity(starter);
    }

}
