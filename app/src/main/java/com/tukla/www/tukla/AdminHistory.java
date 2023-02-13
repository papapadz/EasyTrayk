package com.tukla.www.tukla;

        import static com.tukla.www.tukla.R.id.nav_logOut;

        import android.content.Intent;
        import android.net.Uri;
        import android.os.Bundle;
        import android.support.annotation.NonNull;
        import android.support.design.widget.NavigationView;
        import android.support.v4.view.GravityCompat;
        import android.support.v4.widget.DrawerLayout;
        import android.support.v7.app.ActionBarDrawerToggle;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.Toolbar;
        import android.util.Log;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.ListView;
        import android.widget.TextView;

        import com.bumptech.glide.Glide;
        import com.google.android.gms.tasks.OnSuccessListener;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;
        import com.google.firebase.storage.FirebaseStorage;
        import com.google.firebase.storage.StorageReference;

        import org.w3c.dom.Text;

        import java.io.Serializable;
        import java.time.LocalDateTime;
        import java.util.ArrayList;
        import java.util.List;

        import de.hdodenhof.circleimageview.CircleImageView;

public class AdminHistory extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, Serializable {

    private List<History> listHistory = new ArrayList<>();
    private FirebaseAuth mAuth;
    private ListView listView;
    private String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_history);

        mAuth = FirebaseAuth.getInstance();
        listView = findViewById(R.id.list_history);

        role = (String) getIntent().getSerializableExtra("ROLE");

        Toolbar toolbar = (Toolbar) findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );

        TextView customText = toolbar.findViewById(R.id.toolbarTextView);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        DrawerLayout drawer = (DrawerLayout) findViewById( R.id.admin_drawer_layout_history );
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close );
        drawer.setDrawerListener( toggle );
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById( R.id.nav_admin_view_history );
        navigationView.setNavigationItemSelectedListener( this );

        /** setup drawer **/
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference("users").child(mAuth.getUid()).child("updatedAt").setValue(LocalDateTime.now().toString());

        View navHeader = navigationView.getHeaderView(0);
//        CircleImageView profImg = navHeader.findViewById(R.id.profile_image);
//
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

        database.getReference("history").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listHistory.clear();
                for (DataSnapshot historySnapshot : dataSnapshot.getChildren()) {
                    History history = historySnapshot.getValue(History.class);
                    listHistory.add(history);
                }
                if(listHistory.size()==0)
                    Log.d("TAG LIST HISTORY",listHistory.toString());
                HistoryListAdapter adapter = new HistoryListAdapter(AdminHistory.this, listHistory);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        int id = menuItem.getItemId();

        if(id==R.id.nav_user_list) {
            Intent intent = new Intent(this, AdminActivity.class);
            //finish();
            startActivity(intent);
        } else if(id==R.id.nav_admin_history) {
            //FirebaseDatabase.getInstance().getReference("bookings").child(clickedBookingID).removeValue();
            return true;
        } else if(id==nav_logOut) {
            //FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, Login.class);
            finish();
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById( R.id.admin_drawer_layout_history );
        drawer.closeDrawer( GravityCompat.START );

        return true;
    }
}