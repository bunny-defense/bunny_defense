class TDComponent(parent: Option[TDComponent])
{
    parent match {
        case Some(component) =>
            component.children += this
        case None => {}
    }
    def draw(g: Graphics2D) : Unit = {
        children.foreach({
                (child: TDComponent) =>
                child.draw(g)
                g.setTransform( transform )
            })
    }
}
