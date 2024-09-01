package com.example.impressmap

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() = runTest {
        // Context of the app under test.
//        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
//        Assert.assertEquals("com.example.impressmap", appContext.packageName)
        val databaseRef = FirebaseDatabase.getInstance(
            "https://impressmap-939c5-default-rtdb.europe-west1.firebasedatabase.app"
        ).reference

        val key = databaseRef
            .child("asdas")
            .child("2chFXNKyGXZaE1gvu0wUJQ5ojaq1")
            .child("email")
            .get()
            .await().value
        Assert.assertEquals(key, "vasyai@gmail.com")
    }
}