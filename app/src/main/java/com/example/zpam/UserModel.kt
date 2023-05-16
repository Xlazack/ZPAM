package com.example.zpam

data class UserModel(
    var userID: String? = null,
    var userName: String? = null,
    var userSurname: String? = null,
    var userBirthDate: String? = null,
    var userPesel: Long? = null,
    var userEmail: String? = null,
    var userPhone: Long? = null
) {

}