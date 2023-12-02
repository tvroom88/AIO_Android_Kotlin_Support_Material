package w2022v9o12.simple.camerax_analysis.model

class MRZResult {

    var docId: String? = null
    var docType: String? = null
    var nationality: String? = null
    var issuer: String? = null
    var dateOfBirth: String? = null
    var dateOfExpiration: String? = null
    var gender: String? = null
    var surname: String? = null
    var givenName: String? = null
    var isParsed = false
    var isVerified = false
    var mrzText: String? = null

    override fun toString(): String {
        return "MRZResult{" +
                "DocId='" + docId + '\'' +
                ", DocType=" + docType +
                ", Nationality='" + nationality + '\'' +
                ", Issuer='" + issuer + '\'' +
                ", Birth='" + dateOfBirth + '\'' +
                ", Expiration='" + dateOfExpiration + '\'' +
                ", Gender='" + gender + '\'' +
                ", Surname='" + surname + '\'' +
                ", GivenName='" + givenName + '\'' +
                ", IsParsed=" + isParsed +
                ", IsVerified=" + isVerified +
                ", MrzText='" + mrzText + '\'' +
                '}'
    }
}