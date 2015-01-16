package com.cgreff.redpencilkata

import com.cgreff.redpencilkata.models.Item
import com.cgreff.redpencilkata.client.PriceChanger
import com.cgreff.redpencilkata.storefront.ShoppingPortal
import com.cgreff.redpencilkata.data.ItemStore
import org.junit.Before
import org.junit.Test

import java.time.LocalDate
import java.time.Month

/**
 * General tests for the Red Pencil Promotion kata as a whole. Nothing mocked except the date.
 */
class KataTest {

    static final String ITEM_NAME = 'item'
    static final LocalDate TODAY = LocalDate.of(2000, Month.MAY, 1)

    ShoppingPortal shoppingPortal
    ItemStore itemStore
    PriceChanger priceChanger


    @Before
    void setUp() {
        itemStore = new ItemStore()
        itemStore.putItem(new Item (name: ITEM_NAME, price: 1.00, lastModified: TODAY.minusDays(31)))
        priceChanger = new PriceChanger(itemStore)
        shoppingPortal = new ShoppingPortal(itemStore)
        mockTodaysDate(TODAY)
    }

    @Test
    void 'should start a red pencil promotion when an item is discounted by 20% for the first time'() {
        promoteItem()
    }

    @Test
    void 'should not extend a red pencil promotion when an item is discounted twice within 30 days'() {
        promoteItem()
        LocalDate promotionEndDate = itemStore.getItem(ITEM_NAME).promotion.promotionFinished
        mockTodaysDate(TODAY.plusDays(10))
        priceChanger.updatePrice(ITEM_NAME, 0.75)
        assert shoppingPortal.getItem(ITEM_NAME).promotion.promotionFinished == promotionEndDate
    }

    @Test
    void 'should end a red pencil promotion when an item has its price increased'() {
        promoteItem()
        priceChanger.updatePrice(ITEM_NAME, 0.85)
        assert !shoppingPortal.getItem(ITEM_NAME).promotion
    }

    @Test
    void 'should end a red pencil promotion when an item has its price decreased below 30% of the original price'() {
        promoteItem()
        priceChanger.updatePrice(ITEM_NAME, 0.69)
        assert !shoppingPortal.getItem(ITEM_NAME).promotion
    }

    @Test
    void 'should not end a red pencil promotion when an item has its price decreased to 30% of the original price'() {
        promoteItem()
        priceChanger.updatePrice(ITEM_NAME, 0.7)
        assert shoppingPortal.getItem(ITEM_NAME).promotion
    }

    @Test
    void 'should not start a red pencil promotion for an item that has its price decreased within 30 days of a prior promotions end'() {
        promoteItem()
        priceChanger.updatePrice(ITEM_NAME, 1.0)
        mockTodaysDate(TODAY.plusDays(10))
        priceChanger.updatePrice(ITEM_NAME, 0.9)
        assert !shoppingPortal.getItem(ITEM_NAME).promotion
    }

    @Test
    void 'should not start a red pencil promotion when an item has its price decreased and it has not been stable for 30 days'() {
        mockTodaysDate(TODAY.minusDays(10))
        priceChanger.updatePrice(ITEM_NAME, 0.9)
        assert !shoppingPortal.getItem(ITEM_NAME).promotion
    }

    @Test
    void 'should stop a promotion when 30 days end'() {
        promoteItem()
        mockTodaysDate(TODAY.plusDays(31))
        assert !shoppingPortal.getItem(ITEM_NAME).promotion
    }

    private void promoteItem() {
        priceChanger.updatePrice(ITEM_NAME, 0.8)
        assert shoppingPortal.getItem(ITEM_NAME).promotion
    }

    private void mockTodaysDate(LocalDate date) {
        LocalDate.metaClass.'static'.now = {->
            date
        }
    }
}

