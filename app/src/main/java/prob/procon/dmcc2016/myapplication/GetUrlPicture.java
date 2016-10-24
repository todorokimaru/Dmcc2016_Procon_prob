package prob.procon.dmcc2016.myapplication;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by guest01 on 2016/10/08.
 */

public class GetUrlPicture {
    public void saveFile(String url, int x, int y, int zoom, String mount_name, Context context){

        try {
            URL request_url = new URL(url+mount_name+"-"+zoom+"-"+x+"-"+y+".png");
            HttpURLConnection http = (HttpURLConnection)request_url.openConnection();
            http.setRequestMethod("GET");
            //BufferedReader reader = new BufferedReader(new InputStreamReader(http.getInputStream()));
            Bitmap bmp = BitmapFactory.decodeStream(http.getInputStream());
            savePngLocalStorage(mount_name+"-"+zoom+"-"+x+"-"+y+".png", bmp , context);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public final static boolean savePngLocalStorage(String fileName, Bitmap bitmap, Context context)throws IOException{
        BufferedOutputStream bos = null;
        Bitmap bmp = null;
        try{
            bos = new BufferedOutputStream(context.openFileOutput(fileName, Context.MODE_PRIVATE));
            bmp = bitmap.copy(Bitmap.Config.ARGB_8888,true);
            return bmp.compress(Bitmap.CompressFormat.PNG, 100, bos);
        } finally {
            if(bmp != null){
                bmp.recycle();
                bmp = null;
            }
            try {
                bos.close();
            }catch (Exception e){
                //IOExeption
            }
        }
    }
}
