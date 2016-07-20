package com.example.nanchen.pulltorefreshdemo;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private PullToRefreshListView refresh_lv;
    private List<Music> list;
    private DataAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        refresh_lv = (PullToRefreshListView) findViewById(R.id.main_pull_refresh_lv);
        list = new ArrayList<>();

        //设置可上拉刷新和下拉刷新
        refresh_lv.setMode(PullToRefreshBase.Mode.BOTH);

        //设置刷新时显示的文本
        ILoadingLayout startLayout = refresh_lv.getLoadingLayoutProxy(true,false);
        startLayout.setPullLabel("正在下拉刷新...");
        startLayout.setRefreshingLabel("正在玩命加载中...");
        startLayout.setReleaseLabel("放开以刷新");


        ILoadingLayout endLayout = refresh_lv.getLoadingLayoutProxy(false,true);
        endLayout.setPullLabel("正在上拉刷新...");
        endLayout.setRefreshingLabel("正在玩命加载中...");
        endLayout.setReleaseLabel("放开以刷新");


        refresh_lv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                new LoadDataAsyncTask(MainActivity.this).execute();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                new LoadDataAsyncTask(MainActivity.this).execute();
            }
        });


        loadData();
        adapter = new DataAdapter(this,list);
        refresh_lv.setAdapter(adapter);
    }

    private int count = 1;
    private void loadData(){
        for (int i = 0; i < 10; i++) {
            list.add(new Music("歌曲"+count,"歌手"+count));
            count++;
        }
    }

    /**
     * 异步下载任务
     */
    private static class LoadDataAsyncTask extends AsyncTask<Void,Void,String>{

        private MainActivity mainActivity;

        public LoadDataAsyncTask(MainActivity mainActivity) {
            this.mainActivity = mainActivity;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                Thread.sleep(2000);
                mainActivity.loadData();
                return "seccess";
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * 完成时的方法
         */
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equals("seccess")){
                mainActivity.adapter.notifyDataSetChanged();
                mainActivity.refresh_lv.onRefreshComplete();//刷新完成
            }
        }
    }

    /**
     * 自定义适配器
     */
    private static class DataAdapter extends BaseAdapter{

        private Context context;
        private List<Music> list;

        public DataAdapter(Context context, List<Music> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            if (list != null){
                return list.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder vh;
            if (convertView == null){
                convertView = LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);
                vh = new ViewHolder();
                vh.tv_title = (TextView) convertView.findViewById(R.id.item_title);
                vh.tv_singer = (TextView) convertView.findViewById(R.id.item_singer);
                convertView.setTag(vh);
            }else{
                vh = (ViewHolder) convertView.getTag();
            }
            Music music = (Music) getItem(position);
            vh.tv_title.setText(music.getTitle());
            vh.tv_singer.setText(music.getSinger());
            return convertView;
        }

        class ViewHolder{
            TextView tv_title;
            TextView tv_singer;
        }
    }
}
