package com.sufe.idledrichfish.loginData;

import com.sufe.idledrichfish.database.BmobStudent;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class LoginRepository {

    private static volatile LoginRepository instance;

    private LoginDataSource dataSource;

    // If user credentials will be cached in local storage, it is recommended it be encrypted
    private BmobStudent user = null;

    // private constructor : singleton access
    private LoginRepository(LoginDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static LoginRepository getInstance(LoginDataSource dataSource) {
        if (instance == null) {
            instance = new LoginRepository(dataSource);
        }
        return instance;
    }

    public boolean isLoggedIn() {
        return user != null;
    }

    public void logout() {
        user = null;
        dataSource.logout();
    }

    private void setLoggedInUser(BmobStudent user) {
        this.user = user;
        // If user credentials will be cached in local storage, it is recommended it be encrypted
    }

    public void login(String username, String password) {
        // handle login
        dataSource.login(username, password);
    }
}
