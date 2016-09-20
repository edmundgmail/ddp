package user

import com.ddp.access.TableGenerator

case class TestApp() extends TableGenerator{
  def generate(): Any = {
    System.out.println("hello, world")
    120
  }
}
