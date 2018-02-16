package com.example.mcb.genesisapp.Repository.emineback

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.doAsync
import org.json.JSONObject
import java.io.IOException


/**
 * Created by abertschi on 15.02.18.
 */
class EMineBackend : AnkoLogger {

    val client = OkHttpClient()
    val host = "http://10.2.99.8:5000"
    val json_type = MediaType.parse("application/json; charset=utf-8")

    init {
    }

    fun getRequestInformationUrl(key: String): String {
        return "${host}/api/requests/${key}"
    }

    fun main(args: Array<String>?) {
        doAsync {
            println("creating request !!!")
            var b = EMineBackend()
            var p = CreateTokenRequest("tokeName", "symbol", 100,
                    1, 1, "MyStandardToken")
            b.createTokenObservable(p).subscribe({ response ->
                fetchRequestStatus(response.key).subscribe(
                        { r ->
                            println(r)
                        }, { e -> }
                )
                println("on done")
                print(response)
            }, { error ->
                println("on error")
                println(error.message)
            }
            )
        }
    }


    fun createTokenObservable(payload: CreateTokenRequest): io.reactivex.Observable<CreateTokenResponse> {
        return Observable.create<CreateTokenResponse> { sink ->
            val json = JSONObject()
            json.put("tokenName", payload.tokenName)
                    .put("tokenSymbol", payload.tokenSymbol)
                    .put("decimals", payload.decimals)
                    .put("maxSupply", payload.maxSupply)
                    .put("genesisSupply", payload.genesisSupply)
                    .put("tokenType", payload.tokenType)
                    .put("symbol", payload.tokenSymbol)

            println(json.toString())
            client.newCall(Request.Builder()
                    .url("${host}/api/tokens/create")
                    .post(RequestBody.create(json_type, json.toString()))
                    .build()).enqueue(object : Callback {
                override fun onResponse(call: Call?, response: Response?) {
                    if (response!!.code() == 200) {
                        sink.onNext(createTokenResponse(JSONObject(response!!.body()!!.string())))
                        sink.onComplete()
                    } else {
                        val msg = response!!.body()!!.string()
                        if (msg.contains("error")) {
                            sink.onError(Exception(JSONObject(msg).getString("error")))

                        } else {
                            sink.onError(Exception(msg))
                        }
                        sink.onComplete()
                    }
                }

                override fun onFailure(call: Call?, e: IOException?) {
                    val msg = call!!.request()?.body() ?: e!!.message as String
                    var e = Exception()
                    sink.onError(e)
                    sink.onComplete()
                }
            })

//            todo: http fuel throws exception on http != 400, not caught, how to catch error?
//
//            "${host}/api/tokens/create".httpPost().body(json.toString())
//                    .responseJson { request, response, result ->
//                        val (data, error) = result
//                        if (response.statusCode != 200) {
//                            val msg = result.get().obj()["error"].toString()
////                            sink.onError(Exce)
//                        } else {
//                            sink.onNext(createTokenResponse(result.get().obj()))
//                            sink.onComplete()
//                        }
//                    }
        }.observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun fetchRequestStatus(requestKey: String): io.reactivex.Observable<CreateTokenResponse> {
        return Observable.create<CreateTokenResponse> { sink ->
            client.newCall(Request.Builder()
                    .url("${host}/api/requests/${requestKey}")
                    .get()
                    .build()).enqueue(object : Callback {
                override fun onResponse(call: Call?, response: Response?) {
                    if (response!!.code() == 200) {
                        sink.onNext(createTokenResponse(JSONObject(response!!.body()!!.string())))
                        sink.onComplete()
                    } else {
                        val msg = response!!.body()!!.string()
                        if (msg.contains("error")) {
                            sink.onError(Exception(JSONObject(msg).getString("error")))

                        } else {
                            sink.onError(Exception(msg))
                        }
                        sink.onComplete()
                    }
                }

                override fun onFailure(call: Call?, e: IOException?) {
                    val msg = call!!.request()?.body() ?: e!!.message as String
                    var e = Exception()
                    sink.onError(e)
                    sink.onComplete()
                }
            })
        }
    }

    fun createTokenResponse(json: JSONObject): CreateTokenResponse {
        println(json.toString())
        return CreateTokenResponse(
                created = json.getString("created"),
                key = json.getString("key"),
                status = json.getString("status"),
                tokenAbi = json.getString("token_abi"),
                tokenAddr = json.getString("token_addr"),
                updated = json.getString("updated"),
                version = json.getInt("version"))
    }

    enum class TokenType(val tokenName: String) {
        BASIC_TOKEN("MyStandardToken")
    }

    data class CreateTokenRequest(
            var tokenName: String,
            var tokenSymbol: String,
            var maxSupply: Int,
            var decimals: Int,
            var genesisSupply: Int,
            var tokenType: String
    )

    data class CreateTokenResponse(var status: String,
                                   var created: String,
                                   var key: String,
                                   var tokenAddr: String,
                                   var tokenAbi: String,
                                   var updated: String,
                                   var version: Int) {}
}