package com.lyw.tracker.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.lyw.tracker.R;
import com.lyw.tracker.comm.SP;
import com.umeng.analytics.MobclickAgent;


/**
 * 设置界面
 */
public class SetActivity extends AppCompatActivity {

    Switch swSy;
    Switch swZd;
    Button btn_out_login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        //工具栏、侧滑菜单初始化
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        toolbar.setTitle("设置");
        setSupportActionBar(toolbar);
        swSy= (Switch) findViewById(R.id.sw_sy);
        swZd=(Switch) findViewById(R.id.sw_zd);
        btn_out_login= (Button) findViewById(R.id.btn_out_login);
        swSy.setChecked((Boolean) SP.get(this,"SY",true));
        swZd.setChecked((Boolean) SP.get(this,"ZD",true));
        swZd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SP.put(SetActivity.this,"ZD",isChecked);
            }
        });
        swSy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SP.put(SetActivity.this,"SY",isChecked);
            }
        });
        btn_out_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SP.clear(SetActivity.this);
                startActivity(new Intent(SetActivity.this,LoginActivity.class));
                SetActivity.this.finish();
            }
        });
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
