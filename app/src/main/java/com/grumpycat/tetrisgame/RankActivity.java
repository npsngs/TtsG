package com.grumpycat.tetrisgame;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.grumpycat.tetrisgame.core.StatisticInfo;
import com.grumpycat.tetrisgame.tools.AppCache;
import com.grumpycat.tetrisgame.tools.DBHelper;
import com.grumpycat.tetrisgame.tools.StrUtil;

import java.util.List;

public class RankActivity extends Activity {
    private RankAdapter adapter;
    private AdView mAdView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
        setContentView(R.layout.activity_ranks);
        AppCache.setTypeface(R.id.tv_title, this);
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ListView lv = findViewById(R.id.lv);
        adapter = new RankAdapter();
        lv.setAdapter(adapter);

        MobileAds.initialize(this, "ca-app-pub-6724294817972520~7263293066");
        mAdView = findViewById(R.id.adView);
        mAdView.loadAd(new AdRequest
                .Builder()
                .build());
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                Log.e("Admbs", "ad finishes loading");
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                Log.e("Admbs", "ad request fails errorCode:"+errorCode);
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
                Log.e("Admbs", "onAdOpened");
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
                Log.e("Admbs", "onAdLeftApplication");

            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the user is about to return
                // to the app after tapping on an ad.
                Log.e("Admbs", "onAdClosed");
            }
        });

        new Thread(){
            @Override
            public void run() {
                infos = DBHelper.queryAll();
                if(infos != null && infos.size()>1){
                    long tmp = 0;
                    int size = infos.size();
                    for(int i=0;i<size;i++){
                        StatisticInfo info = infos.get(i);
                        if(info.getEndTime() > tmp){
                            tmp = info.getEndTime();
                            lastGameIndex = i;
                        }
                    }
                }else{
                    lastGameIndex = -1;
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }.start();
    }

    private int lastGameIndex = -1;
    private List<StatisticInfo> infos;
    private class RankAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return infos==null?0:infos.size();
        }

        @Override
        public Object getItem(int position) {
            return infos==null?null:infos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            InfoHolder holder;
            if(view == null){
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                view = inflater.inflate(R.layout.item_rank, parent, false);
                holder = new InfoHolder(view);
                view.setTag(holder);
            }else{
                holder = (InfoHolder) view.getTag();
            }

            StatisticInfo info = infos.get(position);
            holder.tv_rank.setText(String.format("%d", position+1));
            holder.tv_score.setText(getString(R.string.score_fmt, info.getScore()));
            holder.tv_info.setText(getString(R.string.info_fmt,
                    info.getLines(),
                    info.getLvl(),
                    StrUtil.formatYYMMDDHHMM(info.getEndTime())));
            if(lastGameIndex == position){
                view.setBackgroundColor(0xcc121212);
            }else{
                view.setBackgroundColor(Color.TRANSPARENT);
            }
            return view;
        }
    }

    class InfoHolder {
        TextView tv_rank;
        TextView tv_score;
        TextView tv_info;

        InfoHolder(View v){
            tv_rank = v.findViewById(R.id.tv_rank);
            tv_score = v.findViewById(R.id.tv_score);
            tv_info = v.findViewById(R.id.tv_info);

            AppCache.setTypeface(tv_rank);
            AppCache.setTypeface(tv_score);
            AppCache.setTypeface(tv_info);
        }
    }
}
