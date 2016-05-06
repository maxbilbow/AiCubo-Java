package com.maxbilbow.aicubo.model.node;

import com.maxbilbow.aicubo.model.NodeEngine;
import com.maxbilbow.aicubo.model.generic.GenericDomain;
import com.maxbilbow.aicubo.model.node.type.NodeRole;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Transient;

/**
 * Created by Max on 22/04/2016.
 */
@Entity
public class NodeLocation extends GenericDomain<Long>
{
  private NodeEngine   mEngine;

  private float[] mMatrixData;

  private String mName;

  private NodeRole mRole;

  public NodeLocation()
  {

  }


  public String getName()
  {
    return mName;
  }


  @Transient
  public NodeEngine getEngine()
  {
    return mEngine;
  }


  @Override
  public void onSave()
  {
    if (mEngine != null)
    {
      mMatrixData = mEngine.transform().localMatrix().elements();
    }
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

  public void setEngine(NodeEngine aEngine)
  {
    mEngine = aEngine;
    if (mMatrixData != null)
    {
      aEngine.transform().localMatrix().set(mMatrixData);
    }
    else
    {
      mMatrixData = aEngine.transform().localMatrix().elements();
    }
  }

  public float[] getMatrixData()
  {
    return mMatrixData;
  }

  public void setMatrixData(float[] aMatrixData)
  {
    mMatrixData = aMatrixData;
  }
}
