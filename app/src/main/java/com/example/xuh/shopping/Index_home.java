package com.example.xuh.shopping;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.xuh.shopping.tools.RecylerViewAdapter;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;

import static android.widget.Toast.makeText;


public class Index_home extends Fragment implements OnBannerListener {
    protected LinearLayout search_layout;
    private TextView saoyisao,didian,huiyuanma;
    private Banner banner;
    private ArrayList<Integer> list_path;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private View rootView;
    public Index_home() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
            View rootView = inflater.inflate(R.layout.fragment_index_home, container, false);
            initView(rootView);
        return rootView;
    }
    public void initView(View view){
        search_layout = (LinearLayout)view.findViewById(R.id.search_layout);
        saoyisao = (TextView)view.findViewById(R.id.saoyisao);
        didian = (TextView)view.findViewById(R.id.didian);
        huiyuanma = (TextView)view.findViewById(R.id.huiyuanma);
//        初始化搜索栏
        search_layout.bringToFront();
//        初始化banner
        banner = view.findViewById(R.id.banner);
        list_path = new ArrayList<Integer>();
        list_path.add(R.drawable.banner1);
        list_path.add(R.drawable.banner2);
        list_path.add(R.drawable.banner3);
        list_path.add(R.drawable.banner4);
        list_path.add(R.drawable.banner5);
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        banner.setImageLoader(new MyLoader());
        banner.setBannerAnimation(Transformer.Default);
        banner.setDelayTime(3000);
        banner.isAutoPlay(true);
        banner.setIndicatorGravity(BannerConfig.CENTER);
        banner.setImages(list_path).setOnBannerListener(this);
        banner.start();
//        初始化图标
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(),"fonts/iconfont.ttf");
        saoyisao.setTypeface(font);
        didian.setTypeface(font);
        huiyuanma.setTypeface(font);
//        初始化item
        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerview);
        linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setFocusable(false);
        final RecylerViewAdapter adapter = new RecylerViewAdapter(getActivity());

        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new RecylerViewAdapter.OnItemClickListener() {
            @Override
            public void onClick(int id) {
                Intent intent = new Intent( getActivity(),Details.class);
                intent.putExtra( "id",id );
                startActivity( intent );
                getActivity().overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
            }
            @Override
            public void onLongClick(int id) {
                Intent intent = new Intent( getActivity(),Details.class);
                intent.putExtra( "id",id );
                startActivity( intent );
                getActivity().overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
            }
        });
    }
    @Override
    public void OnBannerClick(int position) {

    }

    private class MyLoader extends ImageLoader{
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context.getApplicationContext()).load(path).into(imageView);
        }
    }
}
