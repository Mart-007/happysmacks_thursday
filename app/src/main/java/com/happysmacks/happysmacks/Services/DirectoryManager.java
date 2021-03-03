package com.happysmacks.happysmacks.Services;

import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.RawRes;

import com.happysmacks.happysmacks.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class DirectoryManager {
  public static void toSave(int finalI, File myDir, Bitmap bitmap) {
    try {
      if (!myDir.exists()) {
        myDir.mkdirs();
      }
      String name = "image" + finalI + ".jpg";
      myDir = new File(myDir, name);
      FileOutputStream out = new FileOutputStream(myDir);
      bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
      out.flush();
      out.close();
    } catch (Exception e) {
      // some action
    }
  }

  public static void toSave2(String uriPath, int finalI, File myDir, Context myContext) {
    DownloadManager downloadmanager = (DownloadManager) myContext.getSystemService(Context.DOWNLOAD_SERVICE);
    Uri uri = Uri.parse(uriPath);

    DownloadManager.Request request = new DownloadManager.Request(uri);
    request.setTitle("My File");
//    request.setDescription("Downloading");
//    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
    request.setVisibleInDownloadsUi(false);
    request.setDestinationUri(Uri.parse("file://" + myDir + "/." + finalI + "jpg"));

    downloadmanager.enqueue(request);
  }

  public static void getStickers(Context myContext, List<String> imageListFromApi, String filePath) {
    List<String> stickerList;
    String root = myContext.getExternalFilesDir(null).toString();
    File myDir = new File(root + filePath);
    stickerList = imageListFromApi;

    for (int i = 0; i < stickerList.size(); i++) {
      int finalI = i;
      Picasso.get().load(Uri.parse(stickerList.get(i))).into(new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//          DirectoryManager.toSave(finalI, myDir, bitmap);

        }

        @Override
        public void onBitmapFailed(Exception e, Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
      });
    }
  }

  public static File[] getAllDownloadedResourcesFile(String folder, Context myContext) {
    String root = myContext.getExternalFilesDir(null).toString();
    File myDir = new File(root + folder);
    File pictures[] = myDir.listFiles();
    return pictures;
  }


  public static boolean isEmptyDir(String folder, Context myContext) {
    boolean isEmpty = false;
    String root = myContext.getExternalFilesDir(null).toString();
    File myDir = new File(root + folder);
    File[] contents = myDir.listFiles();

    if (myDir.isDirectory()) {
      System.out.println(folder + " directory not exists");
      isEmpty = true;
      return isEmpty;
    }
// the directory file is not really a directory..
    if (contents == null) {
    }
// Folder is empty
    else if (contents.length == 0) {
      System.out.println(folder + " has no files");
      isEmpty = true;
      return isEmpty;
    }
// Folder contains files
    else {
      System.out.println(folder + " has files");
      return isEmpty;
    }
    return isEmpty;
  }

  public static void checkDir(Context myContext) {
    if (isEmptyDir("/Stickers", myContext) == true) {
      System.out.println("Stickers is empty");
//      getSticker(ApiService.fetchApi(), "/Stickers");
    }

    if (isEmptyDir("/StickersCategory1", myContext) == true) {
      System.out.println("StickersCategory1 is empty");
//      getSticker(ApiService.fetchApi2(), "/StickersCategory1");
    }

    if (isEmptyDir("/StickersCategory2", myContext) == true) {
      System.out.println("StickersCategory2 is empty");
//      getSticker(ApiService.fetchApi2(), "/StickersCategory2");
    }
  }

  public static String[] getAssets(String sticker) {
    Field fields[] = R.raw.class.getDeclaredFields();
    String[] names = new String[fields.length];
    try {
      for (int i = 0; i < fields.length; i++) {
        Field f = fields[i];
        names[i] = f.getName();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return names;
  }

  public static List<String> getAssets2(String sticker) {
    Field fields[] = R.raw.class.getDeclaredFields();
    String[] names = new String[fields.length];
    try {
      for (int i = 0; i < fields.length; i++) {
        Field f = fields[i];
        names[i] = f.getName();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    List<String> namesFinal = new ArrayList<>();
    for (int i = 0; i < names.length; i++)
    {
      if (names[i].contains(sticker))
      {
//        names[i] = null;
//        break;
        System.out.println(names[i]);
        namesFinal.add(names[i]);
      }
    }

    return namesFinal;
  }


  public static File getFileForResource(
    @NonNull Context context, @RawRes int res, @NonNull File outputDir,
    @NonNull String filename) {
    final File outputFile = new File(outputDir, filename);
    final byte[] buffer = new byte[4096];
    InputStream resourceReader = null;
    try {
      try {
        resourceReader = context.getResources().openRawResource(res);
        OutputStream dataWriter = null;
        try {
          dataWriter = new FileOutputStream(outputFile);
          while (true) {
            final int numRead = resourceReader.read(buffer);
            if (numRead <= 0) {
              break;
            }
            dataWriter.write(buffer, 0, numRead);
          }
          return outputFile;
        } finally {
          if (dataWriter != null) {
            dataWriter.flush();
            dataWriter.close();
          }
        }
      } finally {
        if (resourceReader != null) {
          resourceReader.close();
        }
      }
    } catch (IOException e) {
      return null;
    }
  }
}
