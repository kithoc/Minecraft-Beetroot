package com.kithoc.beetroot.models

import java.util.concurrent.CompletableFuture

interface ModelIO {
    fun loadModel(modelName: String): CompletableFuture<Model>
}