package com.happysmacks.happysmacks.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.happysmacks.happysmacks.Adapters.MyAdapter;
import com.happysmacks.happysmacks.R;

public class AddSticker extends AppCompatActivity {

  String imagesList[] = {
    "https://via.placeholder.com/300.png?text=StickerCategory%201",
    "https://via.placeholder.com/300.png?text=StickerCategory%202",
    "https://via.placeholder.com/300.png?text=StickerCategory%203",
    "https://via.placeholder.com/300.png?text=StickerCategory%204",
    "https://via.placeholder.com/300.png?text=StickerCategory%205",
    "https://via.placeholder.com/300.png?text=StickerCategory%206",
    "https://via.placeholder.com/300.png?text=StickerCategory%207",
    "https://via.placeholder.com/300.png?text=StickerCategory%208",
    "https://via.placeholder.com/300.png?text=StickerCategory%209"
  };
  String descriptionList[] ={
          "New Sticker 1",
          "New Sticker 2",
          "New Sticker 3",
          "New Sticker 4",
          "New Sticker 5",
          "New Sticker 6",
          "New Sticker 7",
          "New Sticker 8",
          "New Sticker 9",
  };
  RecyclerView recyclerView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_sticker);
    recyclerView = findViewById(R.id.recyclerView1);

    MyAdapter myAdapter = new MyAdapter(this, imagesList, descriptionList);
    recyclerView.setAdapter(myAdapter);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
  }
  @Override
  public void onBackPressed() {
    super.onBackPressed();
    finishAffinity();
  }
}

