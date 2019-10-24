package com.cat.sutils;

import android.graphics.Bitmap;

import androidx.annotation.Nullable;
import androidx.core.util.Consumer;
import androidx.palette.graphics.Palette;

import java.util.Objects;

public class PaletteHelper {


    public static void generate(Bitmap bitmap, int defaultColor,Consumer<Integer> colorConsumer){
        Objects.requireNonNull(colorConsumer);
        Objects.requireNonNull(bitmap);
        Palette.from(bitmap).generate((palette)->colorConsumer.accept(getColorNeeded(palette,defaultColor)));
    }

    public static int getColorNeeded(Palette palette,int defaultColor){
        int color=palette.getLightVibrantColor(defaultColor);
        int index=0;
        while (color==defaultColor){
            switch (index){
                case 0:
                    color=palette.getDarkVibrantColor(defaultColor);
                    break;
                case 1:
                    color= palette.getVibrantColor(defaultColor);
                    break;
                case 2:
                    color= palette.getLightMutedColor(defaultColor);
                    break;
                case 3:
                    color= palette.getDarkMutedColor(defaultColor);
                    break;
                case 4:
                    color= palette.getMutedColor(defaultColor);
                    break;
            }
            index++;
            if (index>=5) {
                break;
            }
        }
        return color;
    }



}
