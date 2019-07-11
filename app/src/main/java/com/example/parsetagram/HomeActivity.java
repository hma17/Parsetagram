package com.example.parsetagram;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.parsetagram.Fragments.ComposeFragment;
import com.example.parsetagram.Fragments.PostsFragment;
import com.example.parsetagram.Fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {



    public final String TAG = "HomeActivity";

    private BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_home);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        final FragmentManager fragmentManager = getSupportFragmentManager();

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.action_home:
                        fragment = new PostsFragment();
                        //  Toast.makeText(HomeActivity.this, "Home!", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_compose:
                        fragment = new ComposeFragment();
                        //   Toast.makeText(HomeActivity.this, "Compose!", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_profile:
                    default:
                        fragment = new ProfileFragment();
                        //   Toast.makeText(HomeActivity.this, "Profile!", Toast.LENGTH_SHORT).show();
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }
        });


        bottomNavigationView.setSelectedItemId(R.id.action_home);

    }

}