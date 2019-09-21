package com.iyuba.core.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;

import com.iyuba.configation.Constant;

import java.io.File;
import java.net.URI;
import java.util.List;

public class SelectPicUtils {

	/***
	 * 选择一张图片 图片类型，这里是image/*，当然也可以设置限制 如：image/jpeg等
	 * 
	 * @param activity
	 *          Activity
	 */
	@SuppressLint("InlinedApi")
	public static void selectPicture(Activity activity, int requestCode) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
			Intent intent = new Intent();
			intent.setType("image/*");
			intent.setAction(Intent.ACTION_GET_CONTENT);
			activity.startActivityForResult(intent, requestCode);
		} else {
			Intent intent = new Intent();
			intent.setType("image/*");
			intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
			activity.startActivityForResult(intent, requestCode);
		}
	}

	/***
	 * 裁剪图片
	 * 
	 * @param activity
	 *          Activity
	 * @param uri
	 *          图片的Uri
	 */
	public static void cropPicture(Activity activity, Uri uri, int requestCode ,String pathString) {
		Intent innerIntent = new Intent("com.android.camera.action.CROP");
		innerIntent.setDataAndType(uri, "image/*");
		innerIntent.putExtra("crop", "true");// 才能出剪辑的小方框，不然没有剪辑功能，只能选取图片
		innerIntent.putExtra("aspectX", 1); // 放大缩小比例的X
		innerIntent.putExtra("aspectY", 1);// 放大缩小比例的X 这里的比例为： 1:1
		innerIntent.putExtra("outputX", 500); // 这个是限制输出图片大小
		innerIntent.putExtra("outputY", 500);
		innerIntent.putExtra("return-data", false);
		innerIntent.putExtra("scale", true);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			//添加这一句表示对目标应用临时授权该Uri所代表的文件
			innerIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
		}


		Uri uritempFile = Uri.parse("file://"  + Environment.getExternalStorageDirectory().getPath() + "/" + pathString);
		innerIntent.putExtra(MediaStore.EXTRA_OUTPUT, uritempFile);
		innerIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

		activity.startActivityForResult(innerIntent, requestCode);
	}

	public static Uri cropPicture(Activity activity, Uri uri, int requestCode , File file) {
		Intent innerIntent = new Intent("com.android.camera.action.CROP");
		innerIntent.setDataAndType(uri, "image/*");
		innerIntent.putExtra("crop", "true");// 才能出剪辑的小方框，不然没有剪辑功能，只能选取图片
		innerIntent.putExtra("aspectX", 1); // 放大缩小比例的X
		innerIntent.putExtra("aspectY", 1);// 放大缩小比例的X 这里的比例为： 1:1
		innerIntent.putExtra("outputX", 500); // 这个是限制输出图片大小
		innerIntent.putExtra("outputY", 500);
		innerIntent.putExtra("return-data", false);
		innerIntent.putExtra("scale", true);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			//添加这一句表示对目标应用临时授权该Uri所代表的文件
			innerIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
		}


		Uri uritempFile ;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			uritempFile = FileProvider.getUriForFile(activity, Constant.PACKAGE_NAME + ".fileprovider",file);
		}else {
			uritempFile = Uri.fromFile(file);
		}
		List<ResolveInfo> resInfoList = activity.getPackageManager().queryIntentActivities(innerIntent, PackageManager.MATCH_DEFAULT_ONLY);
		for (ResolveInfo resolveInfo : resInfoList) {
			String packageName = resolveInfo.activityInfo.packageName;
			activity.grantUriPermission(packageName, uritempFile, Intent.FLAG_GRANT_WRITE_URI_PERMISSION|Intent.FLAG_GRANT_READ_URI_PERMISSION);
		}
		innerIntent.putExtra(MediaStore.EXTRA_OUTPUT, uritempFile);
		innerIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		activity.startActivityForResult(innerIntent, requestCode);
		return uritempFile;
	}


	/**
	 * 
	 * Get a file path from a Uri. This will get the the path for Storage Access
	 * Framework Documents, as well as the _data field for the MediaStore and
	 * other file-based ContentProviders.
	 * 
	 * @param context
	 *          The context.
	 * @param uri
	 *          The Uri to query.
	 * @author paulburke
	 */
	@SuppressLint("NewApi")
	public static String getPath(final Context context, final Uri uri) {

		final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

		// DocumentProvider
		if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
			// ExternalStorageProvider
			if (isExternalStorageDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/" + split[1];
				}

				// TODO handle non-primary volumes
			}
			// DownloadsProvider
			else if (isDownloadsDocument(uri)) {
				final String id = DocumentsContract.getDocumentId(uri);
				final Uri contentUri = ContentUris.withAppendedId(
						Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

				return getDataColumn(context, contentUri, null, null);
			}
			// MediaProvider
			else if (isMediaDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}

				final String selection = "_id=?";
				final String[] selectionArgs = new String[] { split[1] };

				return getDataColumn(context, contentUri, selection, selectionArgs);
			}
		}
		// MediaStore (and general)
		else if ("content".equalsIgnoreCase(uri.getScheme())) {

			// Return the remote address
			if (isGooglePhotosUri(uri))
				return uri.getLastPathSegment();

			return getDataColumn(context, uri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}

		return null;
	}

	/**
	 * Get the value of the data column for this Uri. This is useful for
	 * MediaStore Uris, and other file-based ContentProviders.
	 * 
	 * @param context
	 *          The context.
	 * @param uri
	 *          The Uri to query.
	 * @param selection
	 *          (Optional) Filter used in the query.
	 * @param selectionArgs
	 *          (Optional) Selection arguments used in the query.
	 * @return The value of the _data column, which is typically a file path.
	 */
	public static String getDataColumn(Context context, Uri uri, String selection,
			String[] selectionArgs) {

		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = { column };

		try {
			cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				final int index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

	/**
	 * @param uri
	 *          The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri
	 *          The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri
	 *          The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri
	 *          The Uri to check.
	 * @return Whether the Uri authority is Google Photos.
	 */
	public static boolean isGooglePhotosUri(Uri uri) {
		return "com.google.android.apps.photos.content".equals(uri.getAuthority());
	}
}
