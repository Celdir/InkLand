package server;

import utils.Ink;
import utils.Orientation;

public class Player{
	Ink[] inks;
	public final Orientation orientation = new Orientation();
	
	public Player(Ink[] inks){
		this.inks = inks;
	}
}