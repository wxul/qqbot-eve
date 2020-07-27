package qqbot.eve

import com.github.kittinunf.fuel.*
import com.github.kittinunf.fuel.core.*
import com.github.kittinunf.result.Result
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import java.text.DecimalFormat

data class HttpGoodModel(val typeid: String = "",
                         val typename: String = "")

data class HttpGoodModelList(val results: List<HttpGoodModel>)

data class HttpPriceItem(val max: Double = 0.0, val min: Double = 0.0, val volume: Long = 0)

data class HttpPriceObject(val all: HttpPriceItem?, val buy: HttpPriceItem?, val sell: HttpPriceItem?)

data class PriceItem(val name: String,
                     var maxBuy: Double = 0.0,
                     var minSell: Double = 0.0,
                     var buyVolumn: Long = 0)

class Market {
    val prefix: String = "https://www.ceve-market.org/api"
    val nameSearch: String = "/searchname"
    val regionId = "10000002"
    val jitaId = "30000142"
    val marketSearch: String = "/market/region/{星域ID}/system/{星系ID}/type/{物品ID}.json"

    fun makeMarketUrl(id: String): String {
        return this.marketSearch.replace("{星域ID}", this.regionId).replace("{星系ID}", this.jitaId).replace("{物品ID}", id)
    }

    fun searchName(name: String): String? {
        val url: String = this.prefix + this.nameSearch
        val (request, response, result) = url
                .httpPost()
                .header(Headers.CONTENT_TYPE, "application/x-www-form-urlencoded")
                .body("name=${name}")
                .response()

        val (data, error) = result
        if (error == null) {
            return String(response.data)
        } else {
            return null
        }
    }

    fun searchNameToObj(name: String): ArrayList<HttpGoodModel>? {
        val res = this.searchName(name)
        if (res == null) {
            return null
        } else {
            val listType = object : TypeToken<ArrayList<HttpGoodModel>>() {}.type
            var goods: ArrayList<HttpGoodModel> = Gson().fromJson(res, listType)
            return goods
        }
    }

    fun searchPrice(id: String): String? {
        val url = this.prefix + this.makeMarketUrl(id)
        println(url)
        val (request, response, result) = url
                .httpGet()
                .response()

        val (data, error) = result
        if (error == null) {
            return String(response.data)
        } else {
            return null
        }
    }

    fun searchPriceToObject(id: String): HttpPriceObject? {
        val res: String? = this.searchPrice(id)
        if (res == null) {
            return null
        } else {
            val listType = object : TypeToken<HttpPriceObject>() {}.type
            val price: HttpPriceObject = Gson().fromJson(res, listType)
            return price
        }
    }

    fun searchPrices(goods: ArrayList<HttpGoodModel>): ArrayList<PriceItem> {
        var prices: ArrayList<PriceItem> = ArrayList<PriceItem>()
        for (good in goods) {
            val price = this.searchPriceToObject(good.typeid)
            if (price != null) {
                prices.add(PriceItem(good.typename, price.buy!!.max, price.sell!!.min, price.buy!!.volume))
            }
        }
        return prices
    }

    fun formatMessage(prices: ArrayList<PriceItem>): String? {
        if (prices.size == 0) return null
        var str = "名称\t\t最低出售\t\t最高收购\t\t收单数\r\n"
        val df1 = DecimalFormat("##,##0.00")
        for (p in prices) {
            str += "${p.name}:\t${df1.format(p.minSell)}\t${df1.format(p.maxBuy)}\t${p.buyVolumn}\r\n"
        }
        return str
    }

    fun query(name: String): String? {
        var res = this.searchNameToObj(name)
        if (res == null) {
            return null
        }
        return this.formatMessage(this.searchPrices(res))
    }
}