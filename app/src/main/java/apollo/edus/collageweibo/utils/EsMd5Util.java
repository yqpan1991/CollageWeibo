package apollo.edus.collageweibo.utils;

import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EsMd5Util {
	private static char[] hexChar = { '0', '1', '2', '3', '4', '5', '6', '7',
			'8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	public static String toHexString(byte[] b) {
		StringBuilder sBuilder = new StringBuilder(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			sBuilder.append(hexChar[((b[i] & 0xf0) >>> 4)]);
			sBuilder.append(hexChar[(b[i] & 0xF)]);
		}

		return sBuilder.toString();
	}

    /**
     * Get the md5 of the whole file
     * @param fileName
     * @return
     */
    public static String getFileMD5(String fileName, boolean fromCache) {
        File file = new File(fileName);
        long mod = file.lastModified();
        String key = file.getAbsolutePath() + "_" + mod;
        if (Md5Map.md5Map.containsKey(key) && fromCache) {
            return Md5Map.md5Map.get(key);
        }
        String md5 = getFileMD5(fileName, -1);
        if (!TextUtils.isEmpty(md5) && fromCache) {
            Md5Map.md5Map.put(key, md5);
        }
        return md5;
    }

	/**
	 * Get the md5 of the whole file
	 * @param fileName
	 * @return
	 */
	public static String getFileMD5(String fileName) {
	    File file = new File(fileName);
	    long mod = file.lastModified();
	    String key = file.getAbsolutePath() + "_" + mod;
	    if (Md5Map.md5Map.containsKey(key)) {
	        return Md5Map.md5Map.get(key);
	    }
		String md5 = getFileMD5(fileName, -1);
		if (!TextUtils.isEmpty(md5)) {
		    Md5Map.md5Map.put(key, md5);
		}
		return md5;
	}
	
	/**
	 *  caculate the md5 of the 256K content in the header of the file
	 * @param fileName
	 * @return the md5
	 */
	public static String getFileHeaderMD5(String fileName) {
        return getFileMD5(fileName, 256*1024);
    }
	
	/**
	 * Get the md5 of the file
	 * @param fileName
	 * @param length The length of the file want to caculate
	 * , The value should be times of 4096
	 * @return
	 */
	public static String getFileMD5(String fileName, long length) {
        InputStream fisInputStream = null;
        String md5 = null;
        try {
            fisInputStream = new FileInputStream(fileName);
            byte[] buffer = new byte[4096];
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            int numRead = 0;
            int totalRead = 0;
            while ((numRead = fisInputStream.read(buffer)) > 0) {
                totalRead += numRead;
                if ((length < 0 || totalRead <= length)) {
                    messageDigest.update(buffer, 0, numRead);
                } else {
                    break;
                }
            }
            md5 = toHexString(messageDigest.digest());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fisInputStream != null) {
                try {
                    fisInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return md5;
    }
	
	public static String toMd5(String paramString) {
        try {
            MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
            localMessageDigest.reset();
            localMessageDigest.update(paramString.getBytes("utf-8"));
            String str = toHexString(localMessageDigest.digest());
            return str;
        } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
            throw new RuntimeException(localNoSuchAlgorithmException);
        } catch (UnsupportedEncodingException localUnsupportedEncodingException) {
            localUnsupportedEncodingException.printStackTrace();
        }
        return "";
    }

    public static String toMd5(byte[] b) {
        try {
            MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
            localMessageDigest.reset();
            localMessageDigest.update(b);
            String str = toHexString(localMessageDigest.digest());
            return str;
        } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
            return "";
        }
    }

}
