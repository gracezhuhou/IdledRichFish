package com.sufe.idledrichfish;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sufe.idledrichfish.database.Label;
import com.sufe.idledrichfish.database.Product;
import com.sufe.idledrichfish.database.Student;
import com.sufe.idledrichfish.database.helper.DbHelper;
import com.sufe.idledrichfish.database.helper.LabelDbHelper;
import com.sufe.idledrichfish.database.helper.ProductDbHelper;
import com.sufe.idledrichfish.database.helper.StudentDbHelper;

import org.litepal.LitePal;

import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        HomeFragment.OnFragmentInteractionListener, PublishmentFragment.OnFragmentInteractionListener,
        MessageFragment.OnFragmentInteractionListener, MyFragment.OnFragmentInteractionListener {

    private FrameLayout mainFrame;
    private BottomNavigationView navView;
    private HomeFragment homeFragment;
    private PublishmentFragment publishmentFragment;
    private MessageFragment messageFragment;
    private MyFragment myFragment;
    private Fragment[] fragments;
    private int lastfragment = 0;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    if (lastfragment != 0) {
                        switchFragment(lastfragment, 0);
                        lastfragment = 0;
                    }
                    return true;
                case R.id.navigation_message:
                    if (lastfragment != 1) {
                        switchFragment(lastfragment, 1);
                        lastfragment = 1;
                    }
                    return true;
                case R.id.navigation_publishment:
                    if (lastfragment != 2) {
                        switchFragment(lastfragment, 2);
                        lastfragment = 2;
                    }
                    return true;
                case R.id.navigation_personal_info:
                    if (lastfragment != 3) {
                        switchFragment(lastfragment, 3);
                        lastfragment = 3;
                    }
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    void initView() {
        homeFragment = new HomeFragment();
        publishmentFragment = new PublishmentFragment();
        messageFragment = new MessageFragment();
        myFragment = new MyFragment();
        fragments = new Fragment[]{homeFragment, messageFragment, publishmentFragment, myFragment};
        mainFrame = (FrameLayout) findViewById(R.id.mainFrame);
        //设置fragment到布局
        getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame, homeFragment).show(homeFragment).commit();

        navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

    /**
     *切换fragment
     */
    private void switchFragment(int lastfragment, int index) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //隐藏上个Fragment
        transaction.hide(fragments[lastfragment]);
        if (!fragments[index].isAdded()) {
            transaction.add(R.id.mainFrame, fragments[index]);
        }
        transaction.show(fragments[index]).commitAllowingStateLoss();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        //Toast.makeText(this,"交流", Toast.LENGTH_LONG).show();
    }
}
