package com.saifu.saifu_v3

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class SaifuV3Application

fun main(args: Array<String>) {
    runApplication<SaifuV3Application>(*args)
}
