package com.zq.tiny


import org.gradle.api.Plugin
import org.gradle.api.Project

class TinyPngPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
//        AppExtension appExtension = project.getExtensions().getByType(AppExtension)
//        appExtension.registerTransform(new TinyPngTransform(project), Collections.EMPTY_LIST)
    }
}