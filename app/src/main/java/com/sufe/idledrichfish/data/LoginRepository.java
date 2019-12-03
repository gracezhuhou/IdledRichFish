package com.sufe.idledrichfish.data;

import com.sufe.idledrichfish.data.model.BmobStudent;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class LoginRepository {

    private static volatile LoginRepository instance;

    private LoginDataSource dataSource;

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

    // 登录
    public void login(String username, String password) {
        // handle login
        dataSource.login(username, password);
    }

    // 是否登录
    public boolean isLoggedIn() {
        return dataSource.isLoggedIn();
    }

    // 注册
    public void signUp(String studentNumber, String name, String password, boolean gender, String imagePath) {
        dataSource.signUp(studentNumber, name, password, gender, imagePath);
    }

    // 登出
    public void logout() {
        user = null;
        dataSource.logOut();
    }

    private void setLoggedInUser(BmobStudent user) {
        this.user = user;
    }
}
