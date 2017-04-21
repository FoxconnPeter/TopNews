package com.example.materialtest;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import org.json.JSONException;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CaiJingFragment extends Fragment {
	private static  int i=1 ;
	public static  final int NEWS_DATA=1;
	private RecyclerView recyclerView;
	private SwipeRefreshLayout swipeRefreshLayout;
	private FruitAdapter fruitAdapter;
	private String url = "http://v.juhe.cn/toutiao/index?type=caijing&key=34380c2654aa698a2572a2e71f456c3e";
	private String url1 = "http://v.juhe.cn/toutiao/index?type=shishang&key=34380c2654aa698a2572a2e71f456c3e";


	private Handler handler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what){
				case NEWS_DATA:
					recyclerView .setAdapter(fruitAdapter);
					break;
				default:

			}
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.caijingframget_layout, container,false);

		recyclerView=(RecyclerView)view.findViewById(R.id.recycler_view);
		GridLayoutManager layoutManager = new GridLayoutManager(getContext(),2);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		recyclerView.setLayoutManager(layoutManager);
		swipeRefreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh);
		swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshFruits();
            }
        });

		getData();

		return view;




	}

	private void refreshFruits() {
		swipeRefreshLayout.setRefreshing(false);
		if (i==1){
			getData1();
			i++;
		}else {
			getData();
			i=1;
		}




	}

	private void getData() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try{
					OkHttpClient client = new OkHttpClient();
					Request request = new Request.Builder().url(url).build();
					Response response = client.newCall(request).execute();
					String responseData = response.body().string();
					showResponse(responseData);
				} catch (Exception e){
					e.printStackTrace();
				}

			}
		}).start();
	}
	private void getData1() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try{
					OkHttpClient client = new OkHttpClient();
					Request request = new Request.Builder().url(url1).build();
					Response response = client.newCall(request).execute();
					String responseData = response.body().string();
					showResponse(responseData);

				} catch (Exception e){
					e.printStackTrace();
				}

			}
		}).start();
	}



	private void showResponse(String responseData) throws JSONException {
		Gson gson = new Gson();
		Bean bean = gson.fromJson(responseData, Bean.class);
		ArrayList<Bean.ResultBean.DataBean> dataBeen = (ArrayList<Bean.ResultBean.DataBean>) bean.getResult().getData();
		fruitAdapter = new FruitAdapter(dataBeen);

		Message message = new Message();
		message.what= NEWS_DATA;
		handler.sendMessage(message);




	}

}
