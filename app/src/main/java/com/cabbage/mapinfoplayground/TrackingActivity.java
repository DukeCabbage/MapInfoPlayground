package com.cabbage.mapinfoplayground;

import android.animation.ArgbEvaluator;
import android.databinding.DataBindingUtil;
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

import com.cabbage.mapinfoplayground.databinding.ActivityTrackingBinding;
import com.google.android.m4b.maps.GoogleMap;
import com.google.android.m4b.maps.OnMapReadyCallback;
import com.google.android.m4b.maps.SupportMapFragment;

import butterknife.Bind;
import butterknife.BindArray;
import butterknife.BindColor;
import butterknife.BindDimen;
import butterknife.ButterKnife;
import timber.log.Timber;

public class TrackingActivity extends AppCompatActivity
        implements OnMapReadyCallback {

    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.zoro_pay_banner) ViewGroup zoroPayBanner;

    @Bind(R.id.fullscreen_job_info) ViewGroup fullscreenJobInfo;

    @Bind(R.id.bottom_sheet) ViewGroup bottomSheet;
    @Bind(R.id.bottom_sheet_label) ViewGroup bottomSheetLabel;
    @Bind(R.id.tv_company_name) TextView tvCompanyName;
    @Bind(R.id.tv_car_number) TextView tvCarNumber;
    @Bind(R.id.iv_arrow) ImageView ivArrow;

    @BindColor(R.color.colorWhite) int colorWhite;
    @BindColor(R.color.colorBlack) int colorBlack;
    @BindColor(R.color.colorAccent) int colorAccent;

    @BindDimen(R.dimen.action_bar_size) int actionBarSize;

    @BindArray(R.array.trip_status) String[] tripStatus;
    @BindArray(R.array.trip_status_title) String[] tripStatusTitle;

    private BookingViewModel mViewModel;
    private TrackingDelegate mDelegate;

    private TripStatus currentTripStatus = TripStatus.BOOKED;

    private SupportMapFragment mMapFragment;
    private GoogleMap mGoogleMap;

    private ArgbEvaluator mArgbEvaluator = new ArgbEvaluator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: parse from intent
        mViewModel = new BookingViewModel(this, JobMockUp.getMockBooking());
        mDelegate = new MockTrackingDelegate(this);

        // Data binding
        ActivityTrackingBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_tracking);
        binding.setViewModel(mViewModel);
        binding.setDelegate(mDelegate);

        // Other view setups
        ButterKnife.bind(this);
        setUpAppBar();
        setUpBottomSheet();

        mMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);
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
        final BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                Timber.i("new State: " + newState);
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
                float mappedOffset = (float) Math.pow((slideOffset - 1.0), 21.0) + 1;
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
            Timber.d("bottom sheet label onClick");
            BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

            Timber.d("current state: " + bottomSheetBehavior.getState());
            switch (bottomSheetBehavior.getState()) {
                case BottomSheetBehavior.STATE_EXPANDED:
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    break;
                default:
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

//        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams)bottomSheet.getLayoutParams();
//        lp.topMargin = shouldShowZoroPayCode(currentTripStatus) ? 2 * actionBarSize : actionBarSize;

        // Due to the margin top, has to setState at the beginning, otherwise will be hidden
        bottomSheet.post(() -> behavior.setState(BottomSheetBehavior.STATE_COLLAPSED));
    }

    public void setTripStage(TripStatus status) {
        Timber.i("Changing stage: " + status.toString());

        String title = tripStatusTitle[status.ordinal()];
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }

        if (mMapFragment.getView() != null) {
            mMapFragment.getView().setVisibility(shouldShowMap(status) ? View.VISIBLE : View.INVISIBLE);
        }

        toggleBottomSheet(status);
        zoroPayBanner.setVisibility(shouldShowZoroPayCode(status) ? View.VISIBLE : View.GONE);
        currentTripStatus = status;
    }

    private void toggleBottomSheet(TripStatus status) {
        boolean isTrackingCar = shouldShowMap(status);
        boolean wasTrackingCar = shouldShowMap(currentTripStatus);

        bottomSheet.setVisibility(isTrackingCar ? View.VISIBLE : View.GONE);
        fullscreenJobInfo.setVisibility(isTrackingCar ? View.GONE : View.VISIBLE);
    }

    private boolean shouldShowZoroPayCode(TripStatus status) {
        if (!mViewModel.mModel.getZoroPayEnable())
            return false;

        return status == TripStatus.BOOKED ||
                status == TripStatus.DISPATCHED ||
                status == TripStatus.ARRIVED;
    }

    private boolean shouldShowMap(TripStatus status) {
        switch (status) {
            case DISPATCHED:
            case ARRIVED:
            case IN_SERVICE:
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onResume() {
        Timber.i("onResume");
        super.onResume();

        setTripStage(TripStatus.BOOKED);
//        mDelegate.startRefreshing();
    }

    @Override
    public void onPause() {
        Timber.i("onPause");
        super.onPause();

        mDelegate.stopRefreshing();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_tracking, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_refresh:
                if (currentTripStatus == null) {
                    setTripStage(TripStatus.BOOKED);
                } else if (currentTripStatus.ordinal() < 5) {
                    setTripStage(TripStatus.values()[currentTripStatus.ordinal() + 1]);
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mGoogleMap = googleMap;
    }

    public TrackingDelegate getTrackingDelegate() {
        return mDelegate;
    }

    public BookingViewModel getBookingViewModel() {
        return mViewModel;
    }
}
