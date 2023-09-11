package com.example.engspace;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.engspace.adapter.ViewPagerAdapter;
import com.example.engspace.main.CreateFragment;
import com.example.engspace.main.HomeFragment;
import com.example.engspace.main.ManageFragment;
import com.example.engspace.main.ProfileFragment;
import com.example.engspace.main.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    HomeFragment homeFragment;
    SearchFragment searchFragment;
    ProfileFragment profileFragment;
    ManageFragment manageFragment;

    private ViewPager2 viewPager2;
    //Navbar
    private BottomNavigationView bottomNavigationView;
    //Hidden Stt & Nav
    private View decorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager2 = findViewById(R.id.viewpager2_container);
        viewPager2.setUserInputEnabled(false);

        //Phần event click add button
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.btn_create);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateFragment createFragment = CreateFragment.newInstance();
                createFragment.show(getSupportFragmentManager(),
                        "create_dialog_fragment");
            }
        });

        //Phần event navigation bar

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setBackground(null);
        bottomNavigationView.getMenu().getItem(2).setEnabled(false);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_home:
                        viewPager2.setCurrentItem(0, true);
                        break;
                    case R.id.action_search:
                        viewPager2.setCurrentItem(1, true);
                        break;
                    case R.id.action_profile:
                        viewPager2.setCurrentItem(2, true);
                        break;
                    case R.id.action_manage:
                        viewPager2.setCurrentItem(3, true);
                        break;
                }
                return false;
            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                switch (position) {
                    case 0:
                        bottomNavigationView.getMenu().findItem(R.id.action_home).setChecked(true);
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.action_search).setChecked(true);
                        break;
                    case 2:
                        bottomNavigationView.getMenu().findItem(R.id.action_profile).setChecked(true);
                        break;
                    case 3:
                        bottomNavigationView.getMenu().findItem(R.id.action_manage).setChecked(true);
                        break;
                }
            }
        });

        setupViewPager(viewPager2);
    }

    private void setupViewPager(ViewPager2 viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), getLifecycle());

        homeFragment = new HomeFragment();
        searchFragment = new SearchFragment();
        profileFragment = new ProfileFragment();
        manageFragment = new ManageFragment();

        adapter.addFragment(homeFragment);
        adapter.addFragment(searchFragment);
        adapter.addFragment(profileFragment);
        adapter.addFragment(manageFragment);

        viewPager.setAdapter(adapter);
    }

    public void moveToSearch(){
        viewPager2.setCurrentItem(1, true);
    }
}