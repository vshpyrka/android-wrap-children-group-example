package com.example.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.core.view.children

class WrapChildLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ViewGroup(context, attrs, defStyleAttr, defStyleRes) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        var curWidth = 0
        var rows = 1
        children.forEach { child ->
            measureChild(
                child,
                widthMeasureSpec,
                heightMeasureSpec
            )
            val layoutParams = child.layoutParams as MarginLayoutParams
            val childWidthWithMargins =
                layoutParams.marginStart + child.measuredWidth + layoutParams.marginEnd
            if (curWidth + childWidthWithMargins >= width) {
                curWidth = childWidthWithMargins
                rows++
            } else {
                curWidth += childWidthWithMargins
            }
        }

        val firstChild = getChildAt(0)
        val layoutParams = firstChild.layoutParams as MarginLayoutParams
        val topMargin = layoutParams.topMargin
        val bottomMargin = layoutParams.bottomMargin
        val height = (firstChild.measuredHeight + topMargin + bottomMargin) * rows

        setMeasuredDimension(width, height)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var currentLeft = 0
        var currentTop = 0
        children.forEach { child ->

            val childWidth = child.measuredWidth
            val childHeight = child.measuredHeight
            val layoutParams = child.layoutParams as MarginLayoutParams

            val childWidthWithMargins =
                layoutParams.marginStart + child.measuredWidth + layoutParams.marginEnd
            val childHeightWithMargins = layoutParams.topMargin + child.measuredHeight + layoutParams.bottomMargin

            if (currentLeft + childWidthWithMargins >= width) {
                currentLeft = 0
                val left = currentLeft + layoutParams.marginStart
                val right = currentLeft + layoutParams.marginStart + childWidth
                val top = currentTop + childHeightWithMargins + layoutParams.topMargin
                val bottom = currentTop + childHeightWithMargins + layoutParams.topMargin + childHeight

                child.layout(
                    left,
                    top,
                    right,
                    bottom
                )
                currentLeft += childWidthWithMargins
                currentTop += childHeightWithMargins

            } else {
                val left = currentLeft + layoutParams.marginStart
                val top = currentTop + layoutParams.topMargin
                val right = currentLeft + layoutParams.marginStart + childWidth
                val bottom = currentTop + childHeight + layoutParams.topMargin
                child.layout(
                    left,
                    top,
                    right,
                    bottom
                )
                currentLeft += childWidthWithMargins
            }
        }
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }

    override fun generateDefaultLayoutParams(): LayoutParams {
        return MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
    }

    override fun generateLayoutParams(p: LayoutParams?): LayoutParams {
        return MarginLayoutParams(p)
    }

    override fun checkLayoutParams(p: LayoutParams?): Boolean {
        return p is MarginLayoutParams
    }

    override fun dispatchDraw(canvas: Canvas) {
        canvas.drawColor(Color.DKGRAY)
        super.dispatchDraw(canvas)
    }
}
