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

    //NFC 진동이 작동되는거 자체를 막음
    fun stopNFCReader(mActivity: Activity) {
        if (adapter != null)
            adapter.enableReaderMode(mActivity, null, NfcAdapter.STATE_TURNING_OFF, null);
    }
}
