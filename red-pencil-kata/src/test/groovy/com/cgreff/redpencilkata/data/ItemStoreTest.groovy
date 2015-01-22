package com.cgreff.redpencilkata.data

import com.cgreff.redpencilkata.models.Item
import org.junit.Before
import org.junit.Test

import java.time.LocalDate

/**
 * Tests for the Item Store!
 */
class ItemStoreTest {

    private static final String ITEM_NAME = 'item'
    private ItemStore itemStore

    @Before
    void setUp() {
        itemStore = new ItemStore()
    }

    @Test
    void 'should properly promote an item'() {
        Item item = new Item(name: ITEM_NAME, price: 1.0)
        itemStore.putItem(item)
        itemStore.promoteItem(item, 0.8)

        assert itemStore.getItem(ITEM_NAME).promotion
        assert itemStore.getItem(ITEM_NAME).promotion.promotionPrice == 0.8
    }

    @Test
    void 'should properly demote an item'() {
        Item item = new Item(name: ITEM_NAME, price: 1.0)
        itemStore.putItem(item)
        itemStore.promoteItem(item, 0.8)

        itemStore.demoteItem(itemStore.getItem(ITEM_NAME))

        assert !itemStore.getItem(ITEM_NAME).promotion
        assert itemStore.getItem(ITEM_NAME).item.price == 0.8
    }
}
