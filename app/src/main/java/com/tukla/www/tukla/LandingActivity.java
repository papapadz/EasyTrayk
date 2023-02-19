package com.tukla.www.tukla;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LandingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        Button btnLandingLogin = findViewById(R.id.btnLandingLogin);
        btnLandingLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LandingActivity.this, Login.class);
                startActivity(intent);
                finish();
            }
        });

        TextView txtLandingClickRegister = findViewById(R.id.txtLandingClickRegister);
        txtLandingClickRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LandingActivity.this, SignUpLanding.class);
                startActivity(intent);
                finish();
            }
        });

        TextView acceptTerms = findViewById(R.id.txtAcceptTermsClick);
        acceptTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View dialogView = getLayoutInflater().inflate(R.layout.terms_conditions, null);

                AlertDialog.Builder termsDialogBuilder = new AlertDialog.Builder(LandingActivity.this);
                termsDialogBuilder.setView(dialogView);

                AlertDialog termsDialog = termsDialogBuilder.create();
                termsDialog.show();

                Button acceptTerms = dialogView.findViewById(R.id.btnTermsAccept);
                acceptTerms.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        termsDialog.dismiss();
                    }
                });
            }
        });
    }
}