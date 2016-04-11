package com.maxbilbow.aicubo.model;

import com.maxbilbow.aicubo.messages.IMessageable;

public interface NodeComponent extends IMessageable
{

  boolean isEnabled();

  void setEnabled(boolean enabled);

  void setNode(Node node);

  Node getNode();

  Transform transform();


//	/**
//	 * Overriden so that multiple behaviours can share the same variables
//	 */
//	Object getValue(String forKey);
//
//	/**
//	 * Overriden so that multiple behaviours can share the same variables
//	 */
//	Object setValue(String forKey, Object value);

}