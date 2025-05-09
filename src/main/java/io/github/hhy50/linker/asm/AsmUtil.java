package io.github.hhy50.linker.asm;

import io.github.hhy50.linker.util.ClassUtil;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

/**
 * The type Asm util.
 */
public class AsmUtil {

    /**
     * To type desc string.
     *
     * @param className the class name
     * @return the string
     */
    public static String toTypeDesc(String className) {
        return "L"+ ClassUtil.className2path(className)+";";
    }

    /**
     * Areturn.
     *
     * @param mv    the mv
     * @param rType the r type
     */
    public static void areturn(MethodVisitor mv, Type rType) {
        if (rType.getSort() == Type.VOID) {
            mv.visitInsn(Opcodes.RETURN);
        } else {
            mv.visitInsn(rType.getOpcode(Opcodes.IRETURN));
        }
    }

    /**
     * Adapt ldc class type.
     *
     * @param visitor the visitor
     * @param type    the type
     */
    public static void adaptLdcClassType(MethodVisitor visitor, Type type) {
        if (type.getSort() <= Type.DOUBLE) {
            visitor.visitFieldInsn(Opcodes.GETSTATIC, getPrimitiveClass(type.getClassName()), "TYPE", "Ljava/lang/Class;");
        } else {
            visitor.visitLdcInsn(type);
        }
    }

    private static String getPrimitiveClass(String className) {
        switch (className) {
            case "void":
                return "java/lang/Void";
            case "boolean":
                return "java/lang/Boolean";
            case "char":
                return "java/lang/Character";
            case "byte":
                return "java/lang/Byte";
            case "short":
                return "java/lang/Short";
            case "int":
                return "java/lang/Integer";
            case "float":
                return "java/lang/Float";
            case "long":
                return "java/lang/Long";
            case "double":
                return "java/lang/Double";
            default:
                return "";
        }
    }

    /**
     * Add args desc type.
     *
     * @param methodType the method type
     * @param newArg     the new arg
     * @param header     the header
     * @return the type
     */
    public static Type addArgsDesc(Type methodType, Type newArg, boolean header) {
        String delimiter = header ? "\\(" : "\\)";
        String[] split = methodType.getDescriptor().split(delimiter);
        split[0] += newArg.getDescriptor();
        return Type.getMethodType(header ? ("("+split[0]+split[1]) : (split[0]+")"+split[1]));
    }

    /**
     * Areturn null.
     *
     * @param mv    the mv
     * @param rType the r type
     */
    public static void areturnNull(MethodVisitor mv, Type rType) {
        mv.visitInsn(Opcodes.ACONST_NULL);
        if (rType.getSort() == Type.VOID) {
            mv.visitInsn(Opcodes.RETURN);
        } else {
            mv.visitInsn(rType.getOpcode(Opcodes.IRETURN));
        }
    }

    /**
     * Calculate lvb offset int.
     *
     * @param isStatic      the is static
     * @param argumentTypes the argument types
     * @return the int
     */
    public static int calculateLvbOffset(boolean isStatic, Type[] argumentTypes) {
        int lvbLen = isStatic ? 0 : 1; // this
        for (Type argumentType : argumentTypes) {
            int setup = argumentType.getSort() == Type.DOUBLE || argumentType.getSort() == Type.LONG ? 2 : 1;
            lvbLen += setup;
        }
        return lvbLen;
    }

    /**
     * Throw no such method.
     *
     * @param write      the write
     * @param methodName the method name
     */
    public static void throwNoSuchMethod(MethodVisitor write, String methodName) {
        write.visitTypeInsn(Opcodes.NEW, "java/lang/NoSuchMethodError");
        write.visitInsn(Opcodes.DUP);
        write.visitLdcInsn(methodName);
        write.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/NoSuchMethodError", "<init>", "(Ljava/lang/String;)V", false);
        write.visitInsn(Opcodes.ATHROW);
    }

    /**
     * Is primitive type boolean.
     *
     * @param type the type
     * @return the boolean
     */
    public static boolean isPrimitiveType(Type type) {
        return type.getSort() > Type.VOID && type.getSort() <= Type.DOUBLE;
    }

    /**
     * Is object type boolean.
     *
     * @param type the type
     * @return the boolean
     */
    public static boolean isObjectType(Type type) {
        return !isPrimitiveType(type);
    }

    /**
     * Is wrap type boolean.
     *
     * @param type the type
     * @return the boolean
     */
    public static boolean isWrapType(Type type) {
        // type 是否是包装类型
        String className = type.getClassName();
        return className.equals("java.lang.Boolean") || className.equals("java.lang.Character") || className.equals("java.lang.Byte")
                || className.equals("java.lang.Short") || className.equals("java.lang.Integer") || className.equals("java.lang.Float")
                || className.equals("java.lang.Long") || className.equals("java.lang.Double");
    }

    /**
     * Gets primitive type.
     *
     * @param type the type
     * @return the primitive type
     */
    public static Type getPrimitiveType(Type type) {
        // 获取对应类型的基本数据类型
        if (type.getClassName().equals("java.lang.Boolean")) {
            return Type.BOOLEAN_TYPE;
        }
        if (type.getClassName().equals("java.lang.Character")) {
            return Type.CHAR_TYPE;
        }
        if (type.getClassName().equals("java.lang.Byte")) {
            return Type.BYTE_TYPE;
        }
        if (type.getClassName().equals("java.lang.Short")) {
            return Type.SHORT_TYPE;
        }
        if (type.getClassName().equals("java.lang.Integer")) {
            return Type.INT_TYPE;
        }
        if (type.getClassName().equals("java.lang.Float")) {
            return Type.FLOAT_TYPE;
        }
        if (type.getClassName().equals("java.lang.Long")) {
            return Type.LONG_TYPE;
        }
        if (type.getClassName().equals("java.lang.Double")) {
            return Type.DOUBLE_TYPE;
        }
        return null;
    }

    /**
     * Gets type.
     *
     * @param clazz the clazz
     * @return the type
     */
    public static Type getType(String clazz) {
        String prefix = "";
        while (clazz.endsWith("[]")) {
            clazz = clazz.substring(0, clazz.length()-2);
            prefix += "[";
        }
        Type primitiveType = getPrimitiveType(clazz);
        if (primitiveType != null) {
            return Type.getType(prefix+primitiveType.getDescriptor());
        }
        return Type.getType(prefix+toTypeDesc(clazz));
    }

    /**
     * Gets primitive type.
     *
     * @param clazz the clazz
     * @return the primitive type
     */
    public static Type getPrimitiveType(String clazz) {
        // 判断是否是基本数据类
        if (clazz.equals("byte")) {
            return Type.BYTE_TYPE;
        }
        if (clazz.equals("short")) {
            return Type.SHORT_TYPE;
        }
        if (clazz.equals("int")) {
            return Type.INT_TYPE;
        }
        if (clazz.equals("long")) {
            return Type.LONG_TYPE;
        }
        if (clazz.equals("float")) {
            return Type.FLOAT_TYPE;
        }
        if (clazz.equals("double")) {
            return Type.DOUBLE_TYPE;
        }
        if (clazz.equals("boolean")) {
            return Type.BOOLEAN_TYPE;
        }
        if (clazz.equals("char")) {
            return Type.CHAR_TYPE;
        }
        return null;
    }

    /**
     * To owner string.
     *
     * @param clazzName the clazz name
     * @return the string
     */
    public static String toOwner(String clazzName) {
        if (clazzName == null) return null;
        return clazzName.replace('.', '/');
    }
}
