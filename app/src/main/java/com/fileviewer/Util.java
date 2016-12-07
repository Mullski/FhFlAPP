package com.fileviewer;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import com.example.antonimuller.fhflapp.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

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

    public static boolean isReadable(File file) {
        if(file.canRead()) {
            return true;
        }
        return false;
    }

    /**
     * Kopierte Methode von http://stackoverflow.com/questions/9292954/how-to-make-a-copy-of-a-file-in-android
     * @param src
     * @param dst
     * @throws IOException
     */
    public static void copyFileToFile(File src, File dst) throws IOException {
        FileInputStream inStream = new FileInputStream(src);
        FileOutputStream outStream = new FileOutputStream(dst);
        FileChannel inChannel = inStream.getChannel();
        FileChannel outChannel = outStream.getChannel();
        inChannel.transferTo(0, inChannel.size(), outChannel);
        inStream.close();
        outStream.close();
    }

    public static void copyFileOrDirectory(String srcDir, String dstDir) {

        try {
            File src = new File(srcDir);
            File dst = new File(dstDir, src.getName());

            if (src.isDirectory()) {

                String files[] = src.list();
                int filesLength = files.length;
                for (int i = 0; i < filesLength; i++) {
                    String src1 = (new File(src, files[i]).getPath());
                    String dst1 = dst.getPath();
                    copyFileOrDirectory(src1, dst1);

                }
            } else {
                copyFile(src, dst);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.getParentFile().exists())
            destFile.getParentFile().mkdirs();

        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }
}
