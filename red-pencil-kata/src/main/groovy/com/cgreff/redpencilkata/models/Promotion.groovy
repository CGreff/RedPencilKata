package com.cgreff.redpencilkata.models

import groovy.transform.Immutable

import java.time.LocalDate

/**
 * An object representing the promoted state of an item.
 */
@Immutable(copyWith = true, knownImmutableClasses = [LocalDate])
class Promotion {
    double promotionPrice
    LocalDate promotionFinished
}
