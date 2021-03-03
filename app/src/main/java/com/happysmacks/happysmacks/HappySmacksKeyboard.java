package com.happysmacks.happysmacks;


import android.content.ClipDescription;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.core.view.inputmethod.EditorInfoCompat;
import androidx.core.view.inputmethod.InputConnectionCompat;
import androidx.core.view.inputmethod.InputContentInfoCompat;

import com.happysmacks.happysmacks.Activities.AddSticker;
import com.happysmacks.happysmacks.Services.ApiService;
import com.happysmacks.happysmacks.Services.DirectoryManager;
import com.happysmacks.happysmacks.Services.ImageSender;
import com.happysmacks.happysmacks.Services.Preferences;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.util.List;


public class HappySmacksKeyboard extends InputMethodService implements KeyboardView.OnKeyboardActionListener {

  //Keyboard variables
  private RelativeLayout kl;
  private KeyboardView kv;
  private LinearLayout iv;
  private LinearLayout customEmojiView;
  private Keyboard keyboard;
  private boolean isSymbolVisible = false;
  private boolean isEmojiVisible = false;
  private boolean isCaps = false;
  //ImageKeyboard Variables
  private static final String AUTHORITY = "com.happysmacks.happysmacks.happysmackskeyboard";
  private static final String MIME_TYPE_PNG = "image/png";
  private String[] rawFiles;
  private List<String> imageAssets;
  private File[] downloadedFiles;
  List<String> stickerList;

  @Override
  public View onCreateInputView() {

    return initKeyboard();
  }

  public View initKeyboard() {

    System.out.println("Call keyboard");
    kl = (RelativeLayout) getLayoutInflater().inflate(R.layout.keyboard_layout_image, null);
    kv = kl.findViewById(R.id.keyboard_view);
    this.isSymbolVisible = false;
    setMainView();
    iv = kl.findViewById(R.id.images_view);
    customEmojiView = kl.findViewById(R.id.custom_emoji_view);
    return kl;
  }

  public void commitImage(String imageDescription, String mimeType, File file) {
    final Uri contentUri = FileProvider.getUriForFile(this, AUTHORITY, file);
    InputContentInfoCompat inputContentInfo = new InputContentInfoCompat(
      contentUri,
      new ClipDescription(imageDescription, new String[]{MIME_TYPE_PNG}),
      null
    );
    InputConnection inputConnection = getCurrentInputConnection();
    EditorInfo editorInfo = getCurrentInputEditorInfo();
    int flags = 0;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
      flags |= InputConnectionCompat.INPUT_CONTENT_GRANT_READ_URI_PERMISSION;
    }
    InputConnectionCompat.commitContent(
      inputConnection, editorInfo, inputContentInfo, flags, null);
  }

  @Override
  public void onCreate() {
    super.onCreate();


  }

  public void initializeImages() {
//    LinearLayout ImageContainer = (LinearLayout) kl.findViewById(R.id.imageContainer);
//    LinearLayout ImageContainerColumn = (LinearLayout) getLayoutInflater().inflate(R.layout.image_container_column, ImageContainer, false);
////    ImageContainerColumn.addView(ImgButton);
    getSticker(ApiService.fetchApi2(), "/StickersCategory1");
    getSticker(ApiService.fetchApi2(), "/StickersCategory2");
    getSticker(ApiService.fetchApi2(), "/StickersCategory3");
    getSticker(ApiService.fetchApi2(), "/StickersCategory4");
    getSticker(ApiService.fetchApi2(), "/StickersCategory5");
    getSticker(ApiService.fetchApi2(), "/StickersCategory6");
    getSticker(ApiService.fetchApi2(), "/StickersCategory7");

  }

  public void onKey(int i, int[] ints) {
    InputConnection ic = getCurrentInputConnection();
    playClick(i);
    switch (i) {
      case Keyboard.KEYCODE_ALT:
        if (this.isSymbolVisible)
          this.setMainView();
        else
          this.setSymbolView();
        break;

      //custom number for switching to emoj
      case 5000:
        if (this.isEmojiVisible)
          this.setMainView();
        else
          this.changeToCustomEmojiView();
        break;

      case Keyboard.KEYCODE_DELETE:
        ic.deleteSurroundingText(1, 0);
        break;

      case Keyboard.KEYCODE_SHIFT:
        isCaps = !isCaps;
        keyboard.setShifted(isCaps);
        kv.invalidateAllKeys();
        break;

      case Keyboard.KEYCODE_MODE_CHANGE:
        changeToImageView();  //Call function to show image view
        break;
      case Keyboard.KEYCODE_DONE:
        ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
        break;

      default:
        char code = (char) i;
        if (Character.isLetter(code) && isCaps)
          code = Character.toUpperCase(code);
        ic.commitText(getEmoticon(i), 1);
    }
  }

  public String getEmoticon(int originalUnicode) {
    return new String(Character.toChars(originalUnicode));
  }

  private boolean isCommitContentSupported(@NonNull String mimeType) {
    if (getCurrentInputEditorInfo() == null) {
      return false;
    }
    final InputConnection ic = getCurrentInputConnection();
    if (ic == null) {
      return false;
    }
    final String[] supportedMimeTypes = EditorInfoCompat.getContentMimeTypes(getCurrentInputEditorInfo());
    for (String supportedMimeType : supportedMimeTypes) {
      if (ClipDescription.compareMimeTypes(mimeType, supportedMimeType)) {
        return true;
      }
    }
    return false;
  }

  private void setSymbolView() {
    keyboard = new Keyboard(this, R.xml.symbols);
    kv.setKeyboard(keyboard);
    kv.setOnKeyboardActionListener(this);
    this.isSymbolVisible = true;
  }

  private void setEmojiView() {
    keyboard = new Keyboard(this, R.xml.emoji);
    kv.setKeyboard(keyboard);
    kv.setOnKeyboardActionListener(this);
    this.isEmojiVisible = true;
  }

  private void setMainView() {
    keyboard = new Keyboard(this, R.xml.qwerty);
    kv.setKeyboard(keyboard);
    kv.setOnKeyboardActionListener(this);
    this.isSymbolVisible = false;
    this.isEmojiVisible = false;
  }

  public void showKeys(View view) {
    // Do something in response to button click
    stickerList.clear();
    removeViews();
    iv.setVisibility(View.GONE);
    kv.setVisibility(View.VISIBLE);
  }

  public void toKeyboard(View view) {
    // Do something in response to button click
    customEmojiView.setVisibility(View.GONE);
    kv.setVisibility(View.VISIBLE);
  }

  public void myStickers(View view) {
    // Do something in response to button click

    Button currentButton = kl.findViewById(R.id.myStickers);
    if (checkActiveTab(currentButton) == false) {
      stickerList.clear();
      removeViews();
      changeButtonColor(currentButton);
      getSticker(ApiService.fetchApi2(), "/StickersCategory1");
      getSticker(ApiService.fetchApi2(), "/StickersCategory1");


    }

  }

  public void lgbtqStickers(View view) {
    // Do something in response to button click

//      Preferences.savePreferences("initCategory2", getApplicationContext());
    Button currentButton = kl.findViewById(R.id.lgbtq);
    if (checkActiveTab(currentButton) == false) {
      stickerList.clear();
      removeViews();
      changeButtonColor(currentButton);
      getSticker(ApiService.fetchApi3(), "/StickersCategory2");
      getSticker(ApiService.fetchApi3(), "/StickersCategory2");


    }

  }

  public void mimeStickers(View view) {
    // Do something in response to button click
    Button currentButton = kl.findViewById(R.id.mime);
    if (checkActiveTab(currentButton) == false) {
      stickerList.clear();
      removeViews();
      changeButtonColor(currentButton);
      getSticker(ApiService.fetchApi2(), "/StickersCategory3");
      getSticker(ApiService.fetchApi2(), "/StickersCategory3");


    }
  }

  public void fetishStickers(View view) {
    // Do something in response to button click
    Button currentButton = kl.findViewById(R.id.fetish);
    if (checkActiveTab(currentButton) == false) {
      stickerList.clear();
      removeViews();
      changeButtonColor(currentButton);
      getSticker(ApiService.fetchApi2(), "/StickersCategory4");
      getSticker(ApiService.fetchApi2(), "/StickersCategory4");


    }
  }

  public void memesStickers(View view) {
    // Do something in response to button click

    Button currentButton = kl.findViewById(R.id.memes);
    if (checkActiveTab(currentButton) == false) {
      stickerList.clear();
      removeViews();
      changeButtonColor(currentButton);
      getSticker(ApiService.fetchApi2(), "/StickersCategory5");
      getSticker(ApiService.fetchApi2(), "/StickersCategory5");


    }
  }

  public void kamasutraStickers(View view) {
    // Do something in response to button click
    Button currentButton = kl.findViewById(R.id.kamasutra);
    if (checkActiveTab(currentButton) == false) {
      stickerList.clear();
      removeViews();
      changeButtonColor(currentButton);
      getSticker(ApiService.fetchApi2(), "/StickersCategory6");
      getSticker(ApiService.fetchApi2(), "/StickersCategory6");


    }
  }

  public void othersStickers(View view) {
    // Do something in response to button click
    Button currentButton = kl.findViewById(R.id.others);
    if (checkActiveTab(currentButton) == false) {
      stickerList.clear();
      removeViews();
      changeButtonColor(currentButton);
      getSticker(ApiService.fetchApi2(), "/StickersCategory7");
      getSticker(ApiService.fetchApi2(), "/StickersCategory7");


    }
  }

  public void toSmileyEmoji(View view) {
    String[] emojiList = {"😀", "😃", "😄", "😁", "😆", "😅", "😂", "🤣", "😊", "😇", "🙂", "🙃", "😉",
      "😌", "😍", "🥰", "😘", "😗", "😙", "😚", "😋", "😛", "😝", "😜", "🤪", "🤨", "🧐", "🤓", "😎",
      "🤩", "🥳", "😏", "😒", "😞", "😔", "😟", "😕", "🙁", "😣", "😖", "😫", "😩", "🥺", "😢",
      "😭", "😤", "😠", "😡", "🤬", "🤯", "😳", "🥵", "🥶", "😱", "😨", "😰", "😥", "😓", "🤗", "🤔",
      "🤭", "🤫", "🤥", "😶", "😐", "😑", "😬", "🙄", "😯", "😦", "😧", "😮", "😲", "🥱", "😴", "🤤",
      "😪", "😵", "🤐", "🥴", "🤢", "🤮", "🤧", "😷", "🤒", "🤕", "🤑", "🤠", "😈", "👿", "👹", "👺",
      "🤡", "👻", "💀",  "👽", "👾", "🤖", "🎃", "😺", "😸", "😹", "😻", "😼", "😽", "🙀", "😿", "😾"};
    removeEmojiViews();
    initEmoji(emojiList);
  }
  public void toSmileyAnimals(View view) {
    String[] emojiList = {"🐶","🐱","🐭","🐹","🐰","🦊","🐻","🐼","🐻‍","🐨","🐯","🦁","🐮","🐷","🐽","🐸",
    "🐵","🙈","🙉","🙊","🐒","🐔","🐧","🐦","🐤","🐣","🐥","🦆","🦅","🦉","🦇","🐺","🐗","🐴","🦄","🐝","🐛",
    "🦋","🐌","🐞","🐜","🦟","🦗","🕷","🕸","🦂","🐢","🐍","🦎","🦖","🦕","🐙","🦑","🦐","🦞","🦀",
    "🐡","🐠","🐟","🐬","🐳","🐋","🦈","🐊","🐅","🐆","🦓","🦍","🦧","🐘","🦛","🦏","🐪","🐫","🦒","🦘",
    "🐃","🐂","🐄","🐎","🐖","🐏","🐑","🦙","🐐","🦌","🐕","🐩","🦮","🐕‍","🦺","🐈","🐈‍","🐓","🦃","🦚",
      "🦜","🦢","🦩","🕊","🐇","🦝","🦨","🦡","🦦","🦥","🐁","🐀","🐿","🦔","🐾","🐉","🐲","🌵","🎄","🌲",
      "🌳","🌴","🌱","🌿","🍀","🎍","🎋","🍃","🍂","🍁","🍄","🐚","🌾","💐","🌷","🌹","🥀","🌺","🌸","🌼","🌻",
      "🌞","🌝","🌛","🌜","🌚","🌕","🌖","🌗","🌘","🌑","🌒","🌓","🌔","🌙","🌎","🌍","🌏","🪐","💫","🌟","✨"
      ,"💥","🔥","🌪","🌈","🌤","🌥","🌦","💨","💧","💦"};
    removeEmojiViews();
    initEmoji(emojiList);
  }
// add emoji list
  public void toSmileyFood(View view) {
    String[] emojiList = {"🍏", "🍎", "🍐", "🍊", "🍋", "🍌", "🍉", "🍇", "🍓","🍈", "🍒", "🍑", "🥭", "🍍", "🥥",
    "🥝", "🍅", "🍆", "🥑", "🥦", "🥬", "🥒", "🌶","🌽", "🥕", "🧄", "🧅", "🥔", "🍠", "🥐", "🥯", "🍞", "🥖",
    "🥨", "🧀", "🥚", "🍳","🧈", "🥞","🧇","🥓", "🥩", "🍗", "🍖", "🦴", "🌭", "🍔", "🍟", "🍕","🥪", "🥙",
    "🧆", "🌮", "🌯","🥗", "🥘","🥫", "🍝", "🍜", "🍲", "🍛", "🍣", "🍱", "🥟", "🦪", "🍤","🍙", "🍚", "🍘", "🍥",
    "🥠","🥮","🍢", "🍡", "🍧", "🍨", "🍦", "🥧", "🧁", "🍰", "🎂","🍮", "🍭", "🍬", "🍫", "🍿", "🍩", "🍪",
    "🌰", "🥜", "🍯", "🥛", "🍼", "☕","🍵", "🧃","🥤", "🍶","🍺","🍻", "🥂", "🍷", "🥃", "🍸", "🍹", "🧉", "🍾",
    "🧊", "🥄", "🍴", "🍽","🥣", "🥡", "🥢", "🧂"};

    removeEmojiViews();
    initEmoji(emojiList);
  }

  public void toSmileySports(View view) {
    String[] emojiList = {"⚽", "🏀", "🏈", "⚾", "️🥎", "🎾", "🏐", "🏉", "🥏", "🎱", "🪀", "🏓","🏸","🏒",
    "🏑", "🥍","🏏","🥅","⛳","🪁","🏹","🎣","🤿","🥊","🥋","🎽","🛹","🥌","🎿","🏂","🪂", "🏋️‍♀","🏋","🏋️‍♂",
    "🤼‍♀","🤼","🤼‍♂","🤸‍♀","🤸","🤸‍♂","🤺", "🤾‍♀","🤾","🤾‍♂","🏌️‍♀","🏌️","🏌️‍♂","🏇","🧘‍♀","🧘","🧘‍♂","🏄‍♀","🏄","🏄‍♂",
    "🏊‍♀","🏊", "🏊‍♂","🤽‍♀","🤽","🤽‍♂","🚣‍♀","🚣","🚣‍♂","🧗‍♀","🧗","🧗‍♂","🚵‍♀","🚵","🚵‍♂","🚴‍♀","🚴","🚴‍♂","🏆", "🥇",
    "🥈","🥉","🏅","🎖","🏵","🎗","🎫","🎟","🎪","🤹","🤹‍♂","🤹‍♀","🎭","🩰","🎨","🎬","🎤","🎧","🎼","🎹","🥁","🎷","🎺",
    "🎸","🪕","🎻","🎲","🎯", "🎳", "🎮", "🎰","🧩"};

    removeEmojiViews();
    initEmoji(emojiList);
  }

  public void toSmileyCars(View view) {
    String[] emojiList = {"🚗", "🚕", "🚙", "🚌", "🚎", "🏎", "🚓","🚑", "🚒", "🚐","🚚", "🚛", "🚜","🦯", "🦽", "🦼",
    "🛴", "🚲", "🛵", "🏍", "🛺","🚨", "🚔", "🚍", "🚘", "🚖", "🚡", "🚠", "🚟", "🚃", "🚋", "🚞", "🚝", "🚄", "🚅",
    "🚈", "🚂", "🚆", "🚇", "🚊", "🚉", "🛫", "🛬", "🛩", "💺", "🛰", "🚀", "🛸", "🚁", "🛶", "⛵", "🚤", "🛥", "🛳","⚓",
    "⛽", "🚧", "🚦", "🚥", "🚏", "🗺", "🗿", "🗽", "🗼", "🏰", "🏯", "🏟","🎡", "🎢", "🎠", "⛲", "🏖", "🏝", "🏜",
    "🌋","🏔", "🗻", "🏕", "⛺", "🏠", "🏡", "🏘", "🏚", "🏗", "🏭","🏢", "🏬", "🏣", "🏤","🏥","🏦","🏨","🏪", "🏫",
    "🏩", "💒", "🏛","⛪", "🕌","🕍", "🛕","🕋","🛤", "🛣", "🗾","🎑", "🏞", "🌅", "🌄", "🌠", "🎇", "🎆","🌇", "🌆",
    "🏙", "🌃"};

    removeEmojiViews();
    initEmoji(emojiList);
  }

  public void toSmileyObjects(View view) {
    String[] emojiList = {"⌚","📱","📲", "💻","🖥","🖨", "🖱", "🖲","🕹", "🗜","💽", "💾", "💿", "📀","📼","📷","📸",
    "📹","🎥","📽","🎞","📞","☎", "📟", "📠","📺", "📻","🎙","🎚", "🎛", "🧭","⏰", "🕰:","🧿","🛍","🛒"};

    removeEmojiViews();
    initEmoji(emojiList);
  }

  public void toSmileySymbols(View view) {
    String[] emojiList = {"❤","🧡","💛", "💚", "💙", "💜", "🖤","🤍","🤎","💔","💕", "💞","💓","💗","💖",
    "💘", "💝","💟","🕉","🔯","🕎","🛐","⛎","♈","♉","♊","♋","♌","♍","♎","♏","♐","♑","♒","♓","🆔",
     "📴", "📳","🈶", "🈚","🈸","🆚","💮","🉐","🈴", "🈵", "🈹", "🈲","🆎", "🆑","🆘","❌","⭕","🛑", "⛔","📛",
    "🚫","💯","💢","🚷","🚯", "🚳", "🚱", "🔞", "📵", "🚭", "❗","❕", "❓", "❔","🔅", "🔆", "🚸","🔱","🔰",
    "✅","🈯","💹","❎","🌐", "💠","🌀", "💤","🏧","🚾","♿","🈳","🛂","🛃", "🛄", "🛅","🚹", "🚺","🚼","🚻",
    "🚮", "🎦", "📶", "🈁", "🔣","🔤", "🔡", "🔠", "🆖","🆗","🆙","🆒","🆕","🆓","⏸","⏹","⏺","⏩","⏫","⏬",
    "🔼","🔽","⏪"};

    removeEmojiViews();
    initEmoji(emojiList);
  }

  public void toSmileyFlags(View view) {
    String[] emojiList = { };

    removeEmojiViews();
    initEmoji(emojiList);
  }
  public void toKamasutraEmoji(View view) {
//  initEmoji();
    removeEmojiViews();
    getStickerFromAssets("kamasutra");
  }

  public void addStickers(View view) {
    System.out.println("print this");
    System.out.println("print this");
    System.out.println("print this");
    try{
      Intent intent = new Intent(this, AddSticker.class);
      intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
      startActivity(intent);
    }catch(Exception e){
      System.out.println(e.toString());
    }

  }



  private void changeToImageView() {
    if (Preferences.retrievePreferences(getApplicationContext()) == false) {
      initializeImages();
    }
    Preferences.savePreferences(getApplicationContext());
    Button currentButton = kl.findViewById(R.id.myStickers);
    changeButtonColor(currentButton);
    getSticker(ApiService.fetchApi2(), "/StickersCategory1");
    getSticker(ApiService.fetchApi2(), "/StickersCategory1");
    kv.setVisibility(View.GONE);
    iv.setVisibility(View.VISIBLE);
  }

  private void changeToCustomEmojiView() {
    getStickerFromAssets("kamasutra");
    kv.setVisibility(View.GONE);
    customEmojiView.setVisibility(View.VISIBLE);
  }

  private void playClick(int i) {
    AudioManager am = (AudioManager) getSystemService(AUDIO_SERVICE);
    switch (i) {
      case 32:
        am.playSoundEffect(AudioManager.FX_KEYPRESS_SPACEBAR);
        break;
      case Keyboard.KEYCODE_DONE:
      case 10:
        am.playSoundEffect(AudioManager.FX_KEYPRESS_DELETE);
        break;
      default:
        am.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD);
    }
  }

  public View getSticker(List<String> imageListFromApi, String filePath) {
    LinearLayout ImageContainer = (LinearLayout) kl.findViewById(R.id.imageContainer);
    LinearLayout ImageContainerColumn = (LinearLayout) getLayoutInflater().inflate(R.layout.image_container_column, ImageContainer, false);

    ImageContainer.removeAllViews();
    String root = getExternalFilesDir(null).toString();
    File myDir = new File(root + filePath);
    //array declaration]
    downloadedFiles = DirectoryManager.getAllDownloadedResourcesFile(filePath, getApplicationContext());
    stickerList = imageListFromApi;

    for (int i = 0; i < stickerList.size(); i++) {
      int finalI = i;
      Picasso.get().load(Uri.parse(stickerList.get(i))).into(new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
          DirectoryManager.toSave(finalI, myDir, bitmap);
        }

        @Override
        public void onBitmapFailed(Exception e, Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
      });
    }

    try {

      for (int i = 0; i < downloadedFiles.length; i++) {
        int finalI = i;
        if ((i % 2) == 0) {
          ImageContainerColumn = (LinearLayout) getLayoutInflater().inflate(R.layout.image_container_column, ImageContainer, false);
        }
        ImageButton ImgButton = (ImageButton) getLayoutInflater().inflate(R.layout.image_button, ImageContainerColumn, false);
        Picasso.get().load(downloadedFiles[i]).placeholder(R.drawable.loading).into(ImgButton);

//        ImgButton.setImageURI(Uri.fromFile(downloadedFiles[i]));
        ImgButton.setTag(downloadedFiles[i].getPath());
        ImgButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            final File tempDir = myDir;
            final File file = downloadedFiles[finalI];
            System.out.println("PRINT");
            System.out.println("PRINT");
            if (isCommitContentSupported(MIME_TYPE_PNG) == true) {
              try {
                HappySmacksKeyboard.this.commitImage("A ${emojiName} logo", MIME_TYPE_PNG, file);
              } catch (Exception e) {
                System.out.println(e.toString());
              }

            } else {
              try {
                ImageSender.sendImage(file, getApplicationContext());
              } catch (Exception e) {
                System.out.println(e.toString());
              }
            }


          }
          //end onclick
        });
        ImageContainerColumn.addView(ImgButton);
        if ((i % 2) == 0) {
          ImageContainer.addView(ImageContainerColumn);
        }

      }
    } catch (Exception e) {

    }
    this.isSymbolVisible = false;
    setMainView();
    iv = kl.findViewById(R.id.images_view);
    customEmojiView = kl.findViewById(R.id.custom_emoji_view);
    return iv;
  }

  public View initEmoji(String[] emojiList) {


    final File imagesDir = new File(getFilesDir(), "images");
    imagesDir.mkdirs();
    LinearLayout ImageContainer = (LinearLayout) kl.findViewById(R.id.customEmojiContainer1);

    LinearLayout ImageContainerColumn = (LinearLayout) getLayoutInflater().inflate(R.layout.image_container_column, ImageContainer, false);

    for (int i = 0; i < emojiList.length; i++) {
      System.out.println(i);
      if ((i % 4) == 0) {
        ImageContainerColumn = (LinearLayout) getLayoutInflater().inflate(R.layout.image_container_column, ImageContainer, false);
      }
//     Button button = (Button) getLayoutInflater().inflate(R.layout.emoji_text, ImageContainerColumn, false);
//      button.setText(emojiList[i]);
      TextView textView = (TextView) getLayoutInflater().inflate(R.layout.emoji_text, ImageContainerColumn, false);
      textView.setText(emojiList[i]);

      int finalI = i;
      textView.setOnClickListener(new View.OnClickListener() {
        InputConnection ic = getCurrentInputConnection();

        @Override
        public void onClick(View view) {
          String emoji = textView.getText().toString();

          ic.commitText(emoji, 1);
        }
      });
      ImageContainerColumn.addView(textView);


      if ((i % 4) == 0) {
        ImageContainer.addView(ImageContainerColumn);
      }


    }
    this.isSymbolVisible = false;
    setMainView();
    iv = kl.findViewById(R.id.images_view);
    customEmojiView = kl.findViewById(R.id.custom_emoji_view);
    return customEmojiView;
  }

  public View getStickerFromAssets(String sticker) {
    //change

    imageAssets = DirectoryManager.getAssets2(sticker);
    final File imagesDir = new File(getFilesDir(), "images");
    imagesDir.mkdirs();
    LinearLayout ImageContainer = (LinearLayout) kl.findViewById(R.id.customEmojiContainer1);

    LinearLayout ImageContainerColumn = (LinearLayout) getLayoutInflater().inflate(R.layout.image_container_column, ImageContainer, false);

    for (int i = 0; i < imageAssets.size(); i++) {
      System.out.println(i);
      if ((i % 4) == 0) {
        ImageContainerColumn = (LinearLayout) getLayoutInflater().inflate(R.layout.image_container_column, ImageContainer, false);
      }
      ImageButton ImgButton = (ImageButton) getLayoutInflater().inflate(R.layout.custom_emoji_image_button, ImageContainerColumn, false);
      ImgButton.setImageResource(getResources().getIdentifier(imageAssets.get(i), "raw", getPackageName()));
      ImgButton.setTag(imageAssets.get(i));

      int finalI = i;
      ImgButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          String emojiName = view.getTag().toString().replaceAll("_", "-");
          final File file = DirectoryManager.getFileForResource(HappySmacksKeyboard.this, getResources().getIdentifier(view.getTag().toString(), "raw", getPackageName()), imagesDir, "image.png");
          if (isCommitContentSupported(MIME_TYPE_PNG) == true) {
            try {
              HappySmacksKeyboard.this.commitImage("A ${emojiName} logo", MIME_TYPE_PNG, file);
            } catch (Exception e) {
              System.out.println(e.toString());
            }

          } else {

            try {
              ImageSender.sendImage(file, getApplicationContext());
            } catch (Exception e) {
              System.out.println(e.toString());
            }
          }


        }
      });
      ImageContainerColumn.addView(ImgButton);


      if ((i % 4) == 0) {
        ImageContainer.addView(ImageContainerColumn);
      }


    }
    this.isSymbolVisible = false;
    setMainView();
    iv = kl.findViewById(R.id.images_view);
    customEmojiView = kl.findViewById(R.id.custom_emoji_view);
    return customEmojiView;
  }


  public void removeViews() {
    LinearLayout ImageContainer = (LinearLayout) kl.findViewById(R.id.imageContainer);
    LinearLayout ImageContainerColumn = (LinearLayout) getLayoutInflater().inflate(R.layout.image_container_column, ImageContainer, false);
    ImageContainer.removeAllViews();
    ImageContainerColumn.removeAllViews();
  }

  public void removeEmojiViews() {
    LinearLayout ImageContainer = (LinearLayout) kl.findViewById(R.id.customEmojiContainer1);
    LinearLayout ImageContainerColumn = (LinearLayout) getLayoutInflater().inflate(R.layout.image_container_column, ImageContainer, false);
    ImageContainer.removeAllViews();
    ImageContainerColumn.removeAllViews();
  }

  public void changeButtonColor(Button paramButton) {

    Button button2 = kl.findViewById(R.id.myStickers);
    Button button3 = kl.findViewById(R.id.lgbtq);
    Button button4 = kl.findViewById(R.id.mime);
    Button button5 = kl.findViewById(R.id.fetish);
    Button button6 = kl.findViewById(R.id.memes);
    Button button7 = kl.findViewById(R.id.kamasutra);
    Button button8 = kl.findViewById(R.id.others);
    Button[] buttons = { button2, button3, button4, button5, button6, button7, button8};

    for (Button singleButton : buttons) {
      singleButton.setTextColor(Color.WHITE);
      singleButton.setBackgroundColor(Color.BLACK);
    }
    paramButton.setBackgroundColor(Color.RED);
    paramButton.setTextColor(Color.WHITE);
  }


  public boolean checkActiveTab(Button paramButton) {
    boolean isActive = false;
    int color = ((ColorDrawable) paramButton.getBackground()).getColor();
    if (color == Color.RED) {
      isActive = true;
    }
    return isActive;
  }

  @Override
  public void onPress(int primaryCode) {

  }

  @Override
  public void onRelease(int primaryCode) {

  }

  @Override
  public void onText(CharSequence text) {
    System.out.println(text);
  }

  @Override
  public void swipeLeft() {
    changeToImageView();
  }

  @Override
  public void swipeRight() {
    changeToImageView();
  }

  @Override
  public void swipeDown() {

  }

  @Override
  public void swipeUp() {

  }


}
