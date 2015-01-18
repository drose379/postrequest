package dylanrose60.postrequest;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.IOException;

public class GetUserInfo extends ActionBarActivity {

    public OkHttpClient client = new OkHttpClient();
    public MediaType type = MediaType.parse("application/json;charset=utf-8");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_info);
        controller();
    }

    public void controller() {
        Button sendButton = (Button) findViewById(R.id.nameSend);
        final EditText userInput = (EditText) findViewById(R.id.nameBox1);
        //Set on click Listener for sendButton, if it is clicked, get the value of editText, pass it to method that uses it
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get userInput
                Editable userE1 = userInput.getText();
                String userName = userE1.toString();
                //Pass it to method
                    String JSONUserString = buildJson(userName);
                    queryDatabase(JSONUserString);
            }
        });
    }

    public String buildJson(String data) {
        JSONStringer builder = new JSONStringer();
        try {
            builder.object();
            builder.key("userName");
            builder.value(data);
            builder.endObject();
            return builder.toString();
        }
        catch(JSONException e) {
            throw new RuntimeException();
        }
    }

    public void queryDatabase(String postBody) {
        RequestBody body = RequestBody.create(type,postBody);
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url("http://codeyourweb.net/httpTest/index.php/select");
        requestBuilder.post(body);
        Request readyRequest = requestBuilder.build();
        Call newCall = client.newCall(readyRequest);
        newCall.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                //Handle failure
            }

            @Override
            public void onResponse(Response response) throws IOException {
                //Get the response as a string
                String stringResponse = getResponse(response);
                try {
                    JSONObject jsonString = new JSONObject(stringResponse);
                        String userString1 = jsonString.getString("userString");
                        String priority = jsonString.getString("priority");
                    setView(userString1,priority);
                    setNotification(stringResponse);
                }catch (JSONException e) {
                    setView("No results found!");
                }
            }
        });
    }

    public String getResponse(Response serverResponse) throws IOException {
        ResponseBody body = serverResponse.body();
        String responseString = body.string();
        return responseString;
    }
    public void setView(final String input,final String priority) {
        final TextView responseView1 = (TextView) findViewById(R.id.responseBox1);
        final TextView priorityTestBox = (TextView) findViewById(R.id.priorityBox);
        responseView1.post(new Runnable() {
            @Override
            public void run() {
                responseView1.setText(input);
                priorityTestBox.setText(priority);
                switch (priority) {
                    case "0" :
                        responseView1.setBackgroundColor(Color.parseColor("#3399FF"));
                        break;
                    case "1" :
                        responseView1.setBackgroundColor(Color.parseColor("#6CC150"));
                        break;
                    case "2" :
                        responseView1.setBackgroundColor(Color.parseColor("#666699"));
                        break;
                    default :
                        responseView1.setBackgroundColor(Color.TRANSPARENT);
                }
            }
        });
    }

    public void setView(final String message) {
        final TextView responseView1 = (TextView) findViewById(R.id.responseBox1);
        responseView1.post(new Runnable() {
            @Override
            public void run() {
                responseView1.setText(message);
            }
        });
    }


    public void setNotification(String input) {
        Color LEDColor = new Color();
            LEDColor.parseColor("white");
        NotificationCompat.Builder NBuilder = new NotificationCompat.Builder(getApplicationContext());
        NBuilder.setSmallIcon(R.drawable.attention_white_50);
        NBuilder.setContentTitle(input);
        NBuilder.setContentText("The result");
        NBuilder.setTicker("Results!");
        NBuilder.setLights(0xff00ff00,500,500);

        Notification FinalNotification = NBuilder.build();
        FinalNotification.flags |= Notification.FLAG_SHOW_LIGHTS;

        NotificationManager NManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NManager.notify(1,FinalNotification);
    }



}
