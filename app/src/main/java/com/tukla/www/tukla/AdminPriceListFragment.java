

package com.tukla.www.tukla;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tukla.www.tukla.R;
import com.tukla.www.tukla.User;
import com.tukla.www.tukla.UserListAdapter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link com.tukla.www.tukla.AdminUserListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminPriceListFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private List<Fare> listPrice = new ArrayList<>();

    public AdminPriceListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdminUserListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminPriceListFragment newInstance(String param1, String param2) {
        AdminPriceListFragment fragment = new AdminPriceListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        View view = inflater.inflate(R.layout.fragment_admin_price_list, container, false);
        ListView listView = view.findViewById(R.id.adminListPrice);
        AdminPriceListAdapter adapter = new AdminPriceListAdapter(getActivity(), listPrice);
        listView.setAdapter(adapter);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        firebaseDatabase.getReference("priceList").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listPrice.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Fare fare = snapshot.getValue(Fare.class);
                    if(fare.getIsActive())
                        listPrice.add(fare);
                }
                AdminPriceListAdapter adapter = new AdminPriceListAdapter(getActivity(), listPrice);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        LinearLayout linearLayout = view.findViewById(R.id.priceNewLinearLayout);
        TextView txtNewPrice = view.findViewById(R.id.newPrice);
        Button newButton = view.findViewById(R.id.btnNewPriceItem);
        Button saveButton = view.findViewById(R.id.newPriceBtnSave);

        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayout.setVisibility(View.VISIBLE);
            }
        });
        DatabaseReference priceRef = firebaseDatabase.getReference("priceList");
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(TextUtils.isDigitsOnly(txtNewPrice.getText().toString())) {
                    priceRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                String childKey = snapshot.getKey();
                                Fare fSnap = snapshot.getValue(Fare.class);

                                if(fSnap.getIsActive())
                                    priceRef.child(childKey).child("isActive").setValue(false);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    String key = priceRef.push().getKey();
                    double price = Double.parseDouble(txtNewPrice.getText().toString());
                    Fare fareObj = new Fare(key, price,true, mAuth.getUid(), LocalDateTime.now().toString());
                    priceRef.child(key).setValue(fareObj);
                    linearLayout.setVisibility(View.GONE);
                    txtNewPrice.setText("");
                } else {
                    txtNewPrice.setError("Input must be numerical!");
                }


            }
        });


        return view;
    }
}