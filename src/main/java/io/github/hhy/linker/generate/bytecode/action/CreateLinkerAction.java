package io.github.hhy.linker.generate.bytecode.action;

import io.github.hhy.linker.entity.MethodHolder;
import io.github.hhy.linker.generate.MethodBody;
import io.github.hhy.linker.generate.bytecode.vars.VarInst;
import org.objectweb.asm.Type;

public class CreateLinkerAction implements Action {

    private final Type linkerType;
    private final VarInst obj;

    public CreateLinkerAction(Type linkerType, VarInst obj) {
        this.linkerType = linkerType;
        this.obj = obj;
    }


    @Override
    public void apply(MethodBody body) {
        Object a = null;
        obj.ifNull(body, Action.returnNull(), new MethodInvokeAction(MethodHolder.LINKER_FACTORY_CREATE_LINKER)
                .setArgs(LdcLoadAction.of(linkerType), obj));
    }
}