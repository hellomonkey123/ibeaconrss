package com.lyw.tracker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lyw.tracker.R;
import com.lyw.tracker.bean.UserInfoBean;
import com.lyw.tracker.comm.SP;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };


    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!TextUtils.isEmpty((CharSequence) SP.get(this, "phone", "")) && !TextUtils.isEmpty((CharSequence) SP.get(this, "phone", "pwd"))) {
            LoginActivity.this.startActivity(new Intent(LoginActivity.this, MainActivity.class));
            this.finish();
        }
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {

                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(mEmailView.getText().toString())) {
                    Toast.makeText(LoginActivity.this, "请输入账号", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(mPasswordView.getText().toString())) {
                    Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                }
                BmobQuery<UserInfoBean> bmobQuery = new BmobQuery<UserInfoBean>();
                bmobQuery.addWhereEqualTo("phone", mEmailView.getText().toString());
                bmobQuery.addWhereEqualTo("pwd", mPasswordView.getText().toString());
                bmobQuery.findObjects(new FindListener<UserInfoBean>() {
                    @Override
                    public void done(List<UserInfoBean> list, BmobException e) {
                        if (null == e) {
                            if (null!=list&&list.size() == 0) {
                                UserInfoBean userInfoBean = new UserInfoBean();
                                userInfoBean.setPhone(mEmailView.getText().toString());
                                userInfoBean.setPwd(mPasswordView.getText().toString());
                                userInfoBean.save(new SaveListener<String>() {
                                    @Override
                                    public void done(String s, BmobException e) {
                                        if (e == null) {
                                            Toast.makeText(LoginActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();
                                            LoginActivity.this.startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                            LoginActivity.this.finish();
                                        }
                                    }
                                });
                            } else {
                                UserInfoBean userInfoBean = new UserInfoBean();
                                userInfoBean.setPhone(mEmailView.getText().toString());
                                userInfoBean.setPwd(mPasswordView.getText().toString());
                                userInfoBean.save(new SaveListener<String>() {
                                    @Override
                                    public void done(String s, BmobException e) {
                                        if (e == null) {
                                            SP.put(LoginActivity.this, "phone", mEmailView.getText().toString());
                                            SP.put(LoginActivity.this, "pwd", mPasswordView.getText().toString());
                                            Toast.makeText(LoginActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();
                                            LoginActivity.this.startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                            LoginActivity.this.finish();
                                        }
                                    }
                                });
                                SP.put(LoginActivity.this, "phone", mEmailView.getText().toString());
                                SP.put(LoginActivity.this, "pwd", mPasswordView.getText().toString());
                                Toast.makeText(LoginActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();
                                LoginActivity.this.startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                LoginActivity.this.finish();
                            }
                        }
                        else {
                            Toast.makeText(LoginActivity.this, "登录失败！", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}

