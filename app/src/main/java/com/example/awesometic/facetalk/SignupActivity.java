package com.example.awesometic.facetalk;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class SignupActivity extends AppCompatActivity {

    final static String LogTag = "Awe_SignupActivity";
    private Singleton single = Singleton.getInstance();
    private DBConnect dbConn = new DBConnect(SignupActivity.this, SignupActivity.this);

    EditText emailInput, passwordInput, nicknameInput, ageInput;
    RadioButton maleRadioButton, femaleRadioButton;
    Button signupButton;

    String email, password, nickname, gender;
    int age;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        emailInput = (EditText) findViewById(R.id.signup_emailInput);
        passwordInput = (EditText) findViewById(R.id.signup_passwordInput);
        nicknameInput = (EditText) findViewById(R.id.signup_nicknameInput);
        ageInput = (EditText) findViewById(R.id.signup_ageInput);
        maleRadioButton = (RadioButton) findViewById(R.id.signup_maleRadioButton);
        femaleRadioButton = (RadioButton) findViewById(R.id.signup_femaleRadioButton);
        signupButton = (Button) findViewById(R.id.signup_signupButton);

        signupButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.signup_signupButton:
                        email = emailInput.getText().toString();
                        password = passwordInput.getText().toString();
                        nickname = nicknameInput.getText().toString();
                        String str_age = ageInput.getText().toString();

                        if (email.length() == 0 || password.length() == 0 || nickname.length() == 0 || str_age.length() == 0) {
                            Toast.makeText(SignupActivity.this, "Fill out the form", Toast.LENGTH_LONG).show();
                            break;

                        } else if (!maleRadioButton.isChecked() && !femaleRadioButton.isChecked()) {
                            Toast.makeText(SignupActivity.this, "Check your gender", Toast.LENGTH_LONG).show();
                            break;

                        } else {
                            age = Integer.parseInt(str_age);
                            if (maleRadioButton.isChecked())
                                gender = maleRadioButton.getText().toString();
                            else
                                gender = femaleRadioButton.getText().toString();

                            int resultCode = dbConn.addUser(email, password, nickname, age, gender);
                            if (resultCode == 1) {
                                Toast.makeText(SignupActivity.this, "Register success! Login your account", Toast.LENGTH_LONG).show();
                            } else if (resultCode == -1 || resultCode == -9) {
                                Toast.makeText(SignupActivity.this, "Register fail!", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(SignupActivity.this, "Something wrong.. resultCode: " + resultCode, Toast.LENGTH_LONG).show();
                            }

                            LoginActivity loginActivity = (LoginActivity) LoginActivity.loginActivity;
                            loginActivity.finish();
                            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                            intent.putExtra("signup_email", email);
                            startActivity(intent);
                            finish();
                            break;
                        }
                }
            }
        });
    }
}
