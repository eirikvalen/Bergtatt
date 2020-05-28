package com.e.gruppe27.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/*
dataklasse som tar imot og holder paa informasjon om en tur
Er en entitet i Room-databasen med navnet på turen som primary key i tabellen
 */

@Entity(tableName = "tur_table")
data class Tur (@PrimaryKey
                val navn : String,
                @ColumnInfo(name = "turid")
                val turID: Int,
                val omrade : String,
                val type : String,
                val gradering : String,
                val beskrivelse: String,
                val bildeSrc : String,
                val gpxSrc : String,
                val lengde : Double,
                val varighet : String,
                val sesong : String,
                var status : Boolean,
                var mineTurer : Boolean)