package apollo.edus.collageweibo.utils;

/**
 * Created by Panda on 2016/5/22.
 */
public class EsFileUtils {

    public static String getFileExtensionAndDot(String fileName){
        if (fileName == null || fileName.length() == 0) {
            return "";
        }else{
            int dotIndex = fileName.lastIndexOf('.');
            if (dotIndex > 0) {
                return fileName.substring(dotIndex).toLowerCase();
            }else{
                return "";
            }
        }

    }
}
