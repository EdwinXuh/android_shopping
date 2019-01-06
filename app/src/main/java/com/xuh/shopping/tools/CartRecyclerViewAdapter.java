package com.xuh.shopping.tools;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xuh.shopping.R;
import com.xuh.shopping.tools.CartInfo;

import java.lang.reflect.Field;
import java.sql.DataTruncation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CartRecyclerViewAdapter extends RecyclerView.Adapter<CartRecyclerViewAdapter.ViewHolder> implements ItemTouchHelperAdapter {
    private Context context;
    private View view;
    private SqlHellper sqlHellper;
    private Map<Integer,Boolean> map = new HashMap<Integer,Boolean>(  );
    private Map<Integer,CartInfo> cartInfoMap = new HashMap<Integer,CartInfo>(  );
    private List<CartInfo> cartInfos = new ArrayList<CartInfo>(  );
    private CartInfo cartInfo;
//    存储商品信息
    private OnItemClickListener OnItemClickListener;
    public CartRecyclerViewAdapter(Context context){
        this.context = context;
            sqlHellper = new SqlHellper( context,"shopping",null,1 );
            SQLiteDatabase db = sqlHellper.getReadableDatabase();
            Cursor cursor = db.rawQuery( "select * from carts where isActive = ?",new String[]{"1"} );
            int i = 0;
            while (cursor.moveToNext()){
                    cartInfo = new CartInfo(
                        cursor.getInt( cursor.getColumnIndex( "id" ) ),
                        cursor.getString( cursor.getColumnIndex( "imgName" ) ),
                        cursor.getString( cursor.getColumnIndex( "info" ) ),
                        cursor.getString( cursor.getColumnIndex( "num" ) ),
                        cursor.getString( cursor.getColumnIndex( "price" ) ),
                        null
                );
                map.put( i,false );
                cartInfos.add( cartInfo );
                cartInfoMap.put( i,cartInfo );
                i++;
            }

    }
    //全选和取消选择
    public void selectAll(Boolean isCheck){
        if (isCheck){
            for (Map.Entry<Integer,Boolean> entry : map.entrySet()){
                map.put( entry.getKey(),true );
            }
        }else {
            for (Map.Entry<Integer,Boolean> entry : map.entrySet()){
                map.put( entry.getKey(),false );
            }
        }
        OnItemClickListener.onItemClickListener(map,cartInfoMap);
        notifyDataSetChanged();
    }
    @Override
    //创建View,被LayoutManager所用
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.cart_item,parent,false);
        ViewHolder holder = new ViewHolder(view,context);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.image.setImageResource(getBitmapByName( cartInfos.get( position ).imgName ));
        holder.good_info.setText( cartInfos.get( position ).info );
        holder.good_price.setText( cartInfos.get( position ).price );
        holder.good_num.setText( cartInfos.get( position ).num );
        holder.cart_check.setChecked( map.get( position ) );
        holder.cart_check.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    map.put( position,true );
                    holder.cart_check.setChecked( true );
                }else {
                    map.put( position,false );
                    holder.cart_check.setChecked( false );
                }
                OnItemClickListener.onItemClickListener(map, cartInfoMap);
            }
        } );
        holder.itemView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(map.get( position )){
                    map.put( position,false );
                    holder.cart_check.setChecked( false );
                }else{
                    map.put( position,true );
                    holder.cart_check.setChecked( true );
                }
                OnItemClickListener.onItemClickListener(map, cartInfoMap);
            }
        } );
        holder.add.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer numberTemp =  Integer.parseInt( holder.good_num.getText().toString());
                numberTemp++;
                holder.good_num.setText( String.valueOf( numberTemp ) );
                CartInfo temp = cartInfoMap.get( position );
                temp.num = String.valueOf( numberTemp );
                cartInfoMap.put( position,temp );
                OnItemClickListener.onItemClickListener( map, cartInfoMap);
            }
        } );
        holder.decline.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer numberTemp =  Integer.parseInt( holder.good_num.getText().toString());
                numberTemp--;
                if(numberTemp < 1)
                    numberTemp = 1;
                holder.good_num.setText( String.valueOf( numberTemp ) );
                CartInfo temp = cartInfoMap.get( position );
                temp.num = String.valueOf( numberTemp );
                cartInfoMap.put( position,temp );
                OnItemClickListener.onItemClickListener( map, cartInfoMap);
            }
        } );
    }
    @Override
    public int getItemCount() {
        return cartInfos.size();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        Collections.swap( cartInfos,fromPosition,toPosition );
        notifyItemMoved( fromPosition,toPosition );
    }

    @Override
    public void OnItemDelete(int position) {
        SQLiteDatabase db = sqlHellper.getWritableDatabase();
        ContentValues values = new ContentValues(  );
        values.put( "isActive",0 );
        long id = db.update( "carts",values,"id=?",new String[]{String.valueOf( cartInfos.get( position ).id )} );
        if (id != 0){
            Toast.makeText( context,"删除成功",Toast.LENGTH_SHORT ).show();
        }
        cartInfos.remove( position );
        notifyItemRemoved( position );
    }

    //接口回调
    public interface OnItemClickListener{
        //click
        void onItemClickListener(Map<Integer,Boolean> map ,Map<Integer,CartInfo> cartInfoMap );
        //long click
    }
    public void setOnItemClickListener(OnItemClickListener OnItemClickListener){
        this.OnItemClickListener = OnItemClickListener;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public final TextView add,decline,good_num,good_price,good_info,money;
        public final ImageView image;
        public final CheckBox cart_check;
        public ViewHolder(View itemView,Context context){
            super(itemView);
            good_info = (TextView)itemView.findViewById( R.id.good_info );
            good_price = (TextView)itemView.findViewById( R.id.good_price );
            good_num = (TextView)itemView.findViewById( R.id.good_num );
            add = (TextView)itemView.findViewById( R.id.add );
            decline = (TextView)itemView.findViewById( R.id.decline );
            image = (ImageView)itemView.findViewById( R.id.image );
            money = (TextView)itemView.findViewById( R.id.money );
            cart_check = (CheckBox)itemView.findViewById( R.id.cart_check );
            Typeface font = Typeface.createFromAsset(context.getAssets(),"fonts/iconfont.ttf" );
            money.setTypeface( font );
        }
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
