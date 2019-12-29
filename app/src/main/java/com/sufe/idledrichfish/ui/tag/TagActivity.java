package com.sufe.idledrichfish.ui.tag;

import android.support.annotation.IntRange;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.KeyEvent;
import android.widget.EditText;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.beloo.widget.chipslayoutmanager.gravity.IChildGravityResolver;
import com.beloo.widget.chipslayoutmanager.layouter.breaker.IRowBreaker;
import com.sufe.idledrichfish.R;
import com.sufe.idledrichfish.data.model.Tag;

import java.util.ArrayList;
import java.util.List;

public class TagActivity extends AppCompatActivity {

    private List<Tag> tags;
    private ChipsAdapter chipsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag);

        setRecycler();

        final EditText edit_tag = findViewById(R.id.edit_tag);
        edit_tag.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                Tag tag = new Tag();
                tag.setName(edit_tag.getText().toString());
                tags.add(tag);
                chipsAdapter.notifyDataSetChanged();
            }
            return false;
        });
    }

    private void setRecycler() {
        final RecyclerView recycler_view = findViewById(R.id.recycler_view);
        ChipsLayoutManager chipsLayoutManager = ChipsLayoutManager.newBuilder(this)
                //set vertical gravity for all items in a row. Default = Gravity.CENTER_VERTICAL
                .setChildGravity(Gravity.TOP)
                //whether RecyclerView can scroll. TRUE by default
                .setScrollingEnabled(true)
                //set maximum views count in a particular row
                .setMaxViewsInRow(5)
                //set gravity resolver where you can determine gravity for item in position.
                //This method have priority over previous one
                .setGravityResolver(new IChildGravityResolver() {
                    @Override
                    public int getItemGravity(int position) {
                        return Gravity.CENTER;
                    }
                })
                //you are able to break row due to your conditions. Row breaker should return true for that views
                .setRowBreaker(new IRowBreaker() {
                    @Override
                    public boolean isItemBreakRow(@IntRange(from = 0) int position) {
                        return position == 6 || position == 11 || position == 2;
                    }
                })
                //a layoutOrientation of layout manager, could be VERTICAL OR HORIZONTAL. HORIZONTAL by default
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                // row strategy for views in completed row, could be STRATEGY_DEFAULT, STRATEGY_FILL_VIEW,
                //STRATEGY_FILL_SPACE or STRATEGY_CENTER
                .setRowStrategy(ChipsLayoutManager.STRATEGY_FILL_SPACE)
                // whether strategy is applied to last row. FALSE by default
                .withLastRow(true)
                .build();
        recycler_view.setLayoutManager(chipsLayoutManager);
        tags = new ArrayList<>();
        chipsAdapter = new ChipsAdapter(tags);
        recycler_view.setAdapter(chipsAdapter);
        recycler_view.setHasFixedSize(true);
    }
}
