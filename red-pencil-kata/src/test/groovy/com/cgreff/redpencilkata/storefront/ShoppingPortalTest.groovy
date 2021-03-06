package com.cgreff.redpencilkata.storefront

import com.cgreff.redpencilkata.DateMockingTest
import com.cgreff.redpencilkata.data.ItemStore
import com.cgreff.redpencilkata.models.CanonicalItem
import com.cgreff.redpencilkata.models.Item
import com.cgreff.redpencilkata.models.Promotion
import org.gmock.WithGMock
import org.junit.Before
import org.junit.Test

import java.time.LocalDate
import java.time.Month

/**
 * Tests for the ShoppingPortal.
 */
@WithGMock
class ShoppingPortalTest extends DateMockingTest {

    private static final String ITEM_NAME = 'item'

    private ShoppingPortal shoppingPortal
    private ItemStore mockItemStore

    @Before
    void setUp() {
        mockItemStore = mock(ItemStore)
        shoppingPortal = new ShoppingPortal(mockItemStore)
        mockTodaysDate(LocalDate.of(2000, Month.MAY, 1))
    }

    @Test
    void 'should correctly return an item whose promotion has not expired'() {
        CanonicalItem item = new CanonicalItem(
                item: new Item(name: ITEM_NAME),
                promotion: new Promotion(promotionFinished: LocalDate.of(2000, Month.JUNE, 1)))

        mockItemStore.getItem(ITEM_NAME).returns(item).once()

        play {
            shoppingPortal.getItem(ITEM_NAME)
        }
    }

    @Test
    void 'should demote an item when a promotion has expired then return it correctly'() {
        CanonicalItem item = new CanonicalItem(
                item: new Item(name: ITEM_NAME),
                promotion: new Promotion(promotionFinished: LocalDate.of(2000, Month.APRIL, 20)))

        mockItemStore.getItem(ITEM_NAME).returns(item).times(2)
        mockItemStore.demoteItem(item).once()

        play {
            shoppingPortal.getItem(ITEM_NAME)
        }
    }
}
