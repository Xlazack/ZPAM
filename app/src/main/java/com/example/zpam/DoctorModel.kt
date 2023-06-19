package com.example.zpam

import java.util.HashMap

data class DoctorModel(
    var userName: String? = null,
    var userSurname: String? = null,
    var userBirthDate: String? = null,
    var userPesel: String? = null,
    var userEmail: String? = null,
    var userPhone: String? = null,
    var userAddress: String? = null,
    var userIsDoctor: Boolean? = null,
    var userBio: String? = null
) {
    val userData = hashMapOf(
        "userName" to userName,
        "userSurname" to userSurname,
        "userBirthDate" to userBirthDate,
        "userPesel" to userPesel,
        "userEmail" to userEmail,
        "userPhone" to userPhone,
        "userAddress" to userAddress,
        "userIsDoctor" to userIsDoctor,
        "userBio" to userBio,
    )
    fun show(): HashMap<String, out Any?> {
        return userData
    }
}