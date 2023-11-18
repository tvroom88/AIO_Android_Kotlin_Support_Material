package w2022v9o12.simple.camerax_analysis

import android.content.Context
import android.content.res.Configuration
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.camera.core.CameraSelector
import com.google.mlkit.vision.text.Text
import kotlin.math.ceil

class GraphicOverlay(context: Context?, attrs: AttributeSet?) :
    View(context, attrs) {

    private val lock = Any()
    private val paint = Paint()
    private val customTexts: MutableList<CustomText> = ArrayList()

    private var mScale: Float? = null
    private var mOffsetX: Float? = null
    private var mOffsetY: Float? = null
    private var cameraSelector: Int = CameraSelector.LENS_FACING_BACK

    //    private lateinit var overlay:GraphicOverlay
    private val ROUND_RECT_CORNER:Float

    init {
        paint.color = Color.BLACK
        paint.textSize = 54.0f
        ROUND_RECT_CORNER = 1F
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        for (customText in customTexts) {

            customText.customTextBlock.boundingBox?.let { box ->
                val rect = calculateRect(
                    customText.imageRect.height().toFloat(),
                    customText.imageRect.width().toFloat(),
                    box
                )
//                canvas.drawRoundRect(rect, ROUND_RECT_CORNER, ROUND_RECT_CORNER, paint)
                canvas.drawText(
                    customText.customTextBlock.text,
                    rect.left,
                    rect.bottom,
                    paint
                )

            }
        }
    }

    private fun calculateRect(height: Float, width: Float, boundingBoxT: Rect): RectF {

        // for land scape
        fun isLandScapeMode(): Boolean {
            return this.context.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        }

        fun whenLandScapeModeWidth(): Float {
            return when (isLandScapeMode()) {
                true -> width
                false -> height
            }
        }

        fun whenLandScapeModeHeight(): Float {
            return when (isLandScapeMode()) {
                true -> height
                false -> width
            }
        }

        val scaleX = this.width.toFloat() / whenLandScapeModeWidth()
        val scaleY = this.height.toFloat() / whenLandScapeModeHeight()
        val scale = scaleX.coerceAtLeast(scaleY)
        this.mScale = scale

        // Calculate offset (we need to center the overlay on the target)
        val offsetX = (this.width.toFloat() - ceil(whenLandScapeModeWidth() * scale)) / 2.0f
        val offsetY = (this.height.toFloat() - ceil(whenLandScapeModeHeight() * scale)) / 2.0f

        this.mOffsetX = offsetX
        this.mOffsetY = offsetY

        val mappedBox = RectF().apply {
            left = boundingBoxT.right * scale + offsetX
            top = boundingBoxT.top * scale + offsetY
            right = boundingBoxT.left * scale + offsetX
            bottom = boundingBoxT.bottom * scale + offsetY
        }

        // for front mode
        if (this.isFrontMode()) {
            val centerX = this.width.toFloat() / 2
            mappedBox.apply {
                left = centerX + (centerX - left)
                right = centerX - (right - centerX)
            }
        }
        return mappedBox
    }


    private fun isFrontMode() = cameraSelector == CameraSelector.LENS_FACING_FRONT

    fun clear() {
        synchronized(lock) { customTexts.clear() }
        postInvalidate()
    }

    fun add(customText: CustomText) {
        synchronized(lock) { customTexts.add(customText) }
    }
    class CustomText constructor(var customTextBlock: Text.TextBlock, var imageRect: Rect)
}