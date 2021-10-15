package com.zq.tiny

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils

class TinyPngTransform extends Transform {

    @Override
    String getName() {
        return "TinyPngTransform"
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    boolean isIncremental() {
        return false
    }

    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        super.transform(transformInvocation)
        //对 inputs ---》 directory ---class 文件进行遍历
        // 2 对 inputs---》jar----》 class 文件进行遍历
        // 3 符合我们项目的包名，并且class文件的路径包含activity.class 文件结尾，还不能是buildconfig。class R.class $.class
        def outputDirectory=transformInvocation.outputProvider
        transformInvocation.inputs.each {
            it.directoryInputs.each {
                println("dirInput abs file path:"+it.file.absolutePath)
                handleDirectory(it.file)
                //把 input---》dir----->dest 目标目录下去
                def dest = outputDirectory.getContentLocation(it.getName(),it.getContentTypes(),it.getScopes(), Format.DIRECTORY)
                FileUtils.copyDirectory(it.file,dest)
            }
            it.jarInputs.each {
                println("JarInput abs file path:"+it.file.absolutePath)
                //对jar 文件 处理完成 返回一个新的jar文件
                def srcFile=handleJar(it.file)
                def jarName=it.name
                def md5= DigestUtils.md5Hex(it.file.absolutePath)
                if (jarName.endsWith(".jar")){
                    jarName=jarName.substring(0,jarName.length()-4)
                }
                def dest = outputDirectory.getContentLocation(md5+jarName,it.getContentTypes(),it.getScopes(), Format.JAR)
                FileUtils.copyFile(srcFile,dest)
            }
        }
    }

    void handleDirectory(File dir){
        if (dir.isDirectory()){
            dir.eachFileRecurse{
                def filePath=it.absolutePath
                println("handleDirectory file path:"+filePath)
            }
        }
    }

    void handleJar(File jarFile){

    }
}