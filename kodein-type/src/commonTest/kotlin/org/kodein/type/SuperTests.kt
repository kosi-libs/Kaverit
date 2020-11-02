package org.kodein.type

import kotlin.test.Test
import kotlin.test.assertEquals


class SuperTests {

    interface I
    interface GI<T>
    class C
    class GC<T>

    @Test fun test00_noAnyInSuper() {
        assertEquals(emptyList(), erased<C>().getSuper())
        assertEquals(emptyList(), erased<I>().getSuper())
        assertEquals(emptyList(), generic<GC<Int>>().getSuper())
        assertEquals(emptyList(), generic<GI<Int>>().getSuper())
        assertEquals(emptyList(), erasedComp(GC::class, erased(Int::class)).getSuper())
        assertEquals(emptyList(), erasedComp(GI::class, erased(Int::class)).getSuper())
    }

}
