package com.example.voiceapplication.Fragment;

import android.annotation.SuppressLint;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.voiceapplication.MainActivity;
import com.example.voiceapplication.R;
import com.example.voiceapplication.databinding.FragmentSecondBinding;
import com.example.voiceapplication.soundRecorder.AudioRecorderWrapper;
import com.example.voiceapplication.soundRecorder.MediaRecorderWrapper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class VoiceRecordingFragment extends Fragment {

    private FragmentSecondBinding binding;
    private View root;
    private TextView recordingState;

    //MediaRecorder使用
    private Button recordingStart;
    private Button recordingStop;
    private MediaRecorderWrapper recorder;

    //AudioRecorder使用
    private Button audioRecordStart;
    private Button audioRecordStop;
    private AudioRecorderWrapper auRecorder;
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentSecondBinding.inflate(inflater, container, false);
        root = binding.getRoot();

        recordingState = root.findViewById(R.id.textview_recording);

        //MediaRecord処理
        recordingStart = root.findViewById(R.id.recording_start);
        recordingStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filePath = MainActivity.getExternalFilePath() + "/";
                String fileName = createTimeFormatFileName() + ".mp4";
                recorder = new MediaRecorderWrapper(filePath, fileName);
                recorder.startMediaRecorder();
                recordingState.setText("録音中");
            }
        });

        //録音停止ボタン処理
        recordingStop = root.findViewById(R.id.recording_stop);
        recordingStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recorder != null){
                    recorder.stopMediaRecorder();
                }
                recordingState.setText("録音停止中");
            }
        });

        //AudioRecord処理
        audioRecordStart = root.findViewById(R.id.recording_start2);
        audioRecordStart.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View v) {
                String filePath = MainActivity.getExternalFilePath() + "/" + "audioRecord";
                String fileName = createTimeFormatFileName() + ".wav";
                // 出力ディレクトリの取得
                File directory = new File(filePath);
                if(!directory.exists()){
                    // ディレクトリが無い場合は生成
                    directory.mkdirs();
                }
                auRecorder = new AudioRecorderWrapper(filePath + "/" + fileName, 44100);
//                auRecorder.startAudioRecord();
                recordingState.setText("録音中");
            }
        });

        audioRecordStop = root.findViewById(R.id.recording_stop2);
        audioRecordStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (auRecorder != null){
                    auRecorder.stopAudioRecord();
                }
                recordingState.setText("録音停止中");
            }
        });



        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(VoiceRecordingFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });

        binding.buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(VoiceRecordingFragment.this)
                        .navigate(R.id.action_SecondFragment_to_ThirdFragment);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        binding = null;
    }

    public static String createTimeFormatFileName(){
        DateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");
        Date date = new Date(System.currentTimeMillis());
        return df.format(date);
    }
}