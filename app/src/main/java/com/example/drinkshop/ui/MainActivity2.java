package com.example.drinkshop.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.drinkshop.R;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;

import org.json.JSONObject;

public class MainActivity2 extends AppCompatActivity {
    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        callbackManager = CallbackManager.Factory.create();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        //you call callbackmanager.onActivityResult to pass the login results to the login manager throw call back manager
        callbackManager.onActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);

        //Graph API
        GraphRequest graphRequest = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.d("Demo",object.toString());

//                        try {
//
//                            String name = object.getString("name");
//                            String id = object.getString("id");
//                            nameProfile.setText(name);
//                            Picasso.get().load("https://graph.facebook.com/"+id+"/picture?type=large").into(imageProfile);
//                            Log.d("image",imageProfile.toString());
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }

                    }
                });
        Bundle bundle = new Bundle();
        bundle.putString("fields","gender, name, id, first_name, last_name");
        graphRequest.setParameters(bundle);
        graphRequest.executeAsync();
    }
    AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if (currentAccessToken ==null){
                LoginManager.getInstance().logOut();
                //  nameProfile.setText("");
                // imageProfile.setImageResource(0);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
    }
}