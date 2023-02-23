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

public class AdminPriceListAdapter extends BaseAdapter {

    private Context context;
    private List<Fare> data;
    private List<Fare> filteredData;

    public AdminPriceListAdapter(Context context, List<Fare> data) {
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
        deleteButton.setVisibility(View.GONE);

        code.setText("Date: "+data.get(i).getCreatedAt());
        name.setText("Price: "+data.get(i).getPrice());

        return view;
    }
}
