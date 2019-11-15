package com.sufe.idledrichfish.database.helper;

import android.util.Log;

import com.sufe.idledrichfish.database.Label;

import org.litepal.LitePal;

import java.util.List;

public class LabelDbHelper extends DbHelper {
    public LabelDbHelper() {
        super();
    }

    /*
        增加label
     */
    public Boolean add(String name) {
        Label label = new Label();
        label.setName(name);
        if(label.save()) {
            Log.v("Database", "Add Label <" + name + "> Success");
            return true;
        }
        else {
            Log.v("Database", "Add Label <" + name + "> Fail");
            return false;
        }
    }

    /*
        删除label
    */
    public void delete(String name) {
        int delNum = LitePal.deleteAll(Label.class, "name = ?", name);
        Log.v("Database","Delete Label <" + name + ">: " + delNum );
    }

    public  List<Label> find(String name) {
        List<Label> labels = LitePal.where("name = ?", name).find(Label.class);
        return labels;
    }
}
