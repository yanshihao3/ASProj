package com.zq.asproj;

/**
 * @program: ASProj
 * @description:
 * @author: 闫世豪
 * @create: 2021-10-09 14:10
 **/

import android.content.Context;

import java.io.File;
import java.lang.reflect.Field;

/**
 * 热修复
 */
public class HotFix {

    public static void fix(Context context, File pathDexFile) throws IllegalAccessException {

        ClassLoader classLoader = context.getClassLoader();
        Field pathListFiled = findFile(classLoader, "pathList");
        Object pathList = pathListFiled.get(classLoader);
    }

    private static Field findFile(Object instance, String filedName) {
        for (Class<?> aClass = instance.getClass(); aClass != null; aClass = aClass.getSuperclass()) {
            try {
                Field field = aClass.getDeclaredField(filedName);
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                return field;
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }

        }
        return null;
    }
}
