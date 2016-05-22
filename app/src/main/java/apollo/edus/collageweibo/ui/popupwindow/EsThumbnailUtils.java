package apollo.edus.collageweibo.ui.popupwindow;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Environment;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import apollo.edus.collageweibo.biz.global.EsGlobal;

/**
 * Created by Panda on 2016/5/22.
 */
public class EsThumbnailUtils {

    private static final int MODE_CROP = 0;
    private static final int MODE_FIT = 1;
    /**
     * @param filePath native file path
     * @param requestWidth if <= 0,use MAX_EDGE*desity
     * @param requestHeight if <= 0,use MAX_EDGE*desity
     * @param scaleUp if the bitmap is small than the requested size should we scale the size up
     * @return
     */
    public static Bitmap createScaleImageThumbnail(String filePath, int requestWidth, int requestHeight, boolean scaleUp){
        int size = Math.max(requestWidth, requestHeight);
        if(size <= 0){
            size = (int) (EsGlobal.getGlobalContext().getResources().getDisplayMetrics().density*60);
        }
        int width = requestWidth, height = requestHeight;
        if (width <= 0 || height <= 0) {
            width = size;
            height = size;
        }
        Bitmap bitmap = null;
        FileInputStream fis = null;
        try {
            String state = Environment.getExternalStorageState();
            if (!(Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY
                    .equals(state))) {
                return null;
            }
            fis = new FileInputStream(filePath);
            FileDescriptor fd = fis.getFD();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 1;
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFileDescriptor(fd, null, options);
            if (options.mCancel || options.outWidth == -1
                    || options.outHeight == -1) {
                return null;
            }
            options.inSampleSize = calculateInSampleSize(options, width, height);
            options.inJustDecodeBounds = false;
            options.inDither = false;
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            bitmap = BitmapFactory.decodeFileDescriptor(fd, null, options);
//            bitmap = ThumbnailUtils.extractThumbnail(bitmap, requestWidth, requestHeight,
//                    ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
            if (requestWidth > 0 && requestHeight > 0) {
//                int bwidth = bitmap.getWidth();
//                int bheight = bitmap.getHeight();
//                float scale = bwidth / (float)bheight;
//                float scaleR = requestWidth / (float)requestHeight;
//                if (bwidth < requestWidth || bheight < requestHeight) {
//                    if (scale > scaleR) {
//                        requestHeight = bheight;
//                        requestWidth = (int)(requestHeight * scaleR);
//                    } else {
//                        requestWidth = bwidth;
//                        requestHeight = (int)(requestWidth / scaleR);
//                    }
//                }

//				Bitmap bitmap2 = Bitmap.createScaledBitmap(bitmap, requestWidth, requestHeight, true);
                Bitmap bitmap2 = createScaledBitmap(bitmap, requestWidth, requestHeight, MODE_CROP, scaleUp);
                if (bitmap != bitmap2) {
                    bitmap.recycle();
                }
                return bitmap2;
            }
            return bitmap;

        } catch (FileNotFoundException e) {

        } catch (Exception e) {
//          DmLog.e(TAG, "", e);
        } catch (OutOfMemoryError error) {
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
            }
        }
        return null;

    }

    public static Bitmap createScaledBitmap(Bitmap unscaledBitmap, int dstWidth, int dstHeight, int op, boolean scaleUp) {
        Rect srcRect = calculateSrcRect(unscaledBitmap.getWidth(), unscaledBitmap.getHeight(), dstWidth, dstHeight, op);
        Rect dstRect = calculateDstRect(unscaledBitmap.getWidth(), unscaledBitmap.getHeight(), dstWidth, dstHeight, op, scaleUp);
        Bitmap scaledBitmap = Bitmap.createBitmap(dstRect.width(), dstRect.height(), Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(scaledBitmap);
        canvas.drawBitmap(unscaledBitmap, srcRect, dstRect, new Paint(Paint.FILTER_BITMAP_FLAG));
        return scaledBitmap;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                          int reqWidth, int reqHeight) {
        // BEGIN_INCLUDE (calculate_sample_size)
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and
            // keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    || (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }

            // This offers some additional logic in case the image has a strange
            // aspect ratio. For example, a panorama may have a much larger
            // width than height. In these cases the total pixels might still
            // end up being too large to fit comfortably in memory, so we should
            // be more aggressive with sample down the image (=larger
            // inSampleSize).

//            long totalPixels = width * height / inSampleSize;
//
//            // Anything more than 2x the requested pixels we'll sample down
//            // further
//            final long totalReqPixelsCap = reqWidth * reqHeight * 2;
//
//            while (totalPixels > totalReqPixelsCap) {
//                inSampleSize *= 2;
//                totalPixels /= 2;
//            }
        }
        return inSampleSize;
        // END_INCLUDE (calculate_sample_size)
    }

    public static Rect calculateSrcRect(int srcWidth, int srcHeight, int dstWidth, int dstHeight, int op) {
        if (op == MODE_CROP) {
            final float srcAspect = (float)srcWidth / (float)srcHeight;
            final float dstAspect = (float)dstWidth / (float)dstHeight;
            if (srcAspect > dstAspect) {
                final int srcRectWidth = (int)(srcHeight * dstAspect);
                final int srcRectLeft = (srcWidth - srcRectWidth) / 2;
                return new Rect(srcRectLeft, 0, srcRectLeft + srcRectWidth, srcHeight);
            } else {
                final int srcRectHeight = (int)(srcWidth / dstAspect);
                final int scrRectTop = (int)(srcHeight - srcRectHeight) / 2;
                return new Rect(0, scrRectTop, srcWidth, scrRectTop + srcRectHeight);
            }
        } else {
            return new Rect(0, 0, srcWidth, srcHeight);
        }
    }

    public static Rect calculateDstRect(int srcWidth, int srcHeight, int dstWidth, int dstHeight, int op, boolean scaleUp) {
        if (op == MODE_FIT) {
            final float srcAspect = (float)srcWidth / (float)srcHeight;
            final float dstAspect = (float)dstWidth / (float)dstHeight;
            if (srcAspect > dstAspect) {
                return new Rect(0, 0, dstWidth, (int)(dstWidth / srcAspect));
            } else {
                return new Rect(0, 0, (int)(dstHeight * srcAspect), dstHeight);
            }
        } else {
            final float srcAspect = (float)srcWidth / (float)srcHeight;
            final float dstAspect = (float)dstWidth / (float)dstHeight;

            if (!scaleUp && (srcWidth < dstWidth || srcHeight < dstHeight)) {
                if (dstAspect > srcAspect) {
                    dstHeight = srcHeight;
                    dstWidth = (int)(dstHeight * dstAspect);
                } else {
                    dstWidth = srcWidth;
                    dstHeight = (int)(dstWidth / dstAspect);
                }
            }
            return new Rect(0, 0, dstWidth, dstHeight);
        }
    }
}
