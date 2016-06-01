package nodomain.freeyourgadget.gadgetbridge.activities;

import android.graphics.Point;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import nodomain.freeyourgadget.gadgetbridge.R;

//import android.app.FragmentManager;
//

/**
 * Created by user on 2016-05-20.
 */
//extends Activity
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {
    static final LatLng youngduri = new LatLng( 37.6117, 126.9952);
    static final LatLng cs_office = new LatLng( 37.6101, 126.9974);

    private GoogleMap map;

    private LocationManager locationManager = null;
    private Double longitude;
    private Double latitude;

    private GpsInfo gps;

    private int timer = 0;
    int count = 0;
    private LatLng ex_point;
    private LatLng current_point;
    private boolean isInit = false;
    Handler handler;

    //double mission_latitude = 37.61005;
    //double mission_longitude = 126.99758;
    double mission_latitude = 37.610019;
    double mission_longitude = 126.996627;
    //static double mission_latitude = 37.61005;
    //static double mission_longitude = 126.99758;
    //static final LatLng lab = new LatLng( 37.61005, 126.99758);

    boolean mission_flag = true;
    /*
    private Double home_latitude;
    private Double home_longitude;
    Geocoder coder;
    String location;
    */
    //coder = new Geocoder(this);
    //map = ((SupportMapFragment) getSupportFragmentManager().findFragmentByld(R.id.map)).getMap();
    //Toast.makeText(getApplicationContext(), "the position of check. Please wait..", Toast.LENGTH_SHORT).show();
    MarkerOptions optnext_Mission = new MarkerOptions();
    public void next_mission(double latitude, double longitude) {

        LatLng next_Mission = new LatLng(latitude, longitude);
        optnext_Mission.position(next_Mission);
        optnext_Mission.title("Mission");
        optnext_Mission.icon(BitmapDescriptorFactory.fromResource(R.drawable.mission));
        map.addMarker(optnext_Mission).showInfoWindow();
        //map.addMarker(optnext_Mission).hideInfoWindow();

        mission_flag = true;

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        /*
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                .getMap();
        Marker seoul = map.addMarker(new MarkerOptions().position(SEOUL)
                .title("Seoul"));

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(SEOUL, 15));

        map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
        */
        /*
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//화면 회전 세로로고정
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        gps = new GpsInfo(MapsActivity.this);
        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(myIntent);

        latitude = gps.getLatitude();
        longitude = gps.getLongitude();
        MapFragment mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        */


        MapsInitializer.initialize(getApplicationContext());
        FragmentManager myFragmentManager = getSupportFragmentManager();
        SupportMapFragment mySupportMapFragment = (SupportMapFragment)myFragmentManager.findFragmentById(R.id.map);
        map = mySupportMapFragment.getMap();
        ex_point = new LatLng(0, 0);
        current_point = new LatLng(0, 0);
        final Button btn_start = (Button)this.findViewById(R.id.btn_start);
        final Button btn_finish = (Button)this.findViewById(R.id.btn_finish);

        init();
        map.setOnMapClickListener(this);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getId() == R.id.btn_start) {
                    handler = new Handler() {
                        @Override
                    public void handleMessage(Message msg) {
                            handler.sendEmptyMessageDelayed(0, 1000);
                            TextView tv = (TextView)findViewById(R.id.tv_timer);
                            TextView mission = (TextView)findViewById(R.id.mission_count);
                            timer++;
                            tv.setText("Timer :  " + timer);
                            mission.setText("Clear : " + count);
                            GpsInfo gps = new GpsInfo(MapsActivity.this);

                            if(gps.isGetLocation()) {
                                Log.d("GPS사용","찍힘");
                                double latitude = gps.getLatitude();
                                double longitude = gps.getLongitude();
                                LatLng latLng = new LatLng(latitude, longitude);
                                //double mission_latitude = 37.61005;
                                //double mission_longitude = 126.99758;
                                //MarkerOptions optnext_Mission = new MarkerOptions();

                                if((latitude - mission_latitude < 0.0001) && (longitude - mission_longitude < 0.0001) && mission_flag)
                                {
                                    count++;
                                    mission_latitude += 0.002;
                                    mission_longitude += 0.002;
                                    map.addMarker(optnext_Mission).hideInfoWindow();

                                    //handler.sendEmptyMessageDelayed(0, 1000);
                                    next_mission(mission_latitude, mission_longitude);
                                    mission_flag = false;

                                    //handler.sendEmptyMessageDelayed(0, 5000);
                                    //Intent intent = new Intent(MapsActivity.this, MissionFragment.class);
                                    //startActivity(intent);
                                    //finish();
                                }
                                //map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                                //map.animateCamera(CameraUpdateFactory.zoomTo(17));
                                current_point = latLng;
                                map.addPolyline(new
                                        PolylineOptions().color(0xFFFF0000).width(30.0f).geodesic(true).add(latLng).add(ex_point));
                                ex_point = latLng;

                                MarkerOptions optFirst = new MarkerOptions();
                                optFirst.alpha(0.5f);
                                optFirst.anchor(0.5f, 0.5f);
                                optFirst.position(latLng);
                                optFirst.title("Current Position");
                                optFirst.snippet("Snippet");

                                optFirst.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
                                map.addMarker(optFirst).showInfoWindow();
                            }
                        }
                    };
                    handler.sendEmptyMessage(0);
                }

            }
        });
        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getId() == R.id.btn_finish) {
                    handler.removeMessages(0);
                }
            }
        });
    }
    public void onMapClick(LatLng point) {
        if(!isInit)
            init();
        Log.d("터치 이벤트", "터치");
        Point screenPt = map.getProjection().toScreenLocation(point);
        GpsInfo gps = new GpsInfo(MapsActivity.this);
        if(gps.isGetLocation()) {
            Log.d("GPS사용","찍힘");
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            LatLng latLng = new LatLng(latitude, longitude);
            map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            map.animateCamera(CameraUpdateFactory.zoomTo(17));
            current_point = latLng;
            map.addPolyline(new PolylineOptions().color(0xFFFF0000).width(30.0f).geodesic(true).add(latLng).add(ex_point));
            ex_point = latLng;

            Log.d("맵좌표", "좌표: 위도(" + String.valueOf(point.latitude) + "), 경도(" + String.valueOf(point.longitude) + ")");
            Log.d("화면좌표", "화면좌표: x(" + String.valueOf(screenPt) + "), Y(" + String.valueOf(screenPt.y) + ")");

            MarkerOptions optFirst = new MarkerOptions();
            optFirst.alpha(0.5f);
            optFirst.anchor(0.5f, 0.5f);
            optFirst.position(latLng);
            optFirst.title("Current Position");
            optFirst.snippet("Snippet");
            optFirst.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
            map.addMarker(optFirst).showInfoWindow();
        }
    }
    private void init() {
        GooglePlayServicesUtil.isGooglePlayServicesAvailable(MapsActivity.this);
        GoogleMap mGoogleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        GpsInfo gps = new GpsInfo(MapsActivity.this);

        //
        MarkerOptions optMission = new MarkerOptions();
        //optMission.alpha(0.5f);
        //optMission.anchor(0.5f, 0.5f);
        optMission.position(youngduri);
        optMission.title("youngduri");
        //optMission.snippet("Snippet");
        optMission.icon(BitmapDescriptorFactory.fromResource(R.drawable.mission));
        map.addMarker(optMission).showInfoWindow();
        //
        optMission.position(cs_office);
        optMission.title("cs_office");
        optMission.icon(BitmapDescriptorFactory.fromResource(R.drawable.mission));
        map.addMarker(optMission).showInfoWindow();
        next_mission(mission_latitude, mission_longitude);
/*
        optMission.position(lab);
        optMission.title("Mission");
        optMission.icon(BitmapDescriptorFactory.fromResource(R.drawable.mission));
        map.addMarker(optMission).showInfoWindow();
*/
        //
        //map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        //Marker Youngduri = map.addMarker(new MarkerOptions().position(youngduri).title("Mission"));

        if(gps.isGetLocation()) {
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            LatLng latLng = new LatLng(latitude, longitude);
            ex_point = latLng;
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(17));

            MarkerOptions optFirst = new MarkerOptions();
            optFirst.alpha(0.5f);
            optFirst.anchor(0.5f, 0.5f);
            optFirst.position(latLng);
            optFirst.title("Start");
            optFirst.snippet("Snippet");
            optFirst.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
            map.addMarker(optFirst).showInfoWindow();
            isInit = true;
        }
    }
    @Override
    public void onMapReady(GoogleMap map) {

        LatLng myLocation = new LatLng(latitude, longitude);
        Log.d("mr", "la: "+latitude);
        Log.d("mr", "lo: " + longitude);
        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 17));
        //마커로 현재 위치 표시
        map.addMarker(new MarkerOptions()
                .title("현재 위치")
                .snippet("innoaus.")
                .position(myLocation));
    }

    /*
    public void setMyPosition()
    {
        LocationManager manager = (LocationManager) getSystempService(this.LOCATION_SERVICE);
        LocationListener locationlistener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if(changed_check == 0)
                {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();

                    LatLng startingPoint = new  LatLng(latitude, longitude);
                    Log.i("My Position :",latitude + "," + longitude);
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(startingPoint, 12));
                    Toast.makeText(getApplicationContext(), "This location search is completed.", Toast.LENGTH_SHORT).show();

                    map.addCircle(new CircleOptions().center(new LatLng(latitude, longitude)).radius(350).strokeColor(Color.RED).fillColor(Color.BLACK));
                    changed_check = 1;
                    arrayPoints = new ArrayList<LatLng>();
                }
                else
                {

                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.i("My Position2 :", latitude + "," + longitude);
            }

            @Override
            public void onProviderEnabled(String provider) {
                Log.i("My Position1 :",latitude + "," + longitude);
            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission())
        {
            return;
        }
        manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationlistener);
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationlistener);
    }
    */
    /*
    public void onResume()
    {
        super.onResume();
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION))
        {
            return;
        }
        map.setMyLocationEnabled(true);
    }
    */
    /*
    public void setMyHome_maker()
    {
        location = "경기도 고양시 일산구";
        List<Address> list = null;

        try
        {
            list = coder.getFromLocationName(location, 10);
        }
        catch(IOException ioe)
        {
            Toast.makeText(getApplicationContext(), "Location Search error", Toast.LENGTH_SHORT).show();
        }
        if(list != null)
        {
            home_latitude = list.get(0).getLatitude();
            home_longitude = list.get(0).getLongitude();

            MarkerOptions marker = new MarKerOptions();

            marker.position(new LatLng(home_latitude, home_longitude));
            marker.title("My Homew");
            marker.draggable(false);
            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.Home_marker));
            map.addMarker(marker);
        }
    }
    */
}
