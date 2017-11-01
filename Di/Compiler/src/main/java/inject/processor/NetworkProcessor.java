package inject.processor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.Processor;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;

import inject.android.MyClassName;
import inject.annotation.network.Api;
import inject.annotation.network.Descriptor;
import inject.annotation.network.Query;
import inject.annotation.network.Retry;
import inject.annotation.network.Url;
import inject.annotation.network.method.Download;
import inject.annotation.network.method.DownloadFile;
import inject.annotation.network.method.Get;
import inject.annotation.network.method.Post;
import inject.annotation.network.method.Upload;

import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.PUBLIC;
import static javax.lang.model.element.Modifier.STATIC;

/**
 * 和LibNetwork结合使用
 * 生成的都是NetworkReq
 *
 * @auther yuansui
 * @since 2017/8/16
 */
@AutoService(Processor.class)
public class NetworkProcessor extends BaseProcessor {

    private static final String KSplit = ", ";

    private interface FieldName {
        String KHost = "KHost";
        String KHostDebuggable = "KHostDebuggable";
        String KBaseHost = "mBaseHost";
        String KBuilder = "builder";
        String KDir = "dir";
        String KFileName = "fileName";
        String KUrl = "url";
    }

    @Override
    protected Class<? extends Annotation> getAnnotationClass() {
        return Descriptor.class;
    }

    @Override
    protected TypeSpec getBuilderSpec(Element annotatedElement) {
        final String name = String.format("%sDescriptor", annotatedElement.getSimpleName());
        TypeSpec.Builder builder = TypeSpec.classBuilder(name)
                .addModifiers(PUBLIC, FINAL);

        /**
         * 构造方法
         */
        MethodSpec.Builder constructor = MethodSpec.constructorBuilder()
                .addModifiers(PRIVATE);
        builder.addMethod(constructor.build());

        /**
         * 寻找{@link Descriptor}
         */
        Descriptor descriptor = annotatedElement.getAnnotation(Descriptor.class);
        if (descriptor != null) {
            // 加入host field
            builder.addField(FieldSpec.builder(String.class, FieldName.KHost, PRIVATE, STATIC, FINAL)
                    .initializer(Format.KString, descriptor.host())
                    .build());

            if (!descriptor.hostDebuggable().isEmpty()) {
                builder.addField(FieldSpec.builder(String.class, FieldName.KHostDebuggable, PRIVATE, STATIC, FINAL)
                        .initializer(Format.KString, descriptor.hostDebuggable())
                        .build());
            }

            builder.addField(FieldSpec.builder(String.class, FieldName.KBaseHost, PRIVATE, STATIC)
                    .initializer(Format.KVal, FieldName.KHostDebuggable)
                    .build());
        }

        /**
         * setDebuggable()
         */
        builder.addMethod(MethodSpec.methodBuilder("setDebuggable")
                .addModifiers(PUBLIC, STATIC)
                .addParameter(TypeName.BOOLEAN, "state")
                .beginControlFlow("if (state)")
                .addStatement("$N = $L", FieldName.KBaseHost, FieldName.KHostDebuggable)
                .nextControlFlow("else")
                .addStatement("$N = $L", FieldName.KBaseHost, FieldName.KHost)
                .endControlFlow()
                .build());

        /**
         * 寻找{@link Api}
         */
        for (Element apiEle : annotatedElement.getEnclosedElements()) {
            Api api = apiEle.getAnnotation(Api.class);
            if (api != null && apiEle.getKind().isInterface()) {
                // 只支持interface
                // 生成对应API的class
                final String apiName = String.format("%sAPI", apiEle.getSimpleName());
                TypeSpec.Builder apiBuilder = TypeSpec.classBuilder(apiName)
                        .addModifiers(PUBLIC, STATIC, FINAL);

                for (Element methodEle : apiEle.getEnclosedElements()) {
                    List<VariableElement> required = new ArrayList<>();
                    List<VariableElement> optional = new ArrayList<>();
                    getAnnotatedFields(methodEle, required, optional);

                    String methodName = methodEle.getSimpleName().toString();

                    String methodClassName = apiName + "_" + methodName;
                    TypeSpec.Builder methodClzBuilder = TypeSpec.classBuilder(methodClassName)
                            .addModifiers(PUBLIC, FINAL, STATIC);

                    StringBuffer paramStatements = getRequiredParamStatement(required);
                    if (isDownloadFileType(methodEle)) {
                        // 下载文件的参数
                        if (paramStatements.length() != 0) {
                            paramStatements.append(KSplit);
                        }
                        paramStatements.append(FieldName.KDir)
                                .append(KSplit)
                                .append(FieldName.KFileName);
                    }

                    // 判断是否声明了Url, 以便多添加一个参数
                    Url url = methodEle.getAnnotation(Url.class);
                    boolean hasUrlParam = url != null && url.assign().isEmpty();
                    if (hasUrlParam) {
                        if (paramStatements.length() != 0) {
                            paramStatements.append(KSplit);
                        }
                        paramStatements.append(FieldName.KUrl);
                    }

                    MethodSpec.Builder methodInstBuilder = MethodSpec.methodBuilder(methodName)
                            .addModifiers(PUBLIC, STATIC, FINAL)
                            .returns(ClassName.bestGuess(methodClassName))
                            .addStatement("$N inst = new $N($L)", methodClassName, methodClassName, paramStatements)
                            .addStatement("return inst");

                    if (isDownloadFileType(methodEle)) {
                        // 下载文件的参数
                        methodInstBuilder.addParameter(ParameterSpec.builder(String.class, FieldName.KDir).build());
                        methodInstBuilder.addParameter(ParameterSpec.builder(String.class, FieldName.KFileName).build());
                    }

                    if (hasUrlParam) {
                        methodInstBuilder.addParameter(ParameterSpec.builder(String.class, FieldName.KUrl).build());
                    }

                    for (Element e : required) {
                        methodInstBuilder.addParameter(getTypeName(e), e.getSimpleName().toString());
                    }

                    writeAPI(methodEle, methodClassName, methodClzBuilder, api);

                    apiBuilder.addMethod(methodInstBuilder.build());
                    apiBuilder.addType(methodClzBuilder.build());
                }

                builder.addType(apiBuilder.build());
            }
        }

        return builder.build();
    }

    private void writeAPI(Element ele, String methodName, TypeSpec.Builder typeBuilder, Api api) {
        List<VariableElement> required = new ArrayList<>();
        List<VariableElement> optional = new ArrayList<>();
        List<VariableElement> all = new ArrayList<>();

        getAnnotatedFields(ele, required, optional);
        all.addAll(required);
        all.addAll(optional);

        // 添加fields
        for (VariableElement e : all) {
            String paramName = e.getSimpleName().toString();
            typeBuilder.addField(getTypeName(e), paramName, PRIVATE);
        }

        MethodSpec.Builder constructorBuilder = MethodSpec.constructorBuilder()
                .addModifiers(PRIVATE);
        if (!required.isEmpty()) {
            for (VariableElement e : required) {
                String paramName = e.getSimpleName().toString();
                constructorBuilder.addParameter(createNonNullParam(e, paramName));
                constructorBuilder.addStatement("this.$N = $N", paramName, paramName);
            }
        }

        String pathVal = null;

        MethodSpec.Builder b = MethodSpec.methodBuilder("build")
                .addModifiers(PUBLIC)
                .returns(MyClassName.KNetworkReq);

        /**
         * 是否有{@link Retry}
         */
        Retry retry = getAnnotation(ele, Retry.class);
        if (retry != null) {
            b.addStatement("$N.retry($L, $L)", FieldName.KBuilder, retry.count(), retry.delay());
        }

        /**
         * 构造函数的参数
         */
        if (isDownloadFileType(ele)) {
            // 下载文件多加入两个默认参数
            typeBuilder.addField(FieldSpec.builder(String.class, FieldName.KDir, PRIVATE).build());
            typeBuilder.addField(FieldSpec.builder(String.class, FieldName.KFileName, PRIVATE).build());

            // 构造函数增加参数
            constructorBuilder.addParameter(createNonNullParam(String.class, FieldName.KDir));
            constructorBuilder.addParameter(createNonNullParam(String.class, FieldName.KFileName));
            constructorBuilder.addStatement("this.$N = $L", FieldName.KDir, FieldName.KDir);
            constructorBuilder.addStatement("this.$N = $L", FieldName.KFileName, FieldName.KFileName);

            pathVal = getAnnotation(ele, DownloadFile.class).value();
            b.addStatement("$N.download($L, $L)", FieldName.KBuilder, FieldName.KDir, FieldName.KFileName);
        } else {
            if (getAnnotation(ele, Get.class) != null) {
                pathVal = getAnnotation(ele, Get.class).value();
                b.addStatement("$N.get()", FieldName.KBuilder);
            } else if (ele.getAnnotation(Post.class) != null) {
                pathVal = getAnnotation(ele, Post.class).value();
                b.addStatement("$N.post()", FieldName.KBuilder);
            } else if (ele.getAnnotation(Upload.class) != null) {
                pathVal = getAnnotation(ele, Upload.class).value();
                b.addStatement("$N.upload()", FieldName.KBuilder);
            } else if (ele.getAnnotation(Download.class) != null) {
                pathVal = getAnnotation(ele, Download.class).value();
                b.addStatement("$N.download()", FieldName.KBuilder);
            }
        }

        String urlName = null;
        Url url = ele.getAnnotation(Url.class);
        if (url != null) {
            if (!url.assign().isEmpty()) {
                // 如果声明了@Url且带有前缀, 直接使用
                urlName = url.assign();
            } else {
                urlName = FieldName.KUrl;
                constructorBuilder.addParameter(createNonNullParam(String.class, FieldName.KUrl));
            }
        }

        /**
         * 构造函数的代码
         */
        if (urlName == null) {
            String apiVal = api.value();
            if (!apiVal.isEmpty()) {
                if (!apiVal.endsWith("/")) {
                    apiVal += "/";
                }
                constructorBuilder.addStatement("this.$N = $T.newBuilder($N + $S + $S)",
                        FieldName.KBuilder,
                        MyClassName.KNetworkReq,
                        FieldName.KBaseHost,
                        apiVal,
                        pathVal);
            } else {
                constructorBuilder.addStatement("this.$N = $T.newBuilder($N + $S)",
                        FieldName.KBuilder,
                        MyClassName.KNetworkReq,
                        FieldName.KBaseHost,
                        pathVal);
            }
        } else if (url != null && !url.assign().isEmpty()) {
            constructorBuilder.addStatement("this.$N = $T.newBuilder($S)", FieldName.KBuilder, MyClassName.KNetworkReq, urlName);
        } else {
            constructorBuilder.addStatement("this.$N = $T.newBuilder($L)", FieldName.KBuilder, MyClassName.KNetworkReq, urlName);
        }

        // 添加builder变量
        typeBuilder.addField(FieldSpec.builder(MyClassName.KNetworkReqBuilder, FieldName.KBuilder, PRIVATE)
                .build());

        for (VariableElement e : optional) {
            String paramName = e.getSimpleName().toString();
            // 生成链式调用方法
            typeBuilder.addMethod(MethodSpec.methodBuilder(paramName)
                    .addModifiers(PUBLIC)
                    .addParameter(getTypeName(e), paramName)
                    .addStatement("this.$N = $L", paramName, paramName)
                    .addStatement("return this")
                    .returns(ClassName.bestGuess(methodName))
                    .build());
        }

        if (!isDownloadFileType(ele)) {
            for (VariableElement e : all) {
                b.addStatement("$N.param($S, $L)", FieldName.KBuilder, getParamName(e), e);
            }
        }

        /**
         * 添加共用参数
         */
        b.addStatement("$N.param($T.getConfig().getCommonParams())", FieldName.KBuilder, MyClassName.KNetwork);
        b.addStatement("$N.header($T.getConfig().getCommonHeaders())", FieldName.KBuilder, MyClassName.KNetwork);

        b.addStatement("return $N.build()", FieldName.KBuilder);

        typeBuilder.addMethod(constructorBuilder.build());

        typeBuilder.addMethod(b.build());
    }

    private void getAnnotatedFields(Element ele, List<VariableElement> required, List<VariableElement> optional) {
        ExecutableElement executableElement = (ExecutableElement) ele;
        for (VariableElement e : executableElement.getParameters()) {
            Query query = e.getAnnotation(Query.class);
            if (query != null) {
                if (query.opt()) {
                    optional.add(e);
                } else {
                    required.add(e);
                }
            } else {
                required.add(e);
            }
        }
    }

    private String getParamName(Element ele) {
        Query query = ele.getAnnotation(Query.class);
        String name = ele.getSimpleName().toString();
        if (query != null) {
            return query.value().isEmpty() ? name : query.value();
        } else {
            return name;
        }
    }

    private StringBuffer getRequiredParamStatement(List<VariableElement> required) {
        StringBuffer statement = new StringBuffer();
        for (int i = 0; i < required.size(); ++i) {
            statement.append(required.get(i).getSimpleName());
            if (i != required.size() - 1) {
                statement.append(KSplit);
            }
        }
        return statement;
    }

    private boolean isDownloadFileType(Element e) {
        return e.getAnnotation(DownloadFile.class) != null;
    }

    private <T extends Annotation> T getAnnotation(Element ele, Class<T> clz) {
        return ele.getAnnotation(clz);
    }
}
