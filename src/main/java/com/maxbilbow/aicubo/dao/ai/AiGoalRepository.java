package com.maxbilbow.aicubo.dao.ai;

import com.maxbilbow.aicubo.model.ai.AiGoal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Max on 22/04/2016.
 */
@Repository
public interface AiGoalRepository extends JpaRepository<AiGoal,Long>
{
}
