package w2022v9o12.simple.camerax_analysis.model

import java.io.Serializable

class AdditionalPersonDetails : Serializable {
    var custodyInformation: String? = null
    var fullDateOfBirth: String? = null
    var nameOfHolder: String? = null
    var otherNames: List<String>? = null
    var otherValidTDNumbers: List<String>? = null
    var permanentAddress: List<String>? = null
    var personalNumber: String? = null
    var personalSummary: String? = null
    var placeOfBirth: List<String>? = null
    var profession: String? = null
    lateinit var proofOfCitizenship: ByteArray
    var tag = 0
    var tagPresenceList: List<Int>? = null
    var telephone: String? = null
    var title: String? = null
}
