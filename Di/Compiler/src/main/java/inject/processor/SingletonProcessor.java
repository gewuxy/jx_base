package inject.processor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.TypeSpec;
import com.sun.source.tree.Tree.Kind;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.ListBuffer;

import java.lang.annotation.Annotation;

import javax.annotation.processing.Processor;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;

import inject.annotation.Singleton;

@AutoService(Processor.class)
public class SingletonProcessor extends BaseProcessor {

    @Override
    protected Class<? extends Annotation> getAnnotationClass() {
        return Singleton.class;
    }

    @Override
    protected TypeSpec createTypeSpec(Element annotatedElement) {
        if (annotatedElement.getKind() == ElementKind.CLASS) {
            print(" assertions inlined class " + ((TypeElement) annotatedElement).getQualifiedName().toString());
            JCTree tree = (JCTree) trees().getTree(annotatedElement);
            TreeTranslator visitor = new Inliner();
            tree.accept(visitor);
        }
        return null;
    }

    private class Inliner extends TreeTranslator {

        private JCVariableDecl variableDecl;

        @Override
        public void visitVarDef(JCVariableDecl jcVariableDecl) {
            super.visitVarDef(jcVariableDecl);

            if (variableDecl == null) {
                variableDecl = jcVariableDecl;
                print("4444");
            }

            if (jcVariableDecl.getName().toString().equals("mTimeoutToast")) {
//                JCVariableDecl decl = maker.VarDef(jcVariableDecl.getModifiers(),
//                        names.fromString("mTime"),
//                        jcVariableDecl.vartype,
//                        jcVariableDecl.getInitializer());
//                List<JCTree> trees = List.of(decl, jcVariableDecl);
//                ListBuffer<JCStatement> r = new ListBuffer<>();
//                result = decl;
//                for (JCVariableDecl var : vars) {
//            String typeIdentString = Character.toUpperCase(var.vartype.toString().charAt(0)) +
//                    var.vartype.toString().substring(1);
//            JCExpression expression = ident("out");
//            expression = maker.Select(expression, utils.getName("write" + typeIdentString));
//            expression = maker.Apply(List.<JCExpression>nil(), expression,
//                    List.of((JCExpression) ident(var.name.toString())));
//                    result = result.append(new StatementFactory(make, utils, var).compactWriteStatement());
//                result = decl;
//                FieldInjectionPlan
            }
        }

        @Override
        public void visitClassDef(JCClassDecl jcClassDecl) {
            super.visitClassDef(jcClassDecl);

            if (jcClassDecl.getKind() != Kind.CLASS) {
                return;
            }

//            JCTree.JCMethodDecl methodDecl = make.MethodDef(jcClassDecl.getModifiers(),
//                    names.fromString("testMethod"),
//                    null,
//                    null,
//                    null,
//                    null,
//                    null,
//                    null);

            JCVariableDecl varDecl = maker().VarDef(maker().Modifiers(Flags.PUBLIC),
                    names().fromString("mTest" + jcClassDecl.getSimpleName()),
                    variableDecl.vartype,
                    null);

            ListBuffer<JCTree> varList = new ListBuffer<>();
            ListBuffer<JCTree> otherList = new ListBuffer<>();

            varList.append(varDecl);
            for (JCTree def : jcClassDecl.defs) {
                switch (def.getKind()) {
                    case VARIABLE: {
                        JCVariableDecl d = (JCVariableDecl) def;
                        if (d.getName().equals(varDecl.getName())) {
                            continue;
                        }
                        varList.append(def);
                    }
                    break;
                    default: {
                        otherList.append(def);
                    }
                    break;
                }
            }

            ListBuffer<JCTree> buffer = new ListBuffer<>();
            buffer.appendList(varList);
            buffer.appendList(otherList);
            jcClassDecl.defs = buffer.toList();
            print(jcClassDecl.defs.toString());

            JCClassDecl decl = maker().ClassDef(jcClassDecl.getModifiers(),
                    jcClassDecl.name,
                    jcClassDecl.getTypeParameters(),
                    jcClassDecl.extending,
                    jcClassDecl.implementing,
                    jcClassDecl.defs);
            result = decl;
        }

        @Override
        public void visitMethodDef(JCMethodDecl jcMethodDecl) {
            super.visitMethodDef(jcMethodDecl);

//            print("3333");

//            jcMethodDecl.mods = (JCModifiers) this.translate((JCTree) jcMethodDecl.mods);
//            jcMethodDecl.restype = (JCExpression) this.translate((JCTree) jcMethodDecl.restype);
//            jcMethodDecl.typarams = this.translateTypeParams(jcMethodDecl.typarams);
//            jcMethodDecl.recvparam = (JCVariableDecl) this.translate((JCTree) jcMethodDecl.recvparam);
//            jcMethodDecl.params = this.translateVarDefs(jcMethodDecl.params);
//            jcMethodDecl.thrown = this.translate(jcMethodDecl.thrown);
//            jcMethodDecl.body = (JCBlock) this.translate((JCTree) jcMethodDecl.body);

//            if (jcMethodDecl.getName().toString().equals("getTimeoutToast")) {
//                JCTree.JCMethodDecl methodDecl = make.MethodDef(jcMethodDecl.getModifiers(),
//                        names.fromString("testMethod"),
//                        jcMethodDecl.restype,
//                        jcMethodDecl.getTypeParameters(),
//                        jcMethodDecl.getParameters(),
//                        jcMethodDecl.getThrows(),
//                        jcMethodDecl.getBody(),
//                        jcMethodDecl.defaultValue);
//                result = methodDecl;
//            }
        }
    }
}