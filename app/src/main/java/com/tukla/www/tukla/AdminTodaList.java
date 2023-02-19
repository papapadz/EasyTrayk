package com.tukla.www.tukla;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Console;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminTodaList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminTodaList extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private List<Toda> listToda = new ArrayList<>();
    public AdminTodaList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdminTodaList.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminTodaList newInstance(String param1, String param2) {
        AdminTodaList fragment = new AdminTodaList();
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
        View view = inflater.inflate(R.layout.fragment_admin_toda_list, container, false);
        ListView listView = view.findViewById(R.id.adminListToda);
        AdminTodaAdapter adapter = new AdminTodaAdapter(getActivity(), listToda);
        listView.setAdapter(adapter);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        LinearLayout linearLayout = view.findViewById(R.id.todaNewLinearLayout);
        TextView code = view.findViewById(R.id.newTodaCode);
        TextView codeName = view.findViewById(R.id.newTodaName);
        Button newButton = view.findViewById(R.id.newTodaBtnNew);
        Button saveButton = view.findViewById(R.id.newTodaBtnSave);

        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayout.setVisibility(View.VISIBLE);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseDatabase.getReference("todaList").child(code.getText().toString()).setValue(new Toda(code.getText().toString(),codeName.getText().toString()));
                linearLayout.setVisibility(View.GONE);
            }
        });

        firebaseDatabase.getReference("todaList").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listToda.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Toda toda = snapshot.getValue(Toda.class);
                    listToda.add(toda);
                }
                AdminTodaAdapter adapter = new AdminTodaAdapter(getActivity(), listToda);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }
}