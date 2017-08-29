package controllers

import javax.inject.Inject

import play.api.mvc.Controller

/**
  * When user visit the localhost:9000/login first, he needs to be redirected to locadlhost:9000/index.html after being
  * authenticated.
  */
class LoginController @Inject() extends Controller{

  private val basicAuth = new BasicAuthAction("user", "pass")
  def login = basicAuth {
    Redirect("/index.html")
  }

}
