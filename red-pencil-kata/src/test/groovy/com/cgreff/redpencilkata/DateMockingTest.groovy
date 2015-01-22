package com.cgreff.redpencilkata

import java.time.LocalDate

/**
 * Class used as a parent for tests that need to mock LocalDate's .now() static method.
 */
abstract class DateMockingTest {

    void mockTodaysDate(LocalDate date) {
        LocalDate.metaClass.'static'.now = {->
            date
        }
    }
}
