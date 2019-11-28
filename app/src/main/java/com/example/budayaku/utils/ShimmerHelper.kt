package com.example.budayaku.utils

import android.view.View
import com.facebook.shimmer.ShimmerFrameLayout

object ShimmerHelper {
    fun StopShimmer(shimmer: ShimmerFrameLayout) {
        shimmer.stopShimmer()
        shimmer.clearAnimation()
        shimmer.visibility = View.GONE
    }
}