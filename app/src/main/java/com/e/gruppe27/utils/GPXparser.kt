package com.e.gruppe27.utils

import android.location.Location
import org.w3c.dom.Document
import org.xml.sax.InputSource
import java.io.StringReader
import java.net.URL
import javax.xml.parsers.DocumentBuilderFactory
import org.apache.commons.io.IOUtils
import java.lang.Exception


class GPXparser{

     fun decodeGPX(gpxsrc: String): List<Location> {

         val list = mutableListOf<Location>()

        val xmlstring = IOUtils.toString(URL(gpxsrc), "UTF-8")

        try {

            val document = convertStringToXMLDocument(xmlstring)
            val elementRoot = document?.documentElement
            val nodelistTrkpt = elementRoot?.getElementsByTagName("trkpt")

            if (nodelistTrkpt != null) {
                for (i in 0 until nodelistTrkpt.length) {
                    val node = nodelistTrkpt.item(i)

                    val attributes = node.attributes
                    val newLatitude = attributes.getNamedItem("lat").textContent
                    val newlatitudeDouble = newLatitude.toDouble()
                    val newLongitude = attributes.getNamedItem("lon").textContent

                    val newlongitudeDouble = newLongitude.toDouble()
                    val newLocationName = "$newLatitude:$newLongitude"
                    val newLocation = Location(newLocationName)
                    newLocation.latitude = newlatitudeDouble
                    newLocation.longitude = newlongitudeDouble
                    list.add(newLocation)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }

    //Parser som produserer DOM objekt trær fra XML content
    private fun convertStringToXMLDocument(xmlString: String): Document? {
        val factory = DocumentBuilderFactory.newInstance()
        try { //Create DocumentBuilder with default configuration
            val builder = factory.newDocumentBuilder()
            //Parse the content to Document object
            return builder.parse(InputSource(StringReader(xmlString)))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }




}