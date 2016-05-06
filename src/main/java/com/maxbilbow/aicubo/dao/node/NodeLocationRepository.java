package com.maxbilbow.aicubo.dao.node;

import com.maxbilbow.aicubo.model.node.NodeLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


/**
 * Created by Max on 22/04/2016.
 */
@Repository
public interface NodeLocationRepository extends JpaRepository<NodeLocation,Long>
{

  @Query("SELECT n FROM NodeLocation n WHERE n.name = ?1")
  NodeLocation findWithName(String aName);
}
