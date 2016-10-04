package prob.procon.dmcc2016.myapplication;

import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * Created by maeda on 2016/10/04.
 */

public class ImageInfo {
    private String image_name;
    private String user_id;
    private String bmp;
    ImageInfo(String image_name, String user_id ,String bmp){
        this.image_name = image_name;
        this.user_id = user_id;
        this.bmp = bmp;
    }
    public String returnName(){
        return image_name;
    }
    public String returnUserId(){
        return user_id;
    }
    public String returnBmp(){
        return bmp;
    }
}
