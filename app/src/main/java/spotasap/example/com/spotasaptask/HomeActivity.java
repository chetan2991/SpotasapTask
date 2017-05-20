package spotasap.example.com.spotasaptask;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeActivity extends AppCompatActivity  implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener
{

    private DrawerLayout mDrawerLayout;
    private int [] tabIcons = {
            R.drawable.video_tab_selector,
            R.drawable.image_tab_selector,
            R.drawable.milestone_tab_selector
    };
    private SliderLayout slider;

    public static final String first_slider_image_url = "http://images.boomsbeat.com/data/images/full/19640/game-of-thrones-season-4-jpg.jpg";
    public static final String second_slider_image_url = "http://tvfiles.alphacoders.com/100/hdclearart-10.png";
    public static final String third_slider_image_url = "http://images.boomsbeat.com/data/images/full/19640/game-of-thrones-season-4-jpg.jpg";
    public static final String fourth_slider_image_url ="http://images.boomsbeat.com/data/images/full/19640/game-of-thrones-season-4-jpg.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        if (viewPager != null) {
            setupViewPager(viewPager);
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        if( tabLayout != null )
        {
            tabLayout.setupWithViewPager(viewPager);
            setupTabIcons(tabLayout);
        }

        slider = (SliderLayout)findViewById(R.id.slider);

        HashMap<String,String> url_maps = new HashMap<String, String>();
        url_maps.put(getString(R.string.image_title_one), first_slider_image_url);
        url_maps.put(getString(R.string.image_title_two), second_slider_image_url);
        url_maps.put(getString(R.string.image_title_three), third_slider_image_url);
        url_maps.put(getString(R.string.image_title_four), fourth_slider_image_url);

        HashMap<String,Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put(getString(R.string.image_title_one),R.drawable.game_of_thrones);
        file_maps.put(getString(R.string.image_title_two),R.drawable.bigbang);
        file_maps.put(getString(R.string.image_title_three),R.drawable.house);
        file_maps.put(getString(R.string.image_title_four), R.drawable.game_of_thrones);

        for(String name : file_maps.keySet()){
            DefaultSliderView textSliderView = new DefaultSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra",name);

            slider.addSlider(textSliderView);
        }
        slider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);

        slider.setCustomAnimation(new DescriptionAnimation());
        slider.setDuration(4000);
        slider.addOnPageChangeListener(this);

    }

    @Override
    protected void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        slider.stopAutoCycle();
        super.onStop();
    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new HomeContentFragment(), getString(R.string.tab_title_videos));
        adapter.addFragment(new HomeContentFragment(), getString(R.string.tab_title_images));
        adapter.addFragment(new HomeContentFragment(), getString(R.string.tab_title_milestone));
        viewPager.setAdapter(adapter);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        mDrawerLayout.closeDrawers();
                        menuItem.setChecked(true);
                        return true;
                    }
                });
    }
    private void setupTabIcons(TabLayout tabLayout )
    {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == android.R.id.home) {
            if(mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
                mDrawerLayout.closeDrawer(Gravity.LEFT);
            }else{
                mDrawerLayout.openDrawer(Gravity.LEFT);
            }

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawers();
            return;
        }
        super.onBackPressed();
    }


    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {}

    @Override
    public void onSliderClick(BaseSliderView slider) {
    }

}
