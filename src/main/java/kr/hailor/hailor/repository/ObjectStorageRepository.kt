package kr.hailor.hailor.repository

import io.awspring.cloud.s3.S3Template
import kr.hailor.hailor.config.properties.HostProperties
import kr.hailor.hailor.config.properties.S3Properties
import org.springframework.stereotype.Repository
import java.io.FileNotFoundException
import java.io.InputStream

@Repository
class ObjectStorageRepository(
    private val s3Properties: S3Properties,
    private val s3Template: S3Template,
    private val hostProperties: HostProperties,
) {
    fun upload(
        key: String,
        stream: InputStream,
    ): String {
        val result = s3Template.upload(s3Properties.bucket, key, stream)
        return result.url.toString()
    }

    fun downloadUrl(key: String): String {
        if (s3Template.objectExists(s3Properties.bucket, key)) {
            return "${hostProperties.cdn}/$key"
        }
        throw FileNotFoundException("File not found")
    }

    fun delete(key: String) {
        s3Template.deleteObject(s3Properties.bucket, key)
    }
}
