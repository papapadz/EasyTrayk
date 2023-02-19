package com.tukla.www.tukla;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class AdminTodaAdapter extends BaseAdapter {

    private Context context;
    private List<Toda> data;
    private List<Toda> filteredData;

    public AdminTodaAdapter(Context context, List<Toda> data) {
        this.context = context;
        this.data = data;
        this.filteredData = data;
    }

    @Override
    public int getCount() {
        if(filteredData.isEmpty())
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

        code.setText("CODE: "+data.get(i).getCode());
        name.setText("TODA: "+data.get(i).getName());
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference("todaList").child(data.get(i).getCode()).removeValue();
            }
        });

        return view;
    }
}
