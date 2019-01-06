package com.example.xuh.shopping;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Index extends AppCompatActivity implements View.OnClickListener{
    private LinearLayout home,dingdan,cart,mine;
    private TextView  item_home, item_message, item_cart, item_mine;
    private TextView icon_home,icon_message,icon_cart,icon_mine;
    private ViewPager vp;
    private Index_home index_home;
    private Index_message index_message;
    private Index_cart index_cart;
    private Index_mine index_mine;
    private List<Fragment> mFragmentList = new ArrayList<Fragment>();
    private FragmentAdapter mFragmentAdapter;
    String[] titles = new String[]{"首页", "订单", "购物车", "我的"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        initViews();
        mFragmentAdapter = new FragmentAdapter(this.getSupportFragmentManager(), mFragmentList);
        vp.setOffscreenPageLimit(4);//ViewPager的缓存为4帧

        vp.setAdapter(mFragmentAdapter);
        vp.setCurrentItem(0);//初始设置ViewPager选中第一帧
        item_home.setTextColor(Color.parseColor("#89baff"));
        icon_home.setTextColor(Color.parseColor("#89baff"));

        //ViewPager的监听事件
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                /*此方法在页面被选中时调用*/
                changeTextColor(position);
                mFragmentAdapter.notifyDataSetChanged();
            }
            @Override
            public void onPageScrollStateChanged(int state) {
                /*此方法是在状态改变的时候调用，其中arg0这个参数有三种状态（0，1，2）。
                arg0==0的时辰默示什么都没做
                arg0 ==1的时辰默示正在滑动
                arg0==2的时辰默示滑动完毕了*/
            }
        });
    }
    /**
     * 初始化布局View
     */
    private void initViews() {
        home = (LinearLayout) findViewById( R.id.home );
        cart = (LinearLayout) findViewById( R.id.cart );
        dingdan = (LinearLayout)findViewById( R.id.dingdan );
        mine = (LinearLayout) findViewById( R.id.mine );

        item_home = (TextView) findViewById(R.id.item_home);
        item_message = (TextView) findViewById(R.id.item_message);
        item_cart = (TextView) findViewById(R.id.item_cart);
        item_mine = (TextView) findViewById(R.id.item_mine);

        icon_home = (TextView) findViewById(R.id.icon_home);
        icon_message = (TextView) findViewById(R.id.icon_message);
        icon_cart = (TextView) findViewById(R.id.icon_cart);
        icon_mine = (TextView) findViewById(R.id.icon_mine);

        Typeface font = Typeface.createFromAsset(getAssets(),"fonts/iconfont.ttf");
        icon_home.setTypeface(font);
        icon_message.setTypeface(font);
        icon_cart.setTypeface(font);
        icon_mine.setTypeface(font);
        icon_home.setText(getResources().getString(R.string.home));
        icon_message.setText(getResources().getString(R.string.message));
        icon_cart.setText(getResources().getString(R.string.cart));
        icon_mine.setText(getResources().getString(R.string.mine));

        home.setOnClickListener( this );
        dingdan.setOnClickListener(this);
        cart.setOnClickListener(this);
        mine.setOnClickListener(this);

        vp = (ViewPager) findViewById(R.id.mainViewPager);
        index_home = new Index_home();
        index_message = new Index_message();
        index_cart = new Index_cart();
        index_mine = new Index_mine();
        //给FragmentList添加数据
        mFragmentList.add(index_home);
        mFragmentList.add(index_message);
        mFragmentList.add(index_cart);
        mFragmentList.add(index_mine);
    }
    /**
     * 点击底部Text 动态修改ViewPager的内容
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home:
                vp.setCurrentItem(0, true);
                break;
            case R.id.dingdan:
                vp.setCurrentItem(1, true);
                break;
            case R.id.cart:
                vp.setCurrentItem(2, true);
                break;
            case R.id.mine:
                vp.setCurrentItem(3, true);
                break;
        }
    }
    public class FragmentAdapter extends FragmentPagerAdapter {

        List<Fragment> fragmentList = new ArrayList<Fragment>();
        public FragmentAdapter(FragmentManager fm, List<Fragment> fragmentList) {
            super(fm);
            this.fragmentList = fragmentList;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
        public  int getItemPosition(Object object){
            return POSITION_NONE;
        }

    }

    /*
     *由ViewPager的滑动修改底部导航Text的颜色
     */
    private void changeTextColor(int position) {
        switch (position){
            case 0:
                changeColor(item_home,icon_home,"#89baff" );
                changeColor( item_cart,icon_cart,"#808080" );
                changeColor( item_mine,icon_mine,"#808080" );
                changeColor( item_message,icon_message,"#808080" );
                break;
            case 1:
                changeColor(item_message,icon_message,"#89baff" );
                changeColor( item_cart,icon_cart,"#808080" );
                changeColor( item_mine,icon_mine,"#808080" );
                changeColor( item_home,icon_home,"#808080" );
                break;
            case 2:
                changeColor(item_cart,icon_cart,"#89baff" );
                changeColor( item_message,icon_message,"#808080" );
                changeColor( item_mine,icon_mine,"#808080" );
                changeColor( item_home,icon_home,"#808080" );
                break;
            case 3:
                changeColor(item_mine,icon_mine,"#89baff" );
                changeColor( item_cart,icon_cart,"#808080" );
                changeColor( item_home,icon_home,"#808080" );
                changeColor( item_message,icon_message,"#808080" );
        }
    }
    public void changeColor(TextView textView ,TextView iconView , String color){
        textView.setTextColor( Color.parseColor( color ) );
        iconView.setTextColor( Color.parseColor( color ) );
    }
}
