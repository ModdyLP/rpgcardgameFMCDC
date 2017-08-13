package utils;

import javafx.scene.control.ContextMenu;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by ModdyLP on 10.08.2017. Website: https://moddylp.de/
 */
public class Utils {
    public static int runden(int s, int v){
        double z = 0;
        z = s - s*v/20;
        z = Math.round(z);
        s = (int)z ;
        if (s<1){
            s=1;
        }
        return s;

    }
}
