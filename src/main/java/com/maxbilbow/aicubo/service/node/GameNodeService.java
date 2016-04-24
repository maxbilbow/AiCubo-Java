package com.maxbilbow.aicubo.service.node;

import com.maxbilbow.aicubo.dao.node.GameNodeRepository;
import com.maxbilbow.aicubo.model.node.GameNode;
import com.maxbilbow.aicubo.service.GenericService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Max on 22/04/2016.
 */
//@Service
public class GameNodeService extends GenericService<GameNodeRepository,GameNode>
{

  @Transactional
  public GameNode newEntity()
  {
    return new GameNode();//save(new GameNode());
  }

  public List<GameNode> getRootNodes()
  {
    return getRepository().findRootNodes();
  }

  public GameNode findWithName(String aName)
  {
    return getRepository().findWithName(aName);
  }
}
