package com.pillskeeper.data

import com.pillskeeper.enums.RelationEnum

/**
 * Data class representing a Friend that can be added
 * @param name The friend's name
 * @param surname The friend's surname
 * @param phone The friend's phone
 * @param email The friend's email
 * @param relationEnum The type of relationship between the friend and the user
 */
data class Friend(
    var name: String,
    var surname: String,
    var phone: String?,
    var email: String?,
    var relationEnum: RelationEnum
)