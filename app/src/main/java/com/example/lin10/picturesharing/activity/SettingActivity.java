package com.example.lin10.picturesharing.activity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.lin10.picturesharing.R;
import com.example.lin10.picturesharing.entity.User;
import com.example.lin10.picturesharing.view.MyCircleImageView;

import java.io.File;
import java.io.FileNotFoundException;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

import static com.example.lin10.picturesharing.activity.MainActivity.START_ALBUM_CODE;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText et_email;
    private EditText et_age;
    private RadioGroup radio_sex;
    private String skinTestPassState = "1";
    private String username;
    private String mpath;
    private Button upload;
    private MyCircleImageView icon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Intent intent = getIntent();
        username = intent.getStringExtra("name");

        icon = findViewById(R.id.icon);

        radio_sex = findViewById(R.id.radio_sex);
        radio_sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radbt = (RadioButton)findViewById(checkedId);
                if(radbt.getText().equals("男")){
                    skinTestPassState = "1";
                }else{
                    skinTestPassState = "2";
                }
            }
        });


        et_email=findViewById(R.id.et_email);
        et_age=findViewById(R.id.et_age);
        upload = findViewById(R.id.bt_upload);
        upload.setOnClickListener(this);
        icon.setOnClickListener(this);

        User user = BmobUser.getCurrentUser(User.class);
        try {
            icon.setImageURL(user.getIcon().getFileUrl());
        }catch(Exception e) {
            icon.setImageResource(R.drawable.icon);
        }

        try {
            et_age.setText(user.getAge().toString());
        }catch(Exception e) {}

        try {
            et_email.setText(user.getMail());
        }catch(Exception e) {}

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.icon:
                if(ContextCompat.checkSelfPermission(SettingActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!=
                        PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(SettingActivity.this,new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }else{
                    openAlbum();
                }

                break;
            case R.id.bt_upload:
                if(mpath==null){
                    User user = BmobUser.getCurrentUser(User.class);
                    try{
                        user.setAge(Integer.parseInt(et_age.getText().toString()));
                        user.setMail(et_email.getText().toString());
                    }catch(Exception e) {}

                    if(skinTestPassState.equals("1")){
                        user.setSex("男");
                    }if (skinTestPassState.equals("2")){
                        user.setSex("女");
                    }
                    user.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e==null){
                                Toast.makeText(SettingActivity.this,"上传成功",Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(SettingActivity.this,"上传失败",Toast.LENGTH_SHORT).show();
                                Log.e("失败","原因",e);

                            }
                        }
                    });

                }else{
                    final BmobFile file=new BmobFile(new File(mpath));
                    file.upload(new UploadFileListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                saveFile(file);
                                Toast.makeText(SettingActivity.this, "成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(SettingActivity.this, "失败", Toast.LENGTH_SHORT).show();
                                Log.e("失败","原因",e);
                            }
                        }
                    });
                }


        }
    }

    private void openAlbum(){
        Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
        startActivityForResult(intent, START_ALBUM_CODE);//打开相册
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();
            mpath = getImagePath(uri, null);

            ContentResolver cr = this.getContentResolver();
            try {
                Log.e("qwe",mpath.toString());
                Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));

                /* 将Bitmap设定到ImageView */
                icon.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                Log.e("qwe", e.getMessage(),e);
            }
        }


        super.onActivityResult(requestCode, resultCode, data);
    }

    private void saveFile(BmobFile file) {
        User user = BmobUser.getCurrentUser(User.class);

        user.setAge(Integer.parseInt(et_age.getText().toString()));
        user.setMail(et_email.getText().toString());
        if(skinTestPassState.equals("1")){
            user.setSex("男");
        }if (skinTestPassState.equals("2")){
            user.setSex("女");
        }

        user.setIcon(file);

        user.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    Toast.makeText(SettingActivity.this,"上传成功",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(SettingActivity.this,"上传失败",Toast.LENGTH_SHORT).show();
                    Log.e("失败","原因",e);

                }
            }
        });

    }
}
