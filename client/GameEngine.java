package	client;

/**
 * This class describes the game loop for this game
 * @author smaboshe
 */

import client.audio.*;
<<<<<<< HEAD:client/GameEngine.java
import client.gui.*;

import java.awt.Color;
=======
import common.*;
>>>>>>> efeaab567c0ccfb058fd5c352edeadb01a7f89d0:client/GameEngine.java
import java.awt.BorderLayout;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.Timer;

<<<<<<< HEAD:client/GameEngine.java
/**
 * This class describes the game loop for this game
 * @author smaboshe
 */
public class GameEngine implements Constants, ActionListener, ActionCallback {
=======
public class GameEngine implements Constants, ActionListener {
>>>>>>> efeaab567c0ccfb058fd5c352edeadb01a7f89d0:client/GameEngine.java
	public boolean gameOver;
	public Map gameMap;
	public ClientViewArea gameViewArea;
	public LocalPlayer localPlayer;
	public InputListener localInputListener;
	
	// Actor lists and sub-lists
	public Vector<Actor> actorList; // This contains all actors in the game
	public Vector<Stone> stoneList; // This only contains stones
	public Vector<Player> playerList; // This only contains players
	public Vector<Projectile> bulletList; // This only contains bullets
	public Vector<Actor> miscList; // This contains stuff that doesn't fit in any of the other
	
	public long lastTime;
	public Timer timer;
	
	public Vector<MapChangeListener> mapListeners;
	
	// Sound stuff
	public GameSoundSystem soundSystem;
	public SoundEffect soundBump;

	// CONSTRUCTORS
	public GameEngine(Map m) {
		gameOver = false;
		gameMap = m;
		mapListeners = new Vector<MapChangeListener>();
		
		actorList = new Vector<Actor>();
		stoneList = new Vector<Stone>();
		playerList = new Vector<Player>();
		bulletList = new Vector<Projectile>();
		miscList = new Vector<Actor>();
		
		gameViewArea = new ClientViewArea(this);
		addButton(-5, -5, 45, 15, "Quit", Color.red);
		
		localInputListener = new InputListener();
		localPlayer = new LocalPlayer(localInputListener);
		gameMap.placePlayer(localPlayer);
		gameViewArea.setLocalPlayer(localPlayer);
		
		addActor(localPlayer);
		
		triggerMapListeners();
		
		// Sound engine stuff:
		soundSystem = new GameSoundSystem();
		soundBump = soundSystem.loadSoundEffect(SOUND_BUMP);
	} // end GameEngine() constructor
	
	
	// GETTERS
	public LocalPlayer getLocalPlayer() {
		return this.localPlayer;
	}
	
	public Map getGameMap() {
		return this.gameMap;
	}

	public boolean getGameOver() {
		return this.gameOver;
	}

	public ClientViewArea getGameViewArea() {
		return this.gameViewArea;
	}

	public InputListener getInputListener() {
		return this.localInputListener;
	}
	
	// SETTERS
	public void setLocalPlayer(LocalPlayer p) {
		this.localPlayer = p;
	}
	
	public void setGameMap(Map m) {
		this.gameMap = m;
		triggerMapListeners();
	}
	
	// OPERATIONS
	public void addActor(Actor a)
	{
		actorList.add(a);
		if (a instanceof Stone)
			stoneList.add((Stone)a);
		else if (a instanceof Projectile)
			bulletList.add((Projectile)a);
		else if (a instanceof Player)
			playerList.add((Player)a);
		else
			miscList.add(a);
	}
	
	public boolean isGameOver() {
		return this.gameOver;
	}
	
	public void gameOver() {
		gameOver = true;
		if (timer != null)
			timer.stop();
	}
	
	public void gameStep()
	{
		checkCollisions();
		updateWorld();
		
		Thread.yield();
	}
	
	public void play() {
		initialize();
		
		timer = new Timer(TIMER_TICK, this);
		timer.start();
		timer.setCoalesce(true);
		
//		while (!isGameOver()) {
//			gameStep();
//			
//			try { Thread.sleep(10); }
//			catch (InterruptedException er) { }
//		}
	}
	
	protected void triggerMapListeners()
	{
		for (MapChangeListener mcl : mapListeners)
			mcl.mapChanged(gameMap);
	}
	
	public void addMapListener(MapChangeListener listener)
	{
		mapListeners.add(listener);
	}
	
	public void initialize() {
		String title = CLIENT_WINDOW_NAME;
		System.out.println(title);
		
		// Set up game window
		JFrame window = new JFrame(title);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Copy the map as a bunch of Stones
		for (int x=0; x < gameMap.getWidth(); x++)
			for (int y=0; y < gameMap.getHeight(); y++)
			{
				if (gameMap.isWall(x, y))
				{
					stoneList.add(new Stone(x, y));
					actorList.add(stoneList.get(stoneList.size() - 1));
				}
			}
				
		window.getContentPane().add(this.gameViewArea, BorderLayout.CENTER);
		window.addKeyListener(this.gameViewArea);
		
		localInputListener.attachListeners(window);
		
		window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);
		
		lastTime = System.currentTimeMillis();
	} // end initialize()
	
	public void checkCollisions() {
		// Environment, Player and projectile collision code goes here
		Rectangle2D bounds1, bounds2;
		Actor actor1, actor2;
		
		// Check players against stones
		Player p;
		for (int i=0; i < playerList.size(); i ++)
		{
			p = playerList.get(i);
			float px = p.getX(), py = p.getY();
			int ix = (int)px, iy = (int)py;
			if (px < 0 || py < 0)
				continue;
			if (px >= gameMap.getWidth() || py >= gameMap.getHeight())
				continue;
			
			px = px - ix;
			py = py - iy;
			if (px < 0.25 && gameMap.isWall(ix - 1, iy))
				p.collideLeft();
			else if (px > 0.75 && gameMap.isWall(ix + 1, iy))
				p.collideRight();
			if (py < 0.25 && gameMap.isWall(ix, iy - 1))
				p.collideUp();
			else if (py > 0.72 && gameMap.isWall(ix, iy + 1))
				p.collideDown();
		}
//		for (int i=0; i < stoneList.size(); i ++)
//		{
//			actor1 = stoneList.get(i);
//			bounds1 = actor1.getBounds();
//			
//			for (int j=0; j < playerList.size(); j ++)
//			{
//				actor2 = playerList.get(j);
//				bounds2 = actor2.getBounds();
//				
//				if (bounds1.intersects(bounds2))
//				{
//					actor1.collision(actor2);
//					actor2.collision(actor1);
//				}
//			}
//		} // end check players against stones
		
		// Check bullets against players and stones
		for (int i=0; i < bulletList.size(); i ++)
		{
			actor1 = bulletList.get(i);
			bounds1 = actor1.getBounds();
			
			for (int j=0; j < playerList.size(); j ++)
			{
				actor2 = playerList.get(j);
				bounds2 = actor2.getBounds();
				
				if (bounds1.intersects(bounds2))
				{
					actor1.collision(actor2);
					actor2.collision(actor1);
				}
			}
			
//			for (int j=0; j < stoneList.size(); j ++)
//			{
//				actor2 = stoneList.get(j);
//				bounds2 = actor2.getBounds();
//				
//				if (bounds1.intersects(bounds2))
//				{
//					actor1.collision(actor2);
//					actor2.collision(actor1);
//				}
//			}
			// Simpler check:
			if (gameMap.isWall((int)actor1.getX(), (int)actor1.getY()))
			{
				actor1.collision(null);
			}
		} // end check bullets against players and stones
		
//		//Vector actorList = this.gameViewArea.actorList;
//		Rectangle2D playerBounds = this.localPlayer.getBounds();
//		for (int i = 0; i < actorList.size(); i = i + 1) {
//			Actor actor1 = actorList.get(i);
//			if (actor1 instanceof TrackingObject) continue;
//			
//			Rectangle2D bound1 = actor1.getBounds();
//			if (bound1.intersects(playerBounds)) {
//				this.localPlayer.collision(actor1);
//				actor1.collision(this.localPlayer);
//			}
//			for (int j = i + 1; j < actorList.size(); j = j + 1) {
//				Actor actor2 = actorList.get(j);
//				if (actor2 instanceof TrackingObject) continue;
//				
//				Rectangle2D bound2 = actor2.getBounds();
//				if (bound1.intersects(bound2)) {
//					actor1.collision(actor2);
//					actor2.collision(actor1);
//				}
//			}
//		}
	} // end checkCollisions()
	
	public void updateWorld() {
		long thisTime = System.currentTimeMillis();
		float dTime = 0.001f*(thisTime - lastTime);
		boolean repaint = false;
		
//		for (Actor a : actorList)
//		{
//			if (a.animate(dTime))
//				repaint = true;
//		}
		
		for (Actor a : playerList)
		{
			if (a.animate(dTime))
				repaint = true;
		}
		
		for (Actor a : bulletList)
		{
			if (a.animate(dTime))
				repaint = true;
		}
		
		for (Actor a : miscList)
		{
			if (a.animate(dTime))
				repaint = true;
		}
		
		lastTime = thisTime;
		if (repaint)
		{
			/*
			 * This may not actually be desireable.
			 * If you bump or fire then stop moving, it won't repaint
			 */
		}
			gameViewArea.repaint();
	} // end updateWorld()
	
	public void actionPerformed(ActionEvent e)
	{
		gameStep();
	}
	
<<<<<<< HEAD:client/GameEngine.java
	public void placePlayer(Player p)
	{
		Vector<SpawnPoint> spawnPoints = gameMap.getSpawnPoints();
		
		if (spawnPoints == null || spawnPoints.size() == 0)
		{
			final int width = gameMap.getWidth(), height = gameMap.getHeight();
			int x = RANDOM.nextInt(width), y = RANDOM.nextInt(height);
			
			while (gameMap.isWall(x, y))
			{
				x = RANDOM.nextInt(width);
				y = RANDOM.nextInt(height);
			}
			
			p.setPosition(x + 0.5f, y + 0.5f);
		}
		else
		{
			p.setPosition(spawnPoints.get(RANDOM.nextInt(spawnPoints.size())).getPosition());
		}
	}
	
	protected void addButton(int x, int y, int width, int height, String label)
	{
		addButton(x, y, width, height, label, Color.green);
	}
	
	protected void addButton(int x, int y, int width, int height, String label, Color c)
	{
		SimpleButton b = new SimpleButton(x, y, width, height, label, c);
		b.addCallback(this);
		gameViewArea.addWidget(b);
	}
	
	public void actionCallback(InteractiveWidget source, int buttons)
	{
		if (source.getLabel().equalsIgnoreCase("Quit"))
		{
			gameOver();
		}
	}
	
=======
>>>>>>> efeaab567c0ccfb058fd5c352edeadb01a7f89d0:client/GameEngine.java
	/* ********************************************* *
	 * These method(s) are for playing sound effects *
	 * ********************************************* */
	/**
	 * Plays a bump sound effect at the specified volume. If the sound is already playing at a lower volume, it is stopped and restarted at the louder volume, otherwise nothing happens.
	 * @param volume	The volume at which to play.
	 */
	public void playBump(float volume)
	{
		playSound(volume, soundBump);
	}
	
	/**
	 * A slightly more generic sound playing method so we don't have to duplicate code all over the place.
	 * This actually handles playing or not playing the sound.
	 * @param volume
	 * @param sound
	 */
	private void playSound(float volume, SoundEffect sound)
	{
		if (sound.isPlaying())
		{
			if (sound.getVolume() < volume)
				sound.stop();
			else
				return;
		}
		
		sound.setVolume(volume);
		sound.play();
	}
} // end class GameEngine
