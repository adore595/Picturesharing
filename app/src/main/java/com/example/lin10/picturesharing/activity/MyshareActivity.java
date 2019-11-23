package com.example.lin10.picturesharing.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.lin10.picturesharing.R;
import com.example.lin10.picturesharing.entity.Img;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class MyshareActivity extends AppCompatActivity {
    private ListView lv_myshareimg_list;
    private String username;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private ImageLoaderBaseAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myshare);

        lv_myshareimg_list = findViewById(R.id.lv_myshareimg_list);

        Intent intent = getIntent();
        username = intent.getStringExtra("name");


        initDate();
    }

private void initDate() {
    BmobQuery<Img> query = new BmobQuery<Img>();
    query.order("-createdAt");
    query.addWhereEqualTo("author",username);
    query.findObjects(new FindListener<Img>() {
        @Override
        public void done(List<Img> list, BmobException e) {
            if (e == null) {
                System.out.println("成功");
                myAdapter = new ImageLoaderBaseAdapter(MyshareActivity.this,list);
                lv_myshareimg_list.setAdapter(myAdapter);
            } else {
                Log.e("查询失败", "原因", e);
            }

        }
    });

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
            holder = (ViewHolder) convertView.getTag();
            // 创建DisplayImageOptions对象并进行相关选项配置
            DisplayImageOptions options = new DisplayImageOptions.Builder()
//                    .showImageOnLoading(R.drawable.timg)// 设置图片下载期间显示的图片
//                    .showImageForEmptyUri(R.drawable.ic_add_black_24dp)// 设置图片Uri为空或是错误的时候显示的图片
//                    .showImageOnFail(R.drawable.ic_add_black_24dp)// 设置图片加载或解码过程中发生错误显示的图片
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
            list.removeAll(list);
            list.addAll(mlist);
            notifyDataSetChanged();
        }

        class ViewHolder {
            ImageView iv_image;
        }

    }
}
