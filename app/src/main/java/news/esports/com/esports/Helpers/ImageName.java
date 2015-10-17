package news.esports.com.esports.Helpers;

/**
 * Created by Renso on 9/12/2015.
 */
public class ImageName implements Extensions{

    private static final String tag = "ImageName";

    public static String getImageName(String desc){
        String imageName = DEFAULT;
        if(desc.toLowerCase().contains(JPG)){
            imageName = desc.substring(desc.indexOf("src=") + 5, desc.indexOf(JPG)+4);
            //Log.i(tag, "" + imageName);
        }else if (desc.toLowerCase().contains(PNG)){
            imageName = desc.substring(desc.indexOf("src=") + 5, desc.indexOf(PNG)+4);
            //Log.i(tag, ""+imageName);
        }
        return imageName;
    }
}
