package com.alexz.messenger.app.ui.common;

import android.content.Context;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.messenger.app.R;

public class VoicePlayerView extends RelativeLayout {

    ImageButton btnPlay;

    public VoicePlayerView(Context context) {
        super(context);
        init(context);
    }

    public void play(){
        btnPlay.setImageResource(R.drawable.ic_play);
    }

    public void pause() {
        btnPlay.setImageResource(R.drawable.ic_pause);
    }

    private void init(Context context){
        inflate(context, R.layout.item_voice_player,this);
        btnPlay = findViewById(R.id.btn_voice_play);
    }

}
