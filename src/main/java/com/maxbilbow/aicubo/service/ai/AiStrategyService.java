package com.maxbilbow.aicubo.service.ai;

import com.maxbilbow.aicubo.dao.ai.AiStrategyRepository;
import com.maxbilbow.aicubo.model.ai.AiStrategy;
import com.maxbilbow.aicubo.service.GenericService;

/**
 * Created by Max on 22/04/2016.
 */
//@Service
public class AiStrategyService extends GenericService<AiStrategyRepository,AiStrategy>
{

  public AiStrategy newStrategy()
  {
    AiStrategy strategy = new AiStrategy();
    return strategy;

  }
}
