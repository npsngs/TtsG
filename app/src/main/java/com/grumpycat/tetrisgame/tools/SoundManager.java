package com.grumpycat.tetrisgame.tools;

import android.content.Context;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;

public class SoundManager {
    private static SoundPool mSoundPoll;
    public static void init(Context context){

        /*if(Build.VERSION.SDK_INT >= 21){
            AudioAttributes abs = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build() ;
            mSoundPoll =  new SoundPool.Builder()
                    .setMaxStreams(100)
                    .setAudioAttributes(abs)
                    .build() ;
        }else{

        }*/
        mSoundPoll = new SoundPool(100, AudioManager.STREAM_MUSIC, 0);
        try{
            AssetManager am = context.getAssets();
            click_id = mSoundPoll.load(am.openFd("sound/click.wav"), 1);
            fast_drop_id = mSoundPoll.load(am.openFd("sound/fast_drop.wav"), 1);
            drop_id = mSoundPoll.load(am.openFd("sound/drop.ogg"), 1);
            lineclear1_id = mSoundPoll.load(am.openFd("sound/line_clear_1.mp3"), 1);
            lineclear4_id = mSoundPoll.load(am.openFd("sound/line_clear_4.wav"), 1);
            gameover_id = mSoundPoll.load(am.openFd("sound/gameover1.mp3"), 1);
            congratulations_id = mSoundPoll.load(am.openFd("sound/gameover2.mp3"), 1);
            countdown_id = mSoundPoll.load(am.openFd("sound/countdown.mp3"), 1);
            lvl_up_id = mSoundPoll.load(am.openFd("sound/levelup.mp3"), 1);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static int click_id;
    private static int fast_drop_id;
    private static int drop_id;
    private static int lineclear1_id;
    private static int lineclear4_id;
    private static int gameover_id;
    private static int congratulations_id;
    private static int countdown_id;
    private static int lvl_up_id;
    public static void playClick(){
        float volume = AppCache.getVolume();
        mSoundPoll.play(click_id, volume, volume, 0, 0, 1f);
    }

    public static void playFastDrop(){
        float volume = AppCache.getVolume();
        mSoundPoll.play(fast_drop_id, volume, volume, 0, 0, 1f);
    }

    public static void playDrop(){
        float volume = AppCache.getVolume();
        mSoundPoll.play(drop_id, volume, volume, 0, 0, 1f);
    }

    public static void playGameover(){
        float volume = AppCache.getVolume();
        mSoundPoll.play(gameover_id, volume, volume, 0, 0, 1f);
    }

    public static void playCountDown(){
        float volume = AppCache.getVolume();
        mSoundPoll.play(countdown_id, volume, volume, 0, 0, 1f);
    }

    public static void stopCountDown() {
        mSoundPoll.stop(countdown_id);
    }

    public static void playCongratulations(){
        float volume = AppCache.getVolume();
        mSoundPoll.play(congratulations_id, volume, volume, 0, 0, 1f);
    }

    public static void playLevelUp(){
        float volume = AppCache.getVolume();
        mSoundPoll.play(lvl_up_id, volume, volume, 0, 0, 1.5f);
    }

    public static void playClearLine(int line){
        if (line < 4){
            mSoundPoll.play(lineclear1_id, 1, 1, 0, 0, 1f);
        }else{
            mSoundPoll.play(lineclear4_id, 1f, 1f, 0, 0, 1f);
        }

    }
}
