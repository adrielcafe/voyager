package cafe.adriel.voyager.core.stack

import cafe.adriel.voyager.core.utils.Quadruple
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

internal class SnapshotStateStackTest {

    @TestFactory
    fun initSingle(): Sequence<DynamicTest> =
        sequenceOf(
            "item 1" to 0,
            "item 1" to 1
        ).map { (item, minSize) ->
            dynamicTest("when min size is $minSize then should not throw") {
                assertDoesNotThrow {
                    SnapshotStateStack(item, minSize)
                }
            }
        }

    @TestFactory
    fun initList(): Sequence<DynamicTest> =
        sequenceOf(
            Triple(emptyList(), 0, false),
            Triple(emptyList(), 1, true),
            Triple(listOf("item 1"), 0, false),
            Triple(listOf("item 1"), 1, false),
            Triple(listOf("item 1", "item 2"), 0, false),
            Triple(listOf("item 1", "item 2"), 1, false)
        ).map { (items, minSize, throws) ->
            dynamicTest(
                "when items size is ${items.size} and min size is $minSize " +
                    "then should ${if (throws) "" else "not"} throw"
            ) {
                if (throws) {
                    assertThrows<IllegalArgumentException> {
                        SnapshotStateStack(items, minSize)
                    }
                } else {
                    assertDoesNotThrow {
                        SnapshotStateStack(items, minSize)
                    }
                }
            }
        }

    @TestFactory
    fun items(): Sequence<DynamicTest> =
        sequenceOf(
            emptyList(),
            listOf("item 1"),
            listOf("item 1", "item 2")
        ).map { items ->
            dynamicTest("when items size is ${items.size} then return same content") {
                val stack = SnapshotStateStack(items)

                assert(stack.items == items)
            }
        }

    @TestFactory
    fun lastOrNull(): Sequence<DynamicTest> =
        sequenceOf(
            Triple(emptyList(), null, false),
            Triple(listOf("item 1"), "item 1", true),
            Triple(listOf("item 1", "item 2"), "item 2", true)
        ).map { (items, expected, success) ->
            dynamicTest("when items size is ${items.size} then return ${if (success) "last" else "null"}") {
                val stack = SnapshotStateStack(items)

                assert(stack.lastItemOrNull == expected)
            }
        }

    @TestFactory
    fun size(): Sequence<DynamicTest> =
        sequenceOf(
            emptyList<String>() to 0,
            listOf("item 1") to 1,
            listOf("item 1", "item 2") to 2
        ).map { (items, expected) ->
            dynamicTest("when items size is ${items.size} then return $expected") {
                val stack = SnapshotStateStack(items)

                assert(stack.size == expected)
            }
        }

    @TestFactory
    fun isEmpty(): Sequence<DynamicTest> =
        sequenceOf(
            emptyList<String>() to true,
            listOf("item 1") to false,
            listOf("item 1", "item 2") to false
        ).map { (items, expected) ->
            dynamicTest("when items size is ${items.size} then return $expected") {
                val stack = SnapshotStateStack(items)

                assert(stack.isEmpty == expected)
            }
        }

    @TestFactory
    fun canPop(): Sequence<DynamicTest> =
        sequenceOf(
            Triple(emptyList(), 0, false),
            Triple(listOf("item 1"), 0, true),
            Triple(listOf("item 1"), 1, false),
            Triple(listOf("item 1", "item 2"), 0, true),
            Triple(listOf("item 1", "item 2"), 1, true)
        ).map { (items, minSize, expected) ->
            dynamicTest("when items size is ${items.size} then return $expected") {
                val stack = SnapshotStateStack(items, minSize)

                assert(stack.canPop == expected)
            }
        }

    @TestFactory
    fun pushSingle(): Sequence<DynamicTest> =
        sequenceOf(
            emptyList<String>() to "new item",
            listOf("item 1") to "new item",
            listOf("item 1", "item 2") to "new item"
        ).map { (items, newItem) ->
            dynamicTest("when stack size is ${items.size} then last item is new item") {
                val stack = SnapshotStateStack(items)

                stack.push(newItem)

                assert(stack.lastItemOrNull == newItem)
            }
        }

    @TestFactory
    fun pushList(): Sequence<DynamicTest> =
        sequenceOf(
            emptyList<String>() to emptyList(),
            listOf("item 1") to listOf("new item"),
            listOf("item 1", "item 2") to listOf("new item 1", "new item 2")
        ).map { (items, newItems) ->
            dynamicTest(
                "when stack size is ${items.size} and new items size is ${newItems.size} " +
                    "then last item is new item"
            ) {
                val stack = SnapshotStateStack(items)

                stack.push(newItems)

                assert(stack.lastItemOrNull == newItems.lastOrNull())
            }
        }

    @TestFactory
    fun replace(): Sequence<DynamicTest> =
        sequenceOf(
            emptyList<String>() to "new item",
            listOf("item 1") to "new item",
            listOf("item 1", "item 2") to "new item"
        ).map { (items, newItem) ->
            dynamicTest("when stack size is ${items.size} then last item is new item") {
                val stack = SnapshotStateStack(items)

                stack.replace(newItem)

                assert(stack.lastItemOrNull == newItem)
            }
        }

    @TestFactory
    fun replaceAll(): Sequence<DynamicTest> =
        sequenceOf(
            emptyList<String>() to "new item",
            listOf("item 1") to "new item",
            listOf("item 1", "item 2") to "new item"
        ).map { (items, newItem) ->
            dynamicTest("when stack size is ${items.size} then last item is new item and size is one") {
                val stack = SnapshotStateStack(items)

                stack.replaceAll(newItem)

                assert(stack.size == 1)
                assert(stack.lastItemOrNull == newItem)
            }
        }

    @TestFactory
    fun pop(): Sequence<DynamicTest> =
        sequenceOf(
            Quadruple(emptyList(), 0, false, null),
            Quadruple(listOf("item 1"), 0, true, null),
            Quadruple(listOf("item 1"), 1, false, "item 1"),
            Quadruple(listOf("item 1", "item 2"), 0, true, "item 1"),
            Quadruple(listOf("item 1", "item 2"), 1, true, "item 1")
        ).map { (items, minSize, expected, lastItem) ->
            dynamicTest(
                "when stack size is ${items.size} and min size is $minSize " +
                    "then return $expected and last item is $lastItem"
            ) {
                val stack = SnapshotStateStack(items, minSize)

                assert(stack.pop() == expected)
                assert(stack.lastItemOrNull == lastItem)
            }
        }

    @TestFactory
    fun popAll(): Sequence<DynamicTest> =
        sequenceOf(
            Quadruple(emptyList(), 0, 0, null),
            Quadruple(listOf("item 1"), 0, 0, null),
            Quadruple(listOf("item 1"), 1, 1, "item 1"),
            Quadruple(listOf("item 1", "item 2"), 0, 0, null),
            Quadruple(listOf("item 1", "item 2"), 1, 1, "item 1")
        ).map { (items, minSize, expected, lastItem) ->
            dynamicTest(
                "when stack size is ${items.size} and min size is $minSize " +
                    "then size is $expected and last item is $lastItem"
            ) {
                val stack = SnapshotStateStack(items, minSize)

                stack.popAll()

                assert(stack.size == expected)
                assert(stack.lastItemOrNull == lastItem)
            }
        }

    @TestFactory
    fun popUntil(): Sequence<DynamicTest> =
        sequenceOf(
            Quadruple(emptyList(), 0, null, null),
            Quadruple(listOf("item 1"), 0, "item 1", "item 1"),
            Quadruple(listOf("item 1"), 1, "item 1", "item 1"),
            Quadruple(listOf("item 1", "item 2"), 0, "item 1", "item 1"),
            Quadruple(listOf("item 1", "item 2"), 1, "item 1", "item 1"),
            Quadruple(listOf("item 1", "item 2"), 0, "item 2", "item 2"),
            Quadruple(listOf("item 1", "item 2"), 1, "item 2", "item 2")
        ).map { (items, minSize, popItem, lastItem) ->
            dynamicTest(
                "when stack size is ${items.size}, min size is $minSize and pop until \"$popItem\" " +
                    "then last item is $lastItem"
            ) {
                val stack = SnapshotStateStack(items, minSize)

                stack.popUntil { it == popItem }

                assert(stack.lastItemOrNull == lastItem)
            }
        }
}
