package com.cgreff.redpencilkata.models

import java.time.LocalDate

/**
 * POGO for the representation of an item's price.
 */
class Price {
    double price
    double lastPrice
    boolean isPromoted
    LocalDate lastModified
    LocalDate promotionFinished
}
