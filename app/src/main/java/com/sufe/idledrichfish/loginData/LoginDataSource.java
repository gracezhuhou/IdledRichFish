package com.sufe.idledrichfish.loginData;

import com.sufe.idledrichfish.database.BmobDBHelper;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public void login(String stuNumber, String password) {

        BmobDBHelper bmobDBHelper = new BmobDBHelper();
        bmobDBHelper.login(stuNumber, password);

    }

    public void logout() {
        // TODO: revoke authentication
    }
}
