package com.tukla.www.tukla;

import static com.tukla.www.tukla.R.id.add;
import static com.tukla.www.tukla.R.id.listviewMessages;
import static com.tukla.www.tukla.R.id.map;
import static com.tukla.www.tukla.R.id.message;
import static com.tukla.www.tukla.R.id.nav_logOut;
import static com.tukla.www.tukla.R.id.nav_profile;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, LoaderManager.LoaderCallbacks<Object>, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, Serializable {

    //RelativeLayout products_select_option;
    Button myCurrentloc,book_button;

    private static final String TAG = MainActivity.class.getSimpleName();
    private final static int PERMISSION_MY_LOCATION = 3;

    private static final int REQUEST_CHECK_SETTINGS = 5000;
    private MapFragment mapFragment;
    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    private Location mLastLocation;
    private LocationRequest request;
    View mapView;
    private boolean mRequestingLocationUpdates;
    CameraUpdate cLocation;
    double latitude,longitude;
    Marker now;

    Geocoder geocoder;
    List<android.location.Address> addresses;
    AutoCompleteTextView txtDropOff;
    TextView priceText;
    TextView distanceText;
    LatLng myPosition;
    LatLng positionUpdate;
    //LatLng targetDestination;
    private FirebaseAuth mAuth;
    private final String CODE_BOOK = "Book";
    private final String CODE_CANCEL = "Cancel";
    private final String CODE_DRIVER_WAIT = "Waiting Driver";
    private final String CODE_DRIVER_OK = "Driver Arrived";
    private final String CODE_DONE = "Done";
    private final String CODE_CHAT = "Chat";

    private Boolean isBookClicked = false;
    String recentBookingID;
    Marker driverMarker;
    Marker destinationMarker;
    Booking myBookingObj;
    //TextView platenumber_text;
    //TextView drivername_text;
    //private String driverName;
    //private String driverPlateNumber;
    User loggedInUser;
    Session thisSession;
    //CardView driver_info;
    NavigationView navigationView;
    // Create a LatLngBounds object for Calamba, Laguna
    final LatLngBounds calambaBounds = new LatLngBounds(
            new LatLng(13.816244, 121.428803), // Southwest corner
            new LatLng(14.061821, 121.564072) // Northeast corner
    );
    private Boolean isMapClick;
    private String txtMyNote;
    private Boolean isCancelled = false;
    private int isNotified = 0;

    private List<DriverLocationObject> listDriverLocationsObject = new ArrayList<>();
    private ListView listViewDriverLocations;
    private LinearLayout linearLayoutnearby;
    private double priceFare;
    AlertDialog.Builder waitingDriverbuilder;
    AlertDialog waitingDriverDialog;
    private ListView listViewChatBox;
    ArrayList<Message> messageArrayList = new ArrayList<>();
    int numPassenger = 1;
    String[] spinnerItems = {"1","2","3"};
    private TextView numPassengerTxt;
    Dialog chatDialog;
    EditText editTextChat;
    Button sendButtonChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setupLocationManager();
        mAuth = FirebaseAuth.getInstance();
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        Locale locale = new Locale("PH");
        Locale.setDefault(locale);
        chatDialog = new Dialog(MainActivity.this);
        chatDialog.setContentView(R.layout.chat_dialog);
        listViewChatBox = chatDialog.findViewById(R.id.list_view_chat);
        editTextChat = chatDialog.findViewById(R.id.edit_text_chat);
        sendButtonChat = chatDialog.findViewById(R.id.button_send_chat);

        sendButtonChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message message = new Message(thisSession.getBooking().getUser(),thisSession.getDriver(),editTextChat.getText().toString(),LocalDateTime.now().toString());
                String key = FirebaseDatabase.getInstance().getReference("messages").push().getKey();
                FirebaseDatabase.getInstance().getReference("messages").child(thisSession.getBooking().getBookingID()).child(key).setValue(message);
                editTextChat.setText("");
            }
        });

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        Toolbar toolbar = (Toolbar) findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );



        listViewDriverLocations = findViewById(R.id.listViewDriverNear);
        linearLayoutnearby = findViewById(R.id.linearNearbyList);
        mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById( R.id.map );
        mapView = mapFragment.getView();
        mapFragment.getMapAsync( this );

        CheckMapPermission();

        DrawerLayout drawer = (DrawerLayout) findViewById( R.id.drawer_layout );
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close );
        drawer.setDrawerListener( toggle );
        toggle.syncState();
        navigationView = (NavigationView) findViewById( R.id.nav_view );
        navigationView.setNavigationItemSelectedListener( this );

        txtDropOff = findViewById(R.id.txt_dropoff);

        myCurrentloc=(Button) findViewById( R.id.myCLocation );
        priceText = (TextView) findViewById(R.id.price_text);
        distanceText = (TextView) findViewById(R.id.distance_text);
        numPassengerTxt = findViewById(R.id.numPassengerTxt);

        book_button=(Button)findViewById(R.id.book_button);
        findNearbyDrivers();
        //book_button.setText(CODE_SOS);
        book_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // TODO Auto-generated method stub
                final ProgressDialog pdialog = ProgressDialog.show(MainActivity.this, "",
                        "Loggin in. Please wait...", true);
                try {
                    if(book_button.getText().toString().equals(CODE_BOOK)) {

                        Address destinationResult = setDestination();

                        if(destinationResult!=null) {

                            showCustomBookDialog(destinationResult.getAddressLine(0));

                            //                        waitBookAccept();

                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setTitle("No location found!");
                            builder.setMessage("Please enter a location within Sariaya, Quezon only.");
                            AlertDialog dialog = builder.create();

                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Do something when the OK button is clicked
                                    dialog.dismiss();
                                }
                            });
                            mMap.clear();
                            txtDropOff.setText("");
                            dialog.show();
                        }
                    } else if(book_button.getText().toString().equals(CODE_CANCEL)){
                        //driver_info.setVisibility(View.GONE);
                        cancelBook();
                    } else if (book_button.getText().toString().equals(CODE_CHAT)) {

                        //MessageAdapter messageAdapter = new MessageAdapter(MainActivity.this, messageArrayList);
                        //listViewChatBox.setAdapter(messageAdapter);
                        chatDialog.show();
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),"An error occured! Try selecting another place.",Toast.LENGTH_SHORT);
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("No location found!");
                    builder.setMessage("Please enter a location within Sariaya, Quezon only.");
                    AlertDialog dialog = builder.create();

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Do something when the OK button is clicked
                            dialog.dismiss();
                        }
                    });
                    mMap.clear();
                    txtDropOff.setText("");
                    dialog.show();
                } finally {
                    pdialog.dismiss();
                }
            }
        });

        txtDropOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.clear();
                //txtDropOff.setText("");
                isMapClick=false;
                txtDropOff.showDropDown();
            }
        });

        txtDropOff.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    loadPlaces();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                txtDropOff.showDropDown();
            }
        });

        DatabaseReference mySessionsRef = database.getReference().child("sessions");
        mySessionsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot sessionSnapshot : dataSnapshot.getChildren()) {

                    Session mySession = sessionSnapshot.getValue(Session.class);

                    if(!mySession.isBookingEmpty())
                        if(mySession.getBooking().getBookingID().equals(recentBookingID) && mySession.getIsAccepted()) {
                            thisSession = mySession;
                            LatLng positionUpdate = new LatLng(mySession.getDriverLocation().getLatitude(),mySession.getDriverLocation().getLongitude());
                            driverMarker.remove();
                            MarkerOptions myMarkerOptions = new MarkerOptions();
                            myMarkerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_tricycle));
                            myMarkerOptions.position(positionUpdate);
                            //myMarkerOptions.title("Driver: "+mySession.getDriver().getFullname()+" | Plate No. "+mySession.getDriver().getDriver().getPlateNumber());

                            CameraUpdate camerUpdate = CameraUpdateFactory.newLatLngZoom( positionUpdate, 20 );
                            mMap.animateCamera(camerUpdate);

                            Location driverLocation = new Location("");
                            driverLocation.setLatitude(mySession.getDriverLocation().getLatitude());
                            driverLocation.setLongitude(mySession.getDriverLocation().getLongitude());

                            Location myCurrLocation = new Location("");
                            myCurrLocation.setLatitude(myPosition.latitude);
                            myCurrLocation.setLongitude(myPosition.longitude);

                            double dist = driverLocation.distanceTo(myCurrLocation)/1000;
                            double time = (dist/20) * 60;

                            if(mySession.getIs50meters() && !mySession.getIsDriverArrived() && isNotified==0) {
                                isNotified=1;
                                mySessionsRef.child(sessionSnapshot.getKey()).child("isDriverArrived").setValue(true);
                                book_button.setText(CODE_CHAT);
                                book_button.setBackgroundColor(getColor(R.color.colorAccent));
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                builder.setTitle("Your Driver is almost there!");
                                builder.setMessage("I am 10 meters away. My plate number is "+myBookingObj.getDriver().getDriver().getPlateNumber());
                                AlertDialog dialog = builder.create();

                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Do something when the OK button is clicked
                                        dialog.dismiss();
                                    }
                                });
                                dialog.show();

                                Location dest = new Location("");
                                dest.setLatitude(mySession.getBooking().getDestination().getLatitude());
                                dest.setLongitude(mySession.getBooking().getDestination().getLongitude());

                                dist = myCurrLocation.distanceTo(dest)/1000;
                                time = (dist/20) * 60;

                                mMap.clear();
                                DownloadTask downloadTask = new DownloadTask();
                                String url = getDirectionsUrl(positionUpdate, new LatLng(mySession.getBooking().getDestination().getLatitude(),mySession.getBooking().getDestination().getLongitude()));
                                downloadTask.execute(url);
                            } else {
                                mMap.clear();
                                DownloadTask downloadTask = new DownloadTask();
                                LatLng myLoc = new LatLng(myCurrLocation.getLatitude(),myCurrLocation.getLongitude());
                                String url = getDirectionsUrl(positionUpdate, myLoc);
                                downloadTask.execute(url);
                            }

                            driverMarker = mMap.addMarker(myMarkerOptions);
                            driverMarker.setTag(mySession.getDriver());

                            String eta = String.format("%.2f",time);
                            String sDist = String.format("%.2f",(dist));

                            listDriverLocationsObject.clear();
                            DriverLocationObject driverLocationObject = new DriverLocationObject(new DriverLocation(mySession.getDriver(),mySession.getDriverLocation(),null,true),"ETA: "+eta+" mins\nDistance: "+sDist+" km");
                            listDriverLocationsObject.add(driverLocationObject);

                            DriverNearList adapter = new DriverNearList(MainActivity.this, listDriverLocationsObject);
                            listViewDriverLocations.setAdapter(adapter);

                            break;
                        }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        database.getReference("priceList").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    Fare fare = snapshot.getValue(Fare.class);
                    if(fare.getIsActive()) {
                        priceFare = fare.getPrice();
                        priceText.setText(String.format("%.2f",priceFare*numPassenger));
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
        //mGoogleApiClient.connect();
        /** setup drawer **/
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference("users").child(mAuth.getUid()).child("updatedAt").setValue(LocalDateTime.now().toString());

        View navHeader = navigationView.getHeaderView(0);
        CircleImageView profImg = navHeader.findViewById(R.id.profile_image);

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imgRef = storageRef.child("images/"+mAuth.getUid()+".jpg");
        imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(profImg.getContext())
                        .load(uri)
                        .fitCenter()
                        .into(profImg);
            }
        });

        database.getReference("users").child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                loggedInUser = user;
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
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById( R.id.drawer_layout );
        if (drawer.isDrawerOpen( GravityCompat.START )) {
            drawer.closeDrawer( GravityCompat.START );
        } else {
            drawer.openDrawer( GravityCompat.START );
//            super.onBackPressed();
//
//            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//            builder.setTitle("Sign out?");
//            builder.setMessage("You have pressed back button. Are you signing out?");
//            AlertDialog dialog = builder.create();
//
//            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    // Do something when the OK button is clicked
//                    dialog.dismiss();
//                    FirebaseDatabase.getInstance().getReference("bookings").child(recentBookingID).removeValue();
//                    Intent intent =new Intent(MainActivity.this,Login.class);
//                    finish();
//                    startActivity(intent);
//                }
//            });
//
//            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
//                }
//            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(googleApiClient.isConnected()){
            setInitialLocation();

        }

        LocationManager service = (LocationManager) getSystemService( LOCATION_SERVICE );
        boolean enabled = service.isProviderEnabled( LocationManager.GPS_PROVIDER );

        // Check if enabled and if not send user to the GPS settings
        if (!enabled) {
            buildAlertMessageNoGps();
        }
        if(enabled){
            //mMap.animateCamera( update );
/*
            Toast.makeText( MainActivity.this, "OnResume:"+latitude+","+longitude, Toast.LENGTH_SHORT ).show();
*/



        }


    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate( R.menu.main, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected( item );
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if(isBookClicked)
            FirebaseDatabase.getInstance().getReference().child("bookings").child(recentBookingID).removeValue();
        if(id==R.id.nav_book) {
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else if(id==R.id.nav_history) {
            //FirebaseDatabase.getInstance().getReference("bookings").child(recentBookingID).removeValue();
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            intent.putExtra("ROLE","PASSENGER");
            startActivity(intent);
            finish();
        } else if(id==nav_logOut) {
            //FirebaseDatabase.getInstance().getReference("bookings").child(recentBookingID).removeValue();

            //FirebaseAuth.getInstance().signOut();

            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
            finish();
        } else if(id==nav_profile) {
            //FirebaseDatabase.getInstance().getReference("bookings").child(recentBookingID).removeValue();
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById( R.id.drawer_layout );
        drawer.closeDrawer( GravityCompat.START );
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.map_style ) );

            if (!success) {
                Log.e( TAG, "Style parsing failed." );
            }
        } catch (Resources.NotFoundException e) {
            Log.e( TAG, "Can't find style. Error: ", e );
        }


        if (ActivityCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then over   riding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        driverMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(50,20)));


        //This line will show your current location on Map with GPS dot
        mMap.setMyLocationEnabled( true );
        locationButton();

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                isMapClick=true;
                if(book_button.getText().toString().equals(CODE_BOOK)) {
                    try {
                        mMap.clear();
                        List<Address> addressList = geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);
                        txtDropOff.setText(addressList.get(0).getAddressLine(0));

                        positionUpdate = new LatLng( latLng.latitude,latLng.longitude );
                        CameraUpdate update = CameraUpdateFactory.newLatLngZoom( positionUpdate, 20 );
                        destinationMarker = mMap.addMarker(new MarkerOptions().position(positionUpdate).icon(BitmapDescriptorFactory.fromResource(R.drawable.finish_flag)));
                        mMap.animateCamera( update );

                    } catch (IOException e) {
                        e.printStackTrace();
                        txtDropOff.setText("");
                    }
                }
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if(isBookClicked) {
                    showCustomDialog(thisSession);
                }
                return false;
            }
        });
    }

    private void setupLocationManager() {
        //buildGoogleApiClient();
        if (googleApiClient == null) {

            googleApiClient = new GoogleApiClient.Builder( this )
                    .addConnectionCallbacks( this )
                    .addOnConnectionFailedListener( this )
                    .addApi( LocationServices.API )
                    .addApi( Places.GEO_DATA_API )
                    .addApi( Places.PLACE_DETECTION_API )
                    .build();
            //mGoogleApiClient = new GoogleApiClient.Builder(this);
        }
        googleApiClient.connect();
        createLocationRequest();
    }

    protected void createLocationRequest() {

        request = new LocationRequest();
        request.setSmallestDisplacement( 10 );
        request.setFastestInterval( 50000 );
        request.setPriority( LocationRequest.PRIORITY_HIGH_ACCURACY );
        request.setNumUpdates( 3 );

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest( request );
        builder.setAlwaysShow( true );

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings( googleApiClient,
                        builder.build() );


        result.setResultCallback( new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {

                    case LocationSettingsStatusCodes.SUCCESS:
                        setInitialLocation();
                        break;

                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied, but this can be fixed
                        // by showing the user a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(
                                    MainActivity.this,
                                    REQUEST_CHECK_SETTINGS );
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;

                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way
                        // to fix the settings so we won't show the dialog.
                        break;
                }
            }
        } );


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d( "onActivityResult()", Integer.toString( resultCode ) );

        //final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK: {

                        setInitialLocation();

                        Toast.makeText( MainActivity.this, "Location enabled", Toast.LENGTH_LONG ).show();
                        mRequestingLocationUpdates = true;
                        break;
                    }
                    case Activity.RESULT_CANCELED: {
                        // The user was asked to change settings, but chose not to
                        Toast.makeText( MainActivity.this, "Location not enabled", Toast.LENGTH_LONG ).show();
                        mRequestingLocationUpdates = false;
                        break;
                    }
                    default: {
                        break;
                    }
                }
                break;
        }
    }

    private void setInitialLocation() {


        if (ActivityCompat.checkSelfPermission( MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission( MainActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates( googleApiClient, request, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                mLastLocation = location;
                double lat=location.getLatitude();
                double lng=location.getLongitude();

                MainActivity.this.latitude=lat;
                MainActivity.this.longitude=lng;

                try {
                    if(now !=null){
                        now.remove();
                    }
                    LatLng positionUpdate = new LatLng( MainActivity.this.latitude,MainActivity.this.longitude );
                    CameraUpdate update = CameraUpdateFactory.newLatLngZoom( positionUpdate, 15 );
                    //now=mMap.addMarker(new MarkerOptions().position(positionUpdate)
                    //      .title("Your Location"));

                    mMap.animateCamera( update );
                    //myCurrentloc.setText( ""+latitude );


                } catch (Exception ex) {

                    ex.printStackTrace();
                    Log.e( "MapException", ex.getMessage() );

                }

                //Geocode current location details
                try {
                    geocoder = new Geocoder(MainActivity.this, Locale.ENGLISH);
                    addresses = geocoder.getFromLocation(latitude, longitude, 1);
                    StringBuilder str = new StringBuilder();
                    if (Geocoder.isPresent()) {
                        /*Toast.makeText(getApplicationContext(),
                                "geocoder present", Toast.LENGTH_SHORT).show();*/
                        android.location.Address returnAddress = addresses.get(0);

                        String localityString = returnAddress.getAddressLine (0);
                        //String city = returnAddress.getAddressLine(1);
                        //String region_code = returnAddress.getAddressLine(2);
                        //String zipcode = returnAddress.getAddressLine(3);

                        str.append( localityString ).append( "" );
                        // str.append( city ).append( "" ).append( region_code ).append( "" );
                        // str.append( zipcode ).append( "" );

                        myCurrentloc.setText(str);
//                        Toast.makeText(getApplicationContext(), str,
//                                Toast.LENGTH_SHORT).show();

                    } else {
                    /*    Toast.makeText(getApplicationContext(),
                                "geocoder not present", Toast.LENGTH_SHORT).show();*/
                    }

// } else {
// Toast.makeText(getApplicationContext(),
// "address not available", Toast.LENGTH_SHORT).show();
// }
                } catch (IOException e) {
// TODO Auto-generated catch block

                    Log.e("tag", e.getMessage());
                }



            }

        } );
    }

    private void CheckMapPermission() {


        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {

            if (ActivityCompat.checkSelfPermission( MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission( MainActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions( MainActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1002 );
            } else {

                setupLocationManager();
            }
        } else {
            setupLocationManager();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult( requestCode, permissions, grantResults );


        switch (requestCode) {
            case 1002: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION )
                            == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission( this,
                            Manifest.permission.ACCESS_COARSE_LOCATION ) == PackageManager.PERMISSION_GRANTED) {

                        setupLocationManager();

                    }
                } else {

                    Toast.makeText( MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT ).show();
                    //finish();
                }
            }
            break;
        }

    }

    public void getLatLang(String placeId) {
        Places.GeoDataApi.getPlaceById( googleApiClient, placeId )
                .setResultCallback( new ResultCallback<PlaceBuffer>() {
                    @Override
                    public void onResult(PlaceBuffer places) {
                        if (places.getStatus().isSuccess() && places.getCount() > 0) {
                            final Place place = places.get( 0 );

                            LatLng latLng = place.getLatLng();

                            try {

                                CameraUpdate update = CameraUpdateFactory.newLatLngZoom( latLng, 15 );
                                mMap.animateCamera( update );


                            } catch (Exception ex) {

                                ex.printStackTrace();
                                Log.e( "MapException", ex.getMessage() );

                            }

                            Log.i( "place", "Place found: " + place.getName() );
                        } else {
                            Log.e( "place", "Place not found" );
                        }
                        places.release();
                    }
                } );
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //AlertMessageNoGps();


    }


    @Override
    public void onConnectionSuspended(int i) {
        //checkLocaionStatus();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        MainActivity.this.latitude=location.getLatitude();
        MainActivity.this.longitude=location.getLongitude();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public Loader<Object> onCreateLoader(int i, Bundle bundle) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Object> loader, Object o) {

    }

    @Override
    public void onLoaderReset(Loader<Object> loader) {

    }

    //GET CURRENT LOCATION BUTTON POSITION....
    private void locationButton() {

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById( map );

        View locationButton = ((View) mapFragment.getView().findViewById( Integer.parseInt( "1" ) ).
                getParent()).findViewById( Integer.parseInt( "2" ) );
        if (locationButton != null && locationButton.getLayoutParams() instanceof RelativeLayout.LayoutParams) {
            // location button is inside of RelativeLayout
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();

            // Align it to - parent BOTTOM|LEFT
            params.addRule( RelativeLayout.ALIGN_PARENT_BOTTOM );
            params.addRule( RelativeLayout.ALIGN_PARENT_LEFT );
            params.addRule( RelativeLayout.ALIGN_PARENT_RIGHT, 0 );
            params.addRule( RelativeLayout.ALIGN_PARENT_TOP, 0 );

            // Update margins, set to 10dp
            final int margin = (int) TypedValue.applyDimension( TypedValue.COMPLEX_UNIT_DIP, 150,
                    getResources().getDisplayMetrics() );
            params.setMargins( margin, margin, margin, margin );

            locationButton.setLayoutParams( params );
        }

    }



    protected void buildAlertMessageNoGps() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please Turn ON your GPS Connection")
                .setTitle( "GPS Not Enabled" )
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private Address setDestination() {

        try {
            //List<Address> addresses = new ArrayList<>();
            // addressList = geocoder.getFromLocationName(paramLocString,1);
            if(isMapClick) {
                addresses = geocoder.getFromLocation(destinationMarker.getPosition().latitude,destinationMarker.getPosition().longitude,1);
                return addresses.get(0);
            } else if(!txtDropOff.getText().toString().equals("")) {
                addresses = geocoder.getFromLocationName(txtDropOff.getText().toString(),1,calambaBounds.southwest.latitude,calambaBounds.southwest.longitude,calambaBounds.northeast.latitude,calambaBounds.northeast.longitude);
                positionUpdate= new LatLng(addresses.get(0).getLatitude(),addresses.get(0).getLongitude());
                return addresses.get(0);
            }
//            for (Address address : addresses) {
//                LatLng location = new LatLng(address.getLatitude(), address.getLongitude());
//                if (calambaBounds.contains(location)) {
//                    return address;
//                }
//            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&key=AIzaSyDNtPbtC0utrTJNz51MJPhC2290Byx51po";

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
        return url;
    }

    private double displayFare(double distance) {

        double fare = 0.00;
        double distanceKm = distance/1000;
        if(distanceKm<=4) {
            fare = priceFare * numPassenger;
        } else {
            int extraDistance = (int) (distanceKm - 4);
            fare = ((extraDistance*5)+(priceFare*numPassenger));
        }

        if(!loggedInUser.getCategory().equals("Regular")) {
            fare = Math.ceil(fare * .90);
        }

        distanceText.setText(distanceKm+"");
        priceText.setText(fare+"");
        numPassengerTxt.setText(numPassenger+"");
        updateFirebase(fare,distanceKm);
        return fare;
    }

    private void updateFirebase(double paramFare, double paramDistance) {
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference myBookingsRef = database.getReference().child("bookings");
        if(!isCancelled)
            recentBookingID = myBookingsRef.push().getKey();
        isCancelled = false;
        isBookClicked = true;
        LatLngDefined l1 = new LatLngDefined(myPosition.latitude,myPosition.longitude);
        LatLngDefined l2 = new LatLngDefined(positionUpdate.latitude,positionUpdate.longitude);
        myBookingObj = new Booking(recentBookingID,loggedInUser, null,LocalDateTime.now().toString(),l1,l2,false,false, paramFare, paramDistance,myCurrentloc.getText().toString(),txtDropOff.getText().toString(),txtMyNote,false,false,numPassenger);
        myBookingsRef.child(recentBookingID).setValue(myBookingObj);

        waitingDriverbuilder = new AlertDialog.Builder(MainActivity.this);
        waitingDriverbuilder.setTitle("Booking successful!");
        waitingDriverbuilder.setMessage("Please wait for a driver...");
        waitingDriverbuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do something when the OK button is clicked
                waitingDriverDialog.dismiss();
            }
        });
        waitingDriverDialog = waitingDriverbuilder.create();
        waitingDriverDialog.show();

        myBookingsRef.child(recentBookingID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    Booking booking = dataSnapshot.getValue(Booking.class);
                    myBookingObj = booking;
                    if(booking.getIsCancelled() && !isCancelled) {
                        waitingDriverDialog.dismiss();
                        isCancelled = true;
                        Toast.makeText(getBaseContext(), "You have cancelled your booking, please book again.", Toast.LENGTH_SHORT).show();
                        isBookClicked = false;
                        //book_button.setText(CODE_CANCEL);
                        //book_button.setBackgroundColor(getColor(R.color.colorRed));
                        //driverMarker.remove();
                        //txtDropOff.setEnabled(true);
                        //txtDropOff.setText("");
                        //priceText.setText("0.00");
                        //distanceText.setText("0");
                    } else if(booking.getIsAccepted() && !booking.getIsArrived()) {
                        //book_button.setText(CODE_DRIVER_WAIT);
                        isCancelled = false;
                        waitingDriverDialog.dismiss();
                        book_button.setBackgroundColor(getColor(R.color.colorRed));
                        Toast.makeText(getBaseContext(), "Driver found! Click Accept or Reject.", Toast.LENGTH_SHORT).show();

                        database.getReference("sessions").child(booking.getBookingID()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if(dataSnapshot.exists()) {
                                    Session sessionx = dataSnapshot.getValue(Session.class);
                                    if(!sessionx.isBookingEmpty() && !sessionx.getIsAccepted()) {
                                        showCustomDialog(sessionx);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    } else if(booking.getIsAccepted() && booking.getIsArrived()) {
                        //priceText.setText(0);
                        //distanceText.setText(0);
                        //mMap.clear();
                        //txtDropOff.setText("");
                        //book_button.setText(CODE_BOOK);
                        //book_button.setBackgroundColor(getColor(R.color.green));
                        Intent intent = new Intent( MainActivity.this, DoneActivity.class );

                        intent.putExtra("BOOKING_ID", booking.getBookingID());
                        intent.putExtra("ROLE","PASSENGER");
                        finish();
                        startActivity( intent );
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showCustomDialog(Session mb) {

        View dialogView = getLayoutInflater().inflate(R.layout.marker_custom_window, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        Button btnOpenChat = dialogView.findViewById(R.id.btnOpenChat);

        if(mb.getIsAccepted())
            btnOpenChat.setVisibility(View.VISIBLE);
        else
            btnOpenChat.setVisibility(View.GONE);

        btnOpenChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create the chat dialog box
                //Dialog chatDialog = new Dialog(MainActivity.this);
                //chatDialog.setContentView(R.layout.chat_dialog);

                // Get references to the views in the chat dialog box
                //listViewChatBox = chatDialog.findViewById(R.id.list_view_chat);
                //MessageAdapter messageAdapter = new MessageAdapter(MainActivity.this, messageArrayList);
                //listViewChatBox.setAdapter(messageAdapter);
                chatDialog.show();
            }
        });

        CircleImageView profImg = dialogView.findViewById(R.id.marker_img);

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imgRef = storageRef.child("images/" + mb.getDriver().getUserID() + ".jpg");
        imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(profImg.getContext())
                        .load(uri)
                        .fitCenter()
                        .into(profImg);
            }
        });

        TextView rating = dialogView.findViewById(R.id.driverRating);
        FirebaseDatabase.getInstance().getReference().child("driverRatings").child(mb.getDriver().getUserID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    rating.setText(dataSnapshot.getValue(Integer.class)+" stars");
                } else
                    rating.setText("No ratings yet");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        dialogView.findViewById(R.id.for_driver_layout).setVisibility(View.VISIBLE);

        dialogView.findViewById(R.id.txt_driver_found).setVisibility(View.VISIBLE);
        TextView name = dialogView.findViewById(R.id.marker_name);
        name.setText(mb.getDriver().getFullname());

        TextView phone = dialogView.findViewById(R.id.marker_phone_number);
        phone.setText(mb.getDriver().getPhone());

        TextView plateNum = dialogView.findViewById(R.id.marker_plate);
        plateNum.setText(mb.getDriver().getDriver().getPlateNumber());

        TextView note = dialogView.findViewById(R.id.txtPassengerNote);
        note.setText(mb.getBooking().getNote());

        Button button = dialogView.findViewById(R.id.marker_btn_accept);
        Button buttonCancel = dialogView.findViewById(R.id.marker_btn_cancel);

        button.setText("Accept");
        buttonCancel.setText("Reject");

        if(mb.getIsAccepted()) {
            button.setVisibility(View.GONE);
            buttonCancel.setVisibility(View.GONE);
        } else {
            button.setVisibility(View.VISIBLE);
            buttonCancel.setVisibility(View.VISIBLE);
        }

       // EditText txtMessage = dialogView.findViewById(R.id.txtMessage);
       // Button btnSendMessage = dialogView.findViewById(R.id.sendMessage);
        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();
        dialog.show();

//        btnSendMessage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Message message = new Message(mb.getBooking().getUser(),mb.getDriver(),txtMessage.getText().toString(),LocalDateTime.now().toString());
//                String key = FirebaseDatabase.getInstance().getReference("messages").push().getKey();
//                FirebaseDatabase.getInstance().getReference("messages").child(mb.getBooking().getBookingID()).child(key).setValue(message);
//                dialog.dismiss();
//            }
//        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listenToChat();
                // Handle button click event
                linearLayoutnearby.setVisibility(View.GONE);
                FirebaseDatabase.getInstance().getReference().child("sessions").child(recentBookingID).child("isAccepted").setValue(true);

                dialog.dismiss();
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnOpenChat.setVisibility(View.GONE);
                cancelBook();
                dialog.dismiss();
            }
        });

    }

    private void showCustomBookDialog(String myDestination) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.add_note, null);

        EditText note = dialogView.findViewById(R.id.txtNote);
        Button button = dialogView.findViewById(R.id.customBtnBook);
        Button button2 = dialogView.findViewById(R.id.customBtnCancel);
        Spinner spinnerNumPassenger = dialogView.findViewById(R.id.spinnerNumPassenger);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNumPassenger.setAdapter(adapter);

        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();
        dialog.show();

        spinnerNumPassenger.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                numPassenger = Integer.parseInt(adapterView.getItemAtPosition(i).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                txtMyNote = note.getText().toString();
                mMap.clear();
                book_button.setText(CODE_CANCEL);
                book_button.setBackgroundColor(getColor(R.color.colorRed));

                txtDropOff.setText(myDestination);
                txtDropOff.setEnabled(false);
                myPosition = new LatLng(MainActivity.this.latitude,MainActivity.this.longitude);

                //positionUpdate = new LatLng( destinationResult.getLatitude(), destinationResult.getLongitude() );
                String directionUrl = getDirectionsUrl(myPosition,positionUpdate);

                //AsyncDirectionsAPI asyncDirectionsAPI = new AsyncDirectionsAPI();
                //asyncDirectionsAPI.execute(directionUrl);

                //double distanceVal = 2;
                // creating a new variable for our request queue
                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, directionUrl, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            double distanceVal = 0;
                            // now we get our response from API in json object format.
                            // in below line we are extracting a string with its key
                            // value from our json object.
                            // similarly we are extracting all the strings from our json object.

                            JSONArray routesObjArray = response.getJSONArray("routes");
                            JSONObject distanceObj = routesObjArray.getJSONObject(0);

                            JSONArray c = distanceObj.getJSONArray("legs");
                            for (int i = 0 ; i < distanceObj.length(); i++) {
                                JSONObject obj = c.getJSONObject(i);
                                JSONObject distanceFinal =  obj.getJSONObject("distance");
                                distanceVal = distanceFinal.getDouble("value");
                                break;
                            }

                            displayFare(distanceVal);
                        } catch (JSONException e) {
                            // if we do not extract data from json object properly.
                            // below line of code is use to handle json exception
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    // this is the error listener method which
                    // we will call if we get any error from API.
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // below line is use to display a toast message along with our error.
                        //Toast.makeText(MainActivity.this, "Fail to get data..", Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("No location found!");
                        builder.setMessage("Please enter a location within Sariaya, Quezon only.");
                        AlertDialog dialog = builder.create();

                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Do something when the OK button is clicked
                                dialog.dismiss();
                            }
                        });
                    }
                });
                // at last we are adding our json
                // object request to our request
                // queue to fetch all the json data.
                queue.add(jsonObjectRequest);

                destinationMarker = mMap.addMarker(new MarkerOptions().position(positionUpdate)
                        .title("Destination")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.finish_flag))
                );

                CameraUpdate update = CameraUpdateFactory.newLatLngZoom( positionUpdate, 15 );
                mMap.animateCamera(update);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle button click event
                cancelBook();
                book_button.setText(CODE_BOOK);
                dialog.dismiss();
            }
        });
    }

    private void cancelBook() {
        //isCancelled = false;
        isBookClicked = false;
        linearLayoutnearby.setVisibility(View.VISIBLE);
        mMap.clear();
        book_button.setText(CODE_BOOK);
        book_button.setBackgroundColor(getColor(R.color.green));
        txtDropOff.setEnabled(true);
        txtDropOff.setText("");
        priceText.setText("0.00");
        distanceText.setText("0");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //database.getReference("bookings").child(recentBookingID).removeValue();
        HashMap<String, Object> myUpdates =  new HashMap();
        myUpdates.put("isClicked", false);
        myUpdates.put("isAccepted", false);
        myUpdates.put("isCancelled",true);
        database.getReference("bookings").child(recentBookingID).updateChildren(myUpdates);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(new LatLng(MainActivity.this.latitude,MainActivity.this.longitude) , 15 );
        mMap.animateCamera(update);
    }

    private void findNearbyDrivers() {

        FirebaseDatabase.getInstance().getReference().child("driverLocations").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!isBookClicked) {
                    listDriverLocationsObject.clear();
                    LatLng center = new LatLng(MainActivity.this.latitude,MainActivity.this.longitude);
                    Location start = new Location("");
                    start.setLatitude(MainActivity.this.latitude);
                    start.setLongitude(MainActivity.this.longitude);

                    for (DataSnapshot driverLocationSnapshot: dataSnapshot.getChildren()) {
                        DriverLocation driverLocation = driverLocationSnapshot.getValue(DriverLocation.class);
                        LatLng driverLoc = new LatLng(driverLocation.getLocation().getLatitude(),driverLocation.getLocation().getLongitude());

                        if(isWithinRadius(driverLoc,center,2) && driverLocation.getIsActive()) {

                            Location end = new Location("");
                            end.setLatitude(driverLoc.latitude);
                            end.setLongitude(driverLoc.longitude);
                            double dist = start.distanceTo(end)/1000;
                            double time = (dist/20) * 60;

                            //String eta = "ETA: "+time+" mins \nDistance: "+(dist/1000)+" km";
                            String eta = String.format("%.2f",time);
                            String sDist = String.format("%.2f",(dist));

                            DriverLocationObject driverLocationObject = new DriverLocationObject(driverLocation,"ETA: "+eta+" mins\nDistance: "+sDist+" km");
                            listDriverLocationsObject.add(driverLocationObject);
                        }
                    }
                    DriverNearList adapter = new DriverNearList(MainActivity.this, listDriverLocationsObject);
                    listViewDriverLocations.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static double haversine(double lat1, double lng1, double lat2, double lng2) {

        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.sin(dLng / 2) * Math.sin(dLng / 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return 6371 * c;
    }

    public static boolean isWithinRadius(LatLng point, LatLng center,double radius) {
        double distance = haversine(point.latitude, point.longitude, center.latitude, center.longitude);
        return distance <= radius;
    }

    public void listenToChat() {

        DatabaseReference messageRef = FirebaseDatabase.getInstance().getReference("messages").child(recentBookingID);

        messageRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(isBookClicked) {
                    Message message = dataSnapshot.getValue(Message.class);
                    messageArrayList.add(message);
                    MessageAdapter messageAdapter = new MessageAdapter(MainActivity.this, messageArrayList);
                    listViewChatBox.setAdapter(messageAdapter);

                    if(message.getReceiver().equals(mAuth.getUid()))
                        Toast.makeText(MainActivity.this,"You have a message", Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception while downloading url", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            String data = "";

            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            parserTask.execute(result);

        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            //MarkerOptions markerOptions = new MarkerOptions();

            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                List<HashMap<String, String>> path = result.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(getColor(R.color.blue));
            }

            // Drawing polyline in the Google Map for the i-th route
            mMap.addPolyline(lineOptions);
        }
    }

    private void loadPlaces() throws IOException {
        Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
        List<Address> addresses = geocoder.getFromLocationName(
                txtDropOff.getText().toString(),
                5,
                calambaBounds.southwest.latitude,
                calambaBounds.southwest.longitude,
                calambaBounds.northeast.latitude,
                calambaBounds.northeast.longitude);
        List<String> xaddressList = new ArrayList<>();

        for(Address address: addresses) {
            xaddressList.add(address.getAddressLine(0));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_dropdown_item_1line, xaddressList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        txtDropOff.setAdapter(adapter);
    }
}
