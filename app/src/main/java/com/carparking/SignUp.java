package com.carparking;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SignUp extends AppCompatActivity implements View.OnClickListener{

    EditText username,password,name,mobile;
    Button login;
    String status;
    UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_up);

        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        name = (EditText)findViewById(R.id.name);
        mobile = (EditText)findViewById(R.id.mobile);

        login = (Button)findViewById(R.id.login);
        login.setOnClickListener(this);


    }


    @Override
    public void onClick(View view) {
        if(view == login){
            new Util().hideSoftKeyboard(this);
            if (new Util().check_connection(this)){
                String names = username.getText().toString();
                String pass = password.getText().toString();
                String nam  = name.getText().toString();
                String mob  = mobile.getText().toString();
                if (new Util().emptyvalidatethree(names,pass,nam,mob)) {
                    validatecredentials(names, pass,nam,mob);
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
            }else{
                new Util().checkinternet(SignUp.this);
            }
        }
    }

    void validatecredentials(final String username, String password,String name,String mobile){

        Map<String, String> params = new HashMap<String, String>();
        params.put("username",username);
        params.put("password",password);
        params.put("name",name);
        params.put("mobile_no",mobile);


        new Util().signup(params, this, new GetResult() {


            @Override
            public void done(JSONObject jsonObject) {
                if (jsonObject != null) {

                    try {
                        status = jsonObject.getString("status");

                        if(status.equals("success")){
                            new Util().showSuccessmessage(SignUp.this,"Successfully Siged Up Login Now !!!!");
                            Intent in = new Intent(SignUp.this, Login.class);
                            startActivity(in);

                        }else{

                            new Util().showerrormessage(SignUp.this,"Something Went Wrong Try again !!!!");
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
