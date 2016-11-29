package com.findmybusnj.findmybusnj.models;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Created by davidaghassi on 11/16/16.
 */

public class ResultDataModelTest {
    ResultDataModel rdm;

    @Before
    public void setUp() {
        rdm = new ResultDataModel();
    }

    @After
    public void teardown() {
        rdm = null;
    }

    @Test
    public void assertSetTimeForZero() {
        rdm.setTime(0);

        assertThat("arriving was set to true, and time is unchanged", rdm.isArriving() == true, is(true));
        assertThat("time hasn't changed from 0", rdm.getTime() == 0, is(true));
    }

    @Test
    public void assertSetTimeForGreaterThanZero() {
        rdm.setTime(1);

        assertThat("time is set to 1", rdm.getTime() == 1, is(true));
    }
}
