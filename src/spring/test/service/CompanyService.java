package spring.test.service;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import spring.test.dao.CompanyDao;
import spring.test.entity.Company;
import spring.test.exception.ParameterException;

@Service
public class CompanyService {
	private static Logger logger = Logger.getLogger(CompanyService.class);
	@Resource
	CompanyDao companyDao;
	
	@Cacheable("getAllCompanys")
    public List<Company> getAllCompanys() throws Exception{
        return companyDao.getAllCompanys();
    }
    
    public Company getCompanyById(int id)throws Exception {
        return companyDao.getCompanyById(id);
    }
    
    public int add(Company entity) throws Exception {
        if(entity.getName()==null||entity.getName().equals("")){
            throw new ParameterException(null,"单位名不能为空");
        }
        int id = companyDao.getNextId();
        entity.setId(id);
        int result= companyDao.add(entity);
        logger.info("result:"+result);
        return result;
    }
    
    @Transactional
    public int add(Company entity1,Company entityBak){
        int rows=0;
        rows=companyDao.add(entity1);
        rows=companyDao.add(entityBak);
        return rows;
    }

    public int delete(int id) {
        return companyDao.delete(id);
    }
    
    /**
     * 多删除
     */
    public int delete(String[] ids){
        int rows=0;
        for (String idStr : ids) {
            int id=Integer.parseInt(idStr);
            rows+=delete(id);
        }
        return rows;
    }

    public int update(Company entity) {
        return companyDao.update(entity);
    }
}
