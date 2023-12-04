package w2022v9o12.simple.camerax_analysis.model

import java.io.Serializable

class EDocument : Serializable {

    private var personDetails: PersonDetails? = null
    private var additionalPersonDetails: AdditionalPersonDetails? = null
    private var additionalDocumentDetails: AdditionalDocumentDetails? = null

    fun getPersonDetails(): PersonDetails? {
        return personDetails
    }

    fun setPersonDetails(personDetails: PersonDetails?) {
        this.personDetails = personDetails
    }

    fun getAdditionalPersonDetail(): AdditionalPersonDetails? {
        return additionalPersonDetails
    }

    fun setAdditionalPersonDetails(additionalPersonDetails: AdditionalPersonDetails?) {
        this.additionalPersonDetails = additionalPersonDetails
    }

    fun getAdditionalDocumentDetail(): AdditionalDocumentDetails? {
        return additionalDocumentDetails
    }

    fun setAdditionalDocumentDetails(additionalDocumentDetails: AdditionalDocumentDetails?) {
        this.additionalDocumentDetails = additionalDocumentDetails
    }
}