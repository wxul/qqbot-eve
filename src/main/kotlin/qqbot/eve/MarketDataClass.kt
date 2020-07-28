package qqbot.eve

/**
 * 请求的搜索物品项，用于json
 */
data class HttpGoodModel(val typeid: String = "",
                         val typename: String = "")

/**
 * 请求的价格项，用于json
 */
data class HttpGoodModelList(val results: List<HttpGoodModel>)

/**
 * 请求的价格项，用于json
 */
data class HttpPriceItem(val max: Double = 0.0, val min: Double = 0.0, val volume: Long = 0)

/**
 * 请求的价格项，用于json
 */
data class HttpPriceObject(val all: HttpPriceItem?, val buy: HttpPriceItem?, val sell: HttpPriceItem?)

/**
 * 价格项
 */
data class PriceItem(val name: String,
                     var maxBuy: Double = 0.0,
                     var minSell: Double = 0.0,
                     var buyVolumn: Long = 0)