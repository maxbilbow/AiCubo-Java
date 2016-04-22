package com.maxbilbow.aicubo.model.node;

import com.maxbilbow.aicubo.engine.math.Vector3;
import com.maxbilbow.aicubo.model.NodeEngine;
import com.maxbilbow.aicubo.model.Transform;
import com.maxbilbow.aicubo.model.generic.GenericDomain;
import com.maxbilbow.aicubo.model.node.type.NodeRole;

import javax.persistence.*;
import java.util.*;

/**
 * Created by Max on 22/04/2016.
 */
@Entity
public class GameNode extends GenericDomain<Long>
{
  private final NodeEngine mEngine;
  private final Transform mTransform;
  private GameNode mParent;
  private List<GameNode> mChildren = new ArrayList<>();
//  private Transform mTransform = new Transform(this);

  private String mName;

  private NodeRole mRole;

  private double mPosX;
  private double mPosY;
  private double mPosZ;

  public GameNode()
  {
    mEngine = new NodeEngine(this);
    mTransform = mEngine.transform();
  }

  public String getName()
  {
    return mName;
  }

  @OneToMany
  public List<GameNode> getChildren()
  {
    return mChildren;
  }


  @ManyToOne
  public GameNode getParent()
  {
    return mParent;
  }

  public void addChild(GameNode child)
  {
    if (!mChildren.contains(child))
    {
      mChildren.add(child);
    }
  }

  public boolean removeChild(GameNode node)
  {
    return mChildren.remove(node);
  }

  @Transient
  public NodeEngine getEngine()
  {
    return mEngine;
  }

  public double getPosX()
  {
    return mPosX;
  }

  public double getPosY()
  {
    return mPosY;
  }

  public double getPosZ()
  {
    return mPosZ;
  }

  @Override
  public void onSave()
  {
    final Vector3 position = mTransform.lastPosition();
    mPosX = position.x;
    mPosY = position.y;
    mPosZ = position.z;
  }

  public void setParent(GameNode aParent)
  {
    mParent = aParent;
  }

  public void setChildren(List<GameNode> aChildren)
  {
    mChildren = aChildren;
  }

  public void setPosX(double aPosX)
  {
    mPosX = aPosX;
  }

  public void setPosY(double aPosY)
  {
    mPosY = aPosY;
  }

  public void setPosZ(double aPosZ)
  {
    mPosZ = aPosZ;
  }

  public void setName(String aName)
  {
    mName = aName;
  }

  @Enumerated(EnumType.STRING)
  public NodeRole getRole()
  {
    return mRole;
  }

  public void setRole(NodeRole aRole)
  {
    mRole = aRole;
  }

}
