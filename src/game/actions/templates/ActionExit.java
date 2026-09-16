package game.actions.templates;

import game.actions.Action;

/**
 * Quit game 🐟
 */
public class ActionExit extends Action {

	public ActionExit()
	{ super(); }

	@Override
	public boolean isExecutable()
	{ return true; }

	@Override
	public void execAction()
	{
		System.out.println("Bye :)");
		System.exit(0);
	}
}
