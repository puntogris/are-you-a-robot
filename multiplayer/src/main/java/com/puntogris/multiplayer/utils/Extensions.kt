package com.puntogris.multiplayer.utils

import androidx.lifecycle.MutableLiveData

fun MutableLiveData<Int>.plusOne(){
    postValue(value?.plus(1))
}