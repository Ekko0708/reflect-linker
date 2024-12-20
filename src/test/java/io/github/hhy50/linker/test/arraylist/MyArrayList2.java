package io.github.hhy50.linker.test.arraylist;

import io.github.hhy50.linker.annotations.Field;
import io.github.hhy50.linker.annotations.Runtime;

/**
 * <p>MyArrayList interface.</p>
 *
 * @author hanhaiyang
 * @version $Id: $Id
 * @since 1.0.0
 */
public interface MyArrayList2 {


    /**
     * <p>getElementData.</p>
     *
     * @return a {@link Object} object.
     */
    @Field.Getter("elementData")
    @Runtime
    Object getElementData();
}
