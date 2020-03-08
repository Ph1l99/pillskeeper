package com.pillskeeper.data

import com.pillskeeper.enums.RelationEnum

class Friend (name: String, phone : String, email: String?, relationEnum: RelationEnum){

    var name : String = name;
    var phone : String = phone;
    var email : String? = email;
    var relation : RelationEnum = relationEnum;

}