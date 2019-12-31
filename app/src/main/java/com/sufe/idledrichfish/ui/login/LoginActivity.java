package com.sufe.idledrichfish.ui.login;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.sufe.idledrichfish.MainActivity;
import com.sufe.idledrichfish.R;
import com.sufe.idledrichfish.SignUpActivity;
import com.sufe.idledrichfish.data.model.Student;

import cn.bmob.v3.Bmob;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    static public Handler loginHandler;

    EditText usernameEditText;
    EditText passwordEditText;
    Button loginButton;
    Button signUpButton;
    ProgressBar loadingProgressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginViewModel = ViewModelProviders.of(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        Bmob.initialize(this, "a0ed5f46dbb3be388267b3726f33ca5c");

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.button_login);
        signUpButton = findViewById(R.id.button_signUp);
        loadingProgressBar = findViewById(R.id.loading);

        // 已经登录则直接跳转至首页
        if (Student.isLogin()) {
            goToActivity(MainActivity.class);
        }

        setHandler();

        setLoginForm();

        /*
         * 点击登录按钮
         */
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                usernameEditText.clearFocus();
                passwordEditText.clearFocus();
                InputMethodManager imm = (InputMethodManager) LoginActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                // 隐藏软键盘
                imm.hideSoftInputFromWindow(LoginActivity.this.getWindow().getDecorView().getWindowToken(), 0);

                loginViewModel.login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        });

        /*
         * 点击注册按钮
         */
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 跳转至注册页面
                goToActivity(SignUpActivity.class);
            }
        });
    }

    /*
     * 获取Bmob返回的登录ErrorCode
     */
    @SuppressLint("HandlerLeak")
    private void setHandler() {
        loginHandler = new Handler() {
            public void handleMessage(Message msg) {
                int errorCode = msg.getData().getInt("errorCode");
                Log.i("Handler", "Error Code " + errorCode);

                if (errorCode == 0) {
                    updateUiWithUser();
                    goToActivity(MainActivity.class);
                }
                else
                    showLoginFailed(errorCode);
            }
        };
    }

    private void updateUiWithUser() {
        loadingProgressBar.setVisibility(View.GONE);
        String welcome = getString(R.string.welcome) + Student.getCurrentUser(Student.class).getName();
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(int errorCode) {
        loadingProgressBar.setVisibility(View.GONE);
        usernameEditText.setText("");
        passwordEditText.setText("");
        Toast.makeText(getApplicationContext(), errorCode + "", Toast.LENGTH_SHORT).show();
    }

    private void setLoginForm() {
        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        /*
         * 监听输入文字的变化
         */
        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });
    }

    /*
     * 跳转Activity
     */
    private void goToActivity(Class c) {
        Intent intent = new Intent(getApplicationContext(), c);
        startActivity(intent);
        finish();
    }
}
