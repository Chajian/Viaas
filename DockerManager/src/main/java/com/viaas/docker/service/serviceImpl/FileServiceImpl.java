package com.viaas.docker.service.serviceImpl;

import com.viaas.docker.common.Constants;
import com.viaas.docker.execption.CustomException;
import com.viaas.docker.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 文件存储文件实现
 * @Date 2023/01/16
 */
@Slf4j
@Service
public class FileServiceImpl implements FileService {

    @Override
    public boolean saveFile(byte[] context, String fileName, String path) {
        File root = new File(path);
        if(!root.exists()||!root.isDirectory())
            throw new CustomException(Constants.PATH_NOT_EXIST);
        FileOutputStream fileOutputStream = null;
        try {
            File file = new File(path+File.separator+fileName);
            if(file.exists())
                throw new CustomException(Constants.FILE_AREALY_EXIST);
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(context);

        } catch (IOException e) {
            e.printStackTrace();
            throw new CustomException(Constants.FILE_WRITE_FAIL);
        }
        finally {
            try {
                if(fileOutputStream!=null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                throw new CustomException(Constants.FILE_WRITE_FAIL);
            }
        }

        return true;
    }

    @Override
    public boolean saveFile(InputStream context, String fileName, String path) {
        File root = new File(path);
        if(!root.exists()||!root.isDirectory())
            throw new CustomException(Constants.PATH_NOT_EXIST);

        FileOutputStream fileOutputStream = null;
        try {
            byte[] bytes = context.readAllBytes();
            File file = new File(path+File.separator+fileName);
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(bytes);
        } catch (IOException e) {
            throw new CustomException(Constants.FILE_WRITE_FAIL);
        }
        finally {
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
                throw new CustomException(Constants.FILE_WRITE_FAIL);
            }
        }
        return false;
    }

    @Override
    public boolean createFolder(String path, String folderName) {
        File root = new File(path);
        if(!root.exists()||!root.isDirectory())
            throw new CustomException(Constants.PATH_NOT_EXIST);



        return false;
    }
}
