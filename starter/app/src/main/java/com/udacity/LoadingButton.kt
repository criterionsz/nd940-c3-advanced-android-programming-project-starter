package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 55.0f
        typeface = Typeface.create("", Typeface.BOLD)
    }
    private val labelDownload = context.getString(R.string.button_title_download)
    private val labelLoading = context.getString(R.string.button_title_loading)
    private var label = labelDownload
    private val pointPosition: PointF = PointF(0.0f, 0.0f)
    private var downloadColor = 0
    private var loadingColor = 0
    private var progress = 0F
    private var rectf = RectF()

    private val valueAnimator = ValueAnimator.ofFloat(0F, 1F).apply {
        addUpdateListener {
            progress = animatedValue as Float
            invalidate()
        }
    }

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
        when (new) {
            ButtonState.Clicked -> {
                isEnabled = false
            }
            ButtonState.Completed -> {
                isEnabled = true
                label = labelDownload
                valueAnimator.cancel()
            }
            ButtonState.Loading -> {
                isEnabled = false
                valueAnimator.duration = 1000L
                valueAnimator.start()
                label = labelLoading
            }
        }
        invalidate()
    }

    init {
        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            downloadColor = getColor(R.styleable.LoadingButton_download, 0)
            loadingColor = getColor(R.styleable.LoadingButton_loading, 0)
        }

        isClickable = true
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        paint.color = downloadColor
        canvas?.drawRect(0F, 0F, widthSize.toFloat(), heightSize.toFloat(), paint)
        if (buttonState == ButtonState.Loading) {
            paint.color = loadingColor
            canvas?.drawRect(0f, 0f, progress  * widthSize, heightSize.toFloat(), paint)
            paint.color = Color.YELLOW
            canvas?.drawArc(rectf, 0f, progress * 360f, true, paint)
        }
        pointPosition.computeXYForTextButton()
        paint.color = Color.WHITE
        canvas?.drawText(label, pointPosition.x, pointPosition.y, paint)

    }

    private fun PointF.computeXYForTextButton() {
        x = (widthSize / 2).toFloat()
        y = (heightSize / 2).toFloat() + 15
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
        rectf = RectF(widthSize - 200f, heightSize / 2 - 20f, widthSize.toFloat() - 150f, heightSize / 2 + 15f)
    }

    fun setStateButton(buttonState: ButtonState) {
        this.buttonState = buttonState
    }

}