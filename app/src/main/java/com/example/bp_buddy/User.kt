package com.example.bp_buddy

class User(registeredUser: Boolean, email: String){
    var userRegistration: Boolean
    var userEmail: String

    init {
        userRegistration = registeredUser
        userEmail = email
    }
}