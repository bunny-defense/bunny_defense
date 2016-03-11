
package utils

trait Continuable
{
    type Continuation = () => Unit

    var continuation : Option[Continuation] = None

    def and_then(cont: Continuation): Unit = {
        continuation = Some(cont)
    }

    def continue(): Unit = {
        continuation match {
            case None => ()
            case Some(cont) => cont()
        }
    }
}
