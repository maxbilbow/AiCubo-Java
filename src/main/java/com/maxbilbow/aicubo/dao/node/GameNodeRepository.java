package com.maxbilbow.aicubo.dao.node;

import com.maxbilbow.aicubo.model.ai.AiGoal;
import com.maxbilbow.aicubo.model.node.GameNode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Max on 22/04/2016.
 */
@Repository
public interface GameNodeRepository extends JpaRepository<AiGoal,Long>
{
  @Query("SELECT n FROM GameNode n where n.parent is NULL")
  List<GameNode> findRootNodes();
}
