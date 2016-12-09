package com.findmybusnj.findmybusnj;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.findmybusnj.findmybusnj.handlers.DatabaseHandler;
import com.findmybusnj.findmybusnj.models.Favorite;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class DatabaseHandlerTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.findmybusnj.findmybusnj", appContext.getPackageName());
    }

    @Test
    public void testInsertionOfFavorite() throws Exception {
        Favorite favorite = new Favorite("", "", 0);
        favorite.setStop("26229");

        DatabaseHandler db = new DatabaseHandler(InstrumentationRegistry.getContext());
        db.addFavorite(favorite);
        assertThat("Favorite was inserted into the database", db.getFavoritesCount() == 1, is(true));
    }
}
