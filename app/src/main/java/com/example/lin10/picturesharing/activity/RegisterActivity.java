package com.example.lin10.picturesharing.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lin10.picturesharing.R;
import com.example.lin10.picturesharing.entity.User;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button  btreg = findViewById(R.id.bt_reg);
        final EditText etreAccount = findViewById(R.id.et_reaccount);
        final EditText etrePwd1 = findViewById(R.id.et_repwd1);
        final EditText etrePwd2 = findViewById(R.id.et_repwd2);

        btreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etreAccount.getText())) {
                    Toast.makeText(RegisterActivity.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(etrePwd1.getText()) && TextUtils.isEmpty(etrePwd2.getText())) {
                    Toast.makeText(RegisterActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!etrePwd1.getText().toString().equals(etrePwd2.getText().toString())) {
                    Toast.makeText(RegisterActivity.this, "两次输入的密码不一样", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    User user = new User();
                    user.setUsername(etreAccount.getText().toString());
                    user.setPassword(etrePwd1.getText().toString());
                    user.signUp(new SaveListener<User>() {
                        @Override
                        public void done(User user, BmobException e) {
                            if (e == null) {
                                Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(RegisterActivity.this, "注册失败，账号已存在", Toast.LENGTH_SHORT).show();
                                Log.e("注册失败", "原因", e);
                            }
                        }
                    });
                }
            }
        });
    }
}
