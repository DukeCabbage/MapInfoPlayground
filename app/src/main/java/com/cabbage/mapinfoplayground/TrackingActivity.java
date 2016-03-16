package com.cabbage.mapinfoplayground;

import android.animation.ArgbEvaluator;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.m4b.maps.GoogleMap;
import com.google.android.m4b.maps.OnMapReadyCallback;
import com.google.android.m4b.maps.SupportMapFragment;

import butterknife.Bind;
import butterknife.BindColor;
import butterknife.ButterKnife;
import timber.log.Timber;

public class TrackingActivity extends AppCompatActivity
        implements OnMapReadyCallback {

    @Bind(R.id.toolbar) Toolbar mToolbar;

    @Bind(R.id.bottom_sheet) ViewGroup bottomSheet;
    @Bind(R.id.bottom_sheet_label) ViewGroup bottomSheetLabel;
    @Bind(R.id.tv_company_name) TextView tvCompanyName;
    @Bind(R.id.tv_car_number) TextView tvCarNumber;
    @Bind(R.id.iv_arrow) ImageView ivArrow;

    @BindColor(R.color.colorWhite) int colorWhite;
    @BindColor(R.color.colorBlack) int colorBlack;
    @BindColor(R.color.colorAccent) int colorAccent;

    private SupportMapFragment mMapFragment;
    private GoogleMap mGoogleMap;

    private ArgbEvaluator mArgbEvaluator = new ArgbEvaluator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);
        ButterKnife.bind(this);

        setUpAppBar();
        setUpBottomSheet();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void setUpAppBar() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.title_tracking);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mToolbar.setNavigationOnClickListener((View v) -> finish());
        } else {
            throw new RuntimeException("Can not find toolbar");
        }
    }

    private void setUpBottomSheet() {
        // State change callback
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                Timber.d("new State: " + newState);
                switch (newState) {
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        // Light background, dark text, arrow up
                        bottomSheetLabel.setBackgroundColor(colorWhite);
                        tvCompanyName.setTextColor(colorBlack);
                        tvCarNumber.setTextColor(colorBlack);

                        Drawable arrowUp = ContextCompat.getDrawable(TrackingActivity.this, R.drawable.ic_keyboard_arrow_up_18dp);
                        arrowUp = DrawableCompat.wrap(arrowUp);
                        DrawableCompat.setTint(arrowUp, colorBlack);
                        ivArrow.setImageDrawable(arrowUp);
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        // Dark background, light text, arrow down
                        bottomSheetLabel.setBackgroundColor(colorAccent);
                        tvCompanyName.setTextColor(colorWhite);
                        tvCarNumber.setTextColor(colorWhite);

                        Drawable arrowDown = ContextCompat.getDrawable(TrackingActivity.this, R.drawable.ic_keyboard_arrow_down_18dp);
                        arrowDown = DrawableCompat.wrap(arrowDown);
                        DrawableCompat.setTint(arrowDown, colorWhite);
                        ivArrow.setImageDrawable(arrowDown);
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // Collapsed: offset = 0.0, Expanded: offset = 1.0
                Timber.d("slide offset: " + slideOffset);

                // Screw the slideOffset, such that if favors 'expanded' state
                float mappedOffset = (float) Math.pow((slideOffset - 1.0), 9.0) + 1;
                Timber.d("new offset: " + mappedOffset);
                int backgroundColor = (Integer) mArgbEvaluator.evaluate(mappedOffset, colorWhite, colorAccent);
                int textColor = (Integer) mArgbEvaluator.evaluate(mappedOffset, colorBlack, colorWhite);
                bottomSheetLabel.setBackgroundColor(backgroundColor);
                tvCompanyName.setTextColor(textColor);
                tvCarNumber.setTextColor(textColor);
            }
        });

        // Onclick callback
        bottomSheetLabel.setOnClickListener((View v) -> {
            Timber.d("onClick");
            BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
            switch (bottomSheetBehavior.getState()) {
                case BottomSheetBehavior.STATE_COLLAPSED:
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    break;
                case BottomSheetBehavior.STATE_EXPANDED:
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    break;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_tracking, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_refresh:
                Toast.makeText(this, "Refresh", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mGoogleMap = googleMap;
    }
}
