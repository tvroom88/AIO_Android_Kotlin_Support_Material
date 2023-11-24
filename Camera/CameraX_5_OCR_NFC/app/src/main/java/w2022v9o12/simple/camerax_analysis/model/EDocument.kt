package w2022v9o12.simple.camerax_analysis.model

import java.io.Serializable
import java.security.PublicKey

class EDocument : Serializable {

    private var personDetails: PersonDetails? = null
    private var additionalPersonDetails: AdditionalPersonDetails? = null
    private val docPublicKey: PublicKey? = null

    fun getPersonDetails(): PersonDetails? {
        return personDetails
    }

    fun setPersonDetails(personDetails: PersonDetails?) {
        this.personDetails = personDetails
    }

    fun getAdditionalPersonDetails(): AdditionalPersonDetails? {
        return additionalPersonDetails
    }

    fun setAdditionalPersonDetails(additionalPersonDetails: AdditionalPersonDetails?) {
        this.additionalPersonDetails = additionalPersonDetails
    }

}