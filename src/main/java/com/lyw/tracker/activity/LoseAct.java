package com.lyw.tracker.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.lyw.tracker.R;
import com.lyw.tracker.adapter.LoseSetAdpater;
import com.lyw.tracker.bean.SettingInfoBean;
import com.lyw.tracker.comm.SP;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class LoseAct extends AppCompatActivity {

    RecyclerView recyLose;
    LoseSetAdpater mLoseSetAdpater;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lose);
        //工具栏、侧滑菜单初始化
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        toolbar.setTitle("丢失列表");
        setSupportActionBar(toolbar);
        recyLose= (RecyclerView) findViewById(R.id.recy_lose);
        mLoseSetAdpater=new LoseSetAdpater();
        recyLose.setLayoutManager(new LinearLayoutManager(this));
        recyLose.setAdapter(mLoseSetAdpater);

        BmobQuery<SettingInfoBean> bmobQuery = new BmobQuery<SettingInfoBean>();
        bmobQuery.addWhereEqualTo("phone", SP.get(LoseAct.this, "phone",""));
        bmobQuery.findObjects(new FindListener<SettingInfoBean>() {
            @Override
            public void done(List<SettingInfoBean> list, BmobException e) {
                if (null==e){
                    mLoseSetAdpater.setDataList(list);
                }
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
