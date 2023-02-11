package com.tukla.www.tukla;

import static com.tukla.www.tukla.R.id.nav_logOut;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdminActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private List<User> listUsers = new ArrayList<>();
    private FirebaseAuth mAuth;
    private ListView listView;
    private EditText userSearch;
    private UserListAdapter adapter;
    private List<User> tempUser = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        mAuth = FirebaseAuth.getInstance();
        listView = findViewById(R.id.list_user);
        userSearch = findViewById(R.id.user_search);
        userSearch.setVisibility(View.GONE);
        Toolbar toolbar = (Toolbar) findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );

        DrawerLayout drawer = (DrawerLayout) findViewById( R.id.admin_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close );
        drawer.setDrawerListener( toggle );
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById( R.id.nav_admin_view );
        navigationView.setNavigationItemSelectedListener( this );
        //drawer.findViewById(R.id.nav_user_lists).setVisibility(View.VISIBLE);
        //navigationView.findViewById(R.id.nav_book).setVisibility(View.GONE);
        //navigationView.findViewById(R.id.nav_history).setVisibility(View.GONE);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("users");
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listUsers.clear();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);
                    if(user.getIsDriver() && (!user.getIsVerified() || user.getIsRejected()))
                        listUsers.add(user);
                }
                adapter = new UserListAdapter(AdminActivity.this, listUsers);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        userSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.search(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        /** setup drawer **/
        database.getReference("users").child(mAuth.getUid()).child("updatedAt").setValue(LocalDateTime.now().toString());

        View navHeader = navigationView.getHeaderView(0);
        //CircleImageView profImg = navHeader.findViewById(R.id.profile_image);

//        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
//        StorageReference imgRef = storageRef.child("images/"+mAuth.getUid()+".jpg");
//        imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//                Glide.with(profImg.getContext())
//                        .load(uri)
//                        .fitCenter()
//                        .into(profImg);
//            }
//        });

        database.getReference("users").child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                TextView txtViewName = navHeader.findViewById(R.id.textView2);
                txtViewName.setText(user.getFullname());

                TextView txtViewEmail = navHeader.findViewById(R.id.textView);
                txtViewEmail.setText(mAuth.getCurrentUser().getEmail());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        /** */
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // Handle navigation view item clicks here.
        int id = menuItem.getItemId();

        if(id==R.id.nav_user_list) {
            //Intent intent = new Intent(this, AdminActivity.class);
            //finish();
            //startActivity(intent);
            return false;
        } else if(id==R.id.nav_admin_history) {
            //FirebaseDatabase.getInstance().getReference("bookings").child(clickedBookingID).removeValue();
            Intent intent = new Intent(this, AdminHistory.class);
            intent.putExtra("ROLE","ADMIN");
            startActivity(intent);
            finish();
        } else if(id==nav_logOut) {
            //FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, Login.class);
            finish();
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById( R.id.admin_drawer_layout );
        drawer.closeDrawer( GravityCompat.START );
        return true;
    }
}