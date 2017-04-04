package iths.com.food.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import iths.com.food.helper.db.DatabaseHelper;
import iths.com.food.model.ICategory;
import iths.com.food.model.IMeal;
import iths.com.food.R;


public class MapViewFragment extends Fragment {

    ArrayList<IMeal> allMeals = new ArrayList<>();
    private MapView mMapView;
    private GoogleMap googleMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        DatabaseHelper db = new DatabaseHelper(getActivity());
        ArrayList<ICategory> categories = db.getCategories();

        for (int i = 0; i < categories.size(); i++) {
            ArrayList<IMeal> meals = db.getCategories().get(i).getMeals();
            for(int j = 0; j < meals.size(); j++) {
                allMeals.add(meals.get(j));
            }
        }
        /**
         * Creates the frame to display the GoogleMap
         */
        mMapView = (MapView) rootView.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        /**
         *  Configures the map with the start position & camera zoom.
         */
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                markLocations();

                LatLng scandinavia = new LatLng(55.5, 15);
                CameraPosition cameraPosition = new CameraPosition.Builder().target(scandinavia).zoom(5).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                /**
                 * Obligatory code block for handling permissions
                 */
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                googleMap.setMyLocationEnabled(true);
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    /**
     * Places map markers on each longitude and latitude that is saved when taking a picture;
     */
    public void markLocations() {
        for (IMeal meal : allMeals) {
            googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(meal.getLatitude(), meal.getLongitude()))
                    .title(meal.getName()).snippet(meal.getDescription()));

        }
    }


}
