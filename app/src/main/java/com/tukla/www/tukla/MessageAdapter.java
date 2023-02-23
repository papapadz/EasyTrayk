package com.tukla.www.tukla;

        import android.content.Context;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.BaseAdapter;
        import android.widget.Button;
        import android.widget.TextView;

        import java.util.List;

public class MessageAdapter extends BaseAdapter {

    private Context context;
    private List<Message> data;
    private List<Message> filteredData;

    public MessageAdapter(Context context, List<Message> data) {
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

            view = LayoutInflater.from(context).inflate(R.layout.empty_message_list, viewGroup, false);
            return view;
        }

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.message_layout_adapter, viewGroup, false);
            TextView code = view.findViewById(R.id.txtCol1);
            TextView name = view.findViewById(R.id.txtCol2);

            code.setText(data.get(i).getSender().getFullname() + " " + data.get(i).getSender().getLastname());
            name.setText(data.get(i).getMessage());
        }


        return view;
    }
}
