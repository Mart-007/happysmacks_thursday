package com.happysmacks.happysmacks.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.happysmacks.happysmacks.R;

public class DashboardActivity extends AppCompatActivity {
  GoogleSignInClient mGoogleSignInClient;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_dashboard);
  }

  public void signOut(View view) {

    mGoogleSignInClient.signOut()
      .addOnCompleteListener(this, new OnCompleteListener<Void>() {
        @Override
        public void onComplete(@NonNull Task<Void> task) {
          // ...
          goBack();
        }
      });

  }

  public void goBack(){
    Intent intent = new Intent(this, LoginActivity.class);
    intent.addFlags( Intent.FLAG_ACTIVITY_NO_HISTORY );
    startActivity(intent);
  }
}