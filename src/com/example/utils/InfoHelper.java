package com.example.utils;

import java.io.File;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

public class InfoHelper extends Activity{
	 public final static String SDCARD_MNT = "/mnt/sdcard";
	 public final static String SDCARD = "/sdcard";
	 
	 	/**
	     * 如果是标准uri，�?过uri获取文件的绝对路�?
	     * @param uri
	     * @return
	     */
		public String getAbsoluteImagePath(Uri uri) {
			String imagePath = "";
	        String [] proj={MediaStore.Images.Media.DATA};
	        Cursor cursor = managedQuery( uri,
	                        proj, 		// Which columns to return
	                        null,       // WHERE clause; which rows to return (all rows)
	                        null,       // WHERE clause selection arguments (none)
	                        null); 		// Order-by clause (ascending by name)
	        
	        if(cursor!=null) {
	        	int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	        	if(cursor.getCount()>0 && cursor.moveToFirst()) {
	            	imagePath = cursor.getString(column_index);
	            }
	        }
	        
	        return imagePath;
	    }
		
	    
	/**
	 * 判断当前Url是否标准的content://样式，如果不是，则返回绝对路�?
	 * @param uri
	 * @return
	 */
	public static String getAbsolutePathFromNoStandardUri(Uri mUri) {	
		String filePath = null;
		
		String mUriString = mUri.toString();
		mUriString = Uri.decode(mUriString);
		
		String pre1 = "file://" + SDCARD + File.separator;
		String pre2 = "file://" + SDCARD_MNT + File.separator;
		
		if(mUriString.startsWith(pre1)) {    
			filePath = Environment.getExternalStorageDirectory().getPath() + File.separator + mUriString.substring(pre1.length());
		}else if(mUriString.startsWith(pre2)) {
			filePath = Environment.getExternalStorageDirectory().getPath() + File.separator + mUriString.substring(pre2.length());
		}
		
		return filePath;
	}
}
