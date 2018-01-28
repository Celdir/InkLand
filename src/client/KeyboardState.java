package client;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import java.util.HashSet;
import utils.Settings;

public class KeyboardState{
	boolean UP, DOWN, LEFT, RIGHT, CLOCK, COUNTER;
	private HashSet<Integer> up, down, left, right, clock, counter;
	private HashSet<Integer> pressed;

	/**
	 * Figure out if a key is currently being pressed (held down)
	 *
	 * @param  int  the ID of the key in question
	 * @return      whether the key is pressed as a true/false
	 */
	public boolean isPressed(Integer k){
		return pressed.contains(k);
	}

	private void getKeyFields(HashSet<Integer> set, String[] keynames){
		for(String key : keynames){
			try{
				Field field = KeyEvent.class.getDeclaredField("VK_"+key);
				set.add(field.getInt(null));
			}
			catch(NoSuchFieldException | SecurityException | IllegalAccessException e){}
		}
	}

	public KeyboardState(Settings settings){
		up = new HashSet<Integer>();
		down = new HashSet<Integer>();
		left = new HashSet<Integer>();
		right = new HashSet<Integer>();
		clock = new HashSet<Integer>();
		counter = new HashSet<Integer>();
		getKeyFields(up, settings.getString("key-up").split(","));
		getKeyFields(down, settings.getString("key-down").split(","));
		getKeyFields(left, settings.getString("key-left").split(","));
		getKeyFields(right, settings.getString("key-right").split(","));
		getKeyFields(clock, settings.getString("key-clock").split(","));
		getKeyFields(counter, settings.getString("key-counter").split(","));
		pressed = new HashSet<Integer>();

		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(
			new KeyEventDispatcher(){@Override public boolean dispatchKeyEvent(KeyEvent key){
				synchronized(pressed){
					if(key.getID() == KeyEvent.KEY_PRESSED){
						pressed.add(key.getKeyCode());
						if(up.contains(key.getKeyCode())) UP = true;
						else if(down.contains(key.getKeyCode())) DOWN = true;
						else if(left.contains(key.getKeyCode())) LEFT = true;
						else if(right.contains(key.getKeyCode())) RIGHT = true;
					}
					else if(key.getID() == KeyEvent.KEY_RELEASED){
						pressed.remove(key.getKeyCode());
						if(up.contains(key.getKeyCode())) UP = false;
						else if(down.contains(key.getKeyCode())) DOWN = false;
						else if(left.contains(key.getKeyCode())) LEFT = false;
						else if(right.contains(key.getKeyCode())) RIGHT = false;
					}
					return false;
				}
			}}
		);
	}
}