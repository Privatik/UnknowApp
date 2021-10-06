package com.io.unknow.util.extends

fun Int.getDateString(): String =
    if (this < 10) "0$this"
    else this.toString()