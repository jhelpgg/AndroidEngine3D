package fr.jhelp.tasks

import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import fr.jhelp.tasks.promise.Promise

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest
{
   @Test
   fun useAppContext()
   {
      // Context of the app under test.
      val appContext = InstrumentationRegistry.getInstrumentation().targetContext
     // assertEquals("fr.jhelp.tasks.test", appContext.packageName)
      parallel { Log.d("TESTONS" , "Yeah !!!!!!!!!!") }
      Thread.sleep(512)
      Log.d("TESTONS" , "DONE")

      val p = Promise<String>()
      p.future.and (IOThread) { Log.d("TESTONS" , "Pint $it") }
      p.result("Vlam")
      Thread.sleep(512)
      Log.d("TESTONS" , "DONE 2")
   }
}
