package com.tukla.www.tukla;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SignUpLanding extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_landing);

        Button btnSignupDriver = findViewById(R.id.btnSignUpDriver2);
        Button btnSignupPassenger = findViewById(R.id.btnSignUpPassenger2);
        Button btnBack = findViewById(R.id.btnBumalik);

       btnSignupDriver.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(SignUpLanding.this, SignUpDriver.class);
               startActivity(intent);
               finish();
           }
       });

        btnSignupPassenger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpLanding.this, SignUp.class);
                startActivity(intent);
                finish();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpLanding.this, LandingActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}