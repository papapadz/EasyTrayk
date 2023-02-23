package com.tukla.www.tukla;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class AdminTermsAdapter extends BaseAdapter {

    private Context context;
    private List<Term> data;
    private List<Term> filteredData;

    public AdminTermsAdapter(Context context, List<Term> data) {
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

        code.setText("#: "+data.get(i).getOrdder());
        name.setText("T&C: "+data.get(i).getTerm());

        return view;
    }
}