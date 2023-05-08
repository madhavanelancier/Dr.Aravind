package com.elancier.meenatchi_CRM.retrofit

import android.graphics.Bitmap
import android.graphics.Bitmap.Config
import android.graphics.BitmapFactory
import android.graphics.BitmapFactory.Options
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect

import java.net.HttpURLConnection
import java.net.URL


object ScalingUtilities {


    fun decodeFile(path: String, dstWidth: Int, dstHeight: Int, scalingLogic: ScalingLogic): Bitmap {
        val options = Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(path, options)
        options.inJustDecodeBounds = false
        options.inSampleSize =
            calculateSampleSize(
                options.outWidth,
                options.outHeight,
                dstWidth,
                dstHeight,
                scalingLogic
            )

        return BitmapFactory.decodeFile(path, options)
    }


    fun createScaledBitmap(unscaledBitmap: Bitmap, dstWidth: Int, dstHeight: Int,
                           scalingLogic: ScalingLogic
    ): Bitmap {
        val srcRect = calculateSrcRect(
            unscaledBitmap.width, unscaledBitmap.height,
            dstWidth, dstHeight, scalingLogic
        )
        val dstRect = calculateDstRect(
            unscaledBitmap.width, unscaledBitmap.height,
            dstWidth, dstHeight, scalingLogic
        )
        val scaledBitmap = Bitmap.createBitmap(dstRect.width(), dstRect.height(),
                Config.ARGB_8888)
        val canvas = Canvas(scaledBitmap)
        canvas.drawBitmap(unscaledBitmap, srcRect, dstRect, Paint(Paint.FILTER_BITMAP_FLAG))

        return scaledBitmap
    }

    enum class ScalingLogic {
        CROP, FIT
    }

    fun calculateSampleSize(srcWidth: Int, srcHeight: Int, dstWidth: Int, dstHeight: Int,
                            scalingLogic: ScalingLogic
    ): Int {
        if (scalingLogic == ScalingUtilities.ScalingLogic.FIT) {
            val srcAspect = srcWidth.toFloat() / srcHeight.toFloat()
            val dstAspect = dstWidth.toFloat() / dstHeight.toFloat()

            return if (srcAspect > dstAspect) {
                srcWidth / dstWidth
            } else {
                srcHeight / dstHeight
            }
        } else {
            val srcAspect = srcWidth.toFloat() / srcHeight.toFloat()
            val dstAspect = dstWidth.toFloat() / dstHeight.toFloat()

            return if (srcAspect > dstAspect) {
                srcHeight / dstHeight
            } else {
                srcWidth / dstWidth
            }
        }
    }

    fun calculateSrcRect(srcWidth: Int, srcHeight: Int, dstWidth: Int, dstHeight: Int,
                         scalingLogic: ScalingLogic
    ): Rect {
        if (scalingLogic == ScalingUtilities.ScalingLogic.CROP) {
            val srcAspect = srcWidth.toFloat() / srcHeight.toFloat()
            val dstAspect = dstWidth.toFloat() / dstHeight.toFloat()

            if (srcAspect > dstAspect) {
                val srcRectWidth = (srcHeight * dstAspect).toInt()
                val srcRectLeft = (srcWidth - srcRectWidth) / 2
                return Rect(srcRectLeft, 0, srcRectLeft + srcRectWidth, srcHeight)
            } else {
                val srcRectHeight = (srcWidth / dstAspect).toInt()
                val scrRectTop = (srcHeight - srcRectHeight) / 2
                return Rect(0, scrRectTop, srcWidth, scrRectTop + srcRectHeight)
            }
        } else {
            return Rect(0, 0, srcWidth, srcHeight)
        }
    }


    fun calculateDstRect(srcWidth: Int, srcHeight: Int, dstWidth: Int, dstHeight: Int,
                         scalingLogic: ScalingLogic
    ): Rect {
        if (scalingLogic == ScalingUtilities.ScalingLogic.FIT) {
            val srcAspect = srcWidth.toFloat() / srcHeight.toFloat()
            val dstAspect = dstWidth.toFloat() / dstHeight.toFloat()

            return if (srcAspect > dstAspect) {
                Rect(0, 0, dstWidth, (dstWidth / srcAspect).toInt())
            } else {
                Rect(0, 0, (dstHeight * srcAspect).toInt(), dstHeight)
            }
        } else {
            return Rect(0, 0, dstWidth, dstHeight)
        }
    }

    fun getImage(ur: String, mode: Int, dstW: Int, dstH: Int): Bitmap? {
        try {
            if (mode == 2) {
                val url = URL(ur)
                val connection = url.openConnection() as HttpURLConnection
                connection.doInput = true
                connection.connect()
                val options = Options()
                options.inJustDecodeBounds = true
                val input = connection.inputStream
                BitmapFactory.decodeStream(input, null, options)
                options.inJustDecodeBounds = false
                options.inSampleSize =
                    calculateSampleSize(
                        options.outWidth, options.outHeight, dstW,
                        dstH, ScalingUtilities.ScalingLogic.FIT
                    )
                val bitmap = BitmapFactory.decodeStream(input, null, options)
                return if (!(bitmap!!.width <= dstW && bitmap
                                .height <= dstH)) {
                    createScaledBitmap(
                        bitmap, dstW, dstH,
                        ScalingUtilities.ScalingLogic.FIT
                    )
                } else {
                    bitmap
                }
            } else {
                val options = Options()
                options.inJustDecodeBounds = true
                BitmapFactory.decodeFile(ur, options)
                options.inJustDecodeBounds = false
                options.inSampleSize =
                    calculateSampleSize(
                        options.outWidth, options.outHeight, dstW,
                        dstH, ScalingUtilities.ScalingLogic.FIT
                    )
                val bitmap = BitmapFactory.decodeFile(ur, options)
                return if (!(bitmap.width <= dstW && bitmap.height <= dstH)) {
                    createScaledBitmap(
                        bitmap, dstW, dstH,
                        ScalingUtilities.ScalingLogic.FIT
                    )
                } else {
                    bitmap
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

    }

    fun getCropedBitmap(bm: Bitmap, dstW: Int, dstH: Int): Bitmap? {
        try {
            return if (!(bm.width <= dstW && bm.height <= dstH)) {
                createScaledBitmap(
                    bm,
                    dstW,
                    dstH,
                    ScalingUtilities.ScalingLogic.FIT
                )
            } else {
                bm
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

    }


    fun getBlurredBitmap(original: Bitmap, radius: Int): Bitmap? {
        if (radius < 1)
            return null

        val width = original.width
        val height = original.height
        val wm = width - 1
        val hm = height - 1
        val wh = width * height
        val div = radius + radius + 1
        val r = IntArray(wh)
        val g = IntArray(wh)
        val b = IntArray(wh)
        var rsum: Int
        var gsum: Int
        var bsum: Int
        var x: Int
        var y: Int
        var i: Int
        var p: Int
        var p1: Int
        var p2: Int
        var yp: Int
        var yi: Int
        var yw: Int
        val vmin = IntArray(Math.max(width, height))
        val vmax = IntArray(Math.max(width, height))
        val dv = IntArray(256 * div)
        i = 0
        while (i < 256 * div) {
            dv[i] = i / div
            i++
        }

        val blurredBitmap = IntArray(wh)
        original.getPixels(blurredBitmap, 0, width, 0, 0, width, height)

        yw = 0
        yi = 0

        y = 0
        while (y < height) {
            rsum = 0
            gsum = 0
            bsum = 0
            i = -radius
            while (i <= radius) {
                p = blurredBitmap[yi + Math.min(wm, Math.max(i, 0))]
                rsum += p and 0xff0000 shr 16
                gsum += p and 0x00ff00 shr 8
                bsum += p and 0x0000ff
                i++
            }
            x = 0
            while (x < width) {
                r[yi] = dv[rsum]
                g[yi] = dv[gsum]
                b[yi] = dv[bsum]

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm)
                    vmax[x] = Math.max(x - radius, 0)
                }
                p1 = blurredBitmap[yw + vmin[x]]
                p2 = blurredBitmap[yw + vmax[x]]

                rsum += (p1 and 0xff0000) - (p2 and 0xff0000) shr 16
                gsum += (p1 and 0x00ff00) - (p2 and 0x00ff00) shr 8
                bsum += (p1 and 0x0000ff) - (p2 and 0x0000ff)
                yi++
                x++
            }
            yw += width
            y++
        }

        x = 0
        while (x < width) {
            bsum = 0
            gsum = bsum
            rsum = gsum
            yp = -radius * width
            i = -radius
            while (i <= radius) {
                yi = Math.max(0, yp) + x
                rsum += r[yi]
                gsum += g[yi]
                bsum += b[yi]
                yp += width
                i++
            }
            yi = x
            y = 0
            while (y < height) {
                blurredBitmap[yi] = -0x1000000 or (dv[rsum] shl 16) or (dv[gsum] shl 8) or dv[bsum]
                if (x == 0) {
                    vmin[y] = Math.min(y + radius + 1, hm) * width
                    vmax[y] = Math.max(y - radius, 0) * width
                }
                p1 = x + vmin[y]
                p2 = x + vmax[y]

                rsum += r[p1] - r[p2]
                gsum += g[p1] - g[p2]
                bsum += b[p1] - b[p2]

                yi += width
                y++
            }
            x++
        }

        return Bitmap.createBitmap(blurredBitmap, width, height, Config.RGB_565)
    }

}
