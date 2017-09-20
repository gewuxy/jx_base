### 在总环境的 build.gradle 写法
    buildscript {
        repositories {
            jcenter()
        }
    
        dependencies {
            classpath 'com.android.tools.build:gradle:2.3.3'
    
            /**
             * 查看方法数
             * github: https://github.com/KeepSafe/dexcount-gradle-plugin
             * 输出位置: {buildDir}/outputs/dexcount/${variant}* 可配置项请查阅
             */
    //        classpath 'com.getkeepsafe.dexcount:dexcount-gradle-plugin:0.7.3'
    
            /**
             * dex分包工具
             * github: https://github.com/ceabie/DexKnifePlugin
             * 版本: 1.6.1: 兼容 Android gradle plugin 2.3.0，在 ART-Runtime编译模式下自动禁用，增加相关提示
             */
    //        classpath 'com.ceabie.dextools:gradle-dexknife-plugin:1.6.1'
    
            /**
             * 能在lib里面使用butterknife
             * 暂时没有使用, 原因是要在使用的每个工程里面声明 annotationProcessor 'com.jakewharton:butterknife-compiler:8.8.1'
             * 不能像自己写的processor一样统一添加
             */
    //        classpath 'com.jakewharton:butterknife-gradle-plugin:8.8.1'
    
            /**
             * 使用lambda表达式
             * github: https://github.com/evant/gradle-retrolambda
             */
            classpath 'me.tatarka:gradle-retrolambda:3.7.0'
        }
    }
    
    allprojects { project ->
        /**
         * 找不到方法判断是否为
         * apply plugin: 'com.android.library'
         * 暂时使用工程名开头为判断
         */
    //    if (project.name.toLowerCase().startsWith('lib')) {
    //        apply plugin: 'com.jakewharton.butterknife'
    //    }
    
        repositories {
            jcenter()
        }
    
        // 检查过时的一些方法, 有时间的话可以打开来一个个修改
    //    gradle.projectsEvaluated {
    //        tasks.withType(JavaCompile) {
    //            options.compilerArgs << "-Xlint:unchecked" << "-Xlint:deprecation"
    //        }
    //    }
    
        // lambda表达式
        apply plugin: 'me.tatarka.retrolambda'
    
        // dex分包工具
    //    apply plugin: 'com.ceabie.dexnkife'
    
        // 查看方法数
    //    apply plugin: 'com.getkeepsafe.dexcount'
    }
    
    task clean(type: Delete) {
        delete rootProject.buildDir
    }
    
    ext.versions = [
            minSdk        : 15,
            compileSdk    : 24,
            buildTools    : '26.0.0',
    
            versionCode   : 100,
            versionName   : '1.0',
    
            java          : JavaVersion.VERSION_1_8,
            supportLibrary: '24+',
    ]
    
    ext.configs = [
            dimension   : 'default',
            abortOnError: false,
    ]
    
    ext.plugs = [
            butterknife: 'com.jakewharton.butterknife',
    ]
    
    ext.deps = [
            support    : [
                    compat      : "com.android.support:support-compat:${versions.supportLibrary}",
                    annotations : "com.android.support:support-annotations:${versions.supportLibrary}",
                    v4          : "com.android.support:support-v4:${versions.supportLibrary}",
                    recyclerview: "com.android.support:recyclerview-v7:${versions.supportLibrary}",
                    gridlayout  : "com.android.support:gridlayout-v7:${versions.supportLibrary}",
                    test        : [
                            runner: 'com.android.support.test:runner:1.0.0',
                    ],
                    dex         : 'com.android.support:multidex:1.0.1',
            ],
            rx         : [
                    java   : 'io.reactivex.rxjava2:rxjava:2.1+',
                    android: 'io.reactivex.rxjava2:rxandroid:2+',
                    binding: 'com.jakewharton.rxbinding2:rxbinding:2.0.0',
            ],
            lottie     : 'com.airbnb.android:lottie:2.2.1', // 加载AE制作出来的json直接展示效果的库
            fresco     : 'com.facebook.fresco:fresco:1.5.0',
            butterknife: [
                    core    : 'com.jakewharton:butterknife:8.8.1',
                    compiler: 'com.jakewharton:butterknife-compiler:8.8.1'
            ],
            square     : [
                    /**
                     * android
                     */
                    ok        : 'com.squareup.okhttp3:okhttp:3.8.0',
                    retrofit  : 'com.squareup.retrofit2:retrofit:2.3.0',
                    leakcanary: [
                            // 内存泄漏检查工具
                            debug  : 'com.squareup.leakcanary:leakcanary-android:1.5.1',
                            release: 'com.squareup.leakcanary:leakcanary-android-no-op:1.5.1'
                    ],
                    /**
                     * java
                     */
                    poet      : 'com.squareup:javapoet:1.9.0', // 以优雅的方式自动生成java代码
            ],
            google     : [
                    /**
                     * android
                     */
                    gson   : 'com.google.code.gson:gson:2.8.1',
                    dagger : [
                            core    : 'com.google.dagger:dagger:2.+',
                            compiler: 'com.google.dagger:dagger-compiler:2.+'
                    ],
                    /**
                     * java
                     */
                    runtime: 'com.google.android:android:4.1.1.4', // 在java项目里调用安卓编译环境
                    auto   : [
                            service: 'com.google.auto.service:auto-service:1.0-rc3', // 自动构建Processor等服务环境
                            common : 'com.google.auto:auto-common:0.8',
                    ],
            ],
            ys         : [
                    core      : ':Android:Libs:LibYS_Rx',
                    network   : ':Android:Libs:LibNetwork_Rx',
                    annotation: ':Android:DI-annotation',
                    compiler  : ':Android:DI-compiler',
                    baidu     : ':Android:Libs:LibBD',
                    um        : ':Android:Libs:LibUM',
            ],
    ]