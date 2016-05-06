package com.maxbilbow.aicubo.service.node;

import com.maxbilbow.aicubo.dao.node.NodeLocationRepository;
import com.maxbilbow.aicubo.model.node.NodeLocation;
import com.maxbilbow.aicubo.service.GenericService;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Max on 22/04/2016.
 */
//@Service
public class NodeLocationService extends GenericService<NodeLocationRepository,NodeLocation>
{

  @Transactional
  public NodeLocation newEntity()
  {
    return new NodeLocation();//save(new GameNode());
  }


  public NodeLocation findWithName(String aName)
  {
    return getRepository().findWithName(aName);
  }
}
