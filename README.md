### 在总环境的 build.gradle 里添加
    ext.deps = [
            android  : [
                    'runtime': 'com.google.android:android:4.1.1.4',
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
                    'service': 'com.google.auto.service:auto-service:1.0-rc3',
                    'common' : 'com.google.auto:auto-common:0.8',
            ],
            'java'   : [
                    'poet': 'com.squareup:javapoet:1.9.0',
            ],
            'rx'     : [
                    'java'   : 'io.reactivex.rxjava2:rxjava:2.1+',
                    'android': 'io.reactivex.rxjava2:rxandroid:2+'
            ],
            'network': [
                    'ok': 'com.squareup.okhttp3:okhttp:3.8.0'
            ],
            'debug'  : [
                    'leakcanary': 'com.squareup.leakcanary:leakcanary-android:1.5.1'
            ],
            'release': [
                    'leakcanary': 'com.squareup.leakcanary:leakcanary-android-no-op:1.5.1'
            ],
            'ys'     : [
                    'core'   : ':Android:Libs:LibYS_Rx',
                    'network': ':Android:Libs:LibNetwork_Rx',
                    'di'     : ':Android:DI-compiler',
                    // 以下两个看情况增加
                    'baidu'  : ':Android:Libs:LibBD',
                    'um'     : ':Android:Libs:LibUM',
            ]
    ]