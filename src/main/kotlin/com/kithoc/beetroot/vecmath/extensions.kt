package com.kithoc.beetroot.vecmath

infix fun Int.to(other: Int) = Vec2i(this, other)
infix fun Float.to(other: Float) = Vec2f(this, other)
infix fun Vec2f.to(other: Float) = Vec3f(this.x, this.y, other)

operator fun Vec2i.rangeTo(other: Vec2i) =
    Rectangle4i.fromTo(this, other)
operator fun Vec2f.rangeTo(other: Vec2f) =
    Rectangle4f.fromTo(this, other)
operator fun Vec3f.rangeTo(other: Vec3f) =
    Cuboid6f.fromTo(this, other)

