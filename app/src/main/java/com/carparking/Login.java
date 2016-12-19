package com.carparking;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class Login extends AppCompatActivity implements View.OnClickListener {


    EditText username,password;
    Button login;
    String status;
    TextView signup;
    UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        login = (Button)findViewById(R.id.login);
        signup = (TextView)findViewById(R.id.signuptext);
        signup.setOnClickListener(this);
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == login) {
            new Util().hideSoftKeyboard(this);
            if (new Util().check_connection(this)) {
                String name = username.getText().toString();
                String pass = password.getText().toString();
                if (new Util().emptyvalidate(name, pass)) {
                    validatecredentials(name, pass);
                } else {
                    if (username.getText().toString().trim().equals("")) {
                        YoYo.with(Techniques.Pulse)
                                .duration(700)
                                .playOn(findViewById(R.id.username));
                    }
                    if (password.getText().toString().trim().equals("")) {
                        YoYo.with(Techniques.Pulse)
                                .duration(700)
                                .playOn(findViewById(R.id.password));
                    }
                }
            }
        }else  if(v == signup){
                Intent in = new Intent(Login.this, SignUp.class);
                startActivity(in);
            }else{
                new Util().checkinternet(Login.this);
            }
        }


    void validatecredentials(final String username, String password){

        Map<String, String> params = new HashMap<String, String>();
        params.put("username",username);
        params.put("password",password);



        new Util().loginvalidate(params, this, new GetResult() {


            @Override
            public void done(JSONObject jsonObject) {
                if (jsonObject != null) {

                    try {
                        status = jsonObject.getString("status");

                        if(status.equals("success")){
                                userLocalStore = new UserLocalStore(getApplicationContext());
                                userLocalStore.setUserloggedIn(true);
                                Intent in = new Intent(Login.this, SlotBooking.class);
                                startActivity(in);

                        }else{

                            new Util().showerrormessage(Login.this,"Either wrong ID or password or not Signed Up !!!!");
                            userLocalStore = new UserLocalStore(getApplicationContext());
                            userLocalStore.clearUserdata();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }
        });
    }


    @Override
    public void onBackPressed() {

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        super.onBackPressed();

    }


    @Override
    protected void onResume() {

        UserLocalStore userLocalStore;
        userLocalStore = new UserLocalStore(this);
        super.onResume();
        if (userLocalStore.getuserloggedIn())
        {
                startActivity(new Intent(this,SlotBooking.class));

        }

    }

}

