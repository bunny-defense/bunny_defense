object MultiplayerMenuState extends MenuState
{
    new gui.WideButton( 50, "Join" )
    {
        override def action() : Unit = {
            StateManager.set_state( new ServerConnectionMenu() )
        }
    }
    new gui.WideButton( 120, "Host & Play" )
    {
        override def action() : Unit = {
            StateManager.set_state( new ClientServerGameState() )
        }
    }
    new gui.WideButton( 190, "Host" )
    {
        override def action() : Unit = {
            StateManager.set_state( new ServerLobby() )
        }
    }
    new gui.WideButton( 260, "Back" )
    {
        override def action() : Unit = {
            StateManager.set_state( PlayMenuState )
        }
    }
}
