package kr.hailor.hailor

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class HailorApplication

fun main(args: Array<String>) {
    runApplication<HailorApplication>(*args)
}
