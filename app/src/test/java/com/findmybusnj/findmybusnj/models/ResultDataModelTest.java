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


}
