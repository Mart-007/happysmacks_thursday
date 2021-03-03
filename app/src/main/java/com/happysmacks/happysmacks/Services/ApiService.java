package com.happysmacks.happysmacks.Services;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ApiService {

    public static List<String> fetchApi2() {
        List<String> imageList = new ArrayList<>();
        OkHttpClient client = new OkHttpClient();
//        String url = "https://my-json-server.typicode.com/typicode/demo/posts";
        String url = "https://dummyapi.io/data/api/user?limit=20";

        Request request = new Request.Builder()
          .addHeader("app-id", "6004fd852a65296af4a4a314")
          .url(url)
          .build();
        CountDownLatch countDownLatch = new CountDownLatch(1);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String myResponse = response.body().string();
                    try {
                        JSONObject json = new JSONObject(myResponse);
//                        String aJsonString = json.getString("data");
                        JSONArray jArray = json.getJSONArray("data");
                        for (int i = 0; i < jArray.length(); i++) {
                            try {
                                JSONObject oneObject = jArray.getJSONObject(i);
                                // Pulling items from the array
                                String picture = oneObject.getString("picture");
                                System.out.println(picture);
                                String email = oneObject.getString("email");
                                imageList.add(i, picture);

                            } catch (JSONException e) {
                                System.out.println(e.toString());
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    countDownLatch.countDown();
                } else {
                    countDownLatch.countDown();
                    System.out.println(response.toString());
                }
            }
        });

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return imageList;
    }

    public static List<String> fetchApi3() {
        List<String> imageList = new ArrayList<>();
        OkHttpClient client = new OkHttpClient();
//        String url = "https://my-json-server.typicode.com/typicode/demo/posts";
        String url = "https://dummyapi.io/data/api/post?limit=20";

        Request request = new Request.Builder()
          .addHeader("app-id", "6004fd852a65296af4a4a314")
          .url(url)
          .build();
        CountDownLatch countDownLatch = new CountDownLatch(1);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String myResponse = response.body().string();
                    try {
                        JSONObject json = new JSONObject(myResponse);
//                        String aJsonString = json.getString("data");
                        JSONArray jArray = json.getJSONArray("data");
                        for (int i = 0; i < jArray.length(); i++) {
                            try {
                                JSONObject oneObject = jArray.getJSONObject(i);
                                // Pulling items from the array
                                String picture = oneObject.getString("image");
                                System.out.println(picture);

                                imageList.add(i, picture);

                            } catch (JSONException e) {
                                System.out.println(e.toString());
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    countDownLatch.countDown();
                } else {
                    countDownLatch.countDown();
                    System.out.println(response.toString());
                }
            }
        });

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return imageList;
    }

    public static String[] staticApi(){
        String[] imageList = {
          "https://via.placeholder.com/200x200?text=Purchase+at+HappySmacks+Now+Img1%20C/O%20https://placeholder.com/",
          "https://via.placeholder.com/200x200?text=Purchase+at+HappySmacks+Now+Img2%20C/O%20https://placeholder.com/",
          "https://via.placeholder.com/200x200?text=Purchase+at+HappySmacks+Now+Img3%20C/O%20https://placeholder.com/",
          "https://via.placeholder.com/200x200?text=Purchase+at+HappySmacks+Now+Img4%20C/O%20https://placeholder.com/",
          "https://via.placeholder.com/200x200?text=Purchase+at+HappySmacks+Now+Img5%20C/O%20https://placeholder.com/",
          "https://via.placeholder.com/200x200?text=Purchase+at+HappySmacks+Now+Img6%20C/O%20https://placeholder.com/",
          "https://via.placeholder.com/200x200?text=Purchase+at+HappySmacks+Now+Img7%20C/O%20https://placeholder.com/",
          "https://via.placeholder.com/200x200?text=Purchase+at+HappySmacks+Now+Img8%20C/O%20https://placeholder.com/",
          "https://via.placeholder.com/200x200?text=Purchase+at+HappySmacks+Now+Img9%20C/O%20https://placeholder.com/",
          "https://via.placeholder.com/200x200?text=Purchase+at+HappySmacks+Now+Img10%20C/O%20https://placeholder.com/",

        };


        return imageList;
    }
}
