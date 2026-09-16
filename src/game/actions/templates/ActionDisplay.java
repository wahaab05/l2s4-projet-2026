package game.actions.templates;

import game.Game;
import game.displays.Display;
import game.actions.Action;

/**
 * Action of displaying the current state of the board
 */
public class ActionDisplay extends Action {

	public ActionDisplay()
	{ super(); }

	@Override
	public boolean isExecutable()
	{ return true; }

	@Override
	public void execAction()
	{
		// TODO to test
		for(Display d: Game.instance().getDisplays())
		{
			d.display();
		}
	}
}
