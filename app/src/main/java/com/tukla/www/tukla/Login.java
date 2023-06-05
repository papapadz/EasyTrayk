package com.tukla.www.tukla;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Random;

public class Login extends AppCompatActivity implements Serializable {

    private FirebaseAuth mAuth;
    private Button Signup;
    private Button Login;
    private EditText email;
    private EditText password;
    private Button SignupDriver;
    private CheckBox showHideCheckBox1;
    private LinearLayout linearLayout1;
//    private LinearLayout linearLayout2;
//    private TextView otp1;
//    private TextView otp2;
//    private TextView otp3;
//    private TextView otp4;
//    private TextView resendOTP;
//    private String otp;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        linearLayout1 = findViewById(R.id.linearLayoutLogin1);
        //linearLayout2 = findViewById(R.id.linearLayoutLogin2);
        Signup = findViewById(R.id.buttonsignup);
        SignupDriver = findViewById(R.id.buttonsignup2);
        Login = findViewById(R.id.buttonlogin);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        mAuth = FirebaseAuth.getInstance();
        showHideCheckBox1 = findViewById(R.id.show_hide_checkbox_1);
        TextView forgotPassword = findViewById(R.id.forgotPassword);
        //otp1 = findViewById(R.id.otp1);
        //otp2 = findViewById(R.id.otp2);
        //otp3 = findViewById(R.id.otp3);
        //otp4 = findViewById(R.id.otp4);
        //resendOTP = findViewById(R.id.resendOTP);
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(Login.this);
                dialog.setContentView(R.layout.forgot_password_layout);

                AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                // Get references to the views in the chat dialog box
                EditText editText = dialog.findViewById(R.id.forgotEmail);
                Button sendButton = dialog.findViewById(R.id.btnForgotSend);

                sendButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(editText.getText().toString().length()>1 && editText.getText().toString().contains("@")) {
                            FirebaseAuth.getInstance().sendPasswordResetEmail(editText.getText().toString())
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                // Password reset email sent successfully
                                                Toast.makeText(Login.this, "Password reset email sent", Toast.LENGTH_SHORT).show();
                                            } else {
                                                // Password reset email failed to send
                                                Toast.makeText(Login.this, "Failed to send password reset email", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                            dialog.dismiss();
                        } else {
                            editText.setError("Check Email");
                            editText.requestFocus();
                        }

                    }
                });
                dialog.show();
            }
        });

        showHideCheckBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // show password
                    password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    // hide password
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                // move cursor to end of text
                password.setSelection(password.length());
            }
        });

        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, SignUp.class);
                finish();
                startActivity(intent);
            }
        });

        SignupDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, SignUpDriver.class);
                finish();
                startActivity(intent);
            }
        });

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptlogin();
            }
        });

        Button backBtn = findViewById(R.id.buttonloginback);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, LandingActivity.class);
                finish();
                startActivity(intent);
            }
        });

        //Button btnOtp = findViewById(R.id.btnEnterOTP);
//        btnOtp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                String myOtp = otp1.getText().toString()+otp2.getText().toString()+otp3.getText().toString()+otp4.getText().toString();
//
//                if(otp.contains(myOtp)) {
//                    FirebaseDatabase.getInstance().getReference("otps").child(mAuth.getUid()).removeValue();
//                    if(user.getIsDriver()) {
//                        Intent intent = new Intent(Login.this, DriverActivity.class);
//                        //finish();
//                        startActivity(intent);
//                    } else {
//                        Intent intent = new Intent(Login.this, MainActivity.class);
//                        //finish();
//                        startActivity(intent);
//                    }
//                } else {
//                    Toast.makeText(getApplicationContext(), "OTP is incorrect!", Toast.LENGTH_SHORT)
//                            .show();
//                }
//            }
//        });

//        resendOTP.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Random rand = new Random();
//                HashMap<String, Object> hashMap = new HashMap<>();
//                hashMap.put("email",email.getText().toString());
//                otp = String.format("%04d%n", rand.nextInt(10000));
//                hashMap.put("otp",otp);
//                FirebaseDatabase.getInstance().getReference().child("otps").child(mAuth.getUid()).updateChildren(hashMap);
//
//                final ProgressDialog pdialog = ProgressDialog.show(Login.this, "",
//                        "Loading. Please wait...", true);
//
//                String url = "https://sendmail.binarybee.org/sendMail.php?email=" + email.getText().toString() + "&otp=" + otp;
//                RequestQueue queue = Volley.newRequestQueue(Login.this);
//                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                        new Response.Listener<String>() {
//                            @Override
//                            public void onResponse(String response) {
//                                pdialog.dismiss();
//                                Toast.makeText(Login.this, "OTP sent to your email",
//                                        Toast.LENGTH_SHORT).show();
//                            }
//                        }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        pdialog.dismiss();
//                        Toast.makeText(Login.this, "Error sending OTP",
//                                Toast.LENGTH_SHORT).show();
//                    }
//                });
//                queue.add(stringRequest);
//            }
//        });
    }
    @Override
    public void onBackPressed() {

        if(linearLayout1.getVisibility()==View.VISIBLE) {
            Intent intent = new Intent(Login.this, LandingActivity.class);
            finish();
            startActivity(intent);
        } else {
            Intent intent = new Intent(Login.this, Login.class);
            finish();
            startActivity(intent);
        }

    }

    private void attemptlogin() {
        String emailId = email.getText().toString();
        String password1 = password.getText().toString();
        if(checkEmailPassword(emailId,password1)) {
            Login(emailId, password1);
        }

    }


    public boolean checkEmailPassword(String Email, String Password) {
        Log.d("Vehicle","email:"+email);
        Log.d("Vehicle","password:"+password);
        email.setError(null);
        password.setError(null);
        if(!Email.contains("@"))
        {
            email.requestFocus();
            email.setError("INVALID EMAIL");
        }
        else{

            if(Password.length()<8)
            {
                password.requestFocus();
                password.setError("Incorrect Pasword");
            }
            else{
                return true;
            }

        }
        return false;
    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        UpdateUI(currentUser);

    }

    private void UpdateUI(FirebaseUser currentUser) {
    }

    private void Login(String email, String password) {
        final ProgressDialog dialog = ProgressDialog.show(Login.this, "",
                "Loggin in. Please wait...", true);
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            //FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            //UpdateUI(firebaseUser);
                            FirebaseDatabase database=FirebaseDatabase.getInstance();
                            DatabaseReference userRef = database.getReference("users").child(mAuth.getUid());
                            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Intent intent;
                                    user = dataSnapshot.getValue(User.class);
                                    if(user.getIsAdmin()) {
                                        intent = new Intent(Login.this, AdminActivity.class);
                                        finish();
                                        startActivity(intent);
                                    } else if(user.getIsRejected()) {
                                        LinearLayout loginlinear = findViewById(R.id.linearLayoutLogin1);
                                        LinearLayout suspendedLinear = findViewById(R.id.loginSuspended);
                                        loginlinear.setVisibility(View.GONE);
                                        suspendedLinear.setVisibility(View.VISIBLE);
                                    } else if(user.getIsVerified()) {

                                        if(user.getIsDriver()) {
                                            intent = new Intent(Login.this, DriverActivity.class);
                                            //finish();
                                            startActivity(intent);
                                        } else {
                                            intent = new Intent(Login.this, MainActivity.class);
                                            //finish();
                                            startActivity(intent);
                                        }

//                                        database.getReference("otps").child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
//                                            @Override
//                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                                if(dataSnapshot.exists()) {
//                                                    final ProgressDialog pdialog = ProgressDialog.show(Login.this, "",
//                                                            "Loading. Please wait...", true);
//                                                    String email = dataSnapshot.child("email").getValue(String.class);
//                                                    otp = dataSnapshot.child("otp").getValue(String.class);
//
//                                                    String url = "https://sendmail.binarybee.org/sendMail.php?email=" + email + "&otp=" + otp;
//                                                    RequestQueue queue = Volley.newRequestQueue(Login.this);
//                                                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                                                            new Response.Listener<String>() {
//                                                                @Override
//                                                                public void onResponse(String response) {
//                                                                    pdialog.dismiss();
//                                                                    linearLayout1.setVisibility(View.GONE);
//                                                                    linearLayout2.setVisibility(View.VISIBLE);
//                                                                }
//                                                            }, new Response.ErrorListener() {
//                                                        @Override
//                                                        public void onErrorResponse(VolleyError error) {
//                                                            pdialog.dismiss();
//                                                            Toast.makeText(Login.this, "Error sending OTP",
//                                                                    Toast.LENGTH_SHORT).show();
//                                                        }
//                                                    });
//                                                    queue.add(stringRequest);
//                                                } else {
//
//                                                }
//                                            }
//
//                                            @Override
//                                            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                            }
//                                        });


                                    } else {
                                        FirebaseAuth.getInstance().signOut();
                                        Toast.makeText(getApplicationContext(), "You are not verified!", Toast.LENGTH_SHORT)
                                                .show();
                                    }
                                    dialog.dismiss();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    dialog.dismiss();
                                }
                            });

                        } else {
                            Toast.makeText(getApplicationContext(), "Email or password is incorrect!", Toast.LENGTH_SHORT)
                                    .show();
                            UpdateUI(null);
                            dialog.dismiss();
                        }
                    }
                });

    }
}