package com.lyw.tracker.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.lyw.tracker.R;
import com.lyw.tracker.base.BaseRecyclerAdapter;
import com.lyw.tracker.base.CommonHolder;
import com.lyw.tracker.bean.SettingInfoBean;
import com.lyw.tracker.comm.SP;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 地图.
 */

public class LoseSetAdpater extends BaseRecyclerAdapter<SettingInfoBean> {
    @Override
    public CommonHolder setViewHolder(ViewGroup parent) {
        return new LoseHodler(parent.getContext(), parent, R.layout.item_lose);
    }

    class LoseHodler extends CommonHolder<SettingInfoBean> {
        TextView tvLose;
        TextView tvTime;
        Button btnDel;
        Button btnLoction;

        public LoseHodler(Context context, ViewGroup root, int layoutRes) {
            super(context, root, layoutRes);
        }

        @Override
        public void bindData(final SettingInfoBean settingInfoBean) {
            tvLose = (TextView) itemView.findViewById(R.id.tv_title);
            btnDel = (Button) itemView.findViewById(R.id.btn_del);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            setTextValue(tvLose, settingInfoBean.getName());
            setTextValue(tvTime, "丢失时间:" + settingInfoBean.getUpdatedAt());
            btnDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    settingInfoBean.delete(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (null == e) {
                                BmobQuery<SettingInfoBean> bmobQuery = new BmobQuery<SettingInfoBean>();
                                bmobQuery.addWhereEqualTo("phone", SP.get(getContext(), "phone", ""));
                                bmobQuery.findObjects(new FindListener<SettingInfoBean>() {
                                    @Override
                                    public void done(List<SettingInfoBean> list, BmobException e) {
                                        if (null == e) {
                                            setDataList(list);
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            });
            btnLoction = (Button) itemView.findViewById(R.id.btn_loction);
            btnLoction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Uri uri = Uri.parse("geo:"+settingInfoBean.getLatitude()+","+settingInfoBean.getLongitude());
//                    Intent it = new Intent(Intent.ACTION_VIEW,uri);
//                    getContext().startActivity(it);
                    if (isAvilible(getContext(), "com.autonavi.minimap")) {
                        try {
                            Intent intent = Intent.getIntent("androidamap://navi?sourceApplication=慧医&poiname=我的目的地&lat=" + settingInfoBean.getLatitude() + "&lon=" + settingInfoBean.getLongitude() + "&dev=0");
                            getContext().startActivity(intent);
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(getContext(), "您尚未安装高德地图", Toast.LENGTH_LONG).show();
                        Uri uri = Uri.parse("market://details?id=com.autonavi.minimap");
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        getContext().startActivity(intent);
                    }
                }
            });
        }

    }

    /* 检查手机上是否安装了指定的软件
    * @param context
    * @param packageName：应用包名
    * @return
            */
    public static boolean isAvilible(Context context, String packageName) {
        //获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        //用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<String>();
        //从pinfo中将包名字逐一取出，压入pName list中
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        //判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }
}
