package qqbot.eve

import kotlinx.coroutines.*
import net.mamoe.mirai.Bot
import net.mamoe.mirai.alsoLogin
import net.mamoe.mirai.join
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.contact.Friend
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.event.subscribeMessages

//import net.mamoe.mirai.event.GroupMessageEvent

class Robot constructor(var robotConfig: ConfigProperty) {
    var config: ConfigProperty = robotConfig
    var miraiBot: Bot = Bot(this.config.QQID, this.config.QQPWD) {
        // 覆盖默认的配置
        fileBasedDeviceInfo("device.json") // 使用 "device.json" 保存设备信息
        // networkLoggerSupplier = { SilentLogger } // 禁用网络层输出
    }

    init {
        println("user id: ${this.config.QQID}")
    }

    suspend fun listen() {
        this.miraiBot.alsoLogin()
        this.message()
        this.miraiBot.join()
    }

    fun message() {
        // eve查价
        val eve_price_prefix = this.config.PREFIX + this.config.EVE_PRICE_SEARCH + " "
        // roll点
        val roll = this.config.PREFIX + this.config.ROLL + " "
        val market = Market()
        // 监听这个 bot 的来自所有群和好友的消息
        this.miraiBot.subscribeMessages {
            // 当接收到消息 == "你好" 时就回复 "你好!"
//            "你好" reply "你好!"

            // 当消息 == "查看 subject" 时, 执行 lambda
//            case("查看 subject") {
//                when (subject) {
//                    is Friend -> {
//                        reply("消息主体为 Friend，你在发私聊消息")
//                    }
//                    is Group -> {
//                        reply("消息主体为 Group，你在群里发消息")
//                    }
//                    is Member -> {
//                        reply("消息主体为 Member，你在发临时消息")
//                    }
//                }
//
//                // 在回复的时候, 一般使用 subject 来作为回复对象.
//                // 因为当群消息时, subject 为这个群.
//                // 当好友消息时, subject 为这个好友.
//                // 所有在 MessagePacket(也就是此时的 this 指代的对象) 中实现的扩展方法, 如刚刚的 "reply", 都是以 subject 作为目标
//            }


            // 当消息里面包含这个类型的消息时
//            has<Image> {
//                // this: MessagePacket
//                // message: MessageChain
//                // sender: QQ
//                // it: String (MessageChain.toString)
//
//
//                // message[Image].download() // 还未支持 download
//                if (this is GroupMessageEvent) {
//                    //如果是群消息
//                    // group: Group
//                    this.group.sendMessage("你在一个群里")
//                    // 等同于 reply("你在一个群里")
//                }
//
//                reply("图片, ID= ${message[Image]}")//获取第一个 Image 类型的消息
//                reply(message)
//            }

//            Regex("hello.*world") matchingReply {
//                "Hello!"
//            }
//
//            Regex("aa") matching {
//                reply("asdfasdasfasda")
//                println(message.toString())
//            }

            startsWith(eve_price_prefix, removePrefix = true) {
                // it: 删除了消息前缀 "我是" 后的消息
                // 如一条消息为 "我是张三", 则此时的 it 为 "张三".

//                reply("你是$it")
                val result = market.query("$it")
                if (result != null) {
                    reply(result)
                } else {
                    reply("没有查到物品: $it")
                }
            }

            startsWith(roll, removePrefix = true) {
                when (subject) {
                    is Group -> {
                        println(subject.toString())
                        reply("${this.senderName}掷出了${(0..100).random()}点(0-100)")
                    }
                }
            }

            atBot {
                val message: String = "使用帮助：\n" +
                        "输入 ${eve_price_prefix} 三钛 查询价格\n" +
                        "输入 ${roll} 进行无聊的roll点看人品"
                reply(message)
            }

//            "123" containsReply "你的消息里面包含 123"


            // 当收到 "我的qq" 就执行 lambda 并回复 lambda 的返回值 String
//            "我的qq" reply { sender.id }

//            "at all" reply AtAll // at 全体成员

            // 如果是这个 QQ 号发送的消息(可以是好友消息也可以是群消息)
//            sentBy(123456789) {
//            }


            // 当消息前缀为 "我是" 时
//            startsWith("我是", removePrefix = true) {
//                // it: 删除了消息前缀 "我是" 后的消息
//                // 如一条消息为 "我是张三", 则此时的 it 为 "张三".
//
//                reply("你是$it")
//            }


            // listener 管理

//            var repeaterListener: CompletableJob? = null
//            contains("开启复读") {
//                repeaterListener?.complete()
//                bot.subscribeGroupMessages {
//                    repeaterListener = contains("复读") {
//                        reply(message)
//                    }
//                }
//
//            }

//            contains("关闭复读") {
//                if (repeaterListener?.complete() == null) {
//                    reply("没有开启复读")
//                } else {
//                    reply("成功关闭复读")
//                }
//            }


//            case("上传好友图片") {
//                val filename = it.substringAfter("上传好友图片")
//                File("C:\\Users\\Him18\\Desktop\\$filename").sendAsImageTo(subject)
//            }
//
//            case("上传群图片") {
//                val filename = it.substringAfter("上传好友图片")
//                File("C:\\Users\\Him18\\Desktop\\$filename").sendAsImageTo(subject)
//            }
        }


    }
}