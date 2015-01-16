package com.cgreff.redpencilkata.models

import groovy.transform.Immutable

import java.time.LocalDate

/**
 * POJO (POGO) describing the Sale Item.
 */
@Immutable(copyWith = true, knownImmutableClasses = [LocalDate])
class Item {
    String name
    double price
    LocalDate lastModified
}
