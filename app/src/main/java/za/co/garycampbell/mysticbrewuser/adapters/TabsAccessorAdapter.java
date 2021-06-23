package za.co.garycampbell.mysticbrewuser.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import za.co.garycampbell.mysticbrewuser.fragments.VendorDashboardProductsFragment;
import za.co.garycampbell.mysticbrewuser.fragments.VendorDashboardServicesFragment;


public class TabsAccessorAdapter extends FragmentPagerAdapter
{
    public TabsAccessorAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position)
    {
        switch (position)
        {
            case 0:
                VendorDashboardProductsFragment productsFragment = new VendorDashboardProductsFragment();
                return productsFragment;
            case 1:
                VendorDashboardServicesFragment servicesFragment = new VendorDashboardServicesFragment();
                return servicesFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position)
        {
            case 0:
                return "Products";
            case 1:
                return "Services";
            default:
                return null;
        }
    }
}
