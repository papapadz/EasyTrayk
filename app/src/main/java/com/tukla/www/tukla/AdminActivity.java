package com.tukla.www.tukla;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
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
import android.webkit.WebView;
import android.webkit.WebViewClient;
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

    private FirebaseAuth mAuth;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        fragmentManager = getSupportFragmentManager();

        mAuth = FirebaseAuth.getInstance();
        Toolbar toolbar = (Toolbar) findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );

        DrawerLayout drawer = (DrawerLayout) findViewById( R.id.admin_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close );
        drawer.setDrawerListener( toggle );
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById( R.id.nav_admin_view );
        navigationView.setNavigationItemSelectedListener( this );

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        /** setup drawer **/
        database.getReference("users").child(mAuth.getUid()).child("updatedAt").setValue(LocalDateTime.now().toString());
        View navHeader = navigationView.getHeaderView(0);
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

        showHomeFragment();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // Handle navigation view item clicks here.
        int id = menuItem.getItemId();

        switch (menuItem.getItemId()) {
            case R.id.nav_user_list:
                showHomeFragment();
                break;
            case R.id.nav_toda_list:
                AdminTodaList todaListFragment = new AdminTodaList();
                fragmentManager.beginTransaction().replace(R.id.fragment_container,todaListFragment).commit();
                break;
            case R.id.nav_terms_conditions_list:
                break;
            case R.id.nav_price_list:
                break;
            case R.id.nav_rating_list:
                AdminHistoryFragment historyListFragment = new AdminHistoryFragment();
                fragmentManager.beginTransaction().replace(R.id.fragment_container,historyListFragment).commit();
                break;
            case R.id.nav_support:
                break;
            case R.id.nav_logOut:
                Intent intent = new Intent(this, Login.class);
                finish();
                startActivity(intent);
                break;
        }

//        if(id==R.id.nav_user_list) {
//            showHomeFragment();
//        } else if(id==nav_toda_list) {
//
//        } else if(id==nav_terms_conditions_list) {
//
//        } else if(id==nav_price_list) {
//
//        } else if(id==R.id.nav_rating_list) {
//            AdminHistoryFragment historyListFragment = new AdminHistoryFragment();
//            fragmentManager.beginTransaction().replace(R.id.fragment_container,historyListFragment).commit();
//        } else if(id==nav_terms_conditions_list) {
//
//        } else if(id==nav_logOut) {
//            //FirebaseAuth.getInstance().signOut();
//            Intent intent = new Intent(this, Login.class);
//            finish();
//            startActivity(intent);
//        }
        DrawerLayout drawer = (DrawerLayout) findViewById( R.id.admin_drawer_layout );
        drawer.closeDrawer( GravityCompat.START );
        return true;
    }

    public void showHomeFragment() {
        AdminUserListFragment adminUserListFragment = new AdminUserListFragment();
        fragmentManager.beginTransaction().replace(R.id.fragment_container,adminUserListFragment).commit();
    }
}