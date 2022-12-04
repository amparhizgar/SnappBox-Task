package ir.amirhparhizgar.snappboxtask.ui.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.withStyledAttributes
import ir.amirhparhizgar.snappboxtask.R
import java.lang.Integer.max


/**
 * Created by AmirHossein Parhizgar on 12/4/2022.
 */
@Suppress("MemberVisibilityCanBePrivate")
class AcceptButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(
    context,
    attrs,
    defStyleAttr
) {

    companion object {
        const val DEFAULT_BUTTON_COLOR: Int = Color.BLACK
        const val DEFAULT_PROGRESS_COLOR: Int = Color.BLUE
        const val DEFAULT_CORNER_Radius: Float = 80f
        const val DEFAULT_ACCEPT_DELAY: Long = 2_000L
        const val DEFAULT_TIMEOUT: Long = 30_000L
    }

    private var startTimeoutMillis: Long = System.currentTimeMillis()
    private var startPressMillis: Long = -1
    private var onClickCalled = false
    private var onTimeOutCalled = false
    private var clipPath = Path()

    var timeOutMillis: Long = DEFAULT_TIMEOUT
        set(value) {
            field = value
            onTimeOutCalled = false
            startTimeoutMillis = System.currentTimeMillis()
        }
    var acceptDelayMillis = DEFAULT_ACCEPT_DELAY

    val buttonPaint = Paint().apply {
        color = buttonColor
        isAntiAlias = true
    }
    val progressPaint = Paint().apply {
        color = progressColor
        isAntiAlias = true
    }

    @ColorInt
    var buttonColor: Int = DEFAULT_BUTTON_COLOR
        set(value) {
            field = value
            buttonPaint.color = value
            invalidate()
        }

    @ColorInt
    var progressColor: Int = DEFAULT_PROGRESS_COLOR
        set(value) {
            field = value
            progressPaint.color = value
            invalidate()
        }

    var cornerRadius: Float = DEFAULT_CORNER_Radius
        set(value) {
            field = value
            invalidate()
        }

    var onTimeOut: (() -> Unit)? = null

    init {
        super.setBackground(null)
        textAlignment = TEXT_ALIGNMENT_CENTER
        gravity = Gravity.CENTER

        context.withStyledAttributes(attrs, R.styleable.AcceptButton) {
            buttonColor =
                getColor(R.styleable.AcceptButton_buttonColor, DEFAULT_BUTTON_COLOR)
            progressColor =
                getColor(R.styleable.AcceptButton_buttonProgressColor, DEFAULT_PROGRESS_COLOR)
            cornerRadius =
                getDimension(R.styleable.AcceptButton_buttonCornerRadius, DEFAULT_CORNER_Radius)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val viewWidth = w.toFloat()
        val viewHeight = h.toFloat()
        clipPath = Path().apply {
            moveTo(cornerRadius, 0F)
            lineTo(viewWidth - cornerRadius, 0F)
            arcTo(viewWidth - 2 * cornerRadius, 0F, viewWidth, 2 * cornerRadius, -90F, 90F, false)
            lineTo(viewWidth, cornerRadius)
            arcTo(
                viewWidth - 2 * cornerRadius,
                viewHeight - 2 * cornerRadius,
                viewWidth,
                viewHeight,
                0F,
                90F,
                false
            )
            lineTo(cornerRadius, viewHeight)
            arcTo(0F, viewHeight - 2 * cornerRadius, 2 * cornerRadius, viewHeight, 90F, 90F, false)
            lineTo(0F, cornerRadius)
            arcTo(0F, 0F, 2 * cornerRadius, 2 * cornerRadius, 180F, 90F, false)

            close()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startPressMillis = System.currentTimeMillis()
                onClickCalled = false
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                startPressMillis = -1
                invalidate()
            }
        }
        return true
    }

    override fun onDraw(canvas: Canvas) {
        canvas.save()
        canvas.clipPath(clipPath)
        drawBackground(canvas)
        if (startPressMillis == -1L)
            drawTimeOut(canvas)
        else
            drawCircle(canvas)
        canvas.restore()
        super.onDraw(canvas)
    }

    private fun drawTimeOut(canvas: Canvas) {
        val spentTime = System.currentTimeMillis() - startTimeoutMillis
        val progress: Float = spentTime.toFloat() / timeOutMillis
        canvas.drawRect(
            0f,
            0f,
            measuredWidth * progress,
            measuredHeight.toFloat(),
            progressPaint
        )
        if (spentTime < timeOutMillis)
            invalidate()
        else {
            if (!onTimeOutCalled) {
                onTimeOut?.invoke()
                onTimeOutCalled = true
            }
        }
    }

    private fun drawCircle(canvas: Canvas) {
        val pressingTime = System.currentTimeMillis() - startPressMillis
        val progress: Float = pressingTime.toFloat() / acceptDelayMillis
        val maxRadius =
            max(measuredHeight, measuredWidth).toFloat() / 2
        canvas.drawCircle(
            measuredWidth.toFloat() / 2,
            measuredHeight.toFloat() / 2,
            maxRadius * progress,
            progressPaint
        )
        if (pressingTime < acceptDelayMillis)
            invalidate()
        else {
            if (!onClickCalled) {
                callOnClick()
                onClickCalled = true
            }
        }
    }

    private fun drawBackground(canvas: Canvas) {
        canvas.drawRect(
            0f,
            0f,
            measuredWidth.toFloat(),
            measuredHeight.toFloat(),
            buttonPaint
        )
    }
}