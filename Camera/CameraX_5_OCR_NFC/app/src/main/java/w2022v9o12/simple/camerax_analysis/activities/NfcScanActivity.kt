package w2022v9o12.simple.camerax_analysis.activities

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.nfc.NfcAdapter
import android.nfc.tech.IsoDep
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import net.sf.scuba.smartcards.CardFileInputStream
import net.sf.scuba.smartcards.CardService
import org.jmrtd.BACKey
import org.jmrtd.BACKeySpec
import org.jmrtd.PassportService
import org.jmrtd.lds.CardSecurityFile
import org.jmrtd.lds.DisplayedImageInfo
import org.jmrtd.lds.PACEInfo
import org.jmrtd.lds.SecurityInfo
import org.jmrtd.lds.icao.DG11File
import org.jmrtd.lds.icao.DG1File
import org.jmrtd.lds.icao.DG2File
import org.jmrtd.lds.icao.DG3File
import org.jmrtd.lds.icao.DG5File
import org.jmrtd.lds.icao.DG7File
import org.jmrtd.lds.icao.MRZInfo
import org.jmrtd.lds.iso19794.FaceImageInfo
import org.jmrtd.lds.iso19794.FaceInfo
import org.jmrtd.lds.iso19794.FingerImageInfo
import w2022v9o12.simple.camerax_analysis.MainApplication
import w2022v9o12.simple.camerax_analysis.R
import w2022v9o12.simple.camerax_analysis.model.AdditionalPersonDetails
import w2022v9o12.simple.camerax_analysis.model.DateUtil
import w2022v9o12.simple.camerax_analysis.model.EDocument
import w2022v9o12.simple.camerax_analysis.model.Image
import w2022v9o12.simple.camerax_analysis.model.ImageUtil
import w2022v9o12.simple.camerax_analysis.model.PersonDetails
import java.util.Date

class NfcScanActivity : AppCompatActivity() {

    private val TAG = NfcScanActivity::class.java.simpleName

    private val MRZ_RESULT = "MRZ_RESULT"

    private lateinit var adapter: NfcAdapter
    private lateinit var mrzInfo: MRZInfo

    private lateinit var passportNumberEditText: EditText
    private lateinit var expirationDateEditText: EditText
    private lateinit var birthDateEditText: EditText

    private var passportNumber = ""
    private var expirationDate = ""
    private var birthDate = ""

    private lateinit var imageView: ImageView
    private lateinit var mContext: Context

    companion object {
        val EDOCUMENT = "EDOCUMENT"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nfc_scan)

        passportNumberEditText = findViewById(R.id.passportNumberEditText)
        expirationDateEditText = findViewById(R.id.expirationDateEditText)
        birthDateEditText = findViewById(R.id.birthDateEditText)

        imageView = findViewById(R.id.imageView)


        // CaptuerActivity로 부터 받은 데이터 추출
        val intent: Intent = getIntent()
        mrzInfo = (intent.getSerializableExtra(MRZ_RESULT) as MRZInfo?)!!
        val application: MainApplication = applicationContext as MainApplication
        imageView.setImageBitmap(application.mBitmap)

        adapter = NfcAdapter.getDefaultAdapter(this)
        setMrzData(mrzInfo)
    }

    override fun onResume() {
        super.onResume()
        if (adapter == null) {
            // 기기가 NFC를 지원하지 않음.
            Toast.makeText(this, "NFC 기능이 없어서 사용할 수가 없습니다..", Toast.LENGTH_SHORT).show()
        } else if (!adapter.isEnabled()) {
            // NFC가 비활성화되어 있음
            val intent = Intent(android.provider.Settings.ACTION_NFC_SETTINGS)
            startActivity(intent)
        } else {
            // NFC가 켜져있음
            val intent: Intent = Intent(applicationContext, this.javaClass)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            val pendingIntent: PendingIntent =
                PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE)
            val filter: Array<Array<String>> = arrayOf(arrayOf("android.nfc.tech.IsoDep"))
            adapter.enableForegroundDispatch(this, pendingIntent, null, filter)
        }
    }


    override fun onPause() {
        super.onPause()
        if (adapter != null) {
            adapter.disableForegroundDispatch(this) //nfc 비활성화
        }
    }

    private fun setMrzData(mrzInfo: MRZInfo?) {
        if (mrzInfo != null) {
            passportNumber = mrzInfo.documentNumber
            expirationDate = mrzInfo.dateOfExpiry
            birthDate = mrzInfo.dateOfBirth
            passportNumberEditText.setText(passportNumber)
            expirationDateEditText.setText(expirationDate)
            birthDateEditText.setText(birthDate)
        }

    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        Log.d("onPostExecute", "onPostExecute111")
        if ((NfcAdapter.ACTION_TECH_DISCOVERED == intent.getAction())) {
            val tag: android.nfc.Tag? = intent.extras?.getParcelable(NfcAdapter.EXTRA_TAG)
            if (tag != null) {
                if (listOf(*tag.techList).contains("android.nfc.tech.IsoDep")) {
                    if (((passportNumber == "") && !passportNumber.isEmpty()
                                && (expirationDate == "") && !expirationDate.isEmpty()
                                && (birthDate == "") && !birthDate.isEmpty())
                    ) {
                        val bacKey: BACKeySpec = BACKey(passportNumber, birthDate, expirationDate)
//                       ReadTask(IsoDep.get(tag), bacKey).execute()

                    } else {
                        Log.d("onPostExecute", "onPostExecute444")
                    }
                } else {
                    Log.d("onPostExecute", "onPostExecute333")
                }
            }
        } else {
            Log.d("onPostExecute", "onPostExecute222")
        }
    }

    private class ReadTask private constructor(
        private val isoDep: IsoDep,
        private val bacKey: BACKeySpec,
        val mContext: Context
    ) : AsyncTask<Void?, Void?, Exception?>() {
        var eDocument: EDocument = EDocument()
        var personDetails: PersonDetails = PersonDetails()
        var additionalPersonDetails: AdditionalPersonDetails = AdditionalPersonDetails()
        val dateUtil = DateUtil()
        val imageUtil = ImageUtil()
        override fun doInBackground(vararg params: Void?): Exception? {
            try {
                val cardService: CardService = CardService.getInstance(isoDep)
                cardService.open()
                val service = PassportService(
                    cardService, PassportService.NORMAL_MAX_TRANCEIVE_LENGTH,
                    PassportService.DEFAULT_MAX_BLOCKSIZE, true, false
                )
                service.open()
                var paceSucceeded = false
                try {
                    val cardSecurityFile =
                        CardSecurityFile(service.getInputStream(PassportService.EF_CARD_SECURITY))
                    val securityInfoCollection: Collection<SecurityInfo> =
                        cardSecurityFile.securityInfos

                    for (securityInfo: SecurityInfo? in securityInfoCollection) {
                        if (securityInfo is PACEInfo) {
                            val paceInfo: PACEInfo = securityInfo
                            service.doPACE(
                                bacKey,
                                paceInfo.objectIdentifier,
                                PACEInfo.toParameterSpec(paceInfo.parameterId),
                                null
                            )
                            paceSucceeded = true
                        }
                    }
                } catch (e: Exception) {
                    Log.w("TAG", e)
                }
                service.sendSelectApplet(paceSucceeded)
                if (!paceSucceeded) {
                    try {
                        service.getInputStream(PassportService.EF_COM).read()
                    } catch (e: java.lang.Exception) {
                        service.doBAC(bacKey)
                    }
                }

                // -- Personal Details -- //
                val dg1In: CardFileInputStream = service.getInputStream(PassportService.EF_DG1)
                val dg1File: DG1File = DG1File(dg1In)
                val mrzInfo: MRZInfo = dg1File.mrzInfo
                personDetails.name =
                    mrzInfo.secondaryIdentifier.replace("<", " ").trim { it <= ' ' }
                personDetails.surname =
                    mrzInfo.primaryIdentifier.replace("<", " ").trim { it <= ' ' }

                personDetails.personalNumber = mrzInfo.personalNumber
                personDetails.gender = mrzInfo.gender.toString()
                personDetails.birthDate = dateUtil.convertFromMrzDate(mrzInfo.dateOfBirth)
                personDetails.expiryDate = dateUtil.convertFromMrzDate(mrzInfo.dateOfExpiry)
                personDetails.serialNumber = mrzInfo.documentNumber
                personDetails.nationality = mrzInfo.nationality
                personDetails.issuerAuthority = mrzInfo.issuingState

                Log.d(
                    "MRZInfoMRZInfo",
                    "name : " + mrzInfo.secondaryIdentifier.replace("<", " ").trim { it <= ' ' })


                // -- Face Image -- //
                val dg2In: CardFileInputStream = service.getInputStream(PassportService.EF_DG2)
                val dg2File = DG2File(dg2In)
                val faceInfos: List<FaceInfo> = dg2File.faceInfos
                val allFaceImageInfos: MutableList<FaceImageInfo> = ArrayList()
                for (faceInfo: FaceInfo in faceInfos) {
                    allFaceImageInfos.addAll(faceInfo.faceImageInfos)
                }
                if (allFaceImageInfos.isNotEmpty()) {
                    val faceImageInfo: FaceImageInfo = allFaceImageInfos.iterator().next()
                    val image: Image = imageUtil.getImage(mContext, faceImageInfo)
                    personDetails.faceImage = image.bitmapImage
                    personDetails.faceImageBase64 = image.base64Image
                }

                // -- Fingerprint (if exist)-- //
                try {
                    val dg3In: CardFileInputStream = service.getInputStream(PassportService.EF_DG3)
                    val dg3File = DG3File(dg3In)
                    val fingerInfos = dg3File.fingerInfos
                    val allFingerImageInfos: MutableList<FingerImageInfo> = ArrayList()
                    for (fingerInfo in fingerInfos) {
                        allFingerImageInfos.addAll(fingerInfo.fingerImageInfos)
                    }
                    val fingerprintsImage: MutableList<Bitmap> = ArrayList()
                    if (allFingerImageInfos.isNotEmpty()) {
                        for (fingerImageInfo in allFingerImageInfos) {
                            val image: Image = imageUtil.getImage(mContext, fingerImageInfo)
                            fingerprintsImage.add(image.bitmapImage)
                        }
                        personDetails.fingerprints = fingerprintsImage
                    }
                } catch (e: java.lang.Exception) {
                    Log.w("TAG", e)
                }

                // -- Portrait Picture -- //
                try {
                    val dg5In: CardFileInputStream = service.getInputStream(PassportService.EF_DG5)
                    val dg5File = DG5File(dg5In)
                    val displayedImageInfos = dg5File.images
                    if (displayedImageInfos.isNotEmpty()) {
                        val displayedImageInfo: DisplayedImageInfo =
                            displayedImageInfos.iterator().next()
                        val image = imageUtil.getImage(mContext, displayedImageInfo)
                        personDetails.portraitImage = image.bitmapImage
                        personDetails.portraitImageBase64 = image.base64Image
                    }
                } catch (e: java.lang.Exception) {
                    Log.w("TAG", e)
                }

//                // -- Signature (if exist) -- //
                try {
                    val dg7In: CardFileInputStream = service.getInputStream(PassportService.EF_DG7)
                    val dg7File = DG7File(dg7In)
                    val signatureImageInfos = dg7File.images
                    if (signatureImageInfos.isNotEmpty()) {
                        val displayedImageInfo: DisplayedImageInfo =
                            signatureImageInfos.iterator().next()
                        val image = imageUtil.getImage(mContext, displayedImageInfo)
                        personDetails.portraitImage = image.bitmapImage
                        personDetails.portraitImageBase64 = image.base64Image
                    }
                } catch (e: java.lang.Exception) {
                    Log.w("TAG", e)
                }

                // -- Additional Details (if exist) -- //
                try {
                    val dg11In = service.getInputStream(PassportService.EF_DG11)
                    val dg11File = DG11File(dg11In)
                    if (dg11File.length > 0) {
                        additionalPersonDetails.custodyInformation = dg11File.custodyInformation
                        additionalPersonDetails.nameOfHolder = dg11File.nameOfHolder
                        additionalPersonDetails.fullDateOfBirth = dg11File.fullDateOfBirth
                        additionalPersonDetails.otherNames = dg11File.otherNames
                        additionalPersonDetails.otherValidTDNumbers = dg11File.otherValidTDNumbers
                        additionalPersonDetails.permanentAddress = dg11File.permanentAddress
                        additionalPersonDetails.personalNumber = dg11File.personalNumber
                        additionalPersonDetails.personalSummary = dg11File.personalSummary
                        additionalPersonDetails.placeOfBirth = dg11File.placeOfBirth
                        additionalPersonDetails.profession = dg11File.profession
                        additionalPersonDetails.proofOfCitizenship = dg11File.proofOfCitizenship
                        additionalPersonDetails.tag = dg11File.tag
                        additionalPersonDetails.tagPresenceList = dg11File.tagPresenceList
                        additionalPersonDetails.telephone = dg11File.telephone
                        additionalPersonDetails.title = dg11File.title
                    }
                } catch (e: java.lang.Exception) {
                    Log.w("TAG", e)
                }

                eDocument.setPersonDetails(personDetails)
                eDocument.setAdditionalPersonDetails(additionalPersonDetails)
            } catch (e: java.lang.Exception) {
                return e
            }
            return null
        }

        override fun onPostExecute(exception: Exception?) {
            Log.d("onPostExecute", "onPostExecute555")
            if (exception == null) {
                val intent = Intent(mContext, ResultActivity::class.java)
                intent.putExtra("EDOCUMENT", eDocument)
                mContext.startActivity(intent)
            } else {
                Log.d("onPostExecute", exception.toString())
            }
        }

    }
}