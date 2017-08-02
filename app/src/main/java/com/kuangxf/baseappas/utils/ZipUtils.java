package com.kuangxf.baseappas.utils;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by kuangxf on 2017/8/2.
 */

public class ZipUtils {

    /**
     * 高度压缩，请保持文件名为原文件名保留的后缀+“.gz”
     * @param srcFile
     * @param desFile
     */
    public static void gZip(File srcFile, File desFile) {
        GZIPOutputStream zos = null;
        FileInputStream fis = null;
        try {
            //创建压缩输出流,将目标文件传入
            zos = new GZIPOutputStream(new FileOutputStream(desFile));
            //创建文件输入流,将源文件传入
            fis = new FileInputStream(srcFile);
            byte[] buffer= new byte[1024];
            int len= -1;
            //利用IO流写入写出的形式将源文件写入到目标文件中进行压缩
            while ((len= (fis.read(buffer)))!= -1) {
                zos.write(buffer,0, len);
            }
            zos.flush();
        }catch (Exception e){
            e.printStackTrace();
        }finally{
            if (zos!=null){
                try {
                    zos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fis!=null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void unGZip(File srcFile,File desFile) {
        GZIPInputStream zis= null;
        FileOutputStream fos = null;
        try {
            //创建压缩输入流,传入源文件
            zis = new GZIPInputStream(new FileInputStream(srcFile));
            //创建文件输出流,传入目标文件
            fos = new FileOutputStream(desFile);
            byte[] buffer= new byte[1024];
            int len= -1;
            //利用IO流写入写出的形式将压缩源文件解压到目标文件中
            while ((len= (zis.read(buffer)))!= -1) {
                fos.write(buffer,0, len);
            }
            fos.flush();
        }catch (Exception e){
            e.printStackTrace();
        }finally{
            if (zis!=null){
                try {
                    zis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos!=null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void zip(String src,String dest) throws IOException {
        //定义压缩输出流
        ZipOutputStream out = null;
        try {
            //传入源文件
            File outFile= new File(dest);
            File fileOrDirectory= new File(src);
            //传入压缩输出流
            out = new ZipOutputStream(new FileOutputStream(outFile));
            //判断是否是一个文件或目录
            //如果是文件则压缩
            if (fileOrDirectory.isFile()){
                zipFileOrDirectory(out,fileOrDirectory, "");
            } else {
                //否则列出目录中的所有文件递归进行压缩
                File[] entries = fileOrDirectory.listFiles();
                for (int i= 0; i < entries.length;i++) {
                    zipFileOrDirectory(out,entries[i],"");
                }
            }
            out.flush();
        }catch(IOException ex) {
            ex.printStackTrace();
        }finally{
            if (out!= null){
                try {
                    out.close();
                }catch(IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private static void zipFileOrDirectory(ZipOutputStream out, File fileOrDirectory, String curPath)throws IOException {
        FileInputStream in = null;
        try {
            //判断目录是否为null
            if (!fileOrDirectory.isDirectory()){
                byte[] buffer= new byte[4096];
                int bytes_read;
                in= new FileInputStream(fileOrDirectory);
                //归档压缩目录
                ZipEntry entry = new ZipEntry(curPath + fileOrDirectory.getName());
                //将压缩目录写到输出流中
                out.putNextEntry(entry);
                while ((bytes_read= in.read(buffer))!= -1) {
                    out.write(buffer,0, bytes_read);
                }
                out.closeEntry();
            } else {
                //列出目录中的所有文件
                File[] entries = fileOrDirectory.listFiles();
                for (int i= 0; i < entries.length;i++) {
                    //递归压缩
                    zipFileOrDirectory(out,entries[i],curPath + fileOrDirectory.getName()+ "/");
                }
            }
        }catch(IOException ex) {
            ex.printStackTrace();
        }finally{
            if (in!= null){
                try {
                    in.close();
                }catch(IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
