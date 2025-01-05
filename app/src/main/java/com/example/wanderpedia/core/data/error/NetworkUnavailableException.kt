package com.example.wanderpedia.core.data.error

import java.io.IOException

class NetworkUnavailableException(message: String = "No network available :(") :
    IOException(message)