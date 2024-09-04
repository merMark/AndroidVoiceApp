package com.example.voiceapplication.Fragment;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.voiceapplication.R;
import com.example.voiceapplication.databinding.FragmentFirstBinding;

import java.util.ArrayList;

public class VoiceRecognitionFragment extends Fragment {

    private FragmentFirstBinding binding;
    private SpeechRecognizer mRecorder;
    View root;
    private Button startVoiceButton;
    private Button stopVoiceButton;
    private TextView voiceText;

    //ActivityResultで実行する際にどのインテントからの送信なのかを判断する任意の数値
    int REQUEST_CODE = 001;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        root = binding.getRoot();
//        root = inflater.inflate(R.layout.fragment_first, container,false);
        voiceText = root.findViewById(R.id.textview_first);
        startVoiceButton = root.findViewById(R.id.button_voice_start);
        startVoiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRecorder = SpeechRecognizer.createSpeechRecognizer(getContext());
                mRecorder.setRecognitionListener(mRecognitionListener);
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                //音声認識モードの指定
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                //
                intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getContext().getPackageName());
                //連続音声認識ができるようにするかどうか
                intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
                //音声認識結果の最大数
                intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 100);
                //音声入力開始のダイアログ表示
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "認識中");
                //言語設定
//                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ja-Latn");

                //音声認識の時間延長テスト　api側のバグで使えないらしい
//                //認識エンジンが音声の聞き取りを停止してから、入力が完了したと見なして認識セッションを終了するまでにかかる時間。
//                intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 200000);
//                //認識セッションの最小の長さを示すオプションの整数。認識エンジンは、この時間が経過する前に音声の認識を停止しません。
//                intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 300000);
//                //音声が聞こえなくなってから、入力が完了したと見なされるまでにかかる時間。
//                intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, 500000);
                try {
                    startActivityForResult(intent, REQUEST_CODE);
                }catch (Exception e){
                    e.printStackTrace();
                }
//                mRecorder.startListening(intent);
            }
        });

        stopVoiceButton = root.findViewById(R.id.button_voice_stop);
        stopVoiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopRecording();
            }
        });

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((REQUEST_CODE == requestCode) &&(RESULT_OK == resultCode)){
            ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            voiceText.setText(results.get(0));
            for (String val: results){

            }
        }

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(VoiceRecognitionFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void stopRecording(){
        if (mRecorder != null){
            mRecorder.stopListening();
            mRecorder.cancel();
            mRecorder.destroy();
            mRecorder = null;
        }
    }

    private RecognitionListener mRecognitionListener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle params) {

        }

        @Override
        public void onBeginningOfSpeech() {

        }

        @Override
        public void onRmsChanged(float rmsdB) {

        }

        @Override
        public void onBufferReceived(byte[] buffer) {

        }

        @Override
        public void onEndOfSpeech() {
            stopRecording();
        }

        @Override
        public void onError(int error) {
            stopRecording();
        }

        @Override
        public void onResults(Bundle results) {
            //通常の音声認識
            ArrayList<String> values = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            for (String val: values){
                voiceText.setText(val);
            }
        }

        @Override
        public void onPartialResults(Bundle partialResults) {
            //連続音声認識
//            String voice = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION).get(0);
//            if (voice.length() > 0){
//                voiceText.setText(voice);
//            }
        }

        @Override
        public void onEvent(int eventType, Bundle params) {

        }
    };

}