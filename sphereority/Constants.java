package sphereority;

/**
 * Some constants that are useful to have close-at-hand
 *
 */
public interface Constants
{
  /**
   * The fastest an object can move in units per second
   */
  public static final float MAXIMUM_SPEED = 25;
  
  /**
   * The amount of friction we have to slow motion down
   */
  public static final float FRICTION_COEFFICIENT = 1 - 0.2f;
  
  /**
   * The speed at which objects track each other
   * (this affects the "slow parent" of objects)
   */
  public static final float TRACKING_SPEED = 1f;
  
  /**
   * The amount of speed you have after bumping into a wall
   */
  public static final float WALL_REBOUND = 0.75f;
  
  /**
   * The amount of speed you have after bumping into another player
   */
  public static final float PLAYER_REBOUND = 0.5f;
  
  /**
   * The speed at which a player accelerates due to keypresses
   */
  public static final float PLAYER_ACCELERATION = 1f;
  
  /**
   * The size of a player in world units
   */
  public static final float PLAYER_SIZE = 0.5f;

	/*
		The width of the player window
	*/
	public static final int GAME_WINDOW_WIDTH = 640;

	/* 
		The height of the player window
	*/
	public static final int GAME_WINDOW_HEIGHT = 480;

	/*
		The height of the playable area
	*/
	public static final int GAME_WINDOW_PLAY_HEIGHT = 400; 
}