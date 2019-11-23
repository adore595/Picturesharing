package com.example.lin10.picturesharing.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lin10.picturesharing.R;
import com.example.lin10.picturesharing.entity.Collect;
import com.example.lin10.picturesharing.entity.Like;
import com.example.lin10.picturesharing.entity.User;
import com.example.lin10.picturesharing.view.MyCircleImageView;
import com.example.lin10.picturesharing.view.MyImageView;
import com.ldoublem.thumbUplib.ThumbUpView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class ShowActivity extends AppCompatActivity {


    private Dialog dialog;
    private ImageView imageview;
    private ImageView image;
    private String strurl;
    private String objectid;
    private Button bt_collection;
    private String username;
    private String author ;
    private String obj;
    private String obj1;
    private MyCircleImageView aaa;
    private ThumbUpView mThumbUpView;
    private boolean a;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        TextView tvauthor = findViewById(R.id.ltt);
        MyImageView imageview = findViewById(R.id.imageView);
        aaa = findViewById(R.id.aaa);
        Intent intent = getIntent();

        author = intent.getStringExtra("author");
        strurl = intent.getStringExtra("imageurl");
        username = intent.getStringExtra("username");
        objectid = intent.getStringExtra("objectid");
        tvauthor.setText(author);

        imageview.setImageURL(strurl);

        init();
        initicon();

        imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });

        bt_collection = findViewById(R.id.bt_collection);
        bt_collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( bt_collection.getText().toString().equals("收藏")){
                    Collect collent = new Collect();
                    collent.setImgobjectid(objectid);
                    collent.setUsername(username);
                    collent.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e==null){
                                Toast.makeText(ShowActivity.this,"收藏成功",Toast.LENGTH_SHORT).show();
                                bt_collection.setText("已收藏");
                                obj = s;
                            }else{
                                Toast.makeText(ShowActivity.this,"收藏失败",Toast.LENGTH_SHORT).show();
                            }
                        }

                    });
                }if (bt_collection.getText().toString().equals("已收藏")){
                    Collect collent = new Collect();
                    collent.setObjectId(obj);
                    collent.delete(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e==null){
                                Toast.makeText(ShowActivity.this,"取消收藏",Toast.LENGTH_SHORT).show();
                                bt_collection.setText("收藏");
                            }else{
                                Toast.makeText(ShowActivity.this,"取消收藏失败",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }

            }
        });

        mThumbUpView = (ThumbUpView) findViewById(R.id.tpv);
        mThumbUpView.setUnLikeType(ThumbUpView.LikeType.broken);
//        mThumbUpView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                mThumbUpView.setOnThumbUp(new ThumbUpView.OnThumbUp() {
////                    @Override
////                    public void like(boolean like) {
////                        if (like==false){
////                            Like likee = new Like();
////                            likee.setUsername(username);
////                            likee.setImgobjectid(objectid);
////                            likee.save(new SaveListener<String>() {
////                                @Override
////                                public void done(String s, BmobException e) {
////                                    if (e==null){
////                                        Toast.makeText(ShowActivity.this,"点赞成功",Toast.LENGTH_SHORT).show();
////                                        obj1 = s;
////                                        a=true;
////                                    }else{
////
////                                    }
////                                }
////
////                            });
//////                            Toast.makeText(ShowActivity.this,"点赞",Toast.LENGTH_SHORT).show();
////                        }else{
////                            Like likee = new Like();
////                            likee.setObjectId(obj1);
////                            likee.delete(new UpdateListener() {
////                                @Override
////                                public void done(BmobException e) {
////                                    if (e==null){
////                                        Toast.makeText(ShowActivity.this,"取消点赞",Toast.LENGTH_SHORT).show();
////                                        a=false;
////                                    }else{
//////                                                    Toast.makeText(ShowActivity.this,"取消收藏失败",Toast.LENGTH_SHORT).show();
////                                    }
////                                }
////                            });
////                        }
////
////
////                    }
////                });
//            }
//        });

        mThumbUpView.setOnThumbUp(new ThumbUpView.OnThumbUp() {
            @Override
            public void like(boolean like) {
                if (like==true){
//                    Toast.makeText(ShowActivity.this,"点赞",Toast.LENGTH_SHORT).show();
                    Like likee = new Like();
                    likee.setUsername(username);
                    likee.setImgobjectid(objectid);
                    likee.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e==null){
                                Toast.makeText(ShowActivity.this,"点赞成功",Toast.LENGTH_SHORT).show();
                                obj1 = s;
                            }else{

                            }
                        }

                    });
                }else{
//                    Toast.makeText(ShowActivity.this,"取消点赞",Toast.LENGTH_SHORT).show();
                    Like likee = new Like();
                    likee.setObjectId(obj1);
                    likee.delete(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e==null){
                                Toast.makeText(ShowActivity.this,"取消点赞",Toast.LENGTH_SHORT).show();
                            }else{
//                                                    Toast.makeText(ShowActivity.this,"取消收藏失败",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        
        initData();

    }

    private void initicon() {
        BmobQuery<User> query = new BmobQuery<User>();
        query.addWhereEqualTo("username", author);
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null) {
                    System.out.println("获取头像成功");
                    try {
                        aaa.setImageURL(list.get(0).getIcon().getUrl());
                    }catch(Exception ee) {
                        aaa.setImageResource(R.drawable.icon);
                    }
                } else {
                    Log.e("头像获取失败", "原因", e);
                }
            }
        });
    }

private void initData() {
    BmobQuery<Collect> query = new BmobQuery<Collect>();
    query.order("-createdAt");
    query.addWhereEqualTo("imgobjectid", objectid);
    query.addWhereEqualTo("username", username);
    query.findObjects(new FindListener<Collect>() {
        @Override
        public void done(List<Collect> list, BmobException e) {
            if (e == null) {
                System.out.println("查询是否收藏");
                if (list.size()!=0 ){
                    bt_collection.setText("已收藏");
                    obj = list.get(0).getObjectId();
                }
            } else {
                Log.e("查询失败", "原因", e);
            }
        }
    });
    BmobQuery<Like> query1 = new BmobQuery<Like>();
    query1.addWhereEqualTo("imgobjectid", objectid);
//    query1.addWhereEqualTo("username", username);
    query1.findObjects(new FindListener<Like>() {
        @Override
        public void done(List<Like> list, BmobException e) {
            if (e == null) {
                System.out.println("查询是否点赞");
                if (list.size()!=0 ){
//                    mThumbUpView.setUnLikeType(ThumbUpView.LikeType.like);
                    mThumbUpView.Like();
                    obj1 = list.get(0).getObjectId();
                    a=true;
                }else {
                    a=false;
                }
            } else {
                Log.e("查询失败", "原因", e);
            }
        }
    });
}

    private void init() {

        dialog = new Dialog(ShowActivity.this,R.style.FullActivity);
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
        attributes.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(attributes);

        image = getImageView();
        dialog.setContentView(image);

        //大图的点击事件（点击让他消失）
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        image.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //弹出的“保存图片”的Dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(ShowActivity.this);
                builder.setItems(new String[]{getResources().getString(R.string.save_picture),"分享"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                saveCroppedImage(((BitmapDrawable) image.getDrawable()).getBitmap());
                                break;
                            case 1:
                                Intent shareIntent = new Intent();
                                shareIntent.setAction(Intent.ACTION_SEND);
                                shareIntent.putExtra(Intent.EXTRA_TEXT, strurl);
                                shareIntent.setType("text/plain");
                                //设置分享列表的标题，并且每次都显示分享列表
                                startActivity(Intent.createChooser(shareIntent, "分享到"));
                                break;

                        }

                    }
                });
                builder.show();
                return true;
            }
        });


    }

    //保存图片
    private void saveCroppedImage(Bitmap bmp) {
        File file = new File("/sdcard/myFolder");
        if (!file.exists())
            file.mkdir();

        file = new File("/sdcard/temp.jpg".trim());
        SimpleDateFormat timesdf = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
        String FileTime =timesdf.format(new Date()).toString();//获取系统时间
        String filename = FileTime.replace(":", "");
        String sName = ".jpg";
        String newFilePath = "/sdcard/myFolder" + "/" + filename  + sName;
        file = new File(newFilePath);
        try {
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 50, fos);
            fos.flush();
            fos.close();
            Toast.makeText(ShowActivity.this,"保存成功",Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }



    private ImageView getImageView(){
        MyImageView imageView = new MyImageView(this);
        imageView.setBackgroundColor(Color.BLACK);

        //宽高
        imageView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        //imageView设置图片
        imageView.setImageURL(strurl);
        return imageView;
    }


}
