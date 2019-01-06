package com.example.xuh.shopping;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.app.Activity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.xuh.shopping.tools.SqlHellper;

import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.*;

public class Register extends Activity {
    private EditText register_user;
    private EditText register_password;
    private EditText register_repassword;
    private TextView register_show,back;
    private TextView register_show2;
    private boolean register_password_show = false;
    private boolean register_repassword_show = false;
    private Button register_btn;
    private SqlHellper sqlHellper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        setupEvents();
    }
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK){
            this.finish();
            overridePendingTransition( R.anim.back_left_in,R.anim.back_right_out );
            return true;
        }
        return super.onKeyDown(keyCode, event );
    }
    protected void initView(){
        register_user = (EditText)findViewById(R.id.register_user);
        register_password = (EditText)findViewById(R.id.register_password);
        register_repassword = (EditText)findViewById(R.id.register_repassword);
        register_show = (TextView)findViewById(R.id.register_show);
        register_show2 = (TextView)findViewById(R.id.register_show2);
        register_btn = (Button)findViewById(R.id.register_btn) ;
        Typeface font = Typeface.createFromAsset(getAssets(),"fonts/iconfont.ttf");
        register_show.setTypeface(font);
        register_show2.setTypeface(font);
        register_show.setText(getResources().getString(R.string.eye_close));
        register_show2.setText(getResources().getString(R.string.eye_close));
        back = (TextView)findViewById( R.id.back );
        back.setTypeface( font );
        back.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition( R.anim.back_left_in,R.anim.back_right_out );
            }
        } );

        sqlHellper = new  SqlHellper(this,"shopping",null,1);
    }
    public String getUsername(){
        return register_user.getText().toString().trim();
    }
    public String getPass(){
        return register_password.getText().toString().trim();
    }
    public String getRepass(){
        return register_repassword.getText().toString().trim();
    }
    protected void setupEvents(){
        register_show.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                register_password_show = !register_password_show;
                if(register_password_show){
                    register_show.setText(getResources().getString(R.string.eye_open));
                    register_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    //密码不可见
                    register_show.setText(getResources().getString(R.string.eye_close));
                    register_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
        register_show2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                register_repassword_show = !register_repassword_show;
                if(register_repassword_show){
                    register_show2.setText(getResources().getString(R.string.eye_open));
                    register_repassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    //密码不可见
                    register_show2.setText(getResources().getString(R.string.eye_close));
                    register_repassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInfoAndCheck();
            }
        });
    }
    public void getInfoAndCheck(){
        Boolean has_register = false;
        if(getUsername().isEmpty()){
            showToast("请输入用户名");return;
        }
        if(getPass().isEmpty()){
            showToast("请输入密码");return;
        }
        if(getRepass().isEmpty()){
            showToast("请确认重复密码");return;
        }
        if(!getPass().equals(getRepass())){
            showToast("两次密码不一致");return;
        }
        SQLiteDatabase db = sqlHellper.getWritableDatabase();
        Cursor cursor = db.query("userInfo",new String[]{"username","password"},"username=?",new String []{getUsername()},null,null,null);
        has_register = cursor.moveToNext();
        if(has_register){
            showToast("您已注册过");
            return;
        }else {
            ContentValues values = new ContentValues();
            values.put("username",getUsername());
            values.put("password",getPass());
            long id = db.insert("userInfo",null,values);
            cunshuju();
            startActivity(new Intent(Register.this,Index.class));
            overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
            finish();
        }
        cursor.close();//关闭游标
        db.close();
    }
    public void cunshuju(){
        List<String> details = new ArrayList<String>();
        List<String> prices = new ArrayList<String>();
        List<String> counts = new ArrayList<String>();
        details.add("【6期免息】Xiaomi/小米 小米8年度旗舰全面屏骁龙845处理器智能手机");
        details.add("男装 仿羊羔绒运动长裤 409050 优衣库UNIQLO");
        details.add("【打折】ZARA新款 男装 宽松棉服夹克外套 02522300800");
        details.add("【官网价限时直降】Apple/苹果 iPhone 8 64G 全网通4G智能手机 苹果8 iPhone8");
        details.add("GXG男装 2018冬季商场同款深色格子长款羊毛大衣 #GA126690");
        details.add("袜子男中筒袜秋冬季纯棉长袜男士保暖款棉袜防臭民族风长筒男袜潮");
        details.add("Microsoft/微软SurfaceGo英特尔4415Y4G 64G 笔记本平板电脑二合一");
        details.add("knighttom2018冬les正装New.G焦糖色羊毛混纺格纹双排扣呢料风衣");
        details.add("CA 大毛领连帽加厚派克大衣 休闲工装棉服男冬");
        details.add("PSO Brand 18AW 秋冬日系复古保暖加厚摇粒绒外套情侣羊羔毛夹克");
        prices.add("2499");prices.add("149");prices.add("499");prices.add("4388");prices.add("499");
        prices.add("29");prices.add("2988");prices.add("688");prices.add("599");prices.add("338");
        counts.add("6668人已付款");counts.add("247192人已付款");counts.add("102人已付款");counts.add("5778人已付款");counts.add("76人已付款");
        counts.add("1250人已付款");counts.add("203人已付款");counts.add("78人已付款");counts.add("131人已付款");counts.add("86人已付款");
        SQLiteDatabase db = sqlHellper.getWritableDatabase();
        ContentValues values = new ContentValues();
        for (int i = 0;i < 10;i++){
            values.put("info",details.get( i ));
            values.put("price",prices.get( i ));
            values.put("counts",counts.get( i ));
            values.put( "imgName","good"+(i+1) );
            long id = db.insert("goods",null,values);
        }
    }
    public void showToast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                makeText(Register.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
