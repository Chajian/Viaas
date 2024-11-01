package com.viaas.docker.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * File文件工具类
 * @author Yanglin
 * @Date 2023/11/08
 */
public class FileUtils {

    public static File bytesToFile(byte[] bytes,String path){
        File file = new File(path);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return file;
    }

    public static byte[] fileToBytes(File file) throws IOException {
        if (file.isFile()) {
            FileInputStream fileInputStream = new FileInputStream(file);
            return fileInputStream.readAllBytes();
        }
        return null;
    }

}
