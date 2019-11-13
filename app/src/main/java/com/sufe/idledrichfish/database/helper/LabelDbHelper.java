package com.sufe.idledrichfish.database.helper;

import android.util.Log;

import com.sufe.idledrichfish.database.Label;

import org.litepal.LitePal;

import java.util.List;

public class LabelDbHelper extends DbHelper {
    public LabelDbHelper() {
        super();
    }

    public Boolean addLabel(String name) {
        Label label = new Label();

        label.setName(name);

        label.save();

        Log.v("Database","Add Label Success");
        return true;
    }

    public  List<Label> getLabel(String name) {
        List<Label> labels = LitePal.where("name = ?", name).find(Label.class);
        return labels;
    }
}
