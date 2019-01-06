package com.example.xuh.shopping;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xuh.shopping.tools.CartInfo;
import com.xuh.shopping.tools.CartRecyclerViewAdapter;
import com.xuh.shopping.tools.ItemTouchHelperCallBack;
import com.xuh.shopping.tools.SqlHellper;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Index_cart extends Fragment {
    private LinearLayout topbar;
    private TextView all_carts;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private CheckBox select_all;
    private Button pay;
    private List<String> lists = new ArrayList<String>();
    private Map<Integer,Boolean> get_map = new HashMap<Integer,Boolean>(  );
    private Map<Integer,CartInfo> cart_info = new HashMap<Integer, CartInfo>(  );
    private CartRecyclerViewAdapter adapter;
    private SqlHellper sqlHellper;
    private View rootView;
    public Index_cart() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_index_cart, container, false);
        initView(rootView);
        return rootView;
    }
    public void initView(View view){
        topbar = (LinearLayout)view.findViewById(R.id.topbar_layout);
        all_carts = (TextView)view.findViewById( R.id.all_carts );
        select_all = (CheckBox)view.findViewById( R.id.select_all );
        pay = (Button)view.findViewById( R.id.pay );
        topbar.bringToFront();
        sqlHellper = new SqlHellper( getActivity(),"shopping",null,1 );
//        初始化item
        initItem(view);
    }
    public void initItem(View view){
        recyclerView = (RecyclerView) view.findViewById(R.id.carts_recyclerview);
        linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setFocusable(false);
        adapter = new CartRecyclerViewAdapter(getActivity());
        // adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

        ItemTouchHelper.Callback callback = new ItemTouchHelperCallBack( adapter );
        ItemTouchHelper touchHelper = new ItemTouchHelper( callback );
        touchHelper.attachToRecyclerView( recyclerView );

        adapter.setOnItemClickListener( new CartRecyclerViewAdapter.OnItemClickListener(){
            @Override
            public void onItemClickListener(Map<Integer,Boolean> map ,Map<Integer,CartInfo> cartInfoMap) {
                get_map = map;
                cart_info = cartInfoMap;
                int total_price=0;
                //遍历map,计算选中的商品总价
                Iterator mapIterator = map.entrySet().iterator();
                while (mapIterator.hasNext()){
                    Map.Entry entry = (Map.Entry)mapIterator.next();
                    if((Boolean) entry.getValue()){
                        CartInfo temp = cartInfoMap.get( entry.getKey() );
                        int tempPrice = Integer.parseInt( temp.price );
                        int tempNum = Integer.parseInt( temp.num );
                        total_price += tempPrice * tempNum;
                    }
                }
                all_carts.setText( String.valueOf( total_price ) );
            }
        });
        select_all.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                adapter.selectAll(isChecked);
            }
        } );
        //INSERT INTO TABLE(col1, col2) SELECT val11, val12 UNION ALL SELECT val21, val22 ;
        pay.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = sqlHellper.getWritableDatabase();
                ContentValues values = new ContentValues(  );
                int sign = 0;
                for (Map.Entry<Integer,Boolean> entry : get_map.entrySet()){
                    if(entry.getValue()){
                        CartInfo temp = cart_info.get( entry.getKey() );
                        String sql = "update carts set isActive = 0 where id = "+temp.id;
                        db.execSQL( sql );
                        values.put( "imgName" ,temp.imgName);
                        values.put( "num",temp.num );
                        values.put( "info",temp.info );
                        values.put( "price",temp.price );
                        db.insert( "dingdans",null,values );
                        values.clear();
                        sign = 1;
                    }
                }
                if (sign == 0){
                    Toast.makeText( getContext(),"请选择您要购买的商品",Toast.LENGTH_SHORT ).show();
                }else{
                    Toast.makeText( getContext(),"购买成功，可在订单里查看",Toast.LENGTH_SHORT ).show();
                    all_carts.setText( String.valueOf( 0 ) );
                    select_all.setChecked( false );
                    adapter = new CartRecyclerViewAdapter(getActivity());
                    recyclerView.setAdapter( adapter );
                }
            }
        } );
    }
}
