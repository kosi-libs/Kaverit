package org.kodein.type

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue


class SuperJvmTests {

    interface A
    interface B : A
    interface C: B

    interface IGP<T>
    open class CGP<T> : IGP<T>

    interface IP<U, V : U> : IGP<V>
    open class CP<U, V : U> : CGP<V>(), IP<U, V>

    interface IC<X : A> : IP<A, X>
    open class CC<X : A> : CP<A, X>(), IC<X>

    interface IGC : IC<B>
    open class CGC : CC<B>(), IGC

    interface IParent<T>
    class IChild<K,V> : IParent<Set<Map<K, V>>>

    open class CParent<T> : IParent<T>
    class CChild<K,V> : CParent<Set<Map<K, V>>>()

    @Test fun test00_parentHasGenerics() {
        assertEquals(2, erased<CGC>().getSuper().count())
        assertEquals(generic<CC<B>>(), erased<CGC>().getSuper()[0])
        assertEquals(generic<IGC>(), erased<CGC>().getSuper()[1])
    }

    @Test fun test01_parentHasMyGenerics() {
        assertEquals(2, generic<CC<B>>().getSuper().count())
        assertEquals(generic<CP<A, B>>(), generic<CC<B>>().getSuper()[0])
        assertEquals(generic<IC<B>>(), generic<CC<B>>().getSuper()[1])
        assertEquals(generic<CP<A, A>>(), erased<CC<*>>().getSuper()[0])
    }

    @Test fun test02_parentHasMyComposites() {
        assertEquals(2, erasedComp(CC::class, erased(B::class)).getSuper().count())
        assertEquals(generic<CP<A, B>>(), erasedComp(CC::class, erased(B::class)).getSuper()[0])
        assertEquals(generic<IC<B>>(), erasedComp(CC::class, erased(B::class)).getSuper()[1])
    }

    @Test fun test03_parentHasSomeOfMyGenerics() {
        assertEquals(2, generic<CP<A, B>>().getSuper().count())
        assertEquals(generic<CGP<B>>(), generic<CP<A, B>>().getSuper()[0])
        assertEquals(generic<IP<A, B>>(), generic<CP<A, B>>().getSuper()[1])
    }

    @Test fun test04_simpleAssign() {
        assertTrue(generic<CC<B>>().isAssignableFrom(erased<CGC>()))
        assertTrue(erasedComp(CC::class, erased<B>()).isAssignableFrom(erased<CGC>()))
        assertTrue(generic<CC<A>>().isAssignableFrom(erased<CGC>()))
        assertTrue(erasedComp(CC::class, erased<A>()).isAssignableFrom(erased<CGC>()))
        assertFalse(generic<CC<C>>().isAssignableFrom(erased<CGC>()))
        assertFalse(erasedComp(CC::class, erased<C>()).isAssignableFrom(erased<CGC>()))
    }

    @Test fun test04_complexAssign() {
        assertTrue(generic<CP<A, B>>().isAssignableFrom(generic<CC<B>>()))
        assertTrue(generic<CP<A, B>>().isAssignableFrom(erasedComp(CC::class, erased<B>())))
        assertTrue(erasedComp(CP::class, erased<A>(), erased<B>()).isAssignableFrom(generic<CC<B>>()))
        assertTrue(erasedComp(CP::class, erased<A>(), erased<B>()).isAssignableFrom(erasedComp(CC::class, erased<B>())))

        assertTrue(generic<CP<A, B>>().isAssignableFrom(erased<CGC>()))
        assertTrue(erasedComp(CP::class, erased<A>(), erased<B>()).isAssignableFrom(erased<CGC>()))

        assertTrue(generic<CP<A, A>>().isAssignableFrom(generic<CC<B>>()))
        assertTrue(generic<CP<A, A>>().isAssignableFrom(erasedComp(CC::class, erased<B>())))
        assertTrue(erasedComp(CP::class, erased<A>(), erased<A>()).isAssignableFrom(generic<CC<B>>()))
        assertTrue(erasedComp(CP::class, erased<A>(), erased<A>()).isAssignableFrom(erasedComp(CC::class, erased<B>())))

        assertTrue(generic<CP<A, A>>().isAssignableFrom(erased<CGC>()))
        assertTrue(erasedComp(CP::class, erased<A>(), erased<A>()).isAssignableFrom(erased<CGC>()))

        assertFalse(generic<CP<B, B>>().isAssignableFrom(generic<CC<B>>()))
        assertFalse(generic<CP<B, B>>().isAssignableFrom(erasedComp(CC::class, erased<B>())))
        assertFalse(erasedComp(CP::class, erased<B>(), erased<B>()).isAssignableFrom(generic<CC<B>>()))
        assertFalse(erasedComp(CP::class, erased<B>(), erased<B>()).isAssignableFrom(erasedComp(CC::class, erased<B>())))

        assertFalse(generic<CP<B, B>>().isAssignableFrom(erased<CGC>()))
        assertFalse(erasedComp(CP::class, erased<B>(), erased<B>()).isAssignableFrom(erased<CGC>()))
    }

    @Test fun test05_childSuperIsRecursivelyReified() {
        assertEquals(erasedComp(IParent::class, erasedComp(Set::class, erasedComp(Map::class, generic<Int>(), generic<String>()))), generic<IChild<Int, String>>().getSuper()[0])
        assertEquals(erasedComp(CParent::class, erasedComp(Set::class, erasedComp(Map::class, generic<Int>(), generic<String>()))), generic<CChild<Int, String>>().getSuper()[0])
    }
}
