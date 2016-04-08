
package utils

import io.Source

object Parameters
{
    private def loadFile(filename: String) : Source = {
        Source.fromFile( "src/main/resources/config/" + filename )
    }

    var player_initial_gold = 150;
    var player_initial_life = 10;

    private def parse_player_config(file: Source)
    {
        val lines = file.getLines().filter( _ != "" ).map(_.split(" "))
        for( line <- lines )
        {
            if( line(0) == "initial_gold" )
                player_initial_gold = line(1).toInt
            else if( line(0) == "initial_life" )
                player_initial_life = line(1).toInt
            else
                throw new Exception( "Unknown configuration option : " + line(0) )
        }
    }

    def load(): Unit = {
        val player_config_file = loadFile( "player.cfg" )
        parse_player_config( player_config_file )
    }
}
