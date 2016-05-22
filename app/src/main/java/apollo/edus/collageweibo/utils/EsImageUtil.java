package apollo.edus.collageweibo.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class EsImageUtil {
	private static final String tag = EsImageUtil.class.getName();

	public static Bitmap b64ToBmp(String b64) {
		Bitmap bm = null;
		try {
			if (b64 != null) {
				byte[] data = Base64.decode(b64, Base64.NO_PADDING);
				//Log.e("xh","data:"+data.length);
				if (data != null) {
					bm = BitmapFactory.decodeByteArray(data, 0, data.length);
				}
			}
		} catch (Exception e) {
			//Log.e("xh","b64ToBmp  e.getMessage():"+e.getMessage());
		}
		//Log.e("xh","b64ToBmp:"+bm);
		return bm;
	}

	public static String bmpToB64(Bitmap bm) {
		String str = null;
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		try {
			if (bm != null) {
				bm.compress(CompressFormat.PNG, 0, output);
				str = Base64.encodeToString(output.toByteArray(), Base64.NO_PADDING);
			}
		} catch (Exception e) {
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
				}
			}
		}
		return str;
	}
}
