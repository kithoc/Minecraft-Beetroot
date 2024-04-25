package com.kithoc.beetroot.models

import com.kithoc.beetroot.vecmath.Cuboid6f
import com.kithoc.beetroot.vecmath.Direction
import com.kithoc.beetroot.vecmath.Vec2f
import com.kithoc.beetroot.vecmath.Vec3f

data class FaceDimension(
    val direction: Direction,
    val position: Vec3f,
    val size: Vec2f,
) {
    val bounds: Cuboid6f
        get() = Cuboid6f.fromSize(position, Vec3f.ZERO.with(direction.axis.u, size.x).with(direction.axis.v, size.y))

    companion object {
        operator fun invoke(direction: Direction, bounds: Cuboid6f) =
            if (bounds.min[direction.axis] != bounds.max[direction.axis])
                throw IllegalArgumentException()
            else
                FaceDimension(
                    direction,
                    bounds.min,
                    Vec2f(bounds.size[direction.axis.u], bounds.size[direction.axis.v])
                )
    }
}