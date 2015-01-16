package com.cgreff.redpencilkata.client

import com.cgreff.redpencilkata.models.CanonicalItem
import com.cgreff.redpencilkata.models.Item
import com.cgreff.redpencilkata.data.ItemStore
import java.time.LocalDate

/**
 * This is supposed to represent the business owner interface for changing the price of one of their items.
 * Nominally it would be controlled as a singleton by some framework, but I haven't gone to the trouble of making
 * the constructor private and managing a static reference to its object.
 * This class will change the price of a given item; potentially triggering a change in the red pencil promotion.
 */
class PriceChanger {

    final ItemStore itemStore

    PriceChanger(ItemStore itemStore) {
        this.itemStore = itemStore
    }

    void updatePrice(String itemName, double price) {
        CanonicalItem canonicalItem = itemStore.getItem(itemName)

        if (needsPromoted(canonicalItem, price)) {
            promoteItem(canonicalItem.item, price)
        } else if (needsDemoted(canonicalItem, price)) {
            demoteItem(canonicalItem)
        } else {
            if (canonicalItem.promotion) {
                canonicalItem.with {
                    item = canonicalItem.item.copyWith(lastModified: LocalDate.now())
                    promotion = canonicalItem.promotion.copyWith(promotionPrice: price)
                }
            } else {
                canonicalItem.item = canonicalItem.item.copyWith(price: price, lastModified: LocalDate.now())
            }
        }
    }

    private void promoteItem(Item item, double price) {
        itemStore.promoteItem(item, price)
    }

    private void demoteItem(CanonicalItem canonicalItem) {
        itemStore.demoteItem(canonicalItem)
    }

    private boolean needsPromoted(CanonicalItem canonicalItem, double newPrice) {
        double oldPrice = canonicalItem.item.price
        if (!canonicalItem.promotion && canonicalItem.item.lastModified.isBefore(LocalDate.now().minusDays(30))) {
            if (newPrice >= (oldPrice * 0.7) && newPrice < (oldPrice * 0.95)) {
                return true
            }
        }

        false
    }

    private boolean needsDemoted(CanonicalItem canonicalItem, double newPrice) {
        double oldPrice = canonicalItem.item.price
        if (canonicalItem.promotion && (newPrice < (oldPrice * 0.7) || newPrice > canonicalItem.promotion.promotionPrice)) {
            return true
        }

        false
    }
}


