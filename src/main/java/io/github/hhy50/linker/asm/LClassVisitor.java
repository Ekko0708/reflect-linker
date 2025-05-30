package io.github.hhy50.linker.asm;

import io.github.hhy50.linker.annotations.Field;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

import java.util.List;

/**
 * The interface L class visitor.
 */
public interface LClassVisitor {

    /**
     * Gets access.
     *
     * @return the access
     */
    @Field.Getter("access")
    int getAccess();

    /**
     * Gets name.
     *
     * @return the name
     */
    @Field.Getter("name")
    String getName();

    /**
     * Gets super name.
     *
     * @return the super name
     */
    @Field.Getter("superName")
    String getSuperName();

    /**
     * Gets interfaces.
     *
     * @return the interfaces
     */
    @Field.Getter("interfaces")
    List<String> getInterfaces();

    /**
     * Gets signature.
     *
     * @return the signature
     */
    @Field.Getter("signature")
    String getSignature();

    /**
     * Visit field field visitor.
     *
     * @param access     the access
     * @param name       the name
     * @param descriptor the descriptor
     * @param signature  the signature
     * @param value      the value
     * @return the field visitor
     */
    FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value);

    /**
     * Visit method method visitor.
     *
     * @param access     the access
     * @param name       the name
     * @param descriptor the descriptor
     * @param signature  the signature
     * @param exceptions the exceptions
     * @return the method visitor
     */
    MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions);

    /**
     * Visit annotation annotation visitor.
     *
     * @param descriptor the descriptor
     * @param visible    the visible
     * @return the annotation visitor
     */
    AnnotationVisitor visitAnnotation(String descriptor, boolean visible);

    /**
     * Accept.
     *
     * @param classVisitor the class visitor
     */
    void accept(ClassVisitor classVisitor);
}
