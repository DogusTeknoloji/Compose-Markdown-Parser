package com.berkkanb.composemarkdown



enum class MarkdownTag(val regex: String,val tokenCharLength: Int){
    BOLD("\\*\\*",2)
}

class MarkdownHandler{
    val markdownPlaceList = mutableListOf<MarkdownPlace>()
    val usefulMarkdownTokenList = mutableListOf<UsefulMarkdownToken>()
    val boldRegex = Regex(MarkdownTag.BOLD.regex)

    fun createTransformedText(markdownText:String): MutableList<UsefulMarkdownToken> {
        val match = boldRegex.findAll(markdownText)
        match.forEach {
            markdownPlaceList.add(MarkdownPlace(it.range,MarkdownTag.BOLD))
        }
        return usefulTransformedText(markdownPlaceList)
    }

    fun usefulTransformedText(markdownPlaceList:MutableList<MarkdownPlace>): MutableList<UsefulMarkdownToken> {
        var startInd = 0
        var endInd = 0
        markdownPlaceList.forEachIndexed { index, markdownToken ->
            if (index % 2 == 0){
                startInd = markdownToken.range.first
            }else {
                endInd = markdownToken.range.last
                usefulMarkdownTokenList.add(UsefulMarkdownToken(startInd,endInd,markdownToken.tokenTag))
            }
        }
        return usefulMarkdownTokenList
    }

}


data class MarkdownPlace(
    val range: IntRange,
    val tokenTag:MarkdownTag,
)
data class UsefulMarkdownToken(
    val start: Int,
    val end: Int,
    val tokenTag:MarkdownTag,
)