package com.tukla.www.tukla;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class UserListAdapter extends BaseAdapter {

    private Context context;
    private List<User> data;
    private List<User> filteredUsers;

    public UserListAdapter(Context context, List<User> data) {
        this.context = context;
        this.data = data;
        this.filteredUsers = data;
    }

    @Override
    public int getCount() {
        if(filteredUsers.isEmpty())
            return 1;
        return filteredUsers.size();
    }

    @Override
    public Object getItem(int position) {
        return filteredUsers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (data.isEmpty()) {
            convertView = LayoutInflater.from(context).inflate(R.layout.empty_list, parent, false);
            return convertView;
        }

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.user_list, parent, false);
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();

        ImageView imageView = convertView.findViewById(R.id.imageView);
        TextView textViewName = convertView.findViewById(R.id.textViewName);
        TextView textViewAddress = convertView.findViewById(R.id.textViewAddress);
        TextView textViewPhone = convertView.findViewById(R.id.textViewPhone);

        Button button = convertView.findViewById(R.id.button);
        Button button2 = convertView.findViewById(R.id.button2);
        LinearLayout linearLayout = convertView.findViewById(R.id.layout_admin_userlist);

        User data = (User) filteredUsers.get(position);
        textViewName.setText("Name: "+data.getLastname()+", "+data.getFullname()+data.getLastname());
        textViewAddress.setText("Address: "+data.getAddress());
        textViewPhone.setText("Phone: "+data.getPhone());
        StorageReference imgRef = storageRef.child("images/" + data.getUserID() + ".jpg");
        imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(imageView.getContext())
                        .load(uri)
                        .fitCenter()
                        .into(imageView);
            }
        });

                    //LinearLayout forDriverLayout = convertView.findViewById(R.id.forDriver);
                    LinearLayout forDriverLayout2 = convertView.findViewById(R.id.driverInfo);

                    StorageReference franchiseRef = storageRef.child("franchises/" + data.getUserID() + ".jpg");
                    StorageReference licRef = storageRef.child("licenses/" + data.getUserID() + ".jpg");
                    StorageReference motorRef = storageRef.child("motors/" + data.getUserID() + ".jpg");
                    StorageReference tricyRef = storageRef.child("tricycles/" + data.getUserID() + ".jpg");

        if(data.getIsDriver()) {
            //TextView textViewTransactions = convertView.findViewById(R.id.textViewTransactions);
            TextView textViewPlateNumber = convertView.findViewById(R.id.textViewPlateNumber);
            TextView textViewToda = convertView.findViewById(R.id.textViewTODA);
            TextView textViewTricycleNumber = convertView.findViewById(R.id.textViewTricycleNumber);

            textViewPlateNumber.setText(data.getDriver().getPlateNumber());
            textViewToda.setText(data.getDriver().getToda());
            textViewTricycleNumber.setText(data.getDriver().getTricycleNumber());
            //forDriverLayout.setVisibility(View.VISIBLE);
            forDriverLayout2.setVisibility(View.VISIBLE);

//            FirebaseDatabase.getInstance().getReference("history").addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    int count = 0;
//                    for(DataSnapshot child: dataSnapshot.getChildren()) {
//                        History history = child.getValue(History.class);
//                        if(history.getSession().getDriver().getUserID().equals(data.getUserID()))
//                            count+=1;
//                        ;                            }
//                    //textViewTransactions.setText(count+"");
//                    //textViewTransactions.setVisibility(View.VISIBLE);
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });


        }

                    linearLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // inflate the layout for the dialog box
                            LayoutInflater inflater = LayoutInflater.from(context);
                            View dialogView = inflater.inflate(R.layout.custom_dialog_imageview, null);
                            // get the ImageView from dialog_box.xml
                            ImageView dialogImageView = dialogView.findViewById(R.id.admin_id_img);
                            ImageView dialogImageView2 = dialogView.findViewById(R.id.admin_id_img2);
                            ImageView dialogImageView3 = dialogView.findViewById(R.id.admin_id_img3);
                            ImageView dialogImageView4 = dialogView.findViewById(R.id.admin_id_img4);
                            // set the image for ImageView
                            licRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Glide.with(dialogImageView2.getContext())
                                            .load(uri)
                                            .fitCenter()
                                            .into(dialogImageView2);
                                }
                            });

                            if(data.getIsDriver()) {
                                dialogImageView.setVisibility(View.VISIBLE);
                                dialogImageView3.setVisibility(View.VISIBLE);
                                dialogImageView4.setVisibility(View.VISIBLE);
                                franchiseRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Glide.with(dialogImageView.getContext())
                                                .load(uri)
                                                .fitCenter()
                                                .into(dialogImageView);
                                    }
                                });



                                motorRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Glide.with(dialogImageView3.getContext())
                                                .load(uri)
                                                .fitCenter()
                                                .into(dialogImageView3);
                                    }
                                });

                                tricyRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Glide.with(dialogImageView4.getContext())
                                                .load(uri)
                                                .fitCenter()
                                                .into(dialogImageView4);
                                    }
                                });
                            }

                            // create the dialog box
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setView(dialogView);
                            // show the dialog box
                            builder.show();
                        }
                    });

        button.setVisibility(View.VISIBLE);
        button2.setVisibility(View.VISIBLE);
        if(data.getIsRejected()) {
            button.setVisibility(View.GONE);
            button2.setVisibility(View.GONE);
        }
        if (data.getIsVerified()) {
            button.setVisibility(View.GONE);
            button2.setVisibility(View.GONE);
        } else {
            button.setVisibility(View.VISIBLE);
            button2.setVisibility(View.GONE);
        }


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog pdialog = ProgressDialog.show(context, "",
                        "Sending email. Please wait...", true);
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("isVerified",true);
                hashMap.put("isRejected",false);
                database.getReference().child("users").child(data.getUserID()).updateChildren(hashMap);
                pdialog.dismiss();
                showDialog("User Verified!");
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.getReference().child("users").child(data.getUserID()).child("isRejected").setValue(true);
                showDialog("User Rejected!");
            }
        });

        return convertView;
    }

    public void showDialog(String message) {

        android.support.v7.app.AlertDialog dialog = new android.support.v7.app.AlertDialog.Builder(this.context)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public void search(String query) {
        filteredUsers.clear();
        if(query.isEmpty()){
            filteredUsers.addAll(data);
        } else {
            query = query.toLowerCase();
            for (User user : data) {
                if (user.getFullname().contains(query) || user.getDriver().getPlateNumber().contains(query)) {
                    filteredUsers.add(user);
                }
            }
        }
        notifyDataSetChanged();
    }
}
