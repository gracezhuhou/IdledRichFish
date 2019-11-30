package com.sufe.idledrichfish.database;

import android.util.Log;

import org.litepal.LitePal;

import java.util.List;

public class LabelDAL {
    private DBHelper dbHelper;

    public LabelDAL() {
        dbHelper = new DBHelper();
    }

    /*
        增加label
     */
    public Boolean insertLabel(Label label) {
//        Label label = new Label();
//        label.setName(name);
        String name = label.getName();
        if(label.save()) {
            Log.i("Database", "Add Label <" + name + "> Success");
            return true;
        }
        else {
            Log.i("Database", "Add Label <" + name + "> Fail");
            return false;
        }
    }

    /*
        删除label
    */
    public boolean deleteLabelByName(String name) {
        int delNum = LitePal.deleteAll(Label.class, "name = ?", name);
        if (delNum >= 1) {
            Log.i("Database","Delete Label <" + name + "> Success: " + delNum );
            return true;
        }
        else {
            Log.i("Database","Delete Label <" + name + "> Fail");
            return false;
        }
    }

    public  Label getLabelByName(String name) {
        List<Label> labels = LitePal.where("name = ?", name).find(Label.class);
        if (labels.size() >= 1)
            return labels.get(0);
        else
            return null;
    }
}
