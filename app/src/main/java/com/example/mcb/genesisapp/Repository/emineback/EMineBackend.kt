package com.example.mcb.genesisapp.Repository.emineback

import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.fuel.httpPost
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.doAsync
import org.json.JSONObject


/**
 * Created by abertschi on 15.02.18.
 */
class EMineBackend : AnkoLogger {

    val client = OkHttpClient()
    val host = "http://10.2.99.8:5000"
    val json_type = MediaType.parse("application/json; charset=utf-8")

    init {
    }

    fun main(args: Array<String>?) {
        doAsync {
            println("creating request !!!")
            var b = EMineBackend()
            var p = CreateTokenRequest("tokeName", "symbol", 100,
                    1, 1, TokenType.BASIC_TOKEN)
            b.createTokenObservable(p).subscribe({ response ->
                print(response)
            }, { error ->
                print(error.message)
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
                    .put("tokenType", payload.tokenType.name)


            RequestBody.create(json_type, json.toString())
            val request = Request.Builder()
                    .url(url)
                    .post(body)
                    .addHeader("Authorization", "header value") //Notice this request has header if you don't need to send a header just erase this part
                    .build()


                    "${host}/api/tokens/create".httpPost().body(json.toString())
                    .responseJson { request, response, result ->
                        val (data, error) = result
                        println(data)
                        println(error)
                        if (response.statusCode != 200) {
                            val msg = result.get().obj()["error"].toString()
//                            sink.onError()
                        }

                        sink.onNext(createTokenResponse(result.get().obj()))
                        sink.onComplete()
                    }
        }.observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun fetchRequestStatus(requestKey: String) {

    }

    fun createTokenResponse(json: JSONObject): CreateTokenResponse {
        return CreateTokenResponse(
                created = json.getString("created"),
                key = json.getString("key"),
                status = json.getString("status"),
                tokenAbi = json.getString("token_abi"),
                tokenAddr = json.getString("token_addr"),
                updated = json.getString("updated"),
                version = json.getInt("version"))
    }

    enum class TokenType(name: String) {
        BASIC_TOKEN("MyBasicToken")
    }

    data class CreateTokenRequest(
            var tokenName: String,
            var tokenSymbol: String,
            var maxSupply: Int,
            var decimals: Int,
            var genesisSupply: Int,
            var tokenType: TokenType
    )

    data class CreateTokenResponse(var status: String,
                                   var created: String,
                                   var key: String,
                                   var tokenAddr: String,
                                   var tokenAbi: String,
                                   var updated: String,
                                   var version: Int) {}
}