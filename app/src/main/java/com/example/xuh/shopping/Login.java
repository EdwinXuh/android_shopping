package com.example.xuh.shopping;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.app.Activity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.xuh.shopping.tools.SharedPreferencesUtils;
import com.xuh.shopping.tools.SqlHellper;

import static android.widget.Toast.*;

public class Login extends Activity {
    private EditText et_user;
    private EditText et_password;
    private Button btn_login;
    private CheckBox checkBox_password;
    private CheckBox checkBox_login;
    private TextView icon_show;
    private boolean show = false;
    private TextView register;
    private Boolean has_register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        setupEvents();
        initData();

    }
    protected void initView(){
        TextView icon_user = (TextView) findViewById(R.id.icon_user);
        TextView icon_pass = (TextView) findViewById(R.id.icon_pass);
        et_user = (EditText)findViewById(R.id.et_user);
        et_password = (EditText)findViewById(R.id.et_password);
        btn_login = (Button)findViewById(R.id.btn_login);
        checkBox_login = (CheckBox)findViewById(R.id.checkBox_login);
        checkBox_password = (CheckBox)findViewById(R.id.checkBox_password);
        icon_show = (TextView)findViewById(R.id.icon_show);
        register = (TextView)findViewById(R.id.register);

        Typeface font = Typeface.createFromAsset(getAssets(),"fonts/iconfont.ttf");
        icon_user.setTypeface(font);
        icon_pass.setTypeface(font);
        icon_show.setTypeface(font);
        icon_user.setText(getResources().getString(R.string.user));
        icon_pass.setText(getResources().getString(R.string.password));
        icon_show.setText(getResources().getString(R.string.eye_close));
    }
    //获取账号
    public String getUser(){
        return et_user.getText().toString().trim();
    }
    //获取密码
    public String getPassword(){
        return  et_password.getText().toString().trim();
    }

    protected  void setupEvents() {
        btn_login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            loadUsername();
            login();
            }
        });
        icon_show.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
            show = !show;
            if(show){
                icon_show.setText(getResources().getString(R.string.eye_open));
                et_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }else{
                //密码不可见
                icon_show.setText(getResources().getString(R.string.eye_close));
                et_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
            }
        });
        register.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
            startActivity(new Intent(Login.this,Register.class));
            overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
            }
        });
    }
    //保存用户账号
    public void loadUsername(){
        if(!getUser().equals("")||!getUser().equals("username")||!getUser().equals("null")){
            SharedPreferencesUtils helper = new SharedPreferencesUtils(this,"setting");
            helper.putValues(new SharedPreferencesUtils.ContentValue("username",getUser()));//存储用户名
        }
    }

    public void login(){
        if (getUser().isEmpty()){
            showToast("请输入用户名！");return;
        }
        if (getPassword().isEmpty()){
            showToast("请输入密码！");return;
        }
        setLoginBtnClickable(false);
        //判断账号密码
        SqlHellper sqlHellper = new SqlHellper(this,"shopping",null,1);
        SQLiteDatabase db = sqlHellper.getWritableDatabase();
        String sql = "select * from userInfo where username = ? and password = ?";
        Cursor cursor = db.rawQuery(sql,new String[]{getUser(),getPassword()});
        if(cursor.moveToFirst()){
            cursor.close();
            showToast("登陆成功");
            loadCheckBoxState();
            startActivity(new Intent(Login.this,Index.class));
            overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
        }else{
            showToast("账号或密码不正确");
        }
        setLoginBtnClickable(true);
    }

    //保存记住密码和自动登陆状态
    public void loadCheckBoxState(){
        loadCheckBoxState(checkBox_password,checkBox_login);
    }
    //保存按钮状态值
    public void loadCheckBoxState(CheckBox checkBox_password , CheckBox checkBox_login){
        SharedPreferencesUtils helper = new SharedPreferencesUtils(this,"setting");
        //如果设置了自动登陆
        if(checkBox_login.isChecked()){
            helper.putValues(
                    new SharedPreferencesUtils.ContentValue("rememberPassword",true),
                    new SharedPreferencesUtils.ContentValue("autoLogin",true),
                    new SharedPreferencesUtils.ContentValue("password",getPassword())
            );
        }else if (!checkBox_password.isChecked()) { //如果没有保存密码，那么自动登录也是不选的
            //创建记住密码和自动登录是默认不选,密码为空
            helper.putValues(
                    new SharedPreferencesUtils.ContentValue("remenberPassword", false),
                    new SharedPreferencesUtils.ContentValue("autoLogin", false),
                    new SharedPreferencesUtils.ContentValue("password", ""));
        } else if (checkBox_password.isChecked()) {   //如果保存密码，没有自动登录
            //创建记住密码为选中和自动登录是默认不选,保存密码数据
            helper.putValues(
                    new SharedPreferencesUtils.ContentValue("remenberPassword", true),
                    new SharedPreferencesUtils.ContentValue("autoLogin", false),
                    new SharedPreferencesUtils.ContentValue("password", getPassword()));
        }
    }

    public void setLoginBtnClickable(Boolean clickable){
        btn_login.setClickable(clickable);
    }

    public void showToast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                makeText(Login.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void initData(){
        //判断用户是否是第一次登陆
        if(firstLogin()){
            checkBox_login.setChecked(false);
            checkBox_password.setChecked(false);
        }
        if(rememberPassword()){
            checkBox_password.setChecked(true);
            setTextNameAndPassword();
        }else{
            setTextName();
        }
        if(autoLogin()){
            checkBox_password.setChecked(true);
            checkBox_login.setChecked(true);
            login();
        }
    }
    //判断是否是第一次登陆
    private boolean firstLogin(){
        SharedPreferencesUtils helper = new SharedPreferencesUtils(this, "setting");
        boolean first = helper.getBoolean("first");
        if (first) {
            //创建一个ContentVaule，设置是第一次登陆，并创建记住密码和自动登录是默认不选，创建账号和密码为空
            helper.putValues(
                    new SharedPreferencesUtils.ContentValue("first", false),
                    new SharedPreferencesUtils.ContentValue("remenberPassword", false),
                    new SharedPreferencesUtils.ContentValue("autoLogin", false),
                    new SharedPreferencesUtils.ContentValue("name", ""),
                    new SharedPreferencesUtils.ContentValue("password", ""));
            return true;
        } else {
            return false;
    }
    }

    private boolean rememberPassword() {
        //获取SharedPreferences对象，使用自定义类的方法来获取对象
        SharedPreferencesUtils helper = new SharedPreferencesUtils(this, "setting");
        return helper.getBoolean("remenberPassword");
    }
    @SuppressLint("SetTextI18n")
    public void setTextName() {
        et_user.setText("" + getLocalName());
    }
    @SuppressLint("SetTextI18n")
    public void setTextNameAndPassword() {
        et_user.setText("" + getLocalName());
        et_password.setText("" + getLocalPassword());
    }
    public String getLocalName(){
        SharedPreferencesUtils helper = new SharedPreferencesUtils(this,"setting");
        return helper.getString("username");
    }
    public String getLocalPassword(){
        SharedPreferencesUtils helper = new SharedPreferencesUtils(this,"setting");
        return helper.getString("password");
    }
    private boolean autoLogin() {
        //获取SharedPreferences对象，使用自定义类的方法来获取对象
        SharedPreferencesUtils helper = new SharedPreferencesUtils(this, "setting");
        return helper.getBoolean("autoLogin");
    }
}
