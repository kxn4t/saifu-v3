package com.saifu.saifu_v3.handler

import org.apache.http.HttpResponse
import org.apache.http.client.ClientProtocolException
import org.apache.http.client.ResponseHandler
import org.apache.http.util.EntityUtils

/**
 * @see <a href="https://hc.apache.org/httpcomponents-client-ga/httpclient/examples/org/apache/http/examples/client/ClientWithResponseHandler.java">
 *     Apache HttpClient
 *     </a>
 */
class APIResponseHandler : ResponseHandler<String> {
    override fun handleResponse(response: HttpResponse?): String? {
        val status = response?.statusLine?.statusCode
        return if (status != null) {
            if (status in 200..299) {
                val entity = response.entity
                entity?.let { EntityUtils.toString(entity) }
            } else {
                throw ClientProtocolException("Unexpected response status: $status")
            }
        } else {
            null
        }
    }
}
