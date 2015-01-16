package com.cgreff.redpencilkata.data

import com.cgreff.redpencilkata.models.CanonicalItem
import com.cgreff.redpencilkata.models.Item
import com.cgreff.redpencilkata.models.Promotion

import java.time.LocalDate

/**
 * Class representing the data layer of the Shopping Portal.
 */
class ItemStore {

    Map<String, CanonicalItem> items

    public ItemStore() {
        items = [:]
    }

    void demoteItem(CanonicalItem canonicalItem) {
        canonicalItem.with {
            item = canonicalItem.item.copyWith(
                    price: canonicalItem.promotion.promotionPrice,
                    lastModified: LocalDate.now())
            promotion = null
        }

        items.put(canonicalItem.item.name, canonicalItem)
    }

    void promoteItem(Item promotedItem, double price) {
        CanonicalItem canonicalItem = new CanonicalItem(
                item: promotedItem.copyWith(lastModified: LocalDate.now()),
                promotion: new Promotion(promotionPrice: price, promotionFinished: LocalDate.now().plusDays(31)))

        items.put(canonicalItem.item.name, canonicalItem)
    }

    CanonicalItem getItem(String itemName) {
        items.get(itemName)
    }

    void putItem(Item item) {
        items.put(item.name, new CanonicalItem(item: item))
    }
}
