package com.Rohan.flashchatnewfirebase

class InstantMessage {

    val message: String
    val author: String

    constructor(message: String, author: String) {
        this.message = message
        this.author = author
    }

    constructor() {}
}
