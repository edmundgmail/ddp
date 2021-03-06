package com.ddp.rest

/**
  * Created by cloudera on 10/17/16.
  */

import spray.http.HttpHeaders._
import spray.http.HttpMethods._
import spray.http.{AllOrigins, HttpMethod, HttpMethods, HttpResponse}
import spray.routing._

// see also https://developer.mozilla.org/en-US/docs/Web/HTTP/Access_control_CORS
trait CorsSupport {
  this: HttpService =>

  private val allowOriginHeader = `Access-Control-Allow-Origin`(AllOrigins)
  private val optionsCorsHeaders = List(
    `Access-Control-Allow-Headers`("Origin, X-Requested-With, Content-Type, Accept, Accept-Encoding, Accept-Language, Host, Referer, User-Agent"),
    `Access-Control-Max-Age`(1728000))

  def cors[T]: Directive0 = mapRequestContext { ctx => ctx.withRouteResponseHandling({
    //It is an option requeset for a resource that responds to some other method
    case Rejected(x) if (ctx.request.method.equals(HttpMethods.OPTIONS) && !x.filter(_.isInstanceOf[MethodRejection]).isEmpty) => {
      val allowedMethods: List[HttpMethod] = x.filter(_.isInstanceOf[MethodRejection]).map(rejection=> {
        rejection.asInstanceOf[MethodRejection].supported
      })
      ctx.complete(HttpResponse().withHeaders(
        `Access-Control-Allow-Methods`(OPTIONS, allowedMethods :_*) ::  allowOriginHeader ::
          optionsCorsHeaders
      ))
    }
  }).withHttpResponseHeadersMapped { headers =>
    allowOriginHeader :: headers

  }
  }
}