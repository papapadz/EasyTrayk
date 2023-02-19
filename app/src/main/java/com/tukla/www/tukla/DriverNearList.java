package com.tukla.www.tukla;

        import android.content.Context;
        import android.support.annotation.NonNull;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.BaseAdapter;
        import android.widget.TextView;

        import com.android.volley.Request;
        import com.android.volley.RequestQueue;
        import com.android.volley.Response;
        import com.android.volley.VolleyError;
        import com.android.volley.toolbox.StringRequest;
        import com.android.volley.toolbox.Volley;
        import com.google.android.gms.maps.model.LatLng;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;

        import org.json.JSONException;
        import org.json.JSONObject;

        import java.util.Calendar;
        import java.util.Date;
        import java.util.List;

public class DriverNearList extends BaseAdapter {

    private Context context;
    private List<DriverLocationObject> data;

    public DriverNearList(Context context, List<DriverLocationObject> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        if(data.isEmpty())
            return 1;
        else
            return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (data.isEmpty()) {
            convertView = LayoutInflater.from(context).inflate(R.layout.no_nearby, parent, false);
            return convertView;
        }

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.driver_near_list, parent, false);
        }
        DriverLocationObject data = (DriverLocationObject) getItem(position);

        TextView txtDriverName = convertView.findViewById(R.id.txtDriverName);
        TextView txtDistance = convertView.findViewById(R.id.txtDistance);
        txtDriverName.setText(data.getDriverLocation().getUser().getFullname()+" "+data.getDriverLocation().getUser().getLastname());
        txtDistance.setText(data.getEta());
        return convertView;
    }

}
