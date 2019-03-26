package nacos.client.util

case class Func[T](func: T => Unit)

class PubSub[TOPIC, MSG] {
  @volatile private var topics = Map.empty[TOPIC, Set[Func[MSG]]]

  def subscribe(topic: TOPIC, func: MSG => Unit): Unit = {
    val funcs = topics.getOrElse(topic, Set()) + Func(func)
    topics = topics.updated(topic, funcs)
  }

  def send(topic: TOPIC, msg: MSG) = {
    topics.get(topic).foreach(funcs => funcs.foreach(_.func(msg)))
  }
  def send(msg: MSG) = {

  }
}
