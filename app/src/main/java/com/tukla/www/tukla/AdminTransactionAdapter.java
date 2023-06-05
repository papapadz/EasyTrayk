package com.tukla.www.tukla;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminTransactionAdapter extends BaseAdapter {

    private Context context;
    private List<User> data;
    private List<User> filteredUsers;


    public AdminTransactionAdapter(Context context, List<User> data) {
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

        List<History> userHistory = new ArrayList<>();

        ImageView imageView = convertView.findViewById(R.id.imageView);
        TextView textViewName = convertView.findViewById(R.id.textViewName);
        TextView textViewAddress = convertView.findViewById(R.id.textViewAddress);
        TextView textViewPhone = convertView.findViewById(R.id.textViewPhone);
        Button button = convertView.findViewById(R.id.button);
        Button button2 = convertView.findViewById(R.id.button2);
        LinearLayout linearLayout = convertView.findViewById(R.id.layout_admin_userlist);

        if(data.get(position).getIsRejected()) {
            button2.setVisibility(View.GONE);

            button.setText("Reactivate");
            button.setVisibility(View.VISIBLE);
        } else {
            button.setText("Reset");
            button.setVisibility(View.VISIBLE);

            button2.setText("Suspend");
            button2.setVisibility(View.VISIBLE);
        }

        User userData = (User) filteredUsers.get(position);
        textViewName.setText(userData.getLastname()+","+userData.getFullname()+" "+userData.getMiddlename());
        textViewAddress.setText(userData.getAddress());
        textViewPhone.setText(userData.getPhone());

        TextView textViewPlateNumber = convertView.findViewById(R.id.textViewPlateNumber);
        TextView textViewToda = convertView.findViewById(R.id.textViewTODA);
        TextView textViewTricycleNumber = convertView.findViewById(R.id.textViewTricycleNumber);

        textViewPlateNumber.setText(userData.getDriver().getPlateNumber());
        textViewToda.setText(userData.getDriver().getToda());
        textViewTricycleNumber.setText(userData.getDriver().getTricycleNumber());

        StorageReference imgRef = storageRef.child("images/" + userData.getUserID() + ".jpg");
        imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(imageView.getContext())
                        .load(uri)
                        .fitCenter()
                        .into(imageView);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("isRejected", true);
                database.getReference().child("users").child(userData.getUserID()).updateChildren(hashMap);
                //Toast.makeText(parent.getContext(), "User Verified!", Toast.LENGTH_SHORT);
                showDialog("Driver suspended!");
            }
        });

        ProgressDialog pdialog = new ProgressDialog(context);
        pdialog.setMessage("Loading, please wait.....");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userData.getIsRejected()) {
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("isRejected", false);
                    database.getReference().child("users").child(userData.getUserID()).updateChildren(hashMap);
                    //Toast.makeText(parent.getContext(), "User Verified!", Toast.LENGTH_SHORT);
                    showDialog("Driver reactivated!");
                } else {
                    pdialog.show();
                    database.getReference("sessions").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot child: dataSnapshot.getChildren()) {
                                Session session = child.getValue(Session.class);
                                if(session.getDriver().getUserID().equals(userData.getUserID())) {
                                    database.getReference("sessions").child(child.getKey()).removeValue();
                                }
                            }
                            pdialog.dismiss();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            pdialog.dismiss();
                        }
                    });
                }
            }
        });

        TextView textViewTransactions = convertView.findViewById(R.id.textViewTransactions);
        textViewTransactions.setVisibility(View.VISIBLE);
        LinearLayout forDriverLayout = convertView.findViewById(R.id.forDriver);
        LinearLayout forDriverLayout2 = convertView.findViewById(R.id.driverInfo);
        forDriverLayout.setVisibility(View.VISIBLE);
        forDriverLayout2.setVisibility(View.VISIBLE);

        database.getReference("sessions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userHistory.clear();
                for(DataSnapshot child: dataSnapshot.getChildren()) {
                    Session session = child.getValue(Session.class);
                    if(session.getDriver().getUserID().equals(userData.getUserID()))  {
                        userHistory.add(new History(session,session.getBooking().getFare(), session.getStartedAt()));
                    }
                }
                textViewTransactions.setText(userHistory.size()+"");
                Log.d("USER HISTORY-1", userHistory.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //List<History> listHistory = new ArrayList<>();
                LayoutInflater inflater = LayoutInflater.from(context);
                View dialogView = inflater.inflate(R.layout.fragment_admin_history_list, null);
                ListView listView = dialogView.findViewById(R.id.adminListHistory);
                TextView total = dialogView.findViewById(R.id.historyTotal);
                total.setVisibility(View.VISIBLE);
                Log.d("USER HISTORY-2",userHistory.toString());
//                for (Map.Entry<String, Session> set : userSessions.entrySet()) {
//                    database.getReference().child("history").child(set.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            if(dataSnapshot.exists()) {
//                                listHistory.add(dataSnapshot.getValue(History.class));
//
                                HistoryListAdapter adapter = new HistoryListAdapter(view.getContext(), userHistory);
                                listView.setAdapter(adapter);
                                total.setText("Total Transactions: "+userHistory.size()+" | Total Amount: Php "+userHistory.size()*5);
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                        }
//                    });
//                }

//                for(Session sessionItem : userSessions) {
//
//                    database.getReference("history").child(sessionItem.getBooking().getBookingID()).addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot historySnapshot) {
//                            History history = historySnapshot.getValue(History.class);
//                            listHistory.add(history);
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                        }
//                    });
//                }

//                database.getReference("history").addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        listHistory.clear();
//                        for (DataSnapshot historySnapshot : dataSnapshot.getChildren()) {
//                            History history = historySnapshot.getValue(History.class);
//                            //for(Session sessionItem : userSessions) {
//                            if(userSessions.containsKey(historySnapshot.getKey()))
//                                listHistory.add(history);
//                            //}
//                        }
//                        HistoryListAdapter adapter = new HistoryListAdapter(view.getContext(), listHistory);
//                        listView.setAdapter(adapter);
//                        total.setText("Total Transactions: "+userSessions.size()+" | Total Amount: Php "+userSessions.size()*5);
//                        pdialogx.dismiss();
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
                // create the dialog box
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setView(dialogView);
                // show the dialog box

                builder.show();
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


}