package w2022v9o12.simple.camerax_analysis.activities

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
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
import org.jmrtd.lds.PACEInfo
import org.jmrtd.lds.SecurityInfo
import org.jmrtd.lds.icao.DG1File
import org.jmrtd.lds.icao.MRZInfo
import w2022v9o12.simple.camerax_analysis.MainApplication
import w2022v9o12.simple.camerax_analysis.R
import w2022v9o12.simple.camerax_analysis.model.AdditionalPersonDetails
import w2022v9o12.simple.camerax_analysis.model.EDocument
import w2022v9o12.simple.camerax_analysis.model.PersonDetails

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

    private class ReadTask private constructor(private val isoDep: IsoDep, private val bacKey: BACKeySpec, val mContext:Context) : AsyncTask<Void?, Void?, Exception?>() {
        var eDocument: EDocument = EDocument()
        var personDetails: PersonDetails = PersonDetails()
        var additionalPersonDetails: AdditionalPersonDetails = AdditionalPersonDetails()
        override fun doInBackground(vararg params: Void?): Exception? {
            try {
                val cardService: CardService = CardService.getInstance(isoDep)
                cardService.open()
                val service = PassportService(cardService, PassportService.NORMAL_MAX_TRANCEIVE_LENGTH,
                    PassportService.DEFAULT_MAX_BLOCKSIZE, true, false)
                service.open()
                var paceSucceeded = false
                try {
                    val cardSecurityFile = CardSecurityFile(service.getInputStream(PassportService.EF_CARD_SECURITY))
                    val securityInfoCollection: Collection<SecurityInfo> = cardSecurityFile.securityInfos

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
                personDetails.name= mrzInfo.secondaryIdentifier.replace("<", " ").trim { it <= ' ' }
                personDetails.surname = mrzInfo.primaryIdentifier.replace("<", " ").trim { it <= ' ' }

//                personDetails.setPersonalNumber(mrzInfo.getPersonalNumber())
//                personDetails.setGender(mrzInfo.getGender().toString())
//                personDetails.setBirthDate(DateUtil.convertFromMrzDate(mrzInfo.getDateOfBirth()))
//                personDetails.setExpiryDate(DateUtil.convertFromMrzDate(mrzInfo.getDateOfExpiry()))
//                personDetails.setSerialNumber(mrzInfo.getDocumentNumber())
//                personDetails.setNationality(mrzInfo.getNationality())
//                personDetails.setIssuerAuthority(mrzInfo.getIssuingState())

                Log.d("MRZInfoMRZInfo", "name : " + mrzInfo.secondaryIdentifier.replace("<", " ").trim { it <= ' ' })
//                if (("I" == mrzInfo.documentCode)) {
//                    docType = DocType.ID_CARD
//                } else if (("P" == mrzInfo.getDocumentCode())) {
//                    docType = DocType.PASSPORT
//                }

//                // -- Face Image -- //
//                val dg2In: CardFileInputStream = service.getInputStream(PassportService.EF_DG2)
//                val dg2File = DG2File(dg2In)
//                val faceInfos: List<FaceInfo> = dg2File.faceInfos
//                val allFaceImageInfos: MutableList<FaceImageInfo> = ArrayList()
//                for (faceInfo: FaceInfo in faceInfos) {
//                    allFaceImageInfos.addAll(faceInfo.faceImageInfos)
//                }
//                if (allFaceImageInfos.isNotEmpty()) {
//                    val faceImageInfo: FaceImageInfo = allFaceImageInfos.iterator().next()
//                    val image: Image = ImageUtil.getImage(this@NfcScanActivity, faceImageInfo)
//                    personDetails.setFaceImage(image.getBitmapImage())
//                    //                    personDetails.setFaceImageBase64(image.getBase64Image());
//                }
//
//                // -- Fingerprint (if exist)-- //
//                try {
//                    val dg3In: CardFileInputStream = service.getInputStream(PassportService.EF_DG3)
//                    val dg3File: DG3File = DG3File(dg3In)
//                    val fingerInfos: kotlin.collections.List<FingerInfo> = dg3File.getFingerInfos()
//                    val allFingerImageInfos: kotlin.collections.MutableList<FingerImageInfo> =
//                        java.util.ArrayList<FingerImageInfo>()
//                    for (fingerInfo: FingerInfo in fingerInfos) {
//                        allFingerImageInfos.addAll(fingerInfo.getFingerImageInfos())
//                    }
//                    val fingerprintsImage: kotlin.collections.MutableList<android.graphics.Bitmap> =
//                        java.util.ArrayList<android.graphics.Bitmap>()
//                    if (!allFingerImageInfos.isEmpty()) {
//                        for (fingerImageInfo: FingerImageInfo? in allFingerImageInfos) {
//                            val image: Image =
//                                ImageUtil.getImage(this@NfcScanActivity, fingerImageInfo)
//                            fingerprintsImage.add(image.getBitmapImage())
//                        }
//
////                        personDetails.setFingerprints(fingerprintsImage);
//                    }
//                } catch (e: java.lang.Exception) {
//                    android.util.Log.w(TAG, e)
//                }
//
//                // -- Portrait Picture -- //
//                try {
//                    val dg5In: CardFileInputStream = service.getInputStream(PassportService.EF_DG5)
//                    val dg5File: DG5File = DG5File(dg5In)
//                    val displayedImageInfos: kotlin.collections.List<DisplayedImageInfo> =
//                        dg5File.getImages()
//                    if (!displayedImageInfos.isEmpty()) {
//                        val displayedImageInfo: DisplayedImageInfo =
//                            displayedImageInfos.iterator().next()
//                        val image: Image =
//                            ImageUtil.getImage(this@NfcScanActivity, displayedImageInfo)
//                        //                        personDetails.setPortraitImage(image.getBitmapImage());
////                        personDetails.setPortraitImageBase64(image.getBase64Image());
//                    }
//                } catch (e: java.lang.Exception) {
//                    android.util.Log.w(TAG, e)
//                }
//
//                // -- Signature (if exist) -- //
//                try {
//                    val dg7In: CardFileInputStream = service.getInputStream(PassportService.EF_DG7)
//                    val dg7File: DG7File = DG7File(dg7In)
//                    val signatureImageInfos: kotlin.collections.List<DisplayedImageInfo> =
//                        dg7File.getImages()
//                    if (!signatureImageInfos.isEmpty()) {
//                        val displayedImageInfo: DisplayedImageInfo =
//                            signatureImageInfos.iterator().next()
//                        val image: Image =
//                            ImageUtil.getImage(this@NfcScanActivity, displayedImageInfo)
//                        //                        personDetails.setPortraitImage(image.getBitmapImage());
////                        personDetails.setPortraitImageBase64(image.getBase64Image());
//                    }
//                } catch (e: java.lang.Exception) {
//                    android.util.Log.w(TAG, e)
//                }
//
//                // -- Additional Details (if exist) -- //
//                try {
//                    val dg11In: CardFileInputStream =
//                        service.getInputStream(PassportService.EF_DG11)
//                    val dg11File: DG11File = DG11File(dg11In)
//                    if (dg11File.getLength() > 0) {
//                        additionalPersonDetails.setCustodyInformation(dg11File.getCustodyInformation())
//                        additionalPersonDetails.setNameOfHolder(dg11File.getNameOfHolder())
//                        additionalPersonDetails.setFullDateOfBirth(dg11File.getFullDateOfBirth())
//                        additionalPersonDetails.setOtherNames(dg11File.getOtherNames())
//                        additionalPersonDetails.setOtherValidTDNumbers(dg11File.getOtherValidTDNumbers())
//                        additionalPersonDetails.setPermanentAddress(dg11File.getPermanentAddress())
//                        additionalPersonDetails.setPersonalNumber(dg11File.getPersonalNumber())
//                        additionalPersonDetails.setPersonalSummary(dg11File.getPersonalSummary())
//                        additionalPersonDetails.setPlaceOfBirth(dg11File.getPlaceOfBirth())
//                        additionalPersonDetails.setProfession(dg11File.getProfession())
//                        additionalPersonDetails.setProofOfCitizenship(dg11File.getProofOfCitizenship())
//                        additionalPersonDetails.setTag(dg11File.getTag())
//                        additionalPersonDetails.setTagPresenceList(dg11File.getTagPresenceList())
//                        additionalPersonDetails.setTelephone(dg11File.getTelephone())
//                        additionalPersonDetails.setTitle(dg11File.getTitle())
//                    }
//                } catch (e: java.lang.Exception) {
//                    android.util.Log.w(TAG, e)
//                }
//
//                // -- Document Public Key -- //
//                try {
//                    val dg15In: CardFileInputStream =
//                        service.getInputStream(PassportService.EF_DG15)
//                    val dg15File: DG15File = DG15File(dg15In)
//                    val publicKey: java.security.PublicKey = dg15File.getPublicKey()
//                    //                    eDocument.setDocPublicKey(publicKey);
//                } catch (e: java.lang.Exception) {
//                    android.util.Log.w(TAG, e)
//                }

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
//                Snackbar.make(mainLayout, exception.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();
                android.util.Log.d("onPostExecute", exception.toString())
            }
        }


    }
}