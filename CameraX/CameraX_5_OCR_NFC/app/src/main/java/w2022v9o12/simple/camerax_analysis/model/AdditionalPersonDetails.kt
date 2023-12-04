package w2022v9o12.simple.camerax_analysis.model

import java.io.Serializable

class AdditionalPersonDetails : Serializable {
    var nameOfHolder: String? = null
    var otherNames: List<String>? = null
    var personalNumber: String? = null
    var placeOfBirth: List<String>? = null
    var permanentAddress: List<String>? = null
    var telephone: String? = null
    var profession: String? = null
    var title: String? = null
    var personalSummary: String? = null
    var proofOfCitizenship: ByteArray?= null
    var otherValidTDNumbers: List<String>? = null
    var custodyInformation: String? = null
}
