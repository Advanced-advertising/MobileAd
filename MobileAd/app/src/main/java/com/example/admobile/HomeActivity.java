package com.example.admobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.example.admobile.fragments.FragmentInteractionListener;
import com.example.admobile.fragments.NotificationFragment;
import com.example.admobile.fragments.ProfileFragment;
import com.example.admobile.fragments.ScreensFragment;
import com.example.admobile.utils.TokenManager;

public class HomeActivity extends AppCompatActivity implements FragmentInteractionListener {
    private ImageButton businessButton;
    private ImageButton notificationButton;
    private ImageButton walletButton;
    private ImageButton profileButton;
    private FrameLayout frameLayout;
    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        businessButton = findViewById(R.id.businessButton);
        notificationButton = findViewById(R.id.notificationButton);
        walletButton = findViewById(R.id.walletButton);
        profileButton = findViewById(R.id.profileButton);

        loadScreenFragment();
        businessButton.setOnClickListener(view -> {
            setDefaultButtonImage();
            businessButton.setImageResource(R.drawable.business_active);
            loadScreenFragment();
        });
        notificationButton.setOnClickListener(view -> {
            setDefaultButtonImage();
            notificationButton.setImageResource(R.drawable.notification_active);
            loadNotificationsFragment();
        });
        walletButton.setOnClickListener(view -> {
            setDefaultButtonImage();
            walletButton.setImageResource(R.drawable.wallet_active);
        });
        profileButton.setOnClickListener(view -> {
            setDefaultButtonImage();
            profileButton.setImageResource(R.drawable.profile_active);
            loadProfileFragment();
        });
    }

    private void setNewFragment(Fragment newFragment) {
        currentFragment = newFragment;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.framelayout, newFragment);
        ft.addToBackStack(null); // Replace fragment
        ft.commit();
    }

    private void loadScreenFragment() {
        ScreensFragment screensFragment = new ScreensFragment(this);
        setNewFragment(screensFragment);
    }

    private void loadProfileFragment() {
        ProfileFragment profileFragment = new ProfileFragment(this);
        setNewFragment(profileFragment);
    }

    private void loadNotificationsFragment() {
        NotificationFragment notificationFragment = new NotificationFragment(this);
        setNewFragment(notificationFragment);
    }

    public void setDefaultButtonImage() {
        businessButton.setImageResource(R.drawable.business);
        profileButton.setImageResource(R.drawable.profile);
        notificationButton.setImageResource(R.drawable.notification);
        walletButton.setImageResource(R.drawable.wallet);
    }

    @Override
    public void onRefreshRequested() {
        if (currentFragment instanceof ProfileFragment) {
            loadProfileFragment();
        } else if (currentFragment instanceof ScreensFragment) {
            loadScreenFragment();
        }
    }

    @Override
    public void onLogout() {
        TokenManager tokenManager = new TokenManager(this);
        tokenManager.clearToken();

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}