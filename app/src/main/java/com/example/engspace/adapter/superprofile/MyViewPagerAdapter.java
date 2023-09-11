package com.example.engspace.adapter.superprofile;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.engspace.superprofile.FolderFragment;
import com.example.engspace.superprofile.InformationFragment;
import com.example.engspace.superprofile.SetFragment;

public class MyViewPagerAdapter extends FragmentStateAdapter {

    public MyViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new InformationFragment();
            case 1:
                return new SetFragment();
            case 2:
                return new FolderFragment();
            default:
                return new InformationFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
