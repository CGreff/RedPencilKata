package com.cgreff.redpencilkata.storefront

import com.cgreff.redpencilkata.models.CanonicalItem
import com.cgreff.redpencilkata.data.ItemStore

import java.time.LocalDate

/**
 * Represents the user-facing Shopping Portal. Effectively just an interface to the ItemStore.
 */
class ShoppingPortal {

    ItemStore itemStore

    ShoppingPortal(ItemStore itemStore) {
        this.itemStore = itemStore
    }

    CanonicalItem getItem(String itemName) {
        CanonicalItem canonicalItem = itemStore.getItem(itemName)
        if (canonicalItem.promotion && promotionIsExpired(canonicalItem)) {
            demoteItem(canonicalItem)
            canonicalItem = itemStore.getItem(itemName)
        }

        canonicalItem
    }

    private boolean promotionIsExpired(CanonicalItem item) {
        !item.promotion.promotionFinished.isAfter(LocalDate.now())
    }

    private void demoteItem(CanonicalItem item) {
        itemStore.demoteItem(item)
    }
}
