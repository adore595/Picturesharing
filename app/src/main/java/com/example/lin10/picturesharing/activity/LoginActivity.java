package com.example.lin10.picturesharing.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lin10.picturesharing.R;
import com.example.lin10.picturesharing.entity.User;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText etPwd;
    private EditText etAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Bmob.initialize(this,"d632b739db8f6eb88939d01cfa70236d");

        etPwd = findViewById(R.id.et_pwd);
        etAccount = findViewById(R.id.et_account);
        Button btlogin = findViewById(R.id.bt_login);
        TextView tvregister = findViewById(R.id.tv_register);
        btlogin.setOnClickListener(this);
        tvregister.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_login:
                User user = new User();
                user.setUsername(etAccount.getText().toString());
                user.setPassword(etPwd.getText().toString());
                user.login(new SaveListener<User>() {
                    @Override
                    public void done(User user, BmobException e) {
                        if (e==null){
                            Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                            String username = etAccount.getText().toString();
                            intent.putExtra("name",username);
                            startActivity(intent);
//                            Intent intent =new Intent(LoginActivity.this,MainActivity.class);
//                            startActivity(intent);
                        }else{
                            Toast.makeText(LoginActivity.this,"登录失败，账号或密码错误",Toast.LENGTH_SHORT).show();
                            Log.e("登录失败","原因",e);
                        }
                    }
                });
                break;
            case R.id.tv_register:
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);

        }

    }
}
