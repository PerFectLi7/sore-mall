package com.iotechn.unimall.admin.api.level;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.iotechn.unimall.core.exception.AdminServiceException;
import com.iotechn.unimall.core.exception.ExceptionDefinition;
import com.iotechn.unimall.core.exception.ServiceException;
import com.iotechn.unimall.data.domain.LevelDO;
import com.iotechn.unimall.data.domain.UserDO;
import com.iotechn.unimall.data.mapper.LevelMapper;
import com.iotechn.unimall.data.mapper.UserMapper;
import com.iotechn.unimall.data.model.Page;
import org.apache.commons.codec.digest.Md5Crypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: kbq
 * Date: 2019-07-11
 * Time: 下午7:57
 */
@Service
public class AdminLevelServiceImpl implements AdminLevelService {

    @Autowired
    private LevelMapper levelMapper;

    @Override
    public LevelDO create(Long adminId, LevelDO levelDO) throws ServiceException {
        Date now = new Date();
        levelDO.setGmtCreate(now);
        levelDO.setGmtUpdate(now);
        if (levelMapper.insert(levelDO) > 0) {
            return levelDO;
        }
        throw new AdminServiceException(ExceptionDefinition.ADMIN_UNKNOWN_EXCEPTION);
    }

    @Override
    public Boolean delete(Long adminId, Long id) throws ServiceException {
        return levelMapper.deleteById(id) > 0;
    }

    @Override
    public LevelDO edit(Long adminId, LevelDO levelDO) throws ServiceException {
        Date now = new Date();
        levelDO.setGmtUpdate(now);
        if (levelMapper.updateById(levelDO) > 0) {
            return levelDO;
        }
        throw new AdminServiceException(ExceptionDefinition.ADMIN_UNKNOWN_EXCEPTION);
    }

    @Override
    public Page<LevelDO> list(Long adminId, Integer pageNo, Integer limit) throws ServiceException {
        QueryWrapper<LevelDO> wrapper = new QueryWrapper();
        Page<LevelDO> levelDOS = levelMapper.selectPage(Page.div(pageNo, limit, LevelDO.class), wrapper);
        return levelDOS;
    }
}
