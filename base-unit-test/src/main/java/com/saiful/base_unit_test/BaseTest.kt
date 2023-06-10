package com.saiful.base_unit_test

import org.junit.After
import org.junit.Before

abstract class BaseTest {

    @Before
    abstract fun setup()

    @After
    abstract fun tearDown()
}