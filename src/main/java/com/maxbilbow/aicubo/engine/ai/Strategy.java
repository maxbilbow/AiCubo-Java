package com.maxbilbow.aicubo.engine.ai;

public class Strategy
{
  String name;
  int    id;

  private AiMethod method;

  public AiMethod getMethod()
  {
    return method;
  }

//	public static HashMap<String, StrategyGroup> combined = new HashMap<>();

  public void setMethod(AiMethod method)
  {
    this.method = method;
  }

  public static final int
          FAIL = -1, UNCERTAIN = 0, SUCCESS = 1;


  public enum State
  {
    WILL_NOT_WORK, FINISHED, WILL_CONTINUE, READY_TO_BEGIN;
  }

  //	public class StrategyGroup {
  int successes = 0, failures = 0;

  //		String name;
//		int id;
//		StrategyGroup(int id, String name) {
//			this.id = id; this.name = name;
//		}
  public int aggregate()
  {
    // TODO Auto-generated method stub
    return successes - failures;
  }

  public double successRate()
  {
    int total = successes + failures;

    return total > 0 ? successes / total : 0;
  }

  @Override
  public String toString()
  {
    return this.name + " => SuccessRate: " + this.successRate() + ", Agg Score: " + this.aggregate();
  }

  //	}
  public Strategy(int id, String name)
  {
    this.id = id;
    this.name = name;
//		if (!combined.containsKey(name)) {
//			combined.put(name, new StrategyGroup(id,name));
//		}
  }

  private int count = 0;

  //	public StrategyGroup group() {
//		return combined.get(this.name);
//	}
  public void success()
  {
    successes++;
    setCount(0);
    state = State.READY_TO_BEGIN;
    System.out.println("Success");
  }

  public void fail()
  {
    failures++;
    state = State.READY_TO_BEGIN;
    setCount(0);
//		System.out.println("Fail");
  }

  public void noOutcome()
  {
    setCount(getCount() + 1);
  }

  //	public int aggregate() {
//		return group().aggregate();
//	}
//	
//	public double successRate() {
//		return group().successRate();
//	}
  int maxTurns;

  public State invoke(Object... args)
  {

    if (method != null)
    {
      return this.state = method.invoke(this, count++, args);
    }

    System.err.println("Warning: Method not implemented");
    return this.state = State.WILL_NOT_WORK;
//			throw new NullPointerException();

  }

  private State state = State.READY_TO_BEGIN;

  public State state()
  {
    return this.state;
  }

  public interface AiMethod
  {
    public State invoke(Strategy ai, int maxTurns, Object... args);
  }

  public static AiMethod DummyMethod()
  {
    return new AiMethod()
    {

      @Override
      public State invoke(Strategy s, int maxTurns, Object... args)
      {
        // TODO Auto-generated method stub
        return State.WILL_NOT_WORK;
      }

    };
  }

  public int getCount()
  {
    return count;
  }

  public void setCount(int count)
  {
    this.count = count;
  }

  public void setState(State state)
  {
    this.state = state;
  }
}
