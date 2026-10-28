package com.example.myapplication.module

import kotlinx.serialization.json.Json

object ClassCustomizer {
    val Json = Json {
        // let parameters using default value could be stored when perform encoding
        encodeDefaults = true
    }
}