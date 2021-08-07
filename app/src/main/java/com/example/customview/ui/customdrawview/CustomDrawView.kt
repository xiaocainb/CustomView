package com.example.customview.ui.customdrawview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.withRotation
import androidx.core.graphics.withTranslation
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.example.customview.R
import kotlinx.coroutines.*
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class CustomDrawView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr), LifecycleObserver {
    private var mWidth = 0f
    private var mHeight = 0f
    private var mRadius = 0f
    private var mAngle = 10f
    private var rotatingJob: Job? = null
    private var sinWaveSamplesPath = Path()
    private val solidLinePaint = Paint().apply {
        style = Paint.Style.STROKE//填充
        strokeWidth = 5f
        color = ContextCompat.getColor(context, R.color.chunjie)//有预览
    }
    private val vectorLinePaint = Paint().apply {
        style = Paint.Style.STROKE//填充
        strokeWidth = 5f
        color = ContextCompat.getColor(context, R.color.design_default_color_surface)//有预览
    }
    private val textPaint = Paint().apply {
        textSize = 50f
        typeface = Typeface.DEFAULT_BOLD//粗体
        color = ContextCompat.getColor(context, R.color.chunjie)
    }
    private var dashedLinePaint = Paint().apply {
        style = Paint.Style.STROKE
        pathEffect = DashPathEffect(floatArrayOf(10f, 10f), 0f)
        color = ContextCompat.getColor(context, R.color.design_default_color_primary)
        strokeWidth = 5f
    }
    private val filledCirclePaint = Paint().apply {
        style = Paint.Style.FILL
        color = ContextCompat.getColor(context, R.color.chunjie)
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w.toFloat()
        mHeight = h.toFloat()
        mRadius = if (w < h / 2) w / 2.toFloat() else h / 4.toFloat()
        mRadius -= 20f
    }

    //  避免在draw中创建对象
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas.apply {
            this?.let { drawAxises(it) }
            this?.let { drawLable(it) }
            this?.let { drawDashedCircle(it) }
            this?.let { drawVector(it) }
            this?.let { drawProjections(it) }
            this?.let { drawSinWave(it) }
        }
    }

    //  坐标轴
    private fun drawAxises(canvas: Canvas) {
//      移动画布
        canvas.withTranslation(mWidth / 2, mHeight / 2) {
            drawLine(-mWidth / 2, 0f, mWidth / 2, 0f, solidLinePaint)
            drawLine(0f, -mHeight / 2, 0f, mHeight / 2, solidLinePaint)
        }
        canvas.withTranslation(mWidth / 2, mHeight / 4 * 3) {
            drawLine(-mWidth / 2, 0f, mWidth / 2, 0f, solidLinePaint)
        }
    }

    //  文本框
    private fun drawLable(canvas: Canvas) {
        canvas.apply {
//          画文本框
            drawRect(100f, 100f, 600f, 250f, solidLinePaint)
//          画文字
            drawText("指数函数和旋转矢量", 120f, 195f, textPaint)
        }
    }

    //  圆
    private fun drawDashedCircle(canvas: Canvas) {
        canvas.withTranslation(mWidth / 2, mHeight / 4 * 3) {
            drawCircle(0f, 0f, mRadius, dashedLinePaint)
        }
    }

    //  矢量
    private fun drawVector(canvas: Canvas) {
        canvas.withTranslation(mWidth / 2, mHeight / 4 * 3) {
            withRotation(-mAngle) {
                drawLine(0f, 0f, mRadius, 0f, vectorLinePaint)
            }
        }
    }

    //  投影
    private fun drawProjections(canvas: Canvas) {
        canvas.withTranslation(mWidth / 2, mHeight / 2) {
            drawCircle(mRadius * cos(mAngle.toRadians()), 0f, 10f, filledCirclePaint)
        }
        canvas.withTranslation(mWidth / 2, mHeight / 4 * 3) {
            drawCircle(mRadius * cos(mAngle.toRadians()), 0f, 10f, filledCirclePaint)
        }
        canvas.withTranslation(mWidth / 2, mHeight / 2) {
            val x = mRadius * cos(mAngle.toRadians())
            val y = mRadius * sin(mAngle.toRadians())
            withTranslation(x, -y) {
                drawLine(0f, 0f, 0f, y, solidLinePaint)
                drawLine(0f, 0f, -mHeight / 4 + y, y, dashedLinePaint)
            }
        }
    }

    //  sin图像
    private fun drawSinWave(canvas: Canvas) {
        canvas.withTranslation(mWidth / 2, mHeight / 2) {
//          这个数量相当于sin的周期，越大周期越短
            val samplesCount = 50
            val dy = mHeight / 2 / samplesCount
            sinWaveSamplesPath.reset()
            sinWaveSamplesPath.moveTo(mRadius * cos(mAngle.toRadians()), 0f)//起点
            repeat(samplesCount) {
                val x = mRadius * cos(it * -0.15 + mAngle.toRadians())
                val y = -dy * it
                sinWaveSamplesPath.quadTo(x.toFloat(), y, x.toFloat(), y)
            }
            drawPath(sinWaveSamplesPath, vectorLinePaint)
            drawTextOnPath("mnls", sinWaveSamplesPath, 1000f, 0f, textPaint)
        }
    }

    //  旋转
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun startRotating() {
        rotatingJob = CoroutineScope(Dispatchers.Main).launch {
            while (true) {
                delay(100)
                mAngle += 5f
                invalidate()
            }
        }
    }

    //  旋转暂停
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun pauseRotating() {
        rotatingJob?.cancel()
    }

    //  角度转弧度
    fun Float.toRadians() = this / 180 * PI.toFloat()
}