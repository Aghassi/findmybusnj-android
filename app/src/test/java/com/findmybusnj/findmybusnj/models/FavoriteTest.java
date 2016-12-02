package com.findmybusnj.findmybusnj.models;

import com.findmybusnj.findmybusnj.models.Favorite;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Tests Favorite Model
 */
public class FavoriteTest {
    Favorite favorite;

    @Before
    public void setUp() {
        favorite = new Favorite();
    }

    @After
    public void teardown() {
        favorite = null;
    }

    @Test
    public void assertPrimaryKeyWithoutRoute() {
        String expected = "11111";
        favorite.setStop(expected);

        assertThat("Primary key equals stop", favorite.generatePrimaryKey().equals(expected), is(true));
    }

    @Test
    public void assertPrimaryKeyWithRoute() {
        String stop = "11111";
        String route = "222";
        String expected = stop + route;

        favorite.setStop(stop);
        favorite.setRoute(route);

        assertThat("Primary key equals stop + route", favorite.generatePrimaryKey().equals(expected), is(true));
    }

    @Test
    public void assertIncrementsFrequency() {
        favorite.setFrequency(0);
        favorite.incrementFrequency();

        assertThat("Frequency incremented by 1", favorite.getFrequency() == 1, is(true));
    }
}