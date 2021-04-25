package com.mexator.petfoodinspector.view

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.util.AttributeSet
import com.mexator.petfoodinspector.R


class CutEdgeCircleImageView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
) : androidx.appcompat.widget.AppCompatImageView(context, attributeSet, defStyleAttr) {

    companion object {
        private const val DEFAULT_CUT_SIZE = 0.9F
        private const val DEFAULT_CUT_OFFSET = 0F
    }

    /**
     * Cut size scale factor
     */
    private val cutSize: Float

    /**
     * Cut offset in view
     */
    private val cutOffset: Float

    init {
        // Apply attributes
        val attrs: TypedArray = context.obtainStyledAttributes(
            attributeSet,
            R.styleable.CutEdgeCircleImageView,
            defStyleAttr,
            defStyleRes
        )

        val smallRadius =
            attrs.getFloat(
                R.styleable.CutEdgeCircleImageView_cutSize,
                DEFAULT_CUT_SIZE
            )
        cutSize = smallRadius

        cutOffset = attrs.getDimension(
            R.styleable.CutEdgeCircleImageView_cutOffset,
            DEFAULT_CUT_OFFSET
        )
        attrs.recycle()
    }

    private val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.RED
        style = Paint.Style.FILL_AND_STROKE
    }

    private val path = Path()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val side = minOf(w, h)

        val centerX = width / 2F
        val centerY = height / 2F
        val bigR = side / 2F

        val circlePath = Path()
        circlePath.addCircle(
            centerX,
            centerY,
            bigR,
            Path.Direction.CCW
        )

        val smallR: Float = bigR * cutSize

        val cutPath = Path()
        cutPath.addCircle(
            centerX - bigR - cutOffset,
            centerY - bigR - cutOffset,
            smallR,
            Path.Direction.CCW
        )
        path.op(circlePath, cutPath, Path.Op.DIFFERENCE)

        // Bitmap shader magic!!
        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        bitmapCanvas = Canvas(bitmap)
        val shader = BitmapShader(
            bitmap,
            Shader.TileMode.REPEAT, Shader.TileMode.REPEAT
        )
        paint.shader = shader
    }

    private lateinit var bitmapCanvas: Canvas
    private lateinit var bitmap: Bitmap

    override fun onDraw(canvas: Canvas) {
        super.onDraw(bitmapCanvas)

        canvas.drawPath(path, paint)
    }
}