package com.raffli.tadarusmobile2

class tadarus_data {
    var juz: Int? = null
    var surat: String? = null
    var ayat: Int? = null
    var tempat: String? = null
    var key: String? = null
    constructor() {}
    constructor(juz: Int?, surat: String?, ayat: Int?, tempat: String?) {
        this.juz = juz
        this.surat = surat
        this.ayat = ayat
        this.tempat = tempat
    }
}