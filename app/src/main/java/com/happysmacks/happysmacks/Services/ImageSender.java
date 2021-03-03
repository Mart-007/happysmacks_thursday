package com.happysmacks.happysmacks.Services;

import android.content.ClipDescription;
import android.content.Context;
import android.content.Intent;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.KeyboardView;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;

import androidx.core.content.FileProvider;
import androidx.core.view.inputmethod.InputConnectionCompat;
import androidx.core.view.inputmethod.InputContentInfoCompat;

import java.io.File;

public class ImageSender{
  private static final String AUTHORITY = "com.happysmacks.happysmacks.happysmackskeyboard";
  private static final String MIME_TYPE_PNG = "image/png";

  public static void sendImage(File file, Context myContext) {
    final Uri contentUri = FileProvider.getUriForFile(myContext, AUTHORITY, file);
    String filePath = contentUri.toString();
    System.out.println(filePath);
    Intent shareIntent = new Intent(Intent.ACTION_SEND);
    shareIntent.setData(contentUri);
    shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    shareIntent.putExtra(Intent.EXTRA_STREAM, filePath);
    shareIntent.setType("image/jpg");
    initSendImage(shareIntent, contentUri, myContext);
  }
  public static void initSendImage(Intent intent, Uri uri, Context myContext) {

    Bundle bundle = intent.getExtras();
    Intent shareIntent = new Intent(Intent.ACTION_SEND);
    shareIntent.setData(uri);
    shareIntent.setType("image/jpg");
    shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(uri.toString()));
    Intent new_intent = Intent.createChooser(shareIntent, "Share via");
    new_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    myContext.startActivity(new_intent);
  }


}
