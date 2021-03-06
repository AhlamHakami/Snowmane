package com.db.dao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.db.model.Command;

import com.db.dao.CommandDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig commandDaoConfig;

    private final CommandDao commandDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        commandDaoConfig = daoConfigMap.get(CommandDao.class).clone();
        commandDaoConfig.initIdentityScope(type);

        commandDao = new CommandDao(commandDaoConfig, this);

        registerDao(Command.class, commandDao);
    }
    
    public void clear() {
        commandDaoConfig.getIdentityScope().clear();
    }

    public CommandDao getCommandDao() {
        return commandDao;
    }

}
