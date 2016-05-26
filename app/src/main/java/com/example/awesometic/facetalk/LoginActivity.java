package com.example.awesometic.facetalk;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    final static String LogTag = "Awe_LoginActivity";
    private Singleton single = Singleton.getInstance();
    private DBConnect dbConn = new DBConnect(LoginActivity.this, LoginActivity.this);

    public static Activity loginActivity;

    EditText emailInput, passwordInput;
    Button loginButton, signupButton;
    CheckBox autoLoginCheckBox;
    Boolean autoLoginChecked;
    SharedPreferences pref;
    SharedPreferences.Editor prefEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginActivity = LoginActivity.this;

        emailInput = (EditText) findViewById(R.id.login_emailInput);
        passwordInput = (EditText) findViewById(R.id.login_passwordInput);
        loginButton = (Button) findViewById(R.id.login_loginButton);
        signupButton = (Button) findViewById(R.id.login_signupButton);
        autoLoginCheckBox = (CheckBox) findViewById(R.id.autoLoginCheckBox);
        pref = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
        prefEditor = pref.edit();

        autoLoginChecked = false;

        loginButton.setOnClickListener(mClickListener);
        signupButton.setOnClickListener(mClickListener);
        autoLoginCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    autoLoginChecked = true;
                } else {
                    autoLoginChecked = false;
                    prefEditor.putString("email", "");
                    prefEditor.putString("password", "");
                    prefEditor.putBoolean("autoLogin", false);
                    prefEditor.apply();
                }
            }
        });

        // If success sign up, auto fill out the email EditText
        Intent intent = getIntent();
        String intentExtra_email = intent.getStringExtra("signup_email");
        emailInput.setText(intentExtra_email);

        // If autoLogin checked, get user login information
        if (pref.getBoolean("autoLogin", true)) {
            Log.d(LogTag, "email: " + pref.getString("email", "") + "\tpassword: " + pref.getString("password", ""));
            emailInput.setText(pref.getString("email", ""));
            passwordInput.setText(pref.getString("password", ""));
            autoLoginCheckBox.setChecked(true);
        }
    }

    Button.OnClickListener mClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.login_loginButton:
                    String email = emailInput.getText().toString();
                    String password = passwordInput.getText().toString();

                    if (email.length() == 0 || password.length() == 0) {
                        Toast.makeText(LoginActivity.this, "Type your email or password!", Toast.LENGTH_LONG).show();
                        break;
                    } else {

                        Boolean validation = loginValidate(email, password);

                        if (validation) {
                            if (autoLoginChecked) {
                                // If autoLogin checked, save values
                                prefEditor.putString("email", email);
                                prefEditor.putString("password", password);
                                prefEditor.putBoolean("autoLogin", true);
                                prefEditor.apply();
                            }

                            gotoMainActivity();
                            finish();
                        } else {
                            passwordInput.setText("");
                            autoLoginCheckBox.setChecked(false);
                        }
                    }
                    break;
                case R.id.login_signupButton:
                    gotoSignupActivity();

                    break;
            }
        }
    };

    private boolean loginValidate(String email, String password) {
        int useridx = dbConn.loginValidation(email, password);

        single.setCurrentUserIdx(useridx);
        single.setCurrentUserEmail(email);
        single.setCurrentUserNickname(dbConn.getNickname(useridx));
        Log.d(LogTag, "Current useridx: " + single.getCurrentUserIdx() + "\temail: " + single.getCurrentUserEmail() + "\tnickname: " + single.getCurrentUserNickname());

        if (useridx == -1) {
            Toast.makeText(LoginActivity.this, "Not a member? Sign-up", Toast.LENGTH_LONG).show();
            return false;

        } else if (useridx == 0) {
            Toast.makeText(LoginActivity.this, "Wrong password!", Toast.LENGTH_LONG).show();
            return false;

        } else if (useridx == -9){
            Toast.makeText(LoginActivity.this, "Something wrong...", Toast.LENGTH_LONG).show();
            return false;

        } else {
            Toast.makeText(LoginActivity.this, "Login Success!", Toast.LENGTH_LONG).show();
            return true;
        }
    }

    private void gotoMainActivity() {
        Log.d(LogTag, "gotoMainActivity()");
        Intent intent = new Intent(this.getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    private void gotoSignupActivity() {
        Log.d(LogTag, "gotoSignupActivity()");
        Intent intent = new Intent(this.getApplicationContext(), SignupActivity.class);
        startActivity(intent);
    }
}
