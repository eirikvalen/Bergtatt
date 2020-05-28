package com.e.navigasjon


import com.e.navigasjon.ui.turinfo.TurDao
import com.e.navigasjon.ui.turinfo.TurInfoRepository
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.Assert.*

class TestTurResponse {

    private val mockTurDao = mock<TurDao>()
    private val repo = TurInfoRepository(mockTurDao)

    //Tester om vi får forventet response fra AvalancheResponseItem
    @Test
    fun testCorrectAvalancheResponse() = runBlocking{
        val lat = 60.66046
        val long = 10.78769


        val response = repo.getAvalancheResponse(lat, long, "1", "", "")

        val expectedRegion = "Oppland sør"
        val responseRegion = response?.regionName

        assertEquals(expectedRegion, responseRegion)

    }

}