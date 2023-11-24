package w2022v9o12.simple.camerax_analysis

import android.app.Application
import android.graphics.Bitmap
import org.spongycastle.jce.provider.BouncyCastleProvider
import java.security.Security

class MainApplication : Application() {

    var mBitmap: Bitmap? = null
    override fun onCreate() {
        super.onCreate()
//        Security.insertProviderAt(BouncyCastleProvider(), 1)
    }
}
