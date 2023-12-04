package w2022v9o12.simple.camerax_analysis

import android.app.Activity
import android.app.Application
import android.graphics.Bitmap
import android.nfc.NfcAdapter


class MainApplication : Application() {
    private lateinit var mBitmap: Bitmap
    private lateinit var adapter: NfcAdapter

    override fun onCreate() {
        super.onCreate()
        adapter = NfcAdapter.getDefaultAdapter(this)
    }

    fun setBitmap(bitmap: Bitmap) {
        this.mBitmap = bitmap
    }

    fun getBitmap(): Bitmap {
        return mBitmap
    }

    fun getNFCAdapter(): NfcAdapter {
        return adapter
    }

    fun enableStopNFCReader(mActivity: Activity) {
        if (adapter != null)
            adapter.enableReaderMode(mActivity, null, NfcAdapter.STATE_TURNING_OFF, null)
    }

    fun disableStopNFCReader(mActivity: Activity) {
        if (adapter != null)
            adapter.disableReaderMode(mActivity)
    }
}
