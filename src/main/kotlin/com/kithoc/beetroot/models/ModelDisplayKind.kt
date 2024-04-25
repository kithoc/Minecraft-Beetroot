package com.kithoc.beetroot.models

data class ModelDisplayKind(val name: String) {
    @Suppress("SpellCheckingInspection")
    companion object {
        val ThirdPersonRightHand = ModelDisplayKind("thirdperson_righthand")
        val ThirdPersonLeftHand = ModelDisplayKind("thirdperson_lefthand")
        val FirstPersonRightHand = ModelDisplayKind("firstperson_righthand")
        val FirstPersonLeftHand = ModelDisplayKind("firstperson_lefthand")
        val GUI = ModelDisplayKind("gui")
        val Head = ModelDisplayKind("head")
        val Ground = ModelDisplayKind("ground")
        val Fixed = ModelDisplayKind("fixed")
    }
}
