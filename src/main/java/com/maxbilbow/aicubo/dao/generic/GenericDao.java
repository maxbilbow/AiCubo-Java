package com.maxbilbow.aicubo.dao.generic;

import com.maxbilbow.aicubo.model.generic.GenericDomain;

import javax.persistence.MappedSuperclass;

/**
 * Created by Max on 18/04/2016.
 */
@MappedSuperclass
public abstract class GenericDao<Entity extends GenericDomain>
{

}
