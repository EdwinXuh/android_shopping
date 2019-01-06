package com.example.xuh.shopping;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.xuh.shopping.tools.ItemTouchHelperCallBack;
import com.xuh.shopping.tools.MessageRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class Index_message extends Fragment {
    private LinearLayout topbar;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private List<String> lists = new ArrayList<String>();
    private View rootView;
    public Index_message() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_index_message, container, false);
        initView(rootView);
        return rootView;
    }
    public void initView(View view){
        topbar = (LinearLayout)view.findViewById(R.id.topbar_layout);
        topbar.bringToFront();
//        初始化item
        recyclerView = (RecyclerView) view.findViewById(R.id.message_recyclerview);
        linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setFocusable(false);
        final MessageRecyclerViewAdapter adapter = new MessageRecyclerViewAdapter(getActivity());
        recyclerView.setAdapter(adapter);

        ItemTouchHelper.Callback callback = new ItemTouchHelperCallBack( adapter );
        ItemTouchHelper touchHelper = new ItemTouchHelper( callback );
        touchHelper.attachToRecyclerView( recyclerView );
    }

}
