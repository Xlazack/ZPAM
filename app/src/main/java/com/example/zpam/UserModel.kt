package com.example.zpam

data class UserModel(
    var userName: String? = null,
    var userSurname: String? = null,
    var userBirthDate: String? = null,
    var userPesel: String? = null,
    var userEmail: String? = null,
    var userPhone: String? = null,
    var userAddress: String? = null,
    var userIsDoctor: Boolean? = null
) {

}