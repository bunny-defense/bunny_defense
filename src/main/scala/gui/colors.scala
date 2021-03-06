
package gui

import swing.Color

/* RGB colors for the whole GUI */

object Colors {

    // SOLID

    // B&W
    val white      = new Color( 255, 255, 255 )
    val lightGrey  = new Color( 200, 200, 200 )
    val lightGray  = lightGrey
    val darkGrey   = new Color( 100, 100, 100 )
    val black      = new Color(   0,   0,   0 )

    // Primitives
    val darkred    = new Color( 200,   0,   0 )
    val red        = new Color( 255,   0,   0 )
    val lightred   = new Color( 255, 200, 200 )

    val darkgreen  = new Color(   0, 127,   0 )
    val green      = new Color(   0, 255,   0 )
    val lightgreen = new Color( 200, 255, 200 )

    val blue       = new Color(   0,   0, 255 )
    val midblue    = new Color(  70,  70, 255 )
    val lightblue  = new Color( 200, 200, 255 )

    // Composites
    val darkyellow = new Color( 200, 200,   0 )
    val yellow     = new Color( 255, 255,   0 )

    val cyan       = new Color(   0, 255, 255 )

    val orange     = new Color( 255, 127,   0 )

    // TRANSPARENT
    val transparent_white = new Color( 255, 255, 255, 127 )
    val transparent_grey  = new Color( 127, 127, 127, 127 )

    val transparent_red   = new Color( 255,   0,   0, 127 )

    val transparent_cyan  = new Color(   0, 255, 255, 127 )
}
