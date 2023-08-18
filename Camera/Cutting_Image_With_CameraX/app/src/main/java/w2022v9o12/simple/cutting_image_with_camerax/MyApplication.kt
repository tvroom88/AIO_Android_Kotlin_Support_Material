package w2022v9o12.simple.cutting_image_with_camerax

import android.app.Application
import android.graphics.Bitmap

class MyApplication : Application() {

    var bitmap: Bitmap? = null;

    fun setBitmap1(mBitmap: Bitmap) {
        bitmap = mBitmap
    }
}