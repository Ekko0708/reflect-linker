package io.github.hhy50.linker.generate.getter;

import io.github.hhy50.linker.define.field.FieldRef;
import io.github.hhy50.linker.generate.AbstractDecorator;
import io.github.hhy50.linker.generate.InvokeClassImplBuilder;
import io.github.hhy50.linker.generate.MethodBody;
import io.github.hhy50.linker.generate.bytecode.LookupMember;
import io.github.hhy50.linker.generate.bytecode.MethodHandleMember;
import io.github.hhy50.linker.generate.bytecode.vars.VarInst;

import java.lang.reflect.Method;

/**
 * <p>GetterDecorator class.</p>
 *
 * @author hanhaiyang
 * @version $Id: $Id
 */
public class GetterDecorator extends AbstractDecorator {

    private Getter<?> getter;
    private final FieldRef fieldRef;
    private final Method methodDefine;

    /**
     * <p>Constructor for GetterDecorator.</p>
     *
     * @param getter a {@link Getter} object.
     * @param fieldRef a {@link FieldRef} object.
     * @param methodDefine a {@link java.lang.reflect.Method} object.
     */
    public GetterDecorator(Getter getter, FieldRef fieldRef, Method methodDefine) {
        this.getter = getter;
        this.fieldRef = fieldRef;
        this.methodDefine = methodDefine;
    }

    /** {@inheritDoc} */
    @Override
    public void define0(InvokeClassImplBuilder classImplBuilder) {
        getter.define(classImplBuilder);
    }

    /** {@inheritDoc} */
    @Override
    public VarInst invoke(MethodBody methodBody) {
        /**
         * get只需要对返回值进行转换就行
         */
        VarInst result = getter.invoke(methodBody);
//        if (AsmUtil.isObjectType(result.getType())) {
//            result.ifNull(methodBody, returnNull());
//        }
        result = typecastResult(methodBody, result, methodDefine.getReturnType());
        result.returnThis(methodBody);
        return null;
    }

    private VarInst wrapLinker(MethodBody methodBody, VarInst result, Class<?> linkerClass) {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public void mhReassign(MethodBody methodBody, LookupMember lookupMember, MethodHandleMember mhMember, VarInst objVar) {
        throw new RuntimeException("Decorator not impl mhReassign() method");
    }
}