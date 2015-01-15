# RedPencilKata

An implementation of Stefan Roock's Red Pencil Code Kata.

## Problem Description ##
*(Taken from: https://stefanroock.wordpress.com/2011/03/04/red-pencil-code-kata/)*

We provide a shopping portal, where dealers can offer their goods (similar to Amazon market place).
We want to support red pencil promotions for reduced prices. During the red pencil promotion the old price is crossed out in red and the new reduced price is written next to it.
To avoid misuse of red pencil promotions the red pencil promotions are activated and deactivated automatically.
The scope of the Code Kata is the implementations of the rules for activation and end of red pencil promotions.

* A red pencil promotion starts due to a price reduction. The price has to be reduced by at least 5% but at most by 30% and the previous price had to be stable for at least 30 days.
* A red pencil promotion lasts 30 days as the maximum length.
* If the price is further reduced during the red pencil promotion the promotion will not be prolonged by that reduction.
* If the price is increased during the red pencil promotion the promotion will be ended immediately.
* If the price if reduced during the red pencil promotion so that the overall reduction is more than 30% with regard to the original price, the promotion is ended immediately.
* After a red pencil promotion is ended additional red pencil promotions may follow – as long as the start condition is valid: the price was stable for 30 days and these 30 days don’t intersect with a previous red pencil promotion.

## Assumptions ##

Some assumptions must be made because requirements are never perfect and there exists no client from which to gather more:

* A red pencil promotion ending after exceeding its duration doesn't change the promotion price back to its original price.
* I'll assume that there's some client interface that allows them to change the price of an item at will (and will implement it).
    * Dealers are presumed to only be able to change their own items' prices and the PriceChanger only acts as a crude approximation to that interface.
    * Otherwise there's no way to trigger a red pencil promotion.
* A red pencil promotion will always run for 30 days unless the price is increased during the promotion.
* Prices will always be sane/sanitized values (non-negative real numbers).
* Items can be uniquely identified by some value (I chose its name).
* The duration will only care about 30 *days* inclusive, and not that 30 * 24 hours have passed since the reduction.
* More may come if I encounter them..