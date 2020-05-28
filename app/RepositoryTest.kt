package com.e.navigasjon


import com.e.navigasjon.model.regionaltSkred.AvalancheRegionalItem
import com.e.navigasjon.model.regionaltSkred.AvalancheWarning
import com.e.navigasjon.model.skred.AvalancheResponseService
import com.e.navigasjon.ui.skredvarsel.RegionaltSkredRepository
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.*


class RepositoryTest {

    private val mockService = mock<AvalancheResponseService>()
    private val repository = RegionaltSkredRepository()


    @ObsoleteCoroutinesApi
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @ObsoleteCoroutinesApi
    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
        repository.avalancheRegionalService = mockService
    }

    @ObsoleteCoroutinesApi
    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }


    @Test
    fun testHentSkredData() = runBlocking{

        val avalancheTestData = createTestAvalancheResponseItem()
        val startdate = "01-01-2020"
        val enddate = "01-01-2020"
        val language = "1"

        whenever(mockService.getAvalancheDataRegional(language,startdate,enddate)).thenReturn(listOf(avalancheTestData))

        val repositoryResponse = repository.getAvalancheResponse(language,startdate,enddate)?.get(0)

        verify(mockService).getAvalancheDataRegional(language,startdate,enddate)

        assertNotNull(repositoryResponse)
        assertEquals(repositoryResponse, avalancheTestData)
    }

    private fun createTestAvalancheResponseItem(): AvalancheRegionalItem {
        val warninglist = listOf(AvalancheWarning("dangerlevel", "maintext", "regionname"))
        return AvalancheRegionalItem(warninglist)
    }
}