package com.example.godaibo.reversisimulator;

import android.content.Context;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.util.DisplayMetrics;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

/**
 * Created by godaibo on 4/2/2015.
 */
public class TableConfig {
    public static int DEFAULT_PIN_SIZE = 50;
    private static MediaPlayer player ;

    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }

    public static int[] pinIdList = new int[] {
                /*
                R.drawable.black_pin40,
                R.drawable.blue_pin40,
                R.drawable.brown_pin40,
                R.drawable.green_pin40,
                R.drawable.lightblue_pin40,
                R.drawable.orange_pin40,
                R.drawable.pink_pin40,
                R.drawable.red_pin40,
                R.drawable.yellow_pin40,
                R.drawable.violet_pin40,
                R.drawable.pin40
                */
    };


    public static int tileBackground = R.drawable.blank;




    public static Animation getFadeScaleOutAnim(Context ctx) {

        return AnimationUtils.loadAnimation(ctx.getApplicationContext(), R.anim.fadeout_scaleout);

    }

    public static Animation getFadeScaleInAnim(Context ctx) {

        return  AnimationUtils.loadAnimation(ctx.getApplicationContext(), R.anim.fadein_scalein);

    }

    public static Animation getRotateAnim(Context ctx) {

        return  AnimationUtils.loadAnimation(ctx.getApplicationContext(), R.anim.rotate);


    }



    public static void playSound(Context ctx) {
        if (player != null) {
            if (player.isPlaying()) {
                return ;
            }
        }
        else
            player = MediaPlayer.create(ctx, R.raw.pinsound);

        _play();
    }

    private static void _play() {
        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                player.start();
            }
        });
        t.start();
    }


}
