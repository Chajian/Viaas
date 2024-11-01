package com.viaas.docker.util;

public class StringUtil {
    public static String execCm(String common,String currentPath){
        char[] chars = common.toCharArray();

        if(chars.length<3||chars[0]!='c'||chars[1]!='d'||chars[2]!=' ') {
            return null;
        }
        return relativePathToAbstractPath(common.substring(3,common.length()),currentPath);
    }

    public static String relativePathToAbstractPath(String path,String currentPath){
        char[] chars = path.toCharArray();
        StringBuilder targetPath = new StringBuilder();
        for(int i = 0 ; i < chars.length;i++){
            if(chars[i]=='/'&&targetPath.length()>0){
                currentPath = currentPath(currentPath,targetPath.toString());
                targetPath.delete(0,targetPath.length());
            }
            if(chars[i]=='/'){
                continue;
            }
            targetPath.append(chars[i]);
        }
        if(targetPath.length()>0){
            currentPath = currentPath(currentPath,targetPath.toString());
        }
        return currentPath;
    }
    public static String currentPath(String current,String targetPath){
        if(targetPath.equals("..")){
            int lastOn = current.lastIndexOf('/');
            if(lastOn==-1||lastOn==0){
                return "/";
            }
            return current.substring(0,lastOn);
        }
        else if(targetPath.equals(".")){
            return current;
        }
        return current.charAt(current.length()-1)=='/'?current+targetPath:current+"/"+targetPath;
    }

    public static String getUpPath(String path){
        int lastOn = path.lastIndexOf('/');
        return path.substring(0,lastOn);
    }
}
