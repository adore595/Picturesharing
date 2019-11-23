package com.example.lin10.picturesharing.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.lin10.picturesharing.R;
import com.example.lin10.picturesharing.entity.Collect;
import com.example.lin10.picturesharing.entity.Img;
import com.example.lin10.picturesharing.entity.User;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class CollectActivity extends AppCompatActivity {

    private ListView lv_collectimg_list;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private ImageLoaderBaseAdapter myAdapter;
    private String username;
    List<Img> listimgg = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);

        lv_collectimg_list = findViewById(R.id.lv_collectimg_list);
        Intent intent = getIntent();

        User user = BmobUser.getCurrentUser(User.class);
        username = user.getUsername();


        initData();
    }

    class ImageLoaderBaseAdapter extends BaseAdapter {

        private LayoutInflater inflater;
        private List<Img> list;

        public ImageLoaderBaseAdapter(Context context, List<Img> list) {
            this.inflater = LayoutInflater.from(context);
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.list_item,
                        null);
                holder = new ImageLoaderBaseAdapter.ViewHolder();
                holder.iv_image = convertView
                        .findViewById(R.id.iv_image);
                convertView.setTag(holder);
            }
            holder = (ImageLoaderBaseAdapter.ViewHolder) convertView.getTag();
            // 创建DisplayImageOptions对象并进行相关选项配置
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .cacheInMemory(true)// 设置下载的图片是否缓存在内存中
                    .cacheOnDisk(true)// 设置下载的图片是否缓存在SD卡中
                    .displayer(new RoundedBitmapDisplayer(20))// 设置成圆角图片
                    .build();// 创建DisplayImageOptions对象
            // 使用ImageLoader加载图片
            imageLoader.displayImage(list.get(position).getImageurl(),
                    holder.iv_image, options);

            return convertView;
        }

        public void refresh(List<Img> mlist) {
//            list.removeAll(list);
            list.addAll(mlist);
            notifyDataSetChanged();
        }

        class ViewHolder {
            ImageView iv_image;
        }
    }

    private void initData() {
        BmobQuery<Collect> query = new BmobQuery<Collect>();
        query.order("-createdAt");
        query.addWhereEqualTo("username", username);
        final List<Collect> listimg = new ArrayList<>();
        query.findObjects(new FindListener<Collect>() {
            @Override
            public void done(List<Collect> list, BmobException e) {
                if (e == null) {
                    System.out.println("查询收藏成功");
                    listimg.addAll(list);
                } else {
                    Log.e("查询失败", "原因", e);
                }

                myAdapter = new ImageLoaderBaseAdapter(CollectActivity.this,listimgg);
                lv_collectimg_list.setAdapter(myAdapter);
                for (int i = 0; i < listimg.size(); i++) {
                    BmobQuery<Img> query1 = new BmobQuery<Img>();
                    query1.order("-createdAt");
                    String objid = listimg.get(i).getImgobjectid();
                    query1.addWhereEqualTo("objectId",objid);
                    query1.findObjects(new FindListener<Img>() {
                        @Override
                        public void done(List<Img> list, BmobException e) {
                            if (e == null) {
                                myAdapter.refresh(list);
                            } else {
                                Log.e("查询失败", "原因", e);
                            }

                        }
                    });
                }

            }
        });


    }
}
