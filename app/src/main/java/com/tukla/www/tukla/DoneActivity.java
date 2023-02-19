package com.tukla.www.tukla;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class DoneActivity extends AppCompatActivity implements Serializable {

    private int selectedRating = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done);

        //CircleImageView imageView = findViewById(R.id.profile_image);
        TextView name = findViewById(R.id.name);
        TextView plateNumber = findViewById(R.id.plateNumberDone);
        TextView locationStart = findViewById(R.id.locationStart);
        TextView locationEnd = findViewById(R.id.locationEnd);
        TextView distanceDone = findViewById(R.id.distanceDone);
        TextView paymentPassenger = findViewById(R.id.paymentPassenger);
        Button btnConfirm = findViewById(R.id.btnConfirm);
        LinearLayout passengerLayout = findViewById(R.id.for_passenger_feedback);
        EditText feedback = findViewById(R.id.passenger_feedback);

        //Session thisSession = (Session) getIntent().getSerializableExtra("SESSION");
        String bookingID = (String) getIntent().getSerializableExtra("BOOKING_ID");
        String role = (String) getIntent().getSerializableExtra("ROLE");

        //FirebaseAuth mAuth = FirebaseAuth.getInstance();
        LinearLayout starRatingLayout = findViewById(R.id.star_rating_layout);
        if(role.equals("DRIVER")) {
            starRatingLayout.setVisibility(View.GONE);
        } else {
            ImageView star1 = findViewById(R.id.star1);
            ImageView star2 = findViewById(R.id.star2);
            ImageView star3 = findViewById(R.id.star3);
            ImageView star4 = findViewById(R.id.star4);
            ImageView star5 = findViewById(R.id.star5);

            star1.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        star1.setImageResource(R.drawable.ic_star);
                        star2.setImageResource(R.drawable.ic_star_border);
                        star3.setImageResource(R.drawable.ic_star_border);
                        star4.setImageResource(R.drawable.ic_star_border);
                        star5.setImageResource(R.drawable.ic_star_border);
                        selectedRating = 1;
                    }
                    return true;
                }
            });

            star2.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        star1.setImageResource(R.drawable.ic_star);
                        star2.setImageResource(R.drawable.ic_star);
                        star3.setImageResource(R.drawable.ic_star_border);
                        star4.setImageResource(R.drawable.ic_star_border);
                        star5.setImageResource(R.drawable.ic_star_border);
                        selectedRating = 2;
                    }
                    return true;
                }
            });

            star3.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        star1.setImageResource(R.drawable.ic_star);
                        star2.setImageResource(R.drawable.ic_star);
                        star3.setImageResource(R.drawable.ic_star);
                        star4.setImageResource(R.drawable.ic_star_border);
                        star5.setImageResource(R.drawable.ic_star_border);
                        selectedRating = 3;
                    }
                    return true;
                }
            });

            star4.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        star1.setImageResource(R.drawable.ic_star);
                        star2.setImageResource(R.drawable.ic_star);
                        star3.setImageResource(R.drawable.ic_star);
                        star4.setImageResource(R.drawable.ic_star);
                        star5.setImageResource(R.drawable.ic_star_border);
                        selectedRating = 4;
                    }
                    return true;
                }
            });

            star5.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        star1.setImageResource(R.drawable.ic_star);
                        star2.setImageResource(R.drawable.ic_star);
                        star3.setImageResource(R.drawable.ic_star);
                        star4.setImageResource(R.drawable.ic_star);
                        star5.setImageResource(R.drawable.ic_star);
                        selectedRating = 5;
                    }
                    return true;
                }
            });
        }



        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference().child("bookings").child(bookingID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Booking booking = dataSnapshot.getValue(Booking.class);

                database.getReference().child("sessions").child(booking.getBookingID()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot sDataSnapshot) {
                        if(sDataSnapshot.exists()) {
                            Session ssd = sDataSnapshot.getValue(Session.class);
                            if(role.equals("DRIVER")) {
                                feedback.setVisibility(View.GONE);
                                btnConfirm.setVisibility(View.VISIBLE);
                                name.setText(booking.getUser().getFullname());
                            }
                            else {

                                passengerLayout.setVisibility(View.VISIBLE);
                                btnConfirm.setVisibility(View.VISIBLE);
                                AlertDialog.Builder builder = new AlertDialog.Builder(DoneActivity.this);
                                builder.setTitle("Thank you for riding with us!");
                                builder.setMessage("Please give a feedback about your trip and your driver " + booking.getDriver().getFullname());
                                AlertDialog dialog = builder.create();
                                dialog.show();
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Do something when the OK button is clicked
                                        dialog.dismiss();
                                    }
                                });
                                paymentPassenger.setText(booking.getFare()+"");
                                name.setText(booking.getDriver().getFullname());
                                plateNumber.setVisibility(View.VISIBLE);
                                plateNumber.setText(booking.getDriver().getDriver().getPlateNumber());
                            }
                            locationStart.setText(booking.getOriginText());
                            locationEnd.setText(booking.getDestinationText());
                            distanceDone.setText(booking.getDistance()+" KM");

                            btnConfirm.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent;
                                    if(role.equals("DRIVER")) {
                                        Session ss = new Session(booking.getDriver(),booking,LocalDateTime.now().toString(),ssd.getDriverLocation(),true,true,false,false,false,true);
                                        FirebaseDatabase database = FirebaseDatabase.getInstance();

                                        database.getReference().child("history")
                                                .child(booking.getBookingID()).setValue(
                                                new History(
                                                        ss,
                                                        Double.parseDouble(paymentPassenger.getText().toString()),
                                                        LocalDateTime.now().toString()
                                                )
                                        );
                                        //FirebaseDatabase.getInstance().getReference("sessions").child(sessionSnapshot.getKey()).removeValue();
                                        //FirebaseDatabase.getInstance().getReference("bookings").child(bookingID).removeValue();
                                        intent = new Intent(DoneActivity.this,DriverActivity.class);
                                        finish();
                                        startActivity(intent);
                                    } else {
                                        Map<String, Object> feedbackUpdates = new HashMap<>();
                                        feedbackUpdates.put("feedback", feedback.getText().toString());
                                        feedbackUpdates.put("updatedAt", LocalDateTime.now().toString());
                                        feedbackUpdates.put("rating", selectedRating);
                                        database.getReference("feedbacks").child(booking.getBookingID()).updateChildren(feedbackUpdates);

                                        //FirebaseDatabase.getInstance().getReference("bookings").child(bookingID).removeValue();
                                        intent = new Intent(DoneActivity.this,MainActivity.class);
                                        finish();
                                        startActivity(intent);
                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}