package com.example.lin10.picturesharing.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lin10.picturesharing.R;
import com.example.lin10.picturesharing.entity.Img;
import com.example.lin10.picturesharing.entity.User;
import com.example.lin10.picturesharing.view.MyImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.File;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener ,View.OnClickListener{

    public static final int SELECT_PIC = 0;
    public static final int START_ALBUM_CODE = 3;
    private ImageLoaderBaseAdapter myAdapter;
    private String username;
    private String username1;
    private ListView lv_img_list;
    private RefreshLayout refreshLayout;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private List<Img> listt;
    private MyImageView iv_icon;
    private TextView tvname;
    private TextView tvmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bmob.initialize(this, "d632b739db8f6eb88939d01cfa70236d");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        FloatingActionButton add = findViewById(R.id.add);//添加按钮
        add.setOnClickListener(this);

        Intent intent = getIntent();
        username = intent.getStringExtra("name");

        lv_img_list =(ListView)findViewById(R.id.lv_img_list);
        refreshLayout = (RefreshLayout)findViewById(R.id.refreshLayout);

        initData();//加载数据
        setPullRefresher();//刷新监听

        lv_img_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this,ShowActivity.class);

                String author = listt.get(position).getAuthor();
                String imageurl = listt.get(position).getImageurl();
                String objectid = listt.get(position).getObjectId();
                intent.putExtra("author",author);
                intent.putExtra("imageurl",imageurl);
                intent.putExtra("objectid",objectid);
                intent.putExtra("username",username);
                startActivity(intent);
            }
        });

        View headView = navigationView.getHeaderView(0);
        iv_icon = headView.findViewById(R.id.iv_icon);
        tvname = headView.findViewById(R.id.tv_name);
        tvmail = headView.findViewById(R.id.tv_mail);
        iv_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"嘻嘻嘻",Toast.LENGTH_SHORT).show();

            }
        });

        User user = BmobUser.getCurrentUser(User.class);
        try {
            iv_icon.setImageURL(user.getIcon().getFileUrl());
        }catch(Exception e) {
            iv_icon.setImageResource(R.drawable.icon);
        }
        tvname.setText(user.getUsername());
        tvmail.setText(user.getMail());



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_collections) {
            Intent intent = new Intent(MainActivity.this,CollectActivity.class);
            intent.putExtra("username",username);
            startActivity(intent);

        } else if (id == R.id.nav_myshare) {
            Intent intent = new Intent(MainActivity.this,MyshareActivity.class);
            intent.putExtra("name",username);
            startActivity(intent);

        } else if (id == R.id.nav_setting) {
            Intent intent = new Intent(MainActivity.this,SettingActivity.class);
            intent.putExtra("name",username);
            startActivity(intent);

        } else if (id == R.id.nav_share) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TEXT, "This is my Share text.");
            shareIntent.setType("text/plain");

            //设置分享列表的标题，并且每次都显示分享列表
            startActivity(Intent.createChooser(shareIntent, "分享到"));

        } else if (id == R.id.nav_signout) {
            BmobUser.logOut();
            final Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



private void initData() {
    BmobQuery<Img> query = new BmobQuery<Img>();
    query.order("-createdAt");
    query.findObjects(new FindListener<Img>() {
        @Override
        public void done(List<Img> list, BmobException e) {
            if (e == null) {
                System.out.println("成功");
                myAdapter = new ImageLoaderBaseAdapter(MainActivity.this,list);
                lv_img_list.setAdapter(myAdapter);
                listt=(list);
            } else {
                Log.e("查询失败", "原因", e);
            }

        }
    });

}

    private void setPullRefresher() {
        refreshLayout.setRefreshHeader(new ClassicsHeader(this));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
//                请求数据
                BmobQuery<Img> query = new BmobQuery<Img>();
                query.order("-createdAt");
                query.findObjects(new FindListener<Img>() {
                    @Override
                    public void done(List<Img> list, BmobException e) {
                        if (e == null) {
                            System.out.println("成功");

                            myAdapter.refresh(list);
                            listt=list;
                            refreshlayout.finishRefresh(1000/*,false*/);

                            User user = BmobUser.getCurrentUser(User.class);
                            try {
                                iv_icon.setImageURL(user.getIcon().getFileUrl());
                            }catch(Exception ee) {
                                iv_icon.setImageResource(R.drawable.icon);
                            }
                            tvname.setText(user.getUsername());
                            tvmail.setText(user.getMail());

                        } else {
                            Log.e("查询失败", "原因", e);
                        }

                    }
                });
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
                holder = new ViewHolder();
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


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add:
                if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!=
                        PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }else{
                    openAlbum();
                }

        }
    }

    private void openAlbum(){
        Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
        startActivityForResult(intent, START_ALBUM_CODE);//打开相册
    }
    //
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();
            String mpath = getImagePath(uri, null);
            //上传图
            final BmobFile file=new BmobFile(new File(mpath));
            file.upload(new UploadFileListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        saveFile(file);
                    } else {
                        Toast.makeText(MainActivity.this, "失败", Toast.LENGTH_SHORT).show();
                        Log.e("失败","原因",e);

                    }
                }
            });
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String getImagePath(Uri uri, String seletion) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, seletion, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

private void saveFile(BmobFile file) {
    Img img = new Img();

    img.setAuthor(username);
    img.setImage(file);
    img.save(new SaveListener<String>() {
        @Override
        public void done(String s, BmobException e) {
            if(e==null){
                Toast.makeText(MainActivity.this,"上传成功",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(MainActivity.this,"上传失败",Toast.LENGTH_SHORT).show();
                Log.e("失败","原因",e);

            }
        }
    });

}
}
