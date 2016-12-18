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
 * Class containing some static Util Functions
 */
public class Util {
    /**
     * getFileTypeIcon
     * Function returns drawable icon based on the given file type
     * @param mContext
     * @param file
     */
    public static Drawable getFileTypeIcon(Context mContext, File file) {
        if(!file.isFile()) {
            if (!file.canRead()) {
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

    /**
     * copyFileOrDirectory
     * Copied Source from http://stackoverflow.com/questions/9292954/how-to-make-a-copy-of-a-file-in-android
     * Copies given file or directory
     * @param String src
     * String absolute Path of source file
     * @param String dst
     * String absolute Path of destination
     * @throws IOException
     */
    public static void copyFileOrDirectory(String srcDir, String dstDir) {

        try {
            File src = new File(srcDir);
            File dst = new File(dstDir, src.getName());

            if (src.isDirectory()) {
                dst.mkdir();

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

    /**
     * copyFile
     * Copied Source from http://stackoverflow.com/questions/9292954/how-to-make-a-copy-of-a-file-in-android
     * @param sourceFile
     * @param destFile
     * @throws IOException
     */
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

    /**
     * deleteFile
     * Deletes File or File and Subfolder of it
     * @param file
     * File to be deleted
     */
    public static void deleteFile(File file) {
        if (file.isDirectory())
        {
            String[] children = file.list();
            for (int i = 0; i < children.length; i++)
            {
                deleteFile(new File(file, children[i]));
            }
        }
        file.delete();
    }

    /**
     * getPathInfo
     * Adds Current Path to Filename. To get absolute Path
     * @param fileName
     * @return String
     * Current Path + param
     */
    public static String getPathInfo(File currentDir, String fileName) {
        if(fileName == File.separator)
            return fileName;

        return currentDir.getAbsolutePath() + File.separator + fileName;
    }
}
