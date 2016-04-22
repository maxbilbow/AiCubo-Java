package com.maxbilbow.aicubo.service.node;

import com.maxbilbow.aicubo.dao.node.GameNodeRepository;
import com.maxbilbow.aicubo.model.node.GameNode;
import com.maxbilbow.aicubo.service.GenericService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Max on 22/04/2016.
 */
@Service
public class GameNodeService extends GenericService<GameNodeRepository,GameNode>
{

  public GameNode newEntity()
  {
    return save(new GameNode());
  }

  public List<GameNode> getRootNodes()
  {
    return getRepository().findRootNodes();
  }
}
