package w2022v9o12.simple.camerax_analysis.model

import java.io.Serializable

class AdditionalDocumentDetails : Serializable {
    var issuingAuthority: String? = null
    var dateOfIssue: String? = null
    var namesOfOtherPersons: List<String>? = null
    var endorsementsAndObservations: String? = null
    var taxOrExitRequirements: String? = null
    var imageOfFront: ByteArray? = null
    var imageOfRear: ByteArray? = null
}