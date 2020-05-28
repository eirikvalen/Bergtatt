package com.e.gruppe27


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import com.e.gruppe27.model.database.TurDatabase
import com.e.gruppe27.ui.turinfo.TurDao
import org.junit.runner.RunWith
import com.e.gruppe27.model.Tur

import org.junit.runners.JUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.*
import org.junit.Assert.*
import java.io.IOException

@RunWith(JUnit4::class)
class TurDatabaseTest {

    @Rule
    @JvmField
    val instantTaskExcecutorRule = InstantTaskExecutorRule()

    private lateinit var turDao: TurDao
    private lateinit var db: TurDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(context, TurDatabase::class.java)
            .build()

        turDao = db.turDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }


    @Test
    fun insertTest() {

        val eksempelTur = Tur(
            "navn", 123, "omraade", "type", "gradering", "beskrivelse",
            "bildesrc", "gpxsrc", 45.0, "varighet", "sesong", status = false, mineTurer = false
        )

        turDao.insert(eksempelTur)
        assertNotNull(turDao)

        val tur = LiveDataTestUtil.getValue(turDao.getTurer())[0]

        assertEquals(eksempelTur, tur)

    }


}