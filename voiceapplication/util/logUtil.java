package com.example.voiceapplication.util;

import android.os.Environment;
import android.util.Log;

import com.example.voiceapplication.MainActivity;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class logUtil {
    // ベースフォルダ名
    protected String baseDirectoryName;
    // ベースファイル名
    protected String baseFileName;

    public logUtil(String fileName){
        // フォルダ名を固定
        this.baseDirectoryName = "log";
        // ファイル名を固定
        this.baseFileName = fileName;
    }

    public void outputLog(String outputText){
        //実行時間の取得
        Timestamp outputTime = new Timestamp(System.currentTimeMillis());
        // 出力ディレクトリの取得
        File directory = createOutputDirectory();
        // 出力ファイルの取得
        File outputFile = createOutputFile(directory, outputTime);
        // 出力テキストの作成
        String newOutputText = createOutputText(outputText,outputTime);

        try(
                // ファイル出力ストリームを生成
                FileOutputStream fos = new FileOutputStream(outputFile, true);
                // テキスト出力ストリームを生成
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos, "UTF-8"));
        ) {
            // テキスト出力
            bw.write(newOutputText);
            // 改行の出力
            bw.newLine();
            bw.flush();
        } catch (IOException e) {
        }
    }

    File createOutputDirectory(){
        // 出力するディレクトリのフルパスを作成
        String externalStorageDirectoryPath = MainActivity.getExternalFilePath();
        //固有パス
        String fullDirPath = externalStorageDirectoryPath + "/" + baseDirectoryName;

        // 出力ディレクトリの取得
        File directory = new File(fullDirPath);
        if(!directory.exists()){
            // ディレクトリが無い場合は生成
            directory.mkdirs();
        }

        return directory;
    }

    File createOutputFile(File directory, Timestamp outputTime){

        // 出力ファイル名の作成

        // 出力するファイルの取得
        File outputFile = new File(directory, baseFileName);
        if(!outputFile.exists()){
            // 出力するファイルが存在しない場合は生成
            createFile(outputFile);
        }

        return outputFile;
    }
    //ファイルの作成処理
    void createFile(File file){
        try{
            file.createNewFile();
        }catch(IOException e){
        }
    }

    //ファイル出力用のテキスト作成
    String createOutputText(String outputText, Timestamp outputTime){
        // 現在日時の作成
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String strOutputTime = sdf.format(outputTime);
        // 出力内容の先頭に現在日時を追加
        String newOutputText = strOutputTime + "," + outputText;
        return newOutputText;
    }

}
