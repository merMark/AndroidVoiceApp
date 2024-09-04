package com.example.voiceapplication.SensorService;

import android.content.Context;
import android.os.PowerManager;

import com.example.voiceapplication.util.logUtil;

public class DisplayStatusService {

    PowerManager pManager;

    logUtil log = new logUtil("displayStatus.txt");
    public DisplayStatusService(Context context){
        this.pManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
    }

    public boolean getDisplayStatus(){
        if (this.pManager != null){
            //画面がonになっている場合はtrue,offになっている場合はfalse

            log.outputLog("displayStatus:," + String.valueOf(this.pManager.isInteractive()));
            return this.pManager.isInteractive();
        }else {
            //pManagerが生成されていなければfalseを返す
            return false;
        }
    }
}
