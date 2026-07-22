package com.example.ejemplodedatastore.data

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.example.ejemplodedatastore.AppStatsProto
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

object AppStatsSerializer : Serializer<AppStatsProto> {

    override val defaultValue: AppStatsProto = AppStatsProto.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): AppStatsProto {
        try {
            return AppStatsProto.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("No se pudo leer el archivo proto.", exception)
        }
    }

    override suspend fun writeTo(t: AppStatsProto, output: OutputStream) {
        t.writeTo(output)
    }
}