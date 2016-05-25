package com.example.awesometic.facetalk;

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
                        if (maleRadioButton.isChecked() || femaleRadioButton.isChecked()) {
                            email = emailInput.getText().toString();
                            password = passwordInput.getText().toString();
                            nickname = nicknameInput.getText().toString();
                            age = Integer.parseInt(ageInput.getText().toString());
                            if (maleRadioButton.isChecked())
                                gender = maleRadioButton.getText().toString();
                            else
                                gender = femaleRadioButton.getText().toString();
                        } else {
                            Toast.makeText(SignupActivity.this, "Check your gender", Toast.LENGTH_LONG).show();
                            break;
                        }

                        int resultCode = dbConn.addUser(email, password, nickname, age, gender);
                        if (resultCode == 1) {
                            Toast.makeText(SignupActivity.this, "Register success! Login your account", Toast.LENGTH_LONG).show();
                        } else if (resultCode == -1 || resultCode == -9) {
                            Toast.makeText(SignupActivity.this, "Register fail!", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(SignupActivity.this, "Something wrong.. resultCode: " + resultCode, Toast.LENGTH_LONG).show();
                        }
                        break;
                }
            }
        });
    }
}
