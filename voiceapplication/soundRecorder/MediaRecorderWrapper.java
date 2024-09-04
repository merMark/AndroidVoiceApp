package com.example.voiceapplication.soundRecorder;

import android.media.MediaRecorder;

import com.example.voiceapplication.MainActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MediaRecorderWrapper {
    //MediaRecorder使用
    private MediaRecorder recorder;
    private String filePath;
    private String fileName;

    public MediaRecorderWrapper(String filePath, String fileName){
        this.filePath = filePath;
        this.fileName = fileName;
    }

    public void startMediaRecorder(){
        //作成する音声ファイルの設定
        recorder = new MediaRecorder();
        //通話用マイクとセカンドマイク  どちらもあまり変わらない
//                recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);

        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        //音声ファイルの保存先
//                MainActivity.getFile(filePath, fileName);
        recorder.setOutputFile(filePath + fileName);
        //録音準備&録音開始
        try {
            recorder.prepare();
            recorder.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopMediaRecorder(){
        try {
            if (recorder != null) {
                recorder.stop();
                recorder.reset();
                recorder.release();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
