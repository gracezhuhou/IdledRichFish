package com.sufe.idledrichfish.database;

public class LabelBLL {
    private LabelDAL labelDAL;

    public LabelBLL() {
        labelDAL = new LabelDAL();
    }

    public boolean insertLabel(Label label) {
        return labelDAL.insertLabel(label);
    }

    public boolean deleteLabelByName(String name) {
        return labelDAL.deleteLabelByName(name);
    }

    public Label getLabelByName(String name) {
        return labelDAL.getLabelByName(name);
    }
}
