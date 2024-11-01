package com.viaas.docker.service;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * 文件存储服务
 * @author Yanglin
 */
public interface FileService {
    boolean saveFile(byte[] context,String fileName,String path) throws FileNotFoundException;

    boolean saveFile(InputStream context, String fileName, String path);

    boolean createFolder(String path,String folderName);
}
