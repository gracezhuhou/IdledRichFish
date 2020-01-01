package com.sufe.idledrichfish.ui.publish;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.sufe.idledrichfish.R;

public class CategoryActivity extends AppCompatActivity {

    private final int REQUEST_CODE_GET_CATEGORY = 12;
    private String category;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        //书籍
        final TextView book = findViewById(R.id.book);
        book.setOnClickListener(view ->{
            Intent intent = new Intent();
            intent.putExtra("category_extra", "书籍");
            setResult(REQUEST_CODE_GET_CATEGORY, intent);
            finish();
        });
        //化妆品
        final TextView cosmetics = findViewById(R.id.cosmetics);
        cosmetics.setOnClickListener(view ->{
            Intent intent = new Intent();
            intent.putExtra("category_extra", "化妆品");
            setResult(REQUEST_CODE_GET_CATEGORY, intent);
            finish();
        });
        //服饰
        final TextView clothes = findViewById(R.id.clothes);
        clothes.setOnClickListener(view ->{
            Intent intent = new Intent();
            intent.putExtra("category_extra", "服饰");
            setResult(REQUEST_CODE_GET_CATEGORY, intent);
            finish();
        });
        //手机数码
        final TextView digital = findViewById(R.id.digital);
        digital.setOnClickListener(view ->{
            Intent intent = new Intent();
            intent.putExtra("category_extra", "手机数码");
            setResult(REQUEST_CODE_GET_CATEGORY, intent);
            finish();
        });
        //家用电器
        final TextView appliances = findViewById(R.id.appliances);
        appliances.setOnClickListener(view ->{
            Intent intent = new Intent();
            intent.putExtra("category_extra", "家用电器");
            setResult(REQUEST_CODE_GET_CATEGORY, intent);
            finish();
        });
        //家具饰品
        final TextView furniture = findViewById(R.id.furniture);
        furniture.setOnClickListener(view ->{
            Intent intent = new Intent();
            intent.putExtra("category_extra", "家具饰品");
            setResult(REQUEST_CODE_GET_CATEGORY, intent);
            finish();
        });
        //鞋靴
        final TextView shoes = findViewById(R.id.shoes);
        shoes.setOnClickListener(view ->{
            Intent intent = new Intent();
            intent.putExtra("category_extra", "鞋靴");
            setResult(REQUEST_CODE_GET_CATEGORY, intent);
            finish();
        });
        //箱包
        final TextView bags = findViewById(R.id.bags);
        bags.setOnClickListener(view ->{
            Intent intent = new Intent();
            intent.putExtra("category_extra", "箱包");
            setResult(REQUEST_CODE_GET_CATEGORY, intent);
            finish();
        });
        //母婴
        final TextView kids = findViewById(R.id.kids);
        kids.setOnClickListener(view ->{
            Intent intent = new Intent();
            intent.putExtra("category_extra", "母婴");
            setResult(REQUEST_CODE_GET_CATEGORY, intent);
            finish();
        });
        //宠物用品
        final TextView pets = findViewById(R.id.pets);
        pets.setOnClickListener(view ->{
            Intent intent = new Intent();
            intent.putExtra("category_extra", "宠物用品");
            setResult(REQUEST_CODE_GET_CATEGORY, intent);
            finish();
        });
        //生鲜水果
        final TextView fruits = findViewById(R.id.fruits);
        fruits.setOnClickListener(view ->{
            Intent intent = new Intent();
            intent.putExtra("category_extra", "生鲜水果");
            setResult(REQUEST_CODE_GET_CATEGORY, intent);
            finish();
        });
        //生活百货
        final TextView commodity = findViewById(R.id.commodity);
        commodity.setOnClickListener(view ->{
            Intent intent = new Intent();
            intent.putExtra("category_extra", "生活百货");
            setResult(REQUEST_CODE_GET_CATEGORY, intent);
            finish();
        });
    }
}
