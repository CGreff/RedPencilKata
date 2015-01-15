package com.cgreff.redpencilkata.storefront

import com.cgreff.redpencilkata.models.Item

/**
 * Represents a list of items and the PriceChanger.
 */
class ShoppingPortal {
    PriceChanger priceChanger
    Map<String, Item> items
}
