package com.tufelmalik.dailykill.data.classes

data class MyCategory(var category: String = ""){

    fun getCat() : String{
        return category
    }
}
