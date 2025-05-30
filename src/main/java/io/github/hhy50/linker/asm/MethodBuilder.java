package io.github.hhy50.linker.asm;

import io.github.hhy50.linker.define.MethodDescriptor;
import io.github.hhy50.linker.generate.MethodBody;
import io.github.hhy50.linker.generate.bytecode.action.Action;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.Map;
import java.util.function.Consumer;

/**
 * The type Method builder.
 */
public class MethodBuilder {

    private final AsmClassBuilder classBuilder;

    private final MethodBody methodBody;

    private final MethodDescriptor descriptor;

    private final int access;

    /**
     * Instantiates a new Method builder.
     *
     * @param classBuilder  the class builder
     * @param access        the access
     * @param descriptor    the descriptor
     * @param methodVisitor the method visitor
     */
    public MethodBuilder(AsmClassBuilder classBuilder, int access, MethodDescriptor descriptor, MethodVisitor methodVisitor) {
        this.classBuilder = classBuilder;
        this.access = access;
        this.descriptor = descriptor;
        this.methodBody = new MethodBody(this, methodVisitor);
    }

    /**
     * Accept method builder.
     *
     * @param consumer the consumer
     * @return the method builder
     */
    public MethodBuilder accept(Consumer<MethodBody> consumer) {
        consumer.accept(methodBody);
        return this;
    }

    /**
     * Intercept asm class builder.
     *
     * @param actions the actions
     * @return the asm class builder
     */
    public AsmClassBuilder intercept(Action... actions) {
        if (actions.length == 0) {
            throw new IllegalArgumentException("intercepts is empty");
        }
        for (Action action : actions) {
            action.apply(methodBody);
        }
        return this.end();
    }

    /**
     * End asm class builder.
     *
     * @return the asm class builder
     */
    public AsmClassBuilder end() {
        this.methodBody.end();
        return this.classBuilder;
    }

    /**
     * Add annotation method builder.
     *
     * @param descriptor the descriptor
     * @param props      the props
     * @return the method builder
     */
    public MethodBuilder addAnnotation(String descriptor, Map<String, Object> props) {
        AnnotationVisitor annotationVisitor = this.methodBody.getWriter()
                .visitAnnotation(descriptor, true);
        if (props != null && !props.isEmpty()) {
            for (Map.Entry<String, Object> kv : props.entrySet()) {
                annotationVisitor.visit(kv.getKey(), kv.getValue());
            }
        }
        annotationVisitor.visitEnd();
        return this;
    }

    /**
     * Gets class builder.
     *
     * @return the class builder
     */
    public AsmClassBuilder getClassBuilder() {
        return classBuilder;
    }

    /**
     * Gets method body.
     *
     * @return the method body
     */
    public MethodBody getMethodBody() {
        return methodBody;
    }

    /**
     * Gets descriptor.
     *
     * @return the descriptor
     */
    public MethodDescriptor getDescriptor() {
        return this.descriptor;
    }

    /**
     * Gets access.
     *
     * @return the access
     */
    public int getAccess() {
        return this.access;
    }

    /**
     * Is static boolean.
     *
     * @return the boolean
     */
    public boolean isStatic() {
        return (access & Opcodes.ACC_STATIC) > 0;
    }
}
