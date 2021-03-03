package com.happysmacks.happysmacks.Activities;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.happysmacks.happysmacks.HappySmacksKeyboard;
import com.happysmacks.happysmacks.R;
import com.sachinvarma.easypermission.EasyPermissionInit;
import com.sachinvarma.easypermission.EasyPermissionList;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    showPermissions();
    setContentView(R.layout.activity_main3);


//    setContentView(R.layout.activity_main2);
//
//    showPermissions();
//
//    Button toSettings;
//    toSettings = findViewById(R.id.flutterButton2);
//
//    toSettings.setOnClickListener(new View.OnClickListener() {
//      @Override
//      public void onClick(View v) {
//        startActivityForResult(new Intent(Settings.ACTION_INPUT_METHOD_SETTINGS), 0);
//      }
//    });
////
////initialize bottombar
//    BottomNavigationView bottomBar = findViewById(R.id.bottomBar);
////set home selected
//    bottomBar.setSelectedItemId(R.id.home);
//
//bottomBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//  @Override
//  public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//    switch(menuItem.getItemId()){
//      case R.id.home:
//         startActivity(new Intent(getApplicationContext(),HomeActivity.class));
//         overridePendingTransition(0,0);
//         return true;
//      case R.id.stickers:
//        startActivity(new Intent(getApplicationContext(),StickerActivity.class));
//        overridePendingTransition(0,0);
//        return true;
//      case R.id.settings:
//        startActivity(new Intent(getApplicationContext(),SettingsActivity.class));
//        overridePendingTransition(0,0);
//        return true;
//    }
//    return false;
//  }
//
//
//});

    // mainview
//
//    setContentView(R.layout.activity_main);
//
//    showPermissions();
//
//    Button toSettings;
//    toSettings = findViewById(R.id.flutterButton);
//
//    toSettings.setOnClickListener(new View.OnClickListener() {
//      @Override
//      public void onClick(View v) {
//        startActivityForResult(new Intent(Settings.ACTION_INPUT_METHOD_SETTINGS), 0);
//      }
//    });
//
//    //initialize bottombar
//    BottomNavigationView bottomBar = findViewById(R.id.bottomBar);
////set home selected
//    bottomBar.setSelectedItemId(R.id.home);
//
//bottomBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//  @Override
//  public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//    switch(menuItem.getItemId()){
//      case R.id.home:
//         startActivity(new Intent(getApplicationContext(),HomeActivity.class));
//         overridePendingTransition(0,0);
//         return true;
//      case R.id.stickers:
//        startActivity(new Intent(getApplicationContext(),StickerActivity.class));
//        overridePendingTransition(0,0);
//        return true;
//      case R.id.settings:
//        startActivity(new Intent(getApplicationContext(),SettingsActivity.class));
//        overridePendingTransition(0,0);
//        return true;
//    }
//    return false;
//  }
//
//
//});

  }

public void showMessage(){
  AlertDialog.Builder messageBox  = new AlertDialog.Builder(this);
  messageBox.setTitle("Happy Smacks");
  messageBox.setMessage("To get started please enable HappySmacks");
  messageBox.setPositiveButton("Ok",
    new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int which) {
        startActivityForResult(new Intent(Settings.ACTION_INPUT_METHOD_SETTINGS), 0);
      }
    });
  messageBox.setCancelable(true);
  messageBox.create().show();
}

  public boolean isInputMethodEnabled() {
    String id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ENABLED_INPUT_METHODS);
    ComponentName defaultInputMethod = ComponentName.unflattenFromString(id);
    ComponentName myInputMethod = new ComponentName(this, HappySmacksKeyboard.class);
    return myInputMethod.equals(defaultInputMethod);
  }

  public void showPermissions(){
    List<String> permission = new ArrayList<>();
    permission.add(EasyPermissionList.READ_EXTERNAL_STORAGE);
    permission.add(EasyPermissionList.WRITE_EXTERNAL_STORAGE);
    new EasyPermissionInit(MainActivity.this, permission);
  }


  public void toLogin(View view) {
//    ProgressDialog.show(this, "Loading", "Initializing Stickers");
//    DownloadSticker.getStickers(this, ApiService.fetchApi2(), "/StickersCategory1");
//    DownloadSticker.getStickers(this, ApiService.fetchApi3(), "/StickersCategory2");
//    DownloadSticker.getStickers(this, ApiService.fetchApi2(), "/StickersCategory3");
//    DownloadSticker.getStickers(this, ApiService.fetchApi3(), "/StickersCategory4");
//    DownloadSticker.getStickers(this, ApiService.fetchApi2(), "/StickersCategory5");
//    DownloadSticker.getStickers(this, ApiService.fetchApi3(), "/StickersCategory6");
//    DownloadSticker.getStickers(this, ApiService.fetchApi2(), "/StickersCategory7");


//    String root = this.getExternalFilesDir(null).toString();
//    File myDir = new File(root + "/StickersCategory1");
//    List<String> stickerList = ApiService.fetchApi2();
//    for(int i=0; i<0; i++){
//      DirectoryManager.toSave2(stickerList.get(i),i, myDir, this);
//    }

    Intent intent = new Intent(this, LoginActivity.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
    startActivity(intent);
  }
}
