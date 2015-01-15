package com.cgreff.redpencilkata

import com.cgreff.redpencilkata.models.Item
import com.cgreff.redpencilkata.models.Price
import com.cgreff.redpencilkata.storefront.PriceChanger
import com.cgreff.redpencilkata.storefront.ShoppingPortal
import org.junit.Before
import org.junit.Test

import java.time.LocalDate
import java.time.Month

/**
 * General tests for the Red Pencil Promotion kata as a whole.
 */
class KataTest {

    private ShoppingPortal shoppingPortal
    private static final String ITEM_NAME = 'item'
    private static final String PROMOTED_ITEM_NAME = 'promoted item'
    private static final String DEPROMOTED_ITEM_NAME = 'depromoted item'
    private static final LocalDate TODAY = LocalDate.of(2000, Month.MAY, 1)

    @Before
    void setUp() {
        shoppingPortal = new ShoppingPortal(
                priceChanger: new PriceChanger(),

                items: [
                        "$ITEM_NAME" : new Item(
                                name: ITEM_NAME,
                                price: new Price(
                                        price: 1.00,
                                        lastPrice: 1.00,
                                        lastModified: TODAY.minusDays(31),
                                        promotionFinished: TODAY.minusDays(31),
                                        isPromoted: false
                                )),
                        "$PROMOTED_ITEM_NAME" : new Item(
                                name: ITEM_NAME,
                                price: new Price(
                                        price: 0.80,
                                        lastPrice: 1.00,
                                        lastModified: TODAY,
                                        promotionFinished: TODAY.plusDays(30),
                                        isPromoted: true
                                )),
                        "$DEPROMOTED_ITEM_NAME" : new Item(
                                name: ITEM_NAME,
                                price: new Price(
                                        price: 1.00,
                                        lastPrice: 1.00,
                                        lastModified: TODAY.minusDays(30),
                                        promotionFinished: TODAY,
                                        isPromoted: false
                                ))
                ]
        )

        mockTodaysDate(TODAY)
    }

    @Test
    void 'should start a red pencil promotion when an item is discounted by 20% for the first time'() {
        shoppingPortal.changePrice(ITEM_NAME, 0.80)
        assertPromotionStatus(shoppingPortal.getItem(ITEM_NAME), true, TODAY.plusDays(30))
    }

    @Test
    void 'should not extend a red pencil promotion when an item is discounted twice within 30 days'() {
        mockTodaysDate(TODAY.plusDays(10))
        shoppingPortal.changePrice(PROMOTED_ITEM_NAME, 0.75)
        assertPromotionStatus(shoppingPortal.getItem(PROMOTED_ITEM_NAME), true, TODAY.plusDays(30))
    }

    @Test
    void 'should end a red pencil promotion when an item has its price increased'() {
        shoppingPortal.changePrice(PROMOTED_ITEM_NAME, 1.25)
        assertPromotionStatus(shoppingPortal.getItem(PROMOTED_ITEM_NAME), false, TODAY)
    }

    @Test
    void 'should end a red pencil promotion when an item has its price decreased below 30% of the original price'() {
        shoppingPortal.changePrice(PROMOTED_ITEM_NAME, 0.69)
        assertPromotionStatus(shoppingPortal.getItem(PROMOTED_ITEM_NAME), false, TODAY)
    }

    @Test
    void 'should not end a red pencil promotion when an item has its price decreased to 30% of the original price'() {
        shoppingPortal.changePrice(PROMOTED_ITEM_NAME, 0.70)
        assertPromotionStatus(shoppingPortal.getItem(PROMOTED_ITEM_NAME), true, TODAY.plusDays(30))
    }

    @Test
    void 'should not start a red pencil promotion for an item that has its price decreased within 30 days of a prior promotion\'s end'() {
        mockTodaysDate(TODAY.plusDays(10))
        shoppingPortal.changePrice(DEPROMOTED_ITEM_NAME, 0.80)
        assertPromotionStatus(shoppingPortal.getItem(DEPROMOTED_ITEM_NAME), false, TODAY)
    }

    @Test
    void 'should not start a red pencil promotion when an item has its price decreased and it has not been stable for 30 days'() {
        mockTodaysDate(TODAY.minusDays(20))
        shoppingPortal.changePrice(ITEM, 0.80)
        assertPromotionStatus(shoppingPortal.getItem(ITEM_NAME), false, TODAY.minusDays(31))
    }

    @Test
    void 'should stop a promotion when 30 days end'() {
        mockTodaysDate(TODAY.plusDays(31))
        assertPromotionStatus(shoppingPortal.getItem(PROMOTED_ITEM_NAME), false, TODAY.plusDays(30))
    }

    private void assertPromotionStatus(Item item, boolean promotionStatus, LocalDate promotionEnd) {
        Price price = item.price
        assert price.isPromoted == promotionStatus
        assert price.promotionFinished == promotionEnd
    }

    private void mockTodaysDate(LocalDate date) {
        LocalDate.metaClass.'static'.now = {->
            date
        }
    }
}

