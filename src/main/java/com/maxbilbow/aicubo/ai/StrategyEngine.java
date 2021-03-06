package com.maxbilbow.aicubo.ai;

import java.util.HashMap;
import java.util.LinkedList;

import com.maxbilbow.aicubo.ai.Strategy.AiMethod;
import com.maxbilbow.aicubo.ai.Strategy.State;
import click.rmx.Bugger;
import click.rmx.engine.math.Tools;



public class StrategyEngine extends HashMap<String, Strategy> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6116209501748928257L;

	public interface IMethods {

		public String[] Strings();
		@Override
		public String toString();
		public int length();
	}
	
	int maxTurns; int dataThreshold = 100; 
	boolean useData = false;
	final String[] keys;
	public StrategyEngine(IMethods ids, int maxTurns) {
		super(ids.length());
		this.maxTurns = maxTurns;
		this.keys = ids.Strings();
		
		for (int i=0; i< keys.length; ++i) {
			this.put(keys[i], new Strategy(i, keys[i]));
		}
		this.history.addFirst(keys[0]);
	}


	public void addMethod(IMethods key, AiMethod method) {
		this.addMethod(key, this.maxTurns, method);
	}
	
	public void addMethod(IMethods key, int maxTurns, AiMethod method) {
		this.get(key.toString()).setMethod(method);
		this.get(key.toString()).maxTurns = maxTurns;
	}
	
//	Strategy current;
	LinkedList<String> history = new LinkedList<>();
	public Strategy invokeCurrent(Object... args) {
		Strategy ai = this.get(history.getFirst());
		
		State state = ai.invoke(args);
		
		if (ai.getCount() > ai.maxTurns) {
			ai.setState(State.FINISHED);
//			next();
		}
		return ai;
//		return st
		
	}
	
	public Strategy getPrevious() {
		if (history.isEmpty())
			return null;
		return this.get(history.get(1));//.getLast());
	}

	public void fail() {
		this.current().fail();
		this.next();
//		Bugger.logAndPrint(current().name + ": Fail", false);
	}
	
	public void success() {
		this.current().success();
		this.next();
//		Bugger.logAndPrint(current().name + ": Success", false);
	}
	
	public void restartCurrent() {
		this.current().setCount(0);
		this.current().setState(State.WILL_CONTINUE);
	}
	
	public Strategy current() {
		return this.get(history.getFirst());
	}
	
	public void next() {
//		Strategy ai = this.current();
//		System.out.println("next!");
		if (useData) {
			history.addFirst(this.findMostSuccessful());
		} else {
			history.addFirst(this.keys[(int) Tools.rBounds(0, keys.length)]);
			if ( --this.dataThreshold <= 0) {
				useData = true;
				Bugger.logAndPrint("NOW USING DATA", true);
			}
		}
		
		
	}


	public String findMostSuccessful() {
		HashMap<String,Strategy> allAi = this;
		Strategy result =  allAi.get(keys[0]);

		
		for (Strategy ai: allAi.values()) {
			if (ai.aggregate() > result.aggregate())
				result = ai;
		}
	
		
		return result.name;
		
	}
	
	
}
