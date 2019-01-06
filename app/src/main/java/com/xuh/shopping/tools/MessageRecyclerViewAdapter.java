package com.xuh.shopping.tools;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xuh.shopping.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MessageRecyclerViewAdapter extends RecyclerView.Adapter<MessageRecyclerViewAdapter.ViewHolder> implements ItemTouchHelperAdapter{
    private Context context;
    private SqlHellper sqlHellper;
    private List<CartInfo> cartInfos = new ArrayList<CartInfo>(  );
    public MessageRecyclerViewAdapter(Context context){
        this.context = context;
        sqlHellper = new SqlHellper( context,"shopping",null,1 );
        SQLiteDatabase db = sqlHellper.getReadableDatabase();
        Cursor cursor = db.rawQuery( "select * from dingdans where isActive = ?",new String[]{"1"} );
        while (cursor.moveToNext()){
            cartInfos.add( new CartInfo(
                    cursor.getInt( cursor.getColumnIndex( "id" ) ),
                    cursor.getString( cursor.getColumnIndex( "imgName" ) ),
                    cursor.getString( cursor.getColumnIndex( "info" ) ),
                    cursor.getString( cursor.getColumnIndex( "num" ) ),
                    cursor.getString( cursor.getColumnIndex( "price" ) ),
                    null
            ) );
        }
        cursor.close();
        db.close();
    }
    @Override
    //创建View,被LayoutManager所用
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dingdan,parent,false);
        ViewHolder holder = new ViewHolder(view,context);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.image.setImageResource(getBitmapByName(cartInfos.get( position ).imgName));
        holder.good_info.setText( cartInfos.get( position ).info );
        holder.good_price.setText( cartInfos.get( position ).price);
        holder.good_num.setText( cartInfos.get( position ).num);
        holder.total_num.setText( "共"+cartInfos.get( position ).num+"件商品  合计：" );
        int num,price,total;
        num = Integer.parseInt( cartInfos.get( position ).num ) ;
        price = Integer.parseInt( cartInfos.get( position ).price );
        total = num * price;
        holder.total_price.setText( String.valueOf( total ) );
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
        long id = db.update( "dingdans",values,"id=?",new String[]{String.valueOf( cartInfos.get( position ).id)} );
        if (id != 0){
            Toast.makeText( context,"删除成功",Toast.LENGTH_SHORT ).show();
        }
        cartInfos.remove( position );
        notifyItemRemoved( position );
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public final TextView good_num,good_price,good_info,total_num,total_price;
        public final ImageView image;
        public ViewHolder(View itemView,Context context){
            super(itemView);
            good_info = (TextView)itemView.findViewById( R.id.good_info );
            good_price = (TextView)itemView.findViewById( R.id.good_price );
            good_num = (TextView)itemView.findViewById( R.id.good_num );
            image = (ImageView)itemView.findViewById( R.id.image );
            total_num = (TextView)itemView.findViewById( R.id.total_num );
            total_price = (TextView)itemView.findViewById( R.id.total_price );
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
