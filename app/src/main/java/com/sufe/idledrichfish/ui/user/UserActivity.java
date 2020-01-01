package com.sufe.idledrichfish.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.sufe.idledrichfish.R;
import com.sufe.idledrichfish.data.ProductDataSource;
import com.sufe.idledrichfish.data.ProductRepository;
import com.sufe.idledrichfish.data.StudentDataSource;
import com.sufe.idledrichfish.data.StudentRepository;


public class UserActivity extends AppCompatActivity {

    private String sellerId;
    private String sellerName;
    static public Handler StudentHandler;
    // 当前聊天的 ID
    private String mChatId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        initData();//获取intent传入的数据

        PageAdapter pageAdapter = new PageAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager2);
        viewPager.setAdapter(pageAdapter);
        TabLayout tabs = findViewById(R.id.toptabs);
        tabs.setupWithViewPager(viewPager);

        setToolbar();

    }

    /**
     * 获取intent传入的数
     */
    private void initData() {
        Intent intent = getIntent();
        mChatId = sellerId = intent.getStringExtra("seller_id_extra");
        sellerName = intent.getStringExtra("seller_Name_extra");
        Log.i("Intent", mChatId);
        StudentRepository.getInstance(new StudentDataSource()).queryStudent(sellerId);
    }

    /**
     * 设置Toolbar
     */
    private void setToolbar() {
        final Toolbar toolbar = findViewById(R.id.backbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);   // 有返回箭头
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        }
        // 返回键监听
        toolbar.setNavigationOnClickListener(view -> finish());
    }
}
