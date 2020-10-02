package my.edu.tarc.finalyearproject.Adapter;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ViewPageAdapter extends FragmentPagerAdapter {

    private  final List<Fragment> fragmentLIst= new ArrayList<>();
    private final List<String> fragmentTitleList = new ArrayList<>();


    public ViewPageAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentLIst.get(position);
    }

    @Override
    public int getCount() {
        return fragmentLIst.size();
    }
    public void addFragment(Fragment fragment,String title){
        fragmentLIst.add(fragment);
        fragmentTitleList.add(title);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitleList.get(position);
    }
}
