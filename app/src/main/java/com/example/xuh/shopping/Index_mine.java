package com.example.xuh.shopping;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


public class Index_mine extends Fragment {
    private TextView daifukuan,daifahuo,daishouhuo,shouhou;
    private TextView yesheng,lingquan,xianzhihuanqian,kefu;
    private TextView huabei,aliyuka,pingjia,zhuti;
    private View rootView;
    public Index_mine() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_index_mine, container, false);
        initView(rootView);
        return rootView;
    }
    public void initView(View view){
        daifukuan = (TextView)view.findViewById(R.id.daifukuan);
        daifahuo = (TextView)view.findViewById(R.id.daifahuo);
        daishouhuo = (TextView)view.findViewById(R.id.daishouhuo);
        shouhou = (TextView)view.findViewById(R.id.shouhou);
        yesheng = (TextView)view.findViewById(R.id.yesheng);
        lingquan = (TextView)view.findViewById(R.id.lingquan);
        xianzhihuanqian = (TextView)view.findViewById(R.id.xianzhihuanqian);
        kefu = (TextView)view.findViewById(R.id.kefu);
        huabei = (TextView)view.findViewById(R.id.huabei);
        aliyuka = (TextView)view.findViewById(R.id.aliyuka);
        pingjia = (TextView)view.findViewById(R.id.wodepingjia);
        zhuti = (TextView)view.findViewById(R.id.zhutihuanfu);


        Typeface fonts = Typeface.createFromAsset(getActivity().getAssets(),"fonts/iconfont.ttf");
        daifukuan.setTypeface(fonts);
        daifahuo.setTypeface(fonts);
        daishouhuo.setTypeface(fonts);
        shouhou.setTypeface(fonts);

        yesheng.setTypeface(fonts);
        lingquan.setTypeface(fonts);
        xianzhihuanqian.setTypeface(fonts);
        kefu.setTypeface(fonts);
        huabei.setTypeface(fonts);
        aliyuka.setTypeface(fonts);
        pingjia.setTypeface(fonts);
        zhuti.setTypeface(fonts);
    }
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        if (rootView !=  null){
//            ((ViewGroup)rootView.getParent()).removeView( rootView );
//        }
//    }
}
