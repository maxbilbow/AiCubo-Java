package com.maxbilbow.aicubo.dao.generic;

import com.maxbilbow.aicubo.model.generic.GenericDomain;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.persistence.MappedSuperclass;
import java.util.List;

/**
 * Created by Max on 18/04/2016.
 */
@MappedSuperclass
public abstract class GenericDao<T extends GenericDomain<? extends Number>>
{
  private Logger         mLogger = LoggerFactory.getLogger(GenericDao.class);
  @Resource
  private       SessionFactory mSessionFactory;
  private       String         mClassName;

  public GenericDao()
  {
    mClassName = getClass().getTypeParameters()[0].getName();
    mLogger.debug(this.getClass().getSimpleName() + " initialized with " + mClassName);
  }

  public Session getSession()
  {
    return mSessionFactory.getCurrentSession();
  }

  public T makePersistent(T aGenericDomain)
  {
    getSession().saveOrUpdate(aGenericDomain);
    return aGenericDomain;
  }

  public T makeTransient(T aGenericDomain)
  {
    getSession().delete(aGenericDomain);
    return aGenericDomain;
  }

  public List<T> batchPersist(List<T> aGenericDomains)
  {
    for (T genericDomain : aGenericDomains)
    {
      getSession().saveOrUpdate(genericDomain);
    }
    return aGenericDomains;
  }

  public List<T> batchMakeTransient(List<T> aGenericDomains)
  {
    for (T genericDomain : aGenericDomains)
    {
      getSession().delete(genericDomain);
    }
    return aGenericDomains;
  }

  public List<T> findAll()
  {
    return getSession().createQuery("from "+getClassName()).list();
  }

  protected String getClassName()
  {
    return mClassName;
  }
}
