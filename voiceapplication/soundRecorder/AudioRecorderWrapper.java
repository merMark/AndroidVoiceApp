package com.example.voiceapplication.soundRecorder;

import android.annotation.SuppressLint;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

public class AudioRecorderWrapper {
    //オーディオレコード用サンプリング周波数
    private final int samplingRate = 44100;

    //録音用のオーディオレコードクラス
    AudioRecord audioRecord;
    //オーディオレコード用バッファのサイズ
    private int bufSize;
    //オーディオレコード用バッファ
    private short[] shortData;
    private MyWaveFile myWaveFile;

    public AudioRecorderWrapper( String filePath, int samplingRate) {
        this.myWaveFile = new MyWaveFile(filePath, samplingRate);
        this.initAudioRecord();
    }

    //AudioRecordの初期化
    @SuppressLint("MissingPermission")
    private void initAudioRecord() {
        // AudioRecordオブジェクトを作成
        bufSize = android.media.AudioRecord.getMinBufferSize(samplingRate,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT);
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
                samplingRate,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                bufSize);

        shortData = new short[bufSize / 2];

        // コールバックを指定
        audioRecord.setRecordPositionUpdateListener(new AudioRecord.OnRecordPositionUpdateListener() {
            // フレームごとの処理
            @Override
            public void onPeriodicNotification(AudioRecord recorder) {
                // 読み込む
                audioRecord.read(shortData, 0, bufSize / 2);
                // ファイルに書き出す
                myWaveFile.addBigEndianData(shortData);
            }

            @Override
            public void onMarkerReached(AudioRecord recorder) {
            }
        });
        // コールバックが呼ばれる間隔を指定
        audioRecord.setPositionNotificationPeriod(bufSize / 2);
        startAudioRecord();
    }

    @SuppressLint("MissingPermission")
    public void startAudioRecord(){
        myWaveFile.createFile();
        audioRecord.startRecording();
    }

    public void stopAudioRecord(){
        audioRecord.stop();
    }

}
