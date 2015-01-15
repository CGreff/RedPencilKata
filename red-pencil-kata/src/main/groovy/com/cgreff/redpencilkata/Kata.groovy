package com.cgreff.redpencilkata

import com.cgreff.redpencilkata.storefront.ShoppingPortal

/**
 * A main class for the Red Pencil Kata. Though it should be noted that really all of the
 * value of the thing will be in tests.
 */
class Kata {

    private static final ShoppingPortal SHOPPING_PORTAL = new ShoppingPortal()

    public static void main(String[] args) {
        System.out.println("For now I'm a helpless main class!")
    }
}
