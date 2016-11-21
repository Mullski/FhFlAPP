package com.fileviewer;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import com.example.antonimuller.fhflapp.R;

import java.io.File;

/**
 * Created by Donny on 21.11.2016.
 */

public class Util {
    public static Drawable getFileTypeIcon(Context mContext, File file) {
        if(!file.isFile()) {
            if (!file.canRead() && !file.canWrite()) {
                return mContext.getResources().getDrawable(R.drawable.folderfilled72);
            }
            else {
                return mContext.getResources().getDrawable(R.drawable.folder72);
            }
        }
        else
        {
            String fileName = file.getName();

            if(Util.isMusic(file))
            {
                return mContext.getResources().getDrawable(R.drawable.audiofile72);
            }
            else if(Util.isVideo(file))
            {
                return mContext.getResources().getDrawable(R.drawable.videofile72);
            }
            else if(Util.isPicture(file))
            {
                return mContext.getResources().getDrawable(R.drawable.imagefile72);
            }
            else
            {
                return mContext.getResources().getDrawable(R.drawable.file72);
            }
        }
    }

    public static boolean isMusic(File file) {

        Uri uri = Uri.fromFile(file);
        String type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(uri.toString()));

        if(type == null)
            return false;
        else
            return (type.toLowerCase().startsWith("audio/"));

    }
    public static boolean isVideo(File file) {

        Uri uri = Uri.fromFile(file);
        String type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(uri.toString()));

        if(type == null)
            return false;
        else
            return (type.toLowerCase().startsWith("video/"));
    }

    public static boolean isPicture(File file) {

        Uri uri = Uri.fromFile(file);
        String type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(uri.toString()));

        if(type == null)
            return false;
        else
            return (type.toLowerCase().startsWith("image/"));
    }
}
