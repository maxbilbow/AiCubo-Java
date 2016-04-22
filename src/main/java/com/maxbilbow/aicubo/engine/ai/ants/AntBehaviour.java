package com.maxbilbow.aicubo.engine.ai.ants;

import com.maxbilbow.aicubo.AiCubo;
import com.maxbilbow.aicubo.config.RMX;
import com.maxbilbow.aicubo.engine.ai.Strategy;
import com.maxbilbow.aicubo.engine.ai.Strategy.AiMethod;
import com.maxbilbow.aicubo.engine.ai.Strategy.State;
import com.maxbilbow.aicubo.engine.ai.StrategyEngine;
import com.maxbilbow.aicubo.engine.behaviours.Behaviour;
import com.maxbilbow.aicubo.engine.behaviours.ICollisionHandler;
import com.maxbilbow.aicubo.engine.behaviours.SpriteBehaviour;
import com.maxbilbow.aicubo.engine.gl.IKeyCallback;
import com.maxbilbow.aicubo.engine.math.Matrix4;
import com.maxbilbow.aicubo.engine.math.Tools;
import com.maxbilbow.aicubo.engine.math.Vector3;
import com.maxbilbow.aicubo.model.Node;
import com.maxbilbow.aicubo.model.Nodes;
import com.maxbilbow.aicubo.model.Scene;
import com.maxbilbow.aicubo.model.collision.CollisionEvent;

import java.util.LinkedList;

public class AntBehaviour extends SpriteBehaviour implements ICollisionHandler, IKeyCallback
{
  public static final String
				  Possessed       = Behaviour.AI_STATE_POSSESSED, //0
          Amble           = "AI_STATE_AMBLE",
          FollowTheLeader = "FollowTheLeader",
          TheLeader       = "TheLeader";

  public static final String
          CanWalkThroughWalls = "CanWalkThroughWalls",
          BumpsPerSecond      = "bumpsPerSecond",
          BumpsLastTime       = "bumpsLastSecond";

  private final int rageLimit = 200;
  //	private int bumpsPerSecond = 0;
  //	private int bumpsLastSecond = 0;

  StrategyEngine crowdStrategies;// = new StrategyEngine();

  //Technniques
  public enum Crowd implements StrategyEngine.IMethods
  {
    DEFAULT("DoNothing"), Turn_180("Turn_180"), JumpUp("JumpUp"), Boost("Boost");

    private final String text;
    //

    /**
     * @param text
     */
    Crowd(final String text)
    {
      this.text = text;
    }

    /* (non-Javadoc)
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString()
    {
      return text;
    }

    @Override
    public int length()
    {
      return Crowd.values().length;
    }

    @Override
    public String[] Strings()
    {
      String[] strings = new String[Crowd.values().length];
      for (int i = 0; i < strings.length; ++i)
      {
        strings[i] = values()[i].toString();
      }
      return strings;
    }


  }


  @Override
  protected void onAwake()
  {


    this.setState(Amble);
    this.setValue(CanWalkThroughWalls, false);

    this.crowdStrategies = new StrategyEngine(Crowd.DEFAULT, 10);

    this.crowdStrategies.addMethod(Crowd.DEFAULT, Strategy.DummyMethod());
    this.crowdStrategies.addMethod(Crowd.Turn_180, new AiMethod()
    {

      @Override
      public Strategy.State invoke(Strategy ai, int maxTurns, Object... args)
      {
        Node n = (Node) args[0];
		  if (ai.getCount() == 0)
		  {
			  n.transform().rootTransform().rotate("yaw", 180 * RMX.PI_OVER_180);
		  }

        return ai.getCount() > maxTurns ? Strategy.State.FINISHED : Strategy.State.WILL_CONTINUE;
      }

    });

    this.crowdStrategies.addMethod(Crowd.JumpUp, 10, new AiMethod()
    {

      @Override
      public State invoke(Strategy ai, int maxTurns, Object... args)
      {
        Node n = (Node) args[0];
		  if (ai.getCount() == 0)
		  {
			  n.broadcastMessage("Jump");
		  }
        return ai.getCount() > maxTurns ? Strategy.State.FINISHED : Strategy.State.WILL_CONTINUE;
      }

    });

    this.crowdStrategies.addMethod(Crowd.Boost, 10, new AiMethod()
    {

      @Override
      public State invoke(Strategy ai, int maxTurns, Object... args)
      {
        Node n = (Node) args[0];
		  if (ai.getCount() < maxTurns)
		  {
			  n.broadcastMessage("applyForce", "forward:-0.2");
		  }
        return ai.getCount() > maxTurns ? Strategy.State.FINISHED : Strategy.State.WILL_CONTINUE;
      }

    });

    Runtime.getRuntime().addShutdownHook(new Thread()
    {
      @Override
      public void run()
      {
//				System.out.println("\nRESULTS:::");
        for (Strategy ai : crowdStrategies.values())
        {
			if (ai.successRate() > 0)
			{
				System.out.println(ai);
			}
        }
      }
    });
  }

  LinkedList<String> lastTechnique = new LinkedList<>();

  private boolean _ready = false;

  void tryToAvoidCrowding(long tick)
  {

    if (bumpCount == 0)
    {
      Strategy ai = crowdStrategies.current();
      if (ai != null && ai.state() == State.WILL_CONTINUE)
      {
        ai.setState(State.FINISHED);
        crowdStrategies.success();
//				System.out.println("Zero Count! " + ai);
      }
    }
    else if (bumpCount > 0 && tick % 60 == 0)
    { //every 10 seconds
      Strategy ai = this.crowdStrategies.invokeCurrent(this.getNode());

      switch (ai.state())
      {
        case FINISHED: //make judgement
          int lastCount = (int) this.getValueOrSetDefault(BumpsLastTime, 0);
          double oldbps = (double) this.getValueOrSetDefault(BumpsPerSecond, (double) 0);
          long time = tick - (long) this.getValueOrSetDefault("bumpTimeStamp", (long) 0);
          double newbps = (bumpCount - lastCount) * 60 / time;
          this.setValue("bumpTimeStamp", tick);
          this.setValue(BumpsLastTime, bumpCount);
          this.setValue(BumpsPerSecond, newbps);

			if (newbps < oldbps)
			{
				crowdStrategies.success();
			}
			else
			{
				crowdStrategies.next();
			}
//				else
//					crowdStrategies.fail();
          break;
        case WILL_NOT_WORK:
          crowdStrategies.fail();
          break;
      }
    }

  }


  @Override
  public void setDefaultState()
  {
    this.setState(FollowTheLeader);
    System.out.println("Amble");
  }

  //	public static Node TheLeader;

  protected Node leader()
  {
    Object theLeader = this.getValue(TheLeader);
	  if (theLeader == null)
	  {
		  theLeader = Leader;
	  }
    if (theLeader == this.getNode())
    {
      setState(TheLeader);
      theLeader = null;
    }
    return theLeader != null ? (Node) theLeader : null;
  }


  @Override
  public void update(Node node)
  {
    long tick = Scene.getCurrent().tick();
    switch (state())
    {
      case Amble:
        this.applyForce("forward:0.2");
        this.applyTorque("yaw:0.05");//, Vector3.Zero);
        this.limit(AiCubo.bounds * 2);
        break;
      case Possessed:
		  if (isStuck(tick))
		  {
			  this.setNotStuck();
		  }
        //			this.limit(AiCubo.bounds * 3.2f);
        return;
      case FollowTheLeader:
        Node leader = this.leader();
        if (leader != null)
        {
          this.turnToFace(leader.transform().position());
          this.getNode().physicsBody().applyForce(0.2f,
                                                  Vector3.makeSubtraction(leader.transform().position(),
                                                                          this.transform().position()
                                                  ).getNormalized(),
                                                  Vector3.Zero
          );

        }
        //			this.limit(AiCubo.bounds * 3);
        break;
      case TheLeader:
        int max = rageLimit;
        float bumpCount = this.bumpCount;
		  if (bumpCount > 0 && tick % 5 == 0)
		  {
			  bumpCount--;
		  }
		  if (bumpCount > max)
		  {
			  bumpCount = max + (bumpCount - max) * 0.05f;
		  }
        float speed = 0.01f * (bumpCount + 1);

        float turn = 0.001f * (max - bumpCount);
        this.applyForce("forward:" + speed);
		  if (turn > 0)
		  {
			  this.applyTorque("yaw:" + turn);//, Vector3.Zero);
		  }
        //			this.limit(AiCubo.bounds * 3.2f);

        break;

    }
    if (state() != AI_STATE_POSSESSED)
    {
      this.tryToAvoidCrowding(tick);
      if (isStuck(tick))
      {
        jump();
        this.setNotStuck();
      }
    }
  }


  void limit(float bounds)
  {
    this.limit(bounds, true);
  }

  boolean limit(float bounds, boolean doubleCheck)
  {
    Matrix4 m = this.transform().localMatrix();
    boolean outOfBounds = false;
    if (m.m30 > bounds)
    {
      m.m30 *= -0.5;
      m.m31 = (float) Tools.rBounds(10, 100);
      outOfBounds = true;
    }
    else if (m.m30 <= -bounds)
    {
      m.m30 *= -0.5;
      m.m31 = (float) Tools.rBounds(10, 100);
      outOfBounds = true;
    }
    if (m.m32 > bounds)
    {
      m.m32 *= -0.5;
      m.m31 = (float) Tools.rBounds(10, 100);
      outOfBounds = true;
    }
    else if (m.m32 <= -bounds)
    {
      m.m32 *= -0.5;
      m.m31 = (float) Tools.rBounds(10, 100);
      outOfBounds = true;
    }

    if (outOfBounds && doubleCheck && limit(bounds, false))
    {
      m.m30 = m.m32 = 0;
      m.m31 = 200;
      outOfBounds = false;
    }
    return outOfBounds;
  }

  public static Node Leader;

  @Override
  public void onEventDidEnd(String theEvent, Object args)
  {
    if (this.state() == Possessed)
    {
      System.out.println("I Am Possessed: " + theEvent + ", " + args);
      return;
    }
    switch (theEvent)
    {
      case TheLeader:
		  if (this.state() != TheLeader)// && this.leader() == null)
		  {
			  this.setValue(TheLeader, args);
		  }
        break;
      case CanWalkThroughWalls:
        this.setValue(CanWalkThroughWalls, Boolean.valueOf(args.toString()));
        break;
      case GET_AI_STATE:
		  if (this.state() != Possessed)
		  {
			  this.setState(args.toString());
		  }
        break;
      case FollowTheLeader:
		  if (this.state() != Possessed && this.state() == FollowTheLeader)
		  {
			  if (args == "current")
			  {
				  this.setValue(TheLeader, Nodes.getCurrent());
			  }
			  else if (args == "random")
			  {
				  this.setValue(TheLeader, Nodes.randomAiNode());
			  }
			  else if (args != null && args.getClass() == Node.class)
			  {
				  this.setValue(TheLeader, args);
			  }
			  else
			  {
				  this.setValue(TheLeader, Leader);
			  }
		  }
        break;
    }
  }

  @Override
  public void onCollisionStart(CollisionEvent event)
  {
    if (bumpCount > rageLimit * 5 || (Boolean) this.getValue(CanWalkThroughWalls))
    {
      event.preventCollision(true);
    }
    String state = this.state();
    switch (state)
    {
      case TheLeader:
		  if (event.getOther(this.getNode()).getValue(GET_AI_STATE) == FollowTheLeader)
		  {
			  bumpCount++;
		  }
        break;
      case Amble:
      case FollowTheLeader:
        if (event.getOther(this.getNode()).getValue(TheLeader) == this)
        {
          this.setState(TheLeader);
          this.jump();
        }
        break;
    }
  }

  @Override
  public void invoke(long window, int key, int scancode, int action, int mods)
  {

  }
//	private long tick;

  private int bumpCount = 0;

  @Override
  public void onCollisionEnd(CollisionEvent event)
  {
    // TODO Auto-generated method stub

    if (stuckState() == STUCK_FALSE && event.getDistanceBetweenPlanes() > 0)
    {
      this.setMightBeStuck(Scene.getCurrent().tick());
    }


  }


}
