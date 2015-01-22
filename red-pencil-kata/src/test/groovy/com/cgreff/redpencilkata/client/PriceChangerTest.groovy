package com.cgreff.redpencilkata.client

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
 * Tests for the PriceChanger.
 */
@WithGMock
class PriceChangerTest extends DateMockingTest {

    private static final String ITEM_NAME = 'item'

    private PriceChanger priceChanger
    private ItemStore mockItemStore

    @Before
    void setUp() {
        mockItemStore = mock(ItemStore)
        priceChanger = new PriceChanger(mockItemStore)
        mockTodaysDate(LocalDate.of(2000, Month.MAY, 1))
    }

    @Test
    void 'should demote an item when its price is increased'() {
        CanonicalItem item = new CanonicalItem(
                item: new Item(name: ITEM_NAME, price: 1.0, lastModified: LocalDate.now()),
                promotion: new Promotion(promotionPrice: 0.8, promotionFinished: LocalDate.now().plusDays(31)))
        mockItemStore.getItem(ITEM_NAME).returns(item).once()
        mockItemStore.demoteItem(item).once()

        play {
            priceChanger.updatePrice(ITEM_NAME, 1.0)
        }
    }

    @Test
    void 'should promote an item when the appropriate price drop occurs'() {
        CanonicalItem item = new CanonicalItem(item: new Item(name: ITEM_NAME, price: 1.0, lastModified: LocalDate.now().minusDays(31)))
        mockItemStore.getItem(ITEM_NAME).returns(item).once()
        mockItemStore.promoteItem(item.item, 0.8).once()

        play {
            priceChanger.updatePrice(ITEM_NAME, 0.8)
        }
    }
}
