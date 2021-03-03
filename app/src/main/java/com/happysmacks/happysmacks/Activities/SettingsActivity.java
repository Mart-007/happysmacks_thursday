package com.happysmacks.happysmacks.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.happysmacks.happysmacks.R;

public class SettingsActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_settings);
    //initialize bottombar
    BottomNavigationView bottomBar = findViewById(R.id.bottomBar);
//set home selected
    bottomBar.setSelectedItemId(R.id.settings);
    bottomBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
      @Override
      public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch(menuItem.getItemId()){
          case R.id.home:
            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
            overridePendingTransition(0,0);
            return true;
          case R.id.stickers:
            startActivity(new Intent(getApplicationContext(),StickerActivity.class));
            overridePendingTransition(0,0);
            return true;
          case R.id.settings:
            startActivity(new Intent(getApplicationContext(),SettingsActivity.class));
            overridePendingTransition(0,0);
            return true;
        }
        return false;
      }


    });

  }

}