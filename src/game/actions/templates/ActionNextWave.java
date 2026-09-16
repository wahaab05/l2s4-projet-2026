package game.actions.templates;

import game.Game;
import game.actions.Action;

/**
 * Action of starting the next wave
 */
public class ActionNextWave extends Action {

	public ActionNextWave()
	{ super(); }

	@Override
	public boolean isExecutable()
	{ return true; }

	@Override
	public void execAction()   // TODO: we should  add in the final livrable Game.instance().setWaveGenerator(new WaveGenerator());//
	{
			Game.instance().generateNextWave();
			Game.instance().startWave();
	}
}
