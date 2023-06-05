package com.tukla.www.tukla;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class NotifsAdapter extends BaseAdapter {

    private Context context;
    private List<Notif> data;
    private List<Notif> filteredData;

    public NotifsAdapter(Context context, List<Notif> data) {
        this.context = context;
        this.data = data;
        this.filteredData = data;
    }

    @Override
    public int getCount() {
        if (filteredData.isEmpty())
            return 1;
        return filteredData.size();
    }

    @Override
    public Object getItem(int i) {
        return filteredData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (data.isEmpty()) {
            view = LayoutInflater.from(context).inflate(R.layout.empty_list, viewGroup, false);
            return view;
        }

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.admin_default_list, viewGroup, false);
        }

        TextView code = view.findViewById(R.id.txtCol1);
        TextView name = view.findViewById(R.id.txtCol2);
        Button deleteButton = view.findViewById(R.id.delete_button);
        deleteButton.setVisibility(View.GONE);

        code.setText(data.get(i).getDate());
        FirebaseDatabase.getInstance().getReference("users").child(data.get(i).getSenderID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User sender = dataSnapshot.getValue(User.class);
                name.setText(data.get(i).getMessage()+"-"+sender.getLastname()+", "+sender.getFullname());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }
}