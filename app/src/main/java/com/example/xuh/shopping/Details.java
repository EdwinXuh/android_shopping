package com.example.xuh.shopping;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xuh.shopping.tools.SharedPreferencesUtils;
import com.xuh.shopping.tools.SqlHellper;

import java.lang.reflect.Field;

public class Details extends AppCompatActivity{
    private TextView back,money,nowprice,oldprice,good_info,addmore,reduce,good_num;
    private ImageView image;
    private Button buy,add,decline,cart;
    private int id;
    private String info,price,counts;
    private SqlHellper sqlHellper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_details );
        this.id =getIntentInfo();
        initView(id);
        setEvents(id);
    }
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK){
            this.finish();
            overridePendingTransition( R.anim.back_left_in,R.anim.back_right_out );
            return true;
        }
        return super.onKeyDown(keyCode, event );
    }
    public void setEvents(final int id){
        back.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition( R.anim.back_left_in,R.anim.back_right_out );
            }
        } );
        add.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer numberTemp =  Integer.parseInt( good_num.getText().toString());
                numberTemp++;
                good_num.setText( String.valueOf( numberTemp ) );
            }
        } );
        decline.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer numberTemp =  Integer.parseInt( good_num.getText().toString());
                numberTemp--;
                if (numberTemp < 1){
                    numberTemp = 1;
                }
                good_num.setText( String.valueOf( numberTemp ) );
            }
        } );
        buy.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = sqlHellper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put( "imgName","good"+id );
                values.put( "num", good_num.getText().toString() );
                values.put("info",good_info.getText().toString());
                values.put("price",nowprice.getText().toString());
                long id = db.insert("dingdans",null,values);
                if(id!=0){
                    Toast.makeText( Details.this,"购买成功，可在订单中查看", Toast.LENGTH_SHORT).show();
                    good_num.setText( "1" );
                }
            }
        } );
        cart.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = sqlHellper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put( "imgName","good"+id );
                values.put("num", good_num.getText().toString());
                values.put("info",good_info.getText().toString());
                values.put("price",nowprice.getText().toString());
                long id = db.insert("carts",null,values);
                if(id!=0) {
                    Toast.makeText( Details.this, "添加购物车成功，可在购物车中查看", Toast.LENGTH_SHORT ).show();
                    good_num.setText( "1" );
                }
            }
        } );
    }
    public void initView(int id){
        SqlHellper sqlHellper = new SqlHellper( this,"shopping",null,1 );
        SQLiteDatabase db = sqlHellper.getReadableDatabase();
        String newid = String.valueOf( id );
        Cursor cursor = db.rawQuery( "select * from goods where id = ?",new String[]{newid} );
        if(cursor.moveToFirst()){
            info = cursor.getString( cursor.getColumnIndex( "info" ) );
            price = cursor.getString( cursor.getColumnIndex( "price" ) );
            counts = cursor.getString( cursor.getColumnIndex( "counts" ) );
        }
        back  = (TextView)findViewById( R.id.back );
        image = (ImageView)findViewById( R.id.image);
        money = (TextView)findViewById( R.id.money );
        nowprice = (TextView)findViewById( R.id.nowprice );
        oldprice = (TextView)findViewById( R.id.oldprice );
        good_info = (TextView)findViewById( R.id.good_info );
        good_num = (TextView)findViewById( R.id.good_num );
        addmore = (TextView)findViewById( R.id.addmore );
        reduce = (TextView)findViewById( R.id.reduce );
        buy = (Button)findViewById( R.id.buy );
        cart = (Button)findViewById( R.id.cart );
        add = (Button)findViewById( R.id.add );
        decline = (Button)findViewById( R.id.decline );

        Typeface font = Typeface.createFromAsset( getAssets(),"fonts/iconfont.ttf" );
        back.setTypeface( font );
        money.setTypeface( font );

        image.setImageResource( getBitmapByName( "good"+id ));
        nowprice.setText( price );
        double oldFloat =Float.parseFloat( price )*1.04;
        int old = (int)oldFloat;
        oldprice.setText( String.valueOf( old ) );
        oldprice.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG|Paint.ANTI_ALIAS_FLAG);
        good_info.setText( info );

        int priceInt = Integer.parseInt( price );
        int addtmp = 0,reduced = 0,top = 0;
        if(priceInt < 99){
            addtmp = 99 - priceInt;
            reduced = 10;
        }else if(priceInt < 999){
            addtmp = priceInt/100;
            addtmp = addtmp+2;
            top = addtmp *100;
            addtmp = top - priceInt;
            reduced  = 40;
        }else if(priceInt < 9999){
            addtmp = priceInt / 100;
            addtmp = addtmp + 2;
            top = addtmp * 100;
            addtmp = top - priceInt;
            reduced = 100;
        }
        addmore.setText( String.valueOf( addtmp ) );
        reduce.setText( String.valueOf( reduced ) );
        this.sqlHellper =  new  SqlHellper(this,"shopping",null,1);
    }
    public int getIntentInfo(){
        return (int)getIntent().getSerializableExtra( "id" );
    }
    public int getBitmapByName(String name){
        Class drawable = R.drawable.class;
        Field field = null;
        try{
            field = drawable.getField( name );
            int images = field.getInt( field.getName() );
            return images;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
