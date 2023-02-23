package com.tukla.www.tukla;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SignUp extends AppCompatActivity {

    private EditText eFullName;
    private EditText eAddress;
    private EditText ePhoneNumber;
    private ImageView imgViewId;
    private EditText email_id;
    private EditText mpassword;
    private EditText mconfirmpassword;
    private Button mButton;
    private FirebaseAuth mAuth;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Bitmap IDBitmap;
    private Boolean isWithImg = false;
    private EditText eMiddleName;
    private EditText eLastName;
    private AutoCompleteTextView eCategory;
    private List<String> categoriesList = new ArrayList<>();
    private static Boolean isRegistered;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth=FirebaseAuth.getInstance();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        eFullName=findViewById(R.id.eFullName);
        eAddress=findViewById(R.id.eAddress);
        ePhoneNumber=findViewById(R.id.ePhoneNumber);
        imgViewId=findViewById(R.id.image_view);
        email_id=findViewById(R.id.eEmail);
        mpassword=findViewById(R.id.mpassword);
        mconfirmpassword=findViewById(R.id.mConfirmPassword);
        mButton=findViewById(R.id.button2);
        eMiddleName=findViewById(R.id.eMiddleName);
        eLastName=findViewById(R.id.eLastName);
        CheckBox showHideCheckBox1 = findViewById(R.id.show_hide_checkbox_1);
        CheckBox showHideCheckBox2 = findViewById(R.id.show_hide_checkbox_2);
        eCategory = findViewById(R.id.eCategory);
        Button buttonBack = findViewById(R.id.button3);

        eCategory.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                eCategory.showDropDown();
            }
        });

        eCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eCategory.showDropDown();
            }
        });

        firebaseDatabase.getReference("categories").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                categoriesList.clear();
                for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
                    String categoryName = categorySnapshot.child("category").getValue(String.class);
                    categoriesList.add(categoryName);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(SignUp.this, android.R.layout.simple_dropdown_item_1line, categoriesList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                eCategory.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        View dialogView = getLayoutInflater().inflate(R.layout.terms_conditions, null);

        TextView tncText = dialogView.findViewById(R.id.tncText);
        firebaseDatabase.getReference("termsList").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tncText.setText("");
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Term term = snapshot.getValue(Term.class);

                    String prevTxt = tncText.getText().toString();
                    String newTxt = prevTxt + term.getOrdder() + "." + term.getTerm()+"\n";
                    tncText.setText(newTxt);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        AlertDialog.Builder termsDialogBuilder = new AlertDialog.Builder(SignUp.this);
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

        Button declineTerms = dialogView.findViewById(R.id.btnTermsCancel);
        declineTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUp.this, SignUpLanding.class);
                startActivity(intent);
                finish();
            }
        });


        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUp.this, SignUpLanding.class);
                startActivity(intent);
                finish();
            }
        });

        showHideCheckBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // show password
                    mpassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    // hide password
                    mpassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                // move cursor to end of text
                mpassword.setSelection(mpassword.length());
            }
        });

        showHideCheckBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // show password
                    mconfirmpassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    // hide password
                    mconfirmpassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                // move cursor to end of text
                mconfirmpassword.setSelection(mconfirmpassword.length());
            }
        });
        LinearLayout linearLayout1 = findViewById(R.id.btnSignUpLinearLayout1);
        LinearLayout linearLayout2 = findViewById(R.id.btnSignUpLinearLayout2);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkEmailPassword(email_id.getText().toString(),mpassword.getText().toString())) {
                    FirebaseAuth mAuth = FirebaseAuth.getInstance();

                    mAuth.fetchSignInMethodsForEmail(email_id.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                                @Override
                                public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                                    if (task.isSuccessful()) {
                                        SignInMethodQueryResult result = task.getResult();
                                        if (result != null && result.getSignInMethods() != null && result.getSignInMethods().size() > 0) {
                                            // Email address is already registered
                                            email_id.requestFocus();
                                            email_id.setError("Email already exists!");
                                        } else {
                                            linearLayout1.setVisibility(View.GONE);
                                            linearLayout2.setVisibility(View.VISIBLE);
                                        }
                                    }
                                }
                            });
                    mAuth.signOut();
                }
            }
        });

        imgViewId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickImageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickImageIntent, PICK_IMAGE_REQUEST);
            }
        });

        Button bntSubmit = findViewById(R.id.button4);
        bntSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mpassword.getText().toString().length()<6)
                {
                    mpassword.requestFocus();
                    mpassword.setError("Password must be at least 6 characters long");
                }

                else if(!mconfirmpassword.getText().toString().equals(mpassword.getText().toString()))
                {
                    mconfirmpassword.requestFocus();
                    mconfirmpassword.setError("Password does not match");

                }
                else {
                    registerUser(email_id.getText().toString(),mpassword.getText().toString());
                }
            }
        });

    }

    private boolean checkEmailPassword(String email, String password) {
        email_id.setError(null);
        mpassword.setError(null);


            if(!isWithImg)
            {
                Toast.makeText(getApplicationContext(), "Profile picture is required!", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
                builder.setTitle("Profile picture is required!");
                builder.setMessage("Please upload an image of you.");
                AlertDialog dialog = builder.create();
                dialog.show();
            } else if(eFullName.getText().toString().equals(""))
            {
                eFullName.requestFocus();
                eFullName.setError("Enter First Name");
            } else if(eLastName.getText().toString().equals(""))
            {
                eLastName.requestFocus();
                eLastName.setError("Enter Last Name");
            }
            else if(eAddress.getText().toString().equals(""))
            {
                eAddress.requestFocus();
                eAddress.setError("Address cannot be empty");
            } else if(ePhoneNumber.getText().toString().equals(""))
            {
                ePhoneNumber.requestFocus();
                ePhoneNumber.setError("Phone Number cannot be empty");
            }
            else if(ePhoneNumber.getText().toString().length()>11)
            {
                ePhoneNumber.requestFocus();
                ePhoneNumber.setError("Phone Number must be 11 numbers long only");
            }
            else if(!email.contains("@"))
            {
                email_id.requestFocus();
                email_id.setError("Invalid Email");
//            } else if(!checkEmail(email_id.getText().toString())) {
//                email_id.requestFocus();
//                email_id.setError("Email already exists!");
            } else if(!categoriesList.contains(eCategory.getText().toString())) {
                eCategory.requestFocus();
                eCategory.setError("Select from category list");
            } else
                return true;

            return false;

    }

    private void registerUser(String email,String password) {
        final ProgressDialog dialog = ProgressDialog.show(SignUp.this, "",
                "Loading. Please wait...", true);
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            dialog.dismiss();
                            showErrorDailog("Successfully Registered. Please Login");

                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            updateDatabase();
                            sendEmailVerification(firebaseUser);
                            Intent intent =new Intent(SignUp.this,Login.class);
                            finish();
                            startActivity(intent);
                            //UpdateUI(firebaseUser);
                        } else {
                            dialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Unsuccessful registration", Toast.LENGTH_SHORT)
                                    .show();
                            UpdateUI(null);
                        }
                    }
                });

    }

    private void sendEmailVerification(FirebaseUser user) {
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Email verification sent successfully
                            Toast.makeText(SignUp.this, "Email verification sent.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // Email verification sending failed
                            Toast.makeText(SignUp.this, "Failed to send email verification.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void updateDatabase() {

        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        myRef=myRef.child("users");

        try {
            uploadFirebase(IDBitmap);
        } catch (Exception e) {

        } finally {
            User user = new User(mAuth.getUid(),eFullName.getText().toString(),eMiddleName.getText().toString(),eLastName.getText().toString(),eAddress.getText().toString(),ePhoneNumber.getText().toString(),false, false, LocalDateTime.now().toString(),false,null,false,eCategory.getText().toString());
            myRef.child(mAuth.getUid()).setValue(user);
        }

    }

    private  void showErrorDailog(String message)
    {
        new android.support.v7.app.AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent =new Intent(SignUp.this,Login.class);
                        finish();
                        startActivity(intent);
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
    private void UpdateUI(Object o) {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //getting bitmap object from uri
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //setting the image to imageview
                imgViewId.setImageBitmap(bitmap);
                // uploading the image to firebase
                //uploadFirebase(bitmap);
                IDBitmap = bitmap;
                isWithImg = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadFirebase(Bitmap bitmap) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        String userId =  mAuth.getCurrentUser().getUid();
        // Create a reference to "images/userId.jpg"
        StorageReference imageRef = storageRef.child("images/"+userId+".jpg");

        // Convert the bitmap to a byte array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        // Upload the image to Firebase Storage
        UploadTask uploadTask = imageRef.putBytes(data);

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Log.d("Exception Error",exception.getMessage());
                Toast.makeText(getBaseContext(), "Error in Registration! Try again!", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
//                Uri downloadUrl = taskSnapshot.getMetadata().getReference().getDownloadUrl().getResult();
//                String imageUrl = downloadUrl.toString();
//                // you can now set the imageUrl to the user object
//                user.setImageUrl(imageUrl);
//                // now you can save the user object with the imageUrl to firebase
//                FirebaseDatabase database = FirebaseDatabase.getInstance();
//                DatabaseReference myRef = database.getReference("Users").child("UserId");
//                String key = myRef.push().getKey();
//                myRef.child(key).setValue(user);
            }
        });
    }
    @Override
    public void onBackPressed()
    {
        // code here to show dialog
        super.onBackPressed();  // optional depending on your needs
        Intent intent =new Intent(SignUp.this,Login.class);
        finish();
        startActivity(intent);
    }

}
