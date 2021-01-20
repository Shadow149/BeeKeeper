package com.example.beekeeping;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class NotificationReceiver extends BroadcastReceiver {

    public NotificationReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        Intent intentService = new Intent(context, NotificationIntentService.class);
        intentService.putExtras(extras);
//        context.startService(intentService);
        NotificationIntentService.enqueueWork(context,intentService);
    }
}
