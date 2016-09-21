package user

import com.ddp.access.UserClassRunner

case class TestApp() extends UserClassRunner{
  def run(): Any = {
    System.out.println("hello, world")
    120
  }
}
