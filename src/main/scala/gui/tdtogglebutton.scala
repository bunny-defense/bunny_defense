
package gui

class TDToggleButton(parent: Option[TDComponent], text: String)
extends TextButton(parent, text)
{
    var on_color = Colors.darkgreen
    var initial_color = color
    var toggled = false
    override def action() : Unit = {
        toggled = !toggled
        if( toggled )
        {
            initial_color = color
            color = on_color
        }
        else
            color = initial_color
    }
}
