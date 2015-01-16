package com.cgreff.redpencilkata

import com.cgreff.redpencilkata.client.PriceChanger
import com.cgreff.redpencilkata.models.Item
import com.cgreff.redpencilkata.storefront.ShoppingPortal
import com.cgreff.redpencilkata.data.ItemStore

import java.time.LocalDate

/**
 * A main class for the Red Pencil Kata. Though it should be noted that really all of the
 * value of the thing will be in tests.
 */
class Kata {

    public static void main(String[] args) {
        ItemStore itemStore = new ItemStore()
        PriceChanger priceChanger = new PriceChanger(itemStore)
        ShoppingPortal shoppingPortal = new ShoppingPortal(itemStore)

        itemStore.putItem(new Item(name: "item", price: 1.00, lastModified: LocalDate.now()))

    }
}
