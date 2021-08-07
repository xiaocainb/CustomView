package com.example.customview.ui.custombubblesurfaceview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CustomBubbleSurfaceView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : SurfaceView(context, attrs, defStyleAttr) {
    private val colors = arrayOf(
        Color.GRAY,
        Color.BLACK,
        Color.BLUE,
        Color.GREEN,
        Color.RED,
        Color.YELLOW,
        Color.WHITE
    )
    private var paint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 5f
        pathEffect = ComposePathEffect(CornerPathEffect(15f), DiscretePathEffect(15f, 10f))
    }
    private val bubbleList = mutableListOf<Bubble>()

    private data class Bubble(val x: Float, val y: Float, val color: Int, var radius: Float)

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val x = event?.x ?: 0f
        val y = event?.y ?: 0f
        val color = colors.random()
        val bubble = Bubble(x, y, color, 1f)
        bubbleList.add(bubble)
        if (bubbleList.size > 2202) bubbleList.removeAt(0)
        return super.onTouchEvent(event)
    }

    init {
        CoroutineScope(Dispatchers.Default).launch {
            while (true) {
                if (holder.surface.isValid) {
                    val canvas = holder.lockCanvas()
                    canvas.drawColor(Color.BLACK)
                    bubbleList.toList().filter { it.radius < 1000 }.forEach {
                        paint.color = it.color
                        canvas.drawCircle(it.x, it.y, it.radius, paint)
                        it.radius += 10f
                    }
                    holder.unlockCanvasAndPost(canvas)
                }
            }
        }
    }
}