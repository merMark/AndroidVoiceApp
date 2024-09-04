package com.example.voiceapplication;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.SpeechRecognizer;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.voiceapplication.databinding.ActivityMainBinding;
import com.example.voiceapplication.service.CustomService;
import com.example.voiceapplication.service.receive.ServiceReceiver;
import com.google.android.material.snackbar.Snackbar;

import android.view.Menu;
import android.view.MenuItem;

import java.io.File;
import java.util.logging.FileHandler;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private static final int PERMISSION_RECORD_AUDIO = 2;

    public static Context context;
    public static File filesDir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        checkRecordable();

        //アプリ固有ディレクトリ作成
        FileHandler(getApplicationContext());

        ServiceReceiver.setBroadCastIntent(getApplicationContext());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public Boolean checkRecordable(){
        if (!SpeechRecognizer.isRecognitionAvailable(getApplicationContext())){
            //音声認識サポート外
            return false;
        }
        if (Build.VERSION.SDK_INT >= 23){
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                    != PackageManager.PERMISSION_GRANTED){
                if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED)){
                    ActivityCompat.requestPermissions(this,new String[]{
                            Manifest.permission.RECORD_AUDIO,Manifest.permission.WRITE_EXTERNAL_STORAGE
                    }, PERMISSION_RECORD_AUDIO);
                    return false;
                }
            }
        }
        return false;
    }

    public static void FileHandler(Context applicationContext){
        context = applicationContext;
        filesDir = applicationContext.getFilesDir();
        context.getExternalFilesDir("");
    }

    public static File getFile(String filePath, String fileName){
        return new File(context.getExternalFilesDir(filePath), fileName);
    }

    public static String getExternalFilePath(){
        return context.getExternalFilesDir("").getPath();
    }

}