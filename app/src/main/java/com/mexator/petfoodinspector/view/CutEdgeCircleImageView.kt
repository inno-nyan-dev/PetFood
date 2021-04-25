package com.mexator.petfoodinspector.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet


class CutEdgeCircleImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : androidx.appcompat.widget.AppCompatImageView(context, attrs, defStyleAttr) {

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

        val smallR: Float = bigR * 0.85F

        val cutPath = Path()
        cutPath.addCircle(
            centerX - bigR,
            centerY - bigR,
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