package com.xuh.shopping.tools;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.xuh.shopping.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class RecylerViewAdapter extends RecyclerView.Adapter<RecylerViewAdapter.ViewHolder> {
    private List<CartInfo> goodsInfo = new ArrayList<CartInfo>(  );
    private OnItemClickListener onItemClickListener;
    private Context context;

    public RecylerViewAdapter(Context context) {
        this.context = context;
        SqlHellper sqlHellper = new SqlHellper( context,"shopping",null,1 );
        SQLiteDatabase db = sqlHellper.getWritableDatabase();
        Cursor cursor = db.rawQuery( "select * from goods",null );
        while (cursor.moveToNext()){
            goodsInfo.add( new CartInfo(
                    cursor.getInt( cursor.getColumnIndex( "id" ) ),
                    cursor.getString( cursor.getColumnIndex( "imgName" ) ),
                    cursor.getString( cursor.getColumnIndex( "info" ) ),
                    null,
                    cursor.getString( cursor.getColumnIndex( "price" ) ),
                    cursor.getString( cursor.getColumnIndex( "counts" ) )
            ) );
        }
        cursor.close();
        db.close();
     }

    //创建View,被LayoutManager所用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.goods_item,null,false);
        ViewHolder holder = new ViewHolder(view,context);
        return holder;
    }

    //数据的绑定
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.textView.setText(goodsInfo.get(position).info);
        holder.imageButton.setImageResource(getBitmapByName( goodsInfo.get( position ).imgName ));
        holder.price.setText(goodsInfo.get(position).price);
        holder.brought_count.setText(goodsInfo.get(position).counts);
        if(onItemClickListener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onClick(goodsInfo.get( position ).id);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onItemClickListener.onLongClick(goodsInfo.get( position ).id);
                    return false;
                }
            });
            holder.imageButton.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onClick(goodsInfo.get( position ).id);
                }
            } );
            holder.imageButton.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onItemClickListener.onLongClick(goodsInfo.get( position ).id);
                    return false;
                }
            });
        }
    }
    //click
    public interface OnItemClickListener{
        void onClick(int id);
        void onLongClick(int id);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
    @Override
    public int getItemCount() {
        return goodsInfo.size();
    }
    //自定义ViewHolder,包含item的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView textView;
        public final ImageButton imageButton;
        public final TextView price;
        public final TextView brought_count;
        private  TextView icon_money;
        public ViewHolder(View itemView,Context context) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.item_goods);
            imageButton = (ImageButton) itemView.findViewById(R.id.good_image);
            price = (TextView)itemView.findViewById(R.id.good_price);
            brought_count = (TextView)itemView.findViewById(R.id.brought_count);
            icon_money = (TextView)itemView.findViewById(R.id.icon_money);
            Typeface fonts = Typeface.createFromAsset(context.getAssets(),"fonts/iconfont.ttf");
            icon_money.setTypeface(fonts);
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
