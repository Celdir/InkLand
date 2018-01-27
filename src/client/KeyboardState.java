package client;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.util.HashSet;

public class KeyboardState{
	HashSet<Integer> pressed;

	/**
	 * Figure out if a key is currently being pressed (held down)
	 *
	 * @param  int  the ID of the key in question
	 * @return      wether the key is pressed as a true/false
	 */
	public boolean isPressed(Integer k){
		return pressed.contains(k);
	}

	public KeyboardState(){
		pressed = new HashSet<Integer>();
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(
			new KeyEventDispatcher(){@Override public boolean dispatchKeyEvent(KeyEvent key){
				if(key.getID() == KeyEvent.KEY_PRESSED) pressed.add(key.getKeyCode());
				else if(key.getID() == KeyEvent.KEY_RELEASED) pressed.remove(key.getKeyCode());
				return false;
			}}
		);
	}
}