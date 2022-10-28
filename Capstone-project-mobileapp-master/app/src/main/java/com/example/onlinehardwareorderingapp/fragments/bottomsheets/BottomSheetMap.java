package com.example.onlinehardwareorderingapp.fragments.bottomsheets;

import android.app.Dialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.example.onlinehardwareorderingapp.R;
import com.example.onlinehardwareorderingapp.interfaces.callbacks.Invoker;
import com.example.onlinehardwareorderingapp.models.BranchModel;
import com.example.onlinehardwareorderingapp.models.ui.LatLong;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

public class BottomSheetMap extends BottomSheetDialogFragment {
    BottomSheetDialog bottomSheetDialog;
    BottomSheetBehavior<View> behavior;
    View view;
    private boolean isClickable;
    private double lat;
    private double lng;
    private ArrayList<BranchModel> branches;
    private int count;
    private Invoker<LatLong, String> listener;
    public BottomSheetMap(){}
    public BottomSheetMap(boolean isClickable, double lat, double lng){
        this.isClickable = isClickable;
        this.lat = lat;
        this.lng = lng;
    }
    public BottomSheetMap(boolean isClickable, double lat, double lng, Invoker<LatLong, String> listener){
        this.isClickable = isClickable;
        this.lat = lat;
        this.lng = lng;
        this.listener = listener;
    }
    public BottomSheetMap(int count, double lat, double lng, ArrayList<BranchModel> branches){
        this.count = count;
        this.lat = lat;
        this.lng = lng;
        this.branches = branches;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        return bottomSheetDialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.bottom_sheet_for_map, container, false);
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                if (isClickable){
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(new LatLng(lat == 0 ? 12.879721 : lat, lng == 0 ? 121.774017 : lng));
                    markerOptions.title("You");
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 20));
                    googleMap.addMarker(markerOptions);
                    googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                        @Override
                        public void onMapClick(@NonNull LatLng latLng) {
                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(latLng);
                            markerOptions.title("You");
                            listener.onClick(new LatLong(latLng.latitude, latLng.longitude), "");
                            googleMap.clear();
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                            googleMap.addMarker(markerOptions);
                        }
                    });
                }else {


                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(new LatLng(lat, lng));
                    markerOptions.title("You");

                    if (count > 0){
                        for (BranchModel branch : branches){
                            MarkerOptions markerOpt = new MarkerOptions();
                            markerOpt.position(new LatLng(branch.getLatitude(), branch.getLongitude()));
                            markerOpt.title(branch.getName() + " (" + String.format("%.2f", branch.getDistance()) + "km)");
                            googleMap.addMarker(markerOpt);
                            googleMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f));
                            googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(branch.getLatitude(), branch.getLongitude())));
                        }
                    }

                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 20));
                    googleMap.addMarker(markerOptions);

                }
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        behavior = BottomSheetBehavior.from((View) view.getParent());
        //behavior.setHideable(true);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING){
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        ImageView cancel = view.findViewById(R.id.cancel_setting);
        ImageView done = view.findViewById(R.id.done_setting);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                dismiss();
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                //behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });

        FrameLayout layout = bottomSheetDialog.findViewById(R.id.bottomSheetLayout);
        assert layout != null;
        layout.setMinimumHeight(Resources.getSystem().getDisplayMetrics().heightPixels);
    }
}
