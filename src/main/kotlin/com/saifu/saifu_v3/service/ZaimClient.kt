package com.saifu.saifu_v3.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.saifu.saifu_v3.config.OAuthConfig
import com.saifu.saifu_v3.handler.APIResponseHandler
import com.saifu.saifu_v3.model.*
import com.saifu.saifu_v3.type.MoneyConditionType
import oauth.signpost.OAuthConsumer
import oauth.signpost.OAuthProvider
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClients
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI

@Component
class ZaimClient(
        private val objectMapper: ObjectMapper,
        private val oAuthConfig: OAuthConfig,
        private val oAuthConsumer: OAuthConsumer,
        private val oAuthProvider: OAuthProvider,
        private val userDetails: UserDetails
) {

    companion object {
        const val API_USER_URL = "https://api.zaim.net/v2/home/user/verify"
        const val API_MONEY_URL = "https://api.zaim.net/v2/home/money"
    }

    private val httpClient = HttpClients.createDefault()
    private val responseHandler = APIResponseHandler()

    // --- OAuth ---
    // step 1 : get request token & redirect to zaim authentication page
    fun retrieveRequestToken(): String {
        return oAuthProvider.retrieveRequestToken(oAuthConsumer, oAuthConfig.CALLBACK_URL)
    }

    // step 2 : receive oauth_verifier & generate tokens
    fun retrieveAccessToken(oauthVerifier: String?) {
        // generate tokens
        oAuthProvider.retrieveAccessToken(oAuthConsumer, oauthVerifier)
        // set tokens to userDetails
        userDetails.apply {
            accessToken = oAuthConsumer.token
            accessTokenSecret = oAuthConsumer.tokenSecret
        }
    }

    // --- zaim operations ---
    fun getUserVerify(): User {
        val uri = URI(API_USER_URL)
        val userJson = sendGetRequest(uri)
        return objectMapper.readValue<Verify>(userJson).user
    }

    fun getMoneyList(queryParameters: Map<MoneyConditionType, String> = mapOf()): List<Money> {

        val uri = UriComponentsBuilder.fromHttpUrl(API_MONEY_URL).also {
            queryParameters.forEach { (k, v) -> it.queryParam(k.key, v) }
        }.build().toUri()

        val moneyJson = sendGetRequest(uri)
        return objectMapper.readValue<MoneyResponse>(moneyJson).moneyList
    }

    private fun sendGetRequest(uri: URI): String {
        val httpGet = HttpGet(uri)

        oAuthConsumer.setTokenWithSecret(userDetails.accessToken, userDetails.accessTokenSecret)
        oAuthConsumer.sign(httpGet)

        return httpClient.execute(httpGet, responseHandler)
    }
}
