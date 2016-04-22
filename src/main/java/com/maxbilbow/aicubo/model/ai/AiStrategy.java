package com.maxbilbow.aicubo.model.ai;

import com.maxbilbow.aicubo.model.generic.GenericDomain;

import javax.persistence.Entity;

/**
 * Created by Max on 18/04/2016.
 */
@Entity
public class AiStrategy extends GenericDomain<Long>
{

  private String mName;
  private long mSuccessCount;
  private long mFailCount;
  private long mUncertainCount;


  public long getFailCount()
  {
    return mFailCount;
  }

  public long getSuccessCount()
  {
    return mSuccessCount;
  }

  public long getUncertainCount()
  {
    return mUncertainCount;
  }

  public String getName()
  {
    return mName;
  }

  public void setUncertainCount(long aUncertainCount)
  {
    mUncertainCount = aUncertainCount;
  }


  public void setName(String aName)
  {
    mName = aName;
  }

  public void setSuccessCount(long aSuccessCount)
  {
    mSuccessCount = aSuccessCount;
  }
  public void setFailCount(long aFailCount)
  {
    mFailCount = aFailCount;
  }
}
