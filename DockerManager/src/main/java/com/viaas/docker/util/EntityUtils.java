package com.viaas.docker.util;



import cn.hutool.core.util.ObjectUtil;
import com.github.dockerjava.api.model.DockerObject;
import com.github.dockerjava.api.model.SearchItem;
import com.viaas.docker.entity.Image;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Chajian
 * 实体类打印
 */
public class EntityUtils {

    public static void print(Object o){
        if(o == null)
            return;
        Class obejctClass = o.getClass();
        Field[] fields = obejctClass.getDeclaredFields();

        System.out.println(obejctClass.toString()+"{\n");
        for(Field field:fields){
            String typeName = field.getType().getName();
            System.out.println(field.getName()+":"+getValue(o,field.getType(),field.getName()));
        }
    }

    public static String toString(Object o){
        Class obejctClass = o.getClass();
        Field[] fields = obejctClass.getDeclaredFields();
        String out = "";
        out += obejctClass.toString()+"{\n";
        for(Field field:fields){
            if(!field.getName().equals("serialVersionUID"))
                out += field.getName()+":"+getValue(o,field.getType(),field.getName())+",\n";
        }
        out += "}";
        return out;
    }

    /**
     *
     * get方法
     * @param type 返回的类型
     * @param fieldName
     * @return
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static Object getValue(Object c,Class type,String fieldName)  {
        char First = fieldName.charAt(0);
        First -=32;
        fieldName = First+fieldName.substring(1,fieldName.length());
        try {
            return excuteMethod(c,"get" + fieldName, null, null);
//            return type.cast(excuteMethod(c,"get" + fieldName, null, null));
        }
        catch (Exception e){
            throw new RuntimeException();
        }
    }

    /**
     * 执行某函数
     * @param methodName
     * @param parameteType
     * @param invokeParamete
     * @return
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public static Object excuteMethod(Object cMain,String methodName, Class[] parameteType, Object[] invokeParamete) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = cMain.getClass().getDeclaredMethod(methodName,parameteType);
        return method.invoke(cMain,invokeParamete);
    }
    public static Image dockerObjectToImage(DockerObject object){
        if(ObjectUtil.isEmpty(object))
            return null;
        com.viaas.docker.entity.Image image = new com.viaas.docker.entity.Image();
        if(object instanceof SearchItem){
            SearchItem item = (SearchItem) object;
            image.setName(item.getName());

        }
        else if(object instanceof com.github.dockerjava.api.model.Image){
            com.github.dockerjava.api.model.Image dockerImage = (com.github.dockerjava.api.model.Image) object;
            if(dockerImage.getRepoTags().length>0){
                image.setName(dockerImage.getRepoTags()[0]);
                int firstOn = image.getName().lastIndexOf(":");
                image.setTag(firstOn==-1?"":image.getName().substring(firstOn+1));
            }
            if(dockerImage.getSize()>0) {
                image.setSize(dockerImage.getSize());
            }
        }

        return image;
    }

}
