package com.sufe.idledrichfish.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.sufe.idledrichfish.R;
import com.sufe.idledrichfish.data.StudentDataSource;
import com.sufe.idledrichfish.data.StudentRepository;


public class UserActivity extends AppCompatActivity {

    private String sellerId;
    private String sellerName;
    static public Handler StudentHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        initData(); // 获取intent传入的数据

        PageAdapter pageAdapter = new PageAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(pageAdapter);
        TabLayout tabs = findViewById(R.id.tab_layout);
        tabs.setupWithViewPager(viewPager);

        setToolbar();

    }

    /**
     * 获取intent传入的数
     */
    private void initData() {
        Intent intent = getIntent();
        sellerId = intent.getStringExtra("seller_id_extra");
        sellerName = intent.getStringExtra("seller_Name_extra");
        StudentRepository.getInstance(new StudentDataSource()).queryStudent(sellerId, 0);
    }

    /**
     * 设置Toolbar
     */
    private void setToolbar() {
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);   // 有返回箭头
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        }
        // 返回键监听
        toolbar.setNavigationOnClickListener(view -> finish());
    }
}
