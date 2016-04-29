
package gui

import collection.mutable.ListBuffer

import java.awt.Graphics2D
import java.awt.MouseInfo

import swing.{Panel, Rectangle}

import runtime.TowerDefense

class MainMenu( render_surface : Panel )
{
    class WideButton( y : Int, text : String )
    extends TextButton( render_surface,
        (TowerDefense.gui_size.width * 0.25).toInt, y,
        (TowerDefense.gui_size.width * 0.50).toInt, 50, text )
    {
    }

    class Menu
    {
        val buttonset = new ListBuffer[TDButton]()
        def +=( button : TDButton ) : Unit = {
            button.enabled = false
            buttonset += button
        }
        def test( str : String ) : Unit = {
            println( str )
        }
    }

    val background_color = Colors.black
    val play             = new Menu()
    val main             = new Menu()
    val multiplayer      = new Menu()
    private var _current_menu    = main

    def current_menu = _current_menu
    def current_menu_=( menu : Menu ) : Unit = {
        _current_menu.buttonset.foreach( _.enabled = false )
        _current_menu = menu
        menu.buttonset.foreach( _.enabled = true )
    }

    main += new WideButton( 50 , "Play" )
    {
        override def on_click() : Unit = {
            current_menu = play
        }
    }
    main += new WideButton( 120, "Quit" )
    {
        override def on_click() : Unit = {
            TowerDefense.quit()
        }
    }

    play += new WideButton( 50 , "Local" )
    play += new WideButton( 120, "Multiplayer" )
    {
        override def on_click() : Unit = {
            current_menu = multiplayer
        }
    }
    play += new WideButton( 190, "Back" )
    {
        override def on_click() : Unit = {
            current_menu = main
        }
    }

    multiplayer += new WideButton( 50, "Join" )
    multiplayer += new WideButton( 120, "Host & Play" )
    multiplayer += new WideButton( 190, "Host" )
    multiplayer += new WideButton( 260, "Back" )
    {
        override def on_click() : Unit = {
            current_menu = play
        }
    }

    current_menu = main

    def render(g: Graphics2D) : Unit = {
        val surface = new Rectangle(
            0, 0,
            TowerDefense.gui_size.width,
            TowerDefense.gui_size.height )
        g.setColor( background_color )
        g.fill( surface )
        for( button <- _current_menu.buttonset )
        {
            button.draw(g)
        }
    }
}
