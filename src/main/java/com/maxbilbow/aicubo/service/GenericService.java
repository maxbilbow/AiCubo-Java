package com.maxbilbow.aicubo.service;

import com.maxbilbow.aicubo.model.generic.GenericDomain;
import com.maxbilbow.aicubo.model.node.GameNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * Created by Max on 22/04/2016.
 */
//@MappedSuperclass
public abstract class GenericService <
        Repository extends JpaRepository,
        Entity extends GenericDomain>
{
  private Repository mRepository;

  @Resource
  private ApplicationContext mApplicationContext;

  private Logger mLogger = LoggerFactory.getLogger(GenericService.class);


  @PostConstruct
  public void init()
  {
    try
    {
      final Class<Repository> clazz = (Class<Repository>) ((ParameterizedType) getClass()
              .getGenericSuperclass())
              .getActualTypeArguments()[0];
      mRepository = mApplicationContext.getBean(clazz);
    }
    catch (Exception e)
    {
      mLogger.error("Error loading " + getClass(),e);
    }

  }

  protected Repository getRepository()
  {
    return mRepository;
  }

  @Transactional
  public Entity save(Entity aEntity)
  {
    aEntity.onSave();
    return (Entity) getRepository().save(aEntity);
  }

  @Transactional
  public List<Entity> saveAll(List<Entity> aEntities)
  {
    aEntities.stream().forEach(this::save);
    return aEntities;
  }

  public List<GameNode> getAll()
  {
    return getRepository().findAll();
  }


}
