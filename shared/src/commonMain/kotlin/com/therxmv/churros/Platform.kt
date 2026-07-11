package com.therxmv.churros

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform