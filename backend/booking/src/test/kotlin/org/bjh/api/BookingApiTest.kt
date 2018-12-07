package org.bjh.api

import org.assertj.core.api.Assertions
import org.bjh.LocalApplicationRunner
import org.junit.Test

class BookingApiTest : LocalApplicationRunner() {
    @Test
    fun someDumTest() {
        Assertions.assertThat(2).isEqualTo(2);
    }
}