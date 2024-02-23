package io.github.kmichaelk.unnandroid.models.source

import com.google.gson.annotations.SerializedName
import io.github.kmichaelk.unnandroid.models.sourceS.SourceSubjectRef

data class SourceSubjectData(

    @SerializedName("files")
    val files: List<SourceSubjectRef.File>?,

    @SerializedName("links")
    val links: List<SourceSubjectRef.Link>?
)
