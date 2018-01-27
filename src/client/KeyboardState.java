package client;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.util.HashSet;

public class KeyboardState{
	HashSet<Integer> pressed;
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