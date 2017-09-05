### 在总环境的 build.gradle 里添加
    ext.versions = [
            'minSdk'        : 15,
            'compileSdk'    : 24,
            'buildTools'    : '26.0.0',
    
            'versionCode'   : 100,
            'versionName'   : '1.0',
    
            'java'          : JavaVersion.VERSION_1_8,
            'supportLibrary': '24+',
    ]
    
    ext.configs = [
            'dimension'   : 'default',
            'abortOnError': false,
    ]
    
    ext.deps = [
            android  : [
                    'runtime': 'com.google.android:android:4.1.1.4', // 在java项目里调用安卓编译环境
            ],
            'support': [
                    'compat'      : "com.android.support:support-compat:${versions.supportLibrary}",
                    'annotations' : "com.android.support:support-annotations:${versions.supportLibrary}",
                    'v4'          : "com.android.support:support-v4:${versions.supportLibrary}",
                    'recyclerview': "com.android.support:recyclerview-v7:${versions.supportLibrary}",
                    'gridlayout'  : "com.android.support:gridlayout-v7:${versions.supportLibrary}",
                    'test'        : [
                            'runner': 'com.android.support.test:runner:1.0.0',
                    ],
                    'dex'         : 'com.android.support:multidex:1.0.1',
            ],
            'auto'   : [
                    'service': 'com.google.auto.service:auto-service:1.0-rc3', // 自动构建Processor等服务环境
                    'common' : 'com.google.auto:auto-common:0.8',
            ],
            'java'   : [
                    'poet': 'com.squareup:javapoet:1.9.0', // 以优雅的方式自动生成java代码
            ],
            'rx'     : [
                    'java'   : 'io.reactivex.rxjava2:rxjava:2.1+',
                    'android': 'io.reactivex.rxjava2:rxandroid:2+'
            ],
            'network': [
                    'ok': 'com.squareup.okhttp3:okhttp:3.8.0',
            ],
            'debug'  : [
                    'leakcanary': 'com.squareup.leakcanary:leakcanary-android:1.5.1' // 内存泄漏检查工具
            ],
            'release': [
                    'leakcanary': 'com.squareup.leakcanary:leakcanary-android-no-op:1.5.1' // 内存泄漏检查工具
            ],
            lottie   : 'com.airbnb.android:lottie:2.2.0', // 加载AE制作出来的json直接展示效果的库
            fresco   : 'com.facebook.fresco:fresco:1.5.0',
            'ys'     : [
                    'core'   : ':Android:Libs:LibYS_Rx',
                    'network': ':Android:Libs:LibNetwork_Rx',
                    'di'     : ':Android:DI-compiler',
                    // 以下两个看情况增加
                    'baidu'  : ':Android:Libs:LibBD',
    //                'um'     : ':Android:Libs:LibUM',
            ],
    ]